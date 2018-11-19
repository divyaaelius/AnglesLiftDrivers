package angles.com.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import angles.com.R;
import angles.com.login.fragment.LicenceFragment;
import angles.com.login.fragment.OtpFragment;
import angles.com.utils.ConstMethod;
import angles.com.utils.FilePath;
import de.hdodenhof.circleimageview.CircleImageView;

import static angles.com.utils.Const.PICK_IMAGE_REQUEST;
import static angles.com.utils.Const.STORAGE_PERMISSION_CODE;

public class SignupActivity extends AppCompatActivity {

    Context context;
    CircleImageView profile_img;
    EditText edt_signup_first_nm, edt_signup_last_nm, edt_signup_mno, edt_signup_email,
            edt_signup_password, edt_signup_password_conf,edt_signup_address;
    Button btn_signup_register;
    TextView img_details,tv_signup_dob;
    private Uri filePath;
    String realFilePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = SignupActivity.this;

        init(); // find all id at here
        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignupActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                } else {
                    ConstMethod.SelectFile(SignupActivity.this);
                }
            }
        });
        tv_signup_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstMethod.dateForamtYYYMMDDWithDesh(context,tv_signup_dob);
            }
        });
        register();  // register calling
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ConstMethod.SelectFile(this);
                    }
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            ConstMethod.LodDebug("File path", "------------>" + filePath);
            realFilePath = FilePath.getPath(this, filePath);
            ConstMethod.LodDebug("real path", "------------>" + realFilePath);
            try {
                Picasso.with(this)
                        .load(filePath)
                        .error(R.drawable.ic_menu_gallery)
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(profile_img);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void register() {
        btn_signup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();

            }
        });


    }

    private void Validation() {

        if (ConstMethod.isInternetOn(this)) {

            if (TextUtils.isEmpty(edt_signup_first_nm.getText().toString())) {
                edt_signup_first_nm.requestFocus();
                edt_signup_first_nm.setError("Enter First Name");

            } else if (TextUtils.isEmpty(edt_signup_mno.getText().toString())) {
                edt_signup_mno.requestFocus();
                edt_signup_mno.setError("Enter Mobile No");

            } else if (TextUtils.isEmpty(edt_signup_email.getText().toString())) {
                edt_signup_email.requestFocus();
                edt_signup_email.setError("Enter Email");

            } else if (TextUtils.isEmpty(edt_signup_address.getText().toString())) {
                edt_signup_address.requestFocus();
                edt_signup_address.setError("Enter Address");

            }else if (TextUtils.isEmpty(tv_signup_dob.getText().toString())) {
                tv_signup_dob.requestFocus();
                tv_signup_dob.setError("Select Date Of Birth");

            } else if (TextUtils.isEmpty(edt_signup_password.getText().toString())) {
                edt_signup_password.requestFocus();
                edt_signup_password.setError("Enter Password");

            } else if (TextUtils.isEmpty(edt_signup_password_conf.getText().toString())) {
                edt_signup_password_conf.requestFocus();
                edt_signup_password_conf.setError("Enter Conform Password");

            } else if (ConstMethod.isValidPassword(edt_signup_password.getText().toString())) {
                if (edt_signup_password.getText().toString().equals(edt_signup_password_conf.getText().toString())) {
                    if (ConstMethod.isValidEmailAddress(edt_signup_email.getText().toString())) {

                      /*  Log.e("Profile Real path","===============>"+realFilePath);
                        if (realFilePath.equals("") || realFilePath.length()==0) {
                            Toast.makeText(this, "Select Image", Toast.LENGTH_SHORT).show();
                        } else {

                        }*/
                        getUserLogin();
                    } else {
                        edt_signup_email.requestFocus();
                        edt_signup_email.setError("Enter Valid Email");

                    }


                } else {
                    edt_signup_password_conf.requestFocus();
                    edt_signup_password_conf.setError("Password MisMatch");

                }
            } else {
                edt_signup_password.requestFocus();
                edt_signup_password.setError("Maximum 6 Character Required");
            }

        } else {
            ConstMethod.NetworkAlert(this);
        }
    }

    private void getUserLogin() {
        LicenceFragment frag = new LicenceFragment();
        Bundle bundle=new Bundle();
        bundle.putString(String.valueOf(R.string.b_nm),edt_signup_first_nm.getText().toString());
        bundle.putString(String.valueOf(R.string.b_mno),edt_signup_mno.getText().toString());
        bundle.putString(String.valueOf(R.string.b_email),edt_signup_email.getText().toString());
        bundle.putString(String.valueOf(R.string.b_add),edt_signup_address.getText().toString());
        bundle.putString(String.valueOf(R.string.b_dob),tv_signup_dob.getText().toString());
        bundle.putString(String.valueOf(R.string.b_pass),edt_signup_password_conf.getText().toString());
        bundle.putString(String.valueOf(R.string.b_profile),realFilePath);
        frag.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.signup_frame, frag).addToBackStack(null).commit();

    }

    private void init() {

        profile_img = findViewById(R.id.profile_img);
        //img_details = findViewById(R.id.img_details);
        edt_signup_first_nm = findViewById(R.id.edt_signup_first_nm);
        edt_signup_mno = findViewById(R.id.edt_signup_mno);
        edt_signup_email = findViewById(R.id.edt_signup_email);
        edt_signup_address = findViewById(R.id.edt_signup_address);
        edt_signup_password = findViewById(R.id.edt_signup_password);
        tv_signup_dob = findViewById(R.id.tv_signup_dob);
        edt_signup_password_conf = findViewById(R.id.edt_signup_password_conf);
        btn_signup_register = findViewById(R.id.btn_signup_register);

    }
}
