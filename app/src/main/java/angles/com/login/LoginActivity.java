package angles.com.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import angles.com.AsynceTask.ServiceAsync;
import angles.com.MainActivity;
import angles.com.R;
import angles.com.utils.Const;
import angles.com.utils.ConstMethod;
import angles.com.utils.PreferenceHelper;

public class LoginActivity extends AppCompatActivity {

    String TAG="LoginActivity";
    Context context;
    EditText edt_login_mno, edt_login_pass;
    Button btn_login_signin;
    Button  sign_in_button_google;
    TextView btn_login_signup,btn_login_forget_pass;
    int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    private PreferenceHelper prefHelp;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init(); // find all id at here
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        if (prefHelp.getLogin()) {
            startActivity(new Intent(context, MainActivity.class));
            finish();

        }
        btn_login_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValidatiion();
            }
        });


        btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(context,SignupActivity.class));
            }
        });

    }

    private void getValidatiion() {

                if (ConstMethod.isInternetOn(LoginActivity.this)) {
                    if (TextUtils.isEmpty(edt_login_mno.getText())) {
                        edt_login_mno.requestFocus();
                        edt_login_mno.setError("Enter Mobile No.");

                    }else if (TextUtils.isEmpty(edt_login_pass.getText())) {
                        edt_login_pass.requestFocus();
                        edt_login_pass.setError("Enter Password");

                    } else if (ConstMethod.isValidPassword(edt_login_pass.getText().toString())) {

                        //service call
                        getUserLogin();


                    } else {
                        edt_login_pass.requestFocus();
                        edt_login_pass.setError("Enter Valid Password");
                    }
                } else {
                    ConstMethod.NetworkAlert(LoginActivity.this);

                }


    }

    private void getUserLogin() {

        final ProgressDialog myDialog = ConstMethod.showProgressDialog(this, getResources().getString(R.string.please_wait));

        final String id = String.valueOf(edt_login_mno.getText().toString().trim());
        String pass = edt_login_pass.getText().toString().trim();

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(Const.Params.IDENTITY, id);
        builder.appendQueryParameter(Const.Params.PASSWORD, pass);
        builder.appendQueryParameter(Const.Params.MOBILE_TOKEN, prefHelp.getDeviceToken());
        builder.appendQueryParameter(Const.Params. MOBILE_DEVICE_ID, deviceId);

        Log.e(TAG, Const.UrlClient.LOGIN_WITHCAR_URL);
        Log.e(TAG, "param  " + builder.toString());

        new ServiceAsync(Const.UrlClient.LOGIN_WITHCAR_URL, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "result ----------> " + result);
                myDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if(jsonObject.getString(Const.Params.STATUS).equals(Const.Params.TRUE))
                    {
                        //String str = jsonObject.getString("status");
                        JSONObject object = jsonObject.getJSONObject(Const.Params.DATA);

                        // get the all data in variable

                        String id1 = object.getString(Const.Params.ID);
                        String username = object.getString(Const.Params.USERNAME);
                        String first_name = object.getString(Const.Params.FIRST_NAME );
                        String email = object.getString(Const.Params.DW_EMAIL);
                        String token = object.getString(Const.Params.TOKEN_ID );
                        String role_id = object.getString(Const.Params.ROLE_ID );
                        String user_details_w_id=object.getString(Const.Params.DW_USERDETAILS_ID );
                        String user_details_v_id=object.getString(Const.Params.DV_USERDETAILS_ID );
                        String driver_profile=object.getString(Const.Params.DW_IMAGE);
                        String driver_type=object.getString(Const.Params.DW_TYPE);


                        prefHelp.putLogin(true);
                        prefHelp.putUserId(id1);
                        prefHelp.putUsername(username);
                        prefHelp.putFIRST_NAME(first_name);
                        prefHelp.putDW_USERDETAILS_ID(user_details_w_id);
                        prefHelp.putDV_USERDETAILS_ID(user_details_v_id);
                        prefHelp.putTOKEN(token);
                        prefHelp.putEmail(email);
                        prefHelp.putPROFILE_IMAGE(driver_profile);
                        prefHelp.putROLE_ID(role_id);
                        prefHelp.putDRIVER_TYPE(driver_type);

                        Log.d(TAG,"USer ID "+id1);
                        Log.d(TAG,"USer name "+username);
                        Log.d(TAG,"USer Details ID "+first_name);
                        Log.d(TAG,"USer Details ID "+user_details_w_id);
                        Log.d(TAG,"USer Details ID "+user_details_v_id);
                        Log.d(TAG,"USer Details ID "+token);
                        Log.d(TAG,"USer Details ID "+email);
                        Log.d(TAG,"USer Details ID "+driver_profile);
                        Log.d(TAG,"USer Details ID "+role_id);
                        Log.d(TAG,"USer Details ID "+driver_type);
                        Log.d(TAG,"USer Details ID "+new PreferenceHelper(context).getDW_USERDETAILS_ID());

                        startActivity(new Intent(context, MainActivity.class));
                    }else {
                        Toast.makeText(context, "Login fail, please try again", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Incorrect Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String result) {
                myDialog.dismiss();
                Log.d(TAG, "result ----------> " + result);
            }

        }, builder).execute();
       // startActivity(new Intent(context, MainActivity.class));
    }

    private void init() {

        context = LoginActivity.this;

        prefHelp = new PreferenceHelper(this);

        edt_login_mno = findViewById(R.id.edt_login_mno);
        edt_login_pass = findViewById(R.id.edt_login_pass);
        //sign_in_button_google = findViewById(R.id.sign_in_button_google);
        btn_login_signin = findViewById(R.id.btn_login_signin);
        btn_login_signup = findViewById(R.id.btn_login_signup);

    }
}
