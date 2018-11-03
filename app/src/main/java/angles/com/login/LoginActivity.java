package angles.com.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import angles.com.MainActivity;
import angles.com.R;

public class LoginActivity extends AppCompatActivity {

    Context context;
    EditText edt_login_mno, edt_login_pass;
    Button btn_login_signin;
    Button  sign_in_button_google;
    TextView btn_login_signup,btn_login_forget_pass;
    int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init(); // find all id at here

        getValidatiion();
        btn_login_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, SignupActivity.class));
            }
        });
       /* sign_in_button_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });*/
    }

    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google sigin", "signInResult:failed code=" + e.getStatusCode());
           // updateUI(null);
        }
    }

    private void getValidatiion() {
        btn_login_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (ConstMethod.isInternetOn(LoginActivity.this)) {
                    if (TextUtils.isEmpty(edt_login_mno.getText())) {
                        edt_login_mno.requestFocus();
                        edt_login_mno.setError("Enter Mobile No.");

                    }else if (TextUtils.isEmpty(edt_login_pass.getText())) {
                        edt_login_pass.requestFocus();
                        edt_login_pass.setError("Enter Password");

                    } else if (ConstMethod.isValidPassword(edt_login_pass.getText().toString())) {*/
                getUserLogin();
/*
                    } else {
                        edt_login_pass.requestFocus();
                        edt_login_pass.setError("Enter Valid Password");
                    }
                } else {
                    ConstMethod.NetworkAlert(LoginActivity.this);

                }*/
            }
        });
    }

    private void getUserLogin() {


        startActivity(new Intent(context, MainActivity.class));
    }

    private void init() {

        context = LoginActivity.this;

        edt_login_mno = findViewById(R.id.edt_login_mno);
        edt_login_pass = findViewById(R.id.edt_login_pass);
        //sign_in_button_google = findViewById(R.id.sign_in_button_google);
        btn_login_signin = findViewById(R.id.btn_login_signin);
        btn_login_signup = findViewById(R.id.btn_login_signup);

    }
}
