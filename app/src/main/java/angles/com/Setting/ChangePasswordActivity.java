package angles.com.Setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import angles.com.AsynceTask.ServiceAsync;
import angles.com.MainActivity;
import angles.com.R;
import angles.com.utils.Const;
import angles.com.utils.ConstMethod;
import angles.com.utils.PreferenceHelper;

import static angles.com.utils.Const.UrlClient.CHANGE_PASSWORD_URL;


public class ChangePasswordActivity extends AppCompatActivity {

    EditText old_pass,new_pass,confm_new_pass;
    Button change_pass;
    Context context;
    String TAG="ChangePasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        FindID();   // find all id at here
        change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean fieldsOK = ConstMethod.validate(new EditText[]{old_pass, new_pass, confm_new_pass
                }, new String[]{"Enter Old Password","Enter New Password","Confirm New Password"});

                if(fieldsOK){
                    if(new_pass.getText().toString().trim().equals(confm_new_pass.getText().toString())){
                        ServiceCall();
                    }else {
                        confm_new_pass.setError("Password Not Match");
                    }
                }
            }
        });


    }

    private void ServiceCall() {
        final String oPass = old_pass.getText().toString().trim();
        final String nPass = new_pass.getText().toString().trim();
        final String cnPass = confm_new_pass.getText().toString().trim();

        Uri.Builder builder = new Uri.Builder();
        builder.appendQueryParameter(Const.Params.IDENTITY, new PreferenceHelper(context).getUSER_NAME());
        builder.appendQueryParameter(Const.Params.OLD, oPass);
        builder.appendQueryParameter(Const.Params.NEW, nPass);
        builder.appendQueryParameter(Const.Params.NEW_CONFIRM, cnPass);
        builder.appendQueryParameter(Const.Params.USER_ID, new PreferenceHelper(context).getUserId());

        Log.d(TAG, "builder ----------->" + builder);

        new ServiceAsync(CHANGE_PASSWORD_URL, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {

                Log.d(TAG, "----------->" + result);

                Toast.makeText(context, "Password Change successfully", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(context, MainActivity.class));
                finish();

            }
            @Override
            public void onFailure(String result) {
                Log.d(TAG, "----------->" + result);
            }
        }, builder).execute();
    }

    private void FindID() {
    context=ChangePasswordActivity.this;

        old_pass=findViewById(R.id.old_pass);
        new_pass=findViewById(R.id.new_pass);
        confm_new_pass=findViewById(R.id.confm_new_pass);
        change_pass=findViewById(R.id.change_pass);
    }
}
