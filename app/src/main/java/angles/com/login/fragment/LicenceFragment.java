package angles.com.login.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import angles.com.R;
import angles.com.utils.ConstMethod;
import angles.com.utils.FilePath;

import static android.app.Activity.RESULT_OK;
import static angles.com.utils.Const.PICK_IMAGE_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class LicenceFragment extends Fragment {

    String TAG="LicenceFragment";
    ImageView lic_img;
    EditText edt_lic_no;
    TextView edt_lic_issued_date, edt_lic_expiry_date;
    RadioGroup radio_group;
    RadioButton radio_angle,radio_with_car_angle;
    Button btn_lic_register;
    private Uri filePath;
    String realFilePath="";
    String name,mno,email,add,dob,pass,profile;
    int status;
    boolean validate;

    public LicenceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_licence, container, false);
        initId(v);

        name=getArguments().getString   (String.valueOf(R.string.b_nm));
        mno=getArguments().getString    (String.valueOf(R.string.b_mno));
        email=getArguments().getString  (String.valueOf(R.string.b_email));
        add=getArguments().getString    (String.valueOf(R.string.b_add));
        dob=getArguments().getString    (String.valueOf(R.string.b_dob));
        pass=getArguments().getString   (String.valueOf(R.string.b_pass));
        profile=getArguments().getString(String.valueOf(R.string.b_profile));

        Log.d(TAG,"Bundls value ------->"+"name "+name+" mno "+mno+" email "+email+" add "+add+" dob "+dob
        +" pass "+pass+" profile "+profile);



        edt_lic_issued_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstMethod.DateFunction(getContext(),edt_lic_issued_date);
            }
        });
        edt_lic_expiry_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstMethod.DateFunction(getContext(),edt_lic_expiry_date);
            }
        });
        lic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             selectFile();
            }
        });

        checkedRadio();
        btn_lic_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Validation();

                if(validate=true) {
                    Log.e("status", "================>" + status);
                    if (status == 1) {
                        Log.e("status", "================>" + status);
                        OtpFragment frag = new OtpFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.lic_fragment, frag).addToBackStack(null).commit();

                    } else if (status == 2) {
                        Log.e("status", "================>" + status);
                        VehicalServiceFragment fragment = new VehicalServiceFragment();
                        Bundle bundle=new Bundle();
                        bundle.putString(String.valueOf(R.string.b_lic_no),edt_lic_no.getText().toString());
                        bundle.putString(String.valueOf(R.string.b_lic_img),realFilePath);
                        bundle.putString(String.valueOf(R.string.b_issue_date),edt_lic_issued_date.getText().toString());
                        bundle.putString(String.valueOf(R.string.b_exp_date),edt_lic_expiry_date.getText().toString());
                        bundle.putString(String.valueOf(R.string.b_nm),name);
                        bundle.putString(String.valueOf(R.string.b_mno),mno);
                        bundle.putString(String.valueOf(R.string.b_email),email);
                        bundle.putString(String.valueOf(R.string.b_add),add);
                        bundle.putString(String.valueOf(R.string.b_dob),dob);
                        bundle.putString(String.valueOf(R.string.b_pass),pass);
                        bundle.putString(String.valueOf(R.string.b_profile),profile);
                        fragment.setArguments(bundle);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.lic_fragment, fragment).addToBackStack(null).commit();
                    }
                }
            }
        });

        return v;
    }

    private void checkedRadio() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radio_angle){
                    status=1;
                    Toast.makeText(getContext(), "angele", Toast.LENGTH_SHORT).show();
                    Log.e("status","================>"+status);
                    // btn_lic_register.setText("Register");

                }else if(checkedId==R.id.radio_with_car_angle){
                    status=2;
                    Toast.makeText(getContext(), "angle with care", Toast.LENGTH_SHORT).show();
                    Log.e("status","================>"+status);
                    //btn_lic_register.setText("Continue");

                }
            }
        });
    }

    private void selectFile() {

        String[] mimeTypes = {"image/*"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
            if (mimeTypes.length > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            }
        } else {
            String mimeTypesStr = "";

            for (String mimeType : mimeTypes) {
                mimeTypesStr += mimeType + "|";
            }

            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        }
        startActivityForResult(Intent.createChooser(intent, "Select"), PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      //  super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            ConstMethod.LodDebug("fragment File path", "------------>" + filePath);
            realFilePath = FilePath.getPath(getActivity(), filePath);
            ConstMethod.LodDebug("fragment real path", "------------>" + realFilePath);
            try {
                Picasso.with(getContext())
                        .load(filePath)
                        .error(R.drawable.ic_menu_gallery)
                        .placeholder(R.drawable.ic_menu_camera)
                        .into(lic_img);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void Validation() {

        if (ConstMethod.isInternetOn(getActivity())) {

            if (TextUtils.isEmpty(edt_lic_no.getText())) {
                edt_lic_no.requestFocus();
                edt_lic_no.setError("Enter Licence Number");

            }  else if (TextUtils.isEmpty(edt_lic_issued_date.getText())) {
                edt_lic_issued_date.requestFocus();
                edt_lic_issued_date.setError("Select Licence Issued Date");
                Toast.makeText(getContext(), "Select Licence Issued Date", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(edt_lic_expiry_date.getText())) {
                edt_lic_expiry_date.requestFocus();
                edt_lic_expiry_date.setError("Select Licence Expiry Date");
                Toast.makeText(getContext(), "Select Licence Expiry Date", Toast.LENGTH_SHORT).show();

            }else if(radio_group.getCheckedRadioButtonId() == -1){
                Toast.makeText(getContext(),"Select Angle type",Toast.LENGTH_SHORT).show();
            }else {
                Log.e("Profile Real path", "===============>" + realFilePath);
                if (realFilePath.equals("") || realFilePath.length() == 0) {
                    Toast.makeText(getContext(), "Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    LicenceCalling();
                    validate=true;
                }
            }

        } else {
            ConstMethod.NetworkAlert(getActivity());
        }

    }

    private void LicenceCalling() {



    }

    private void initId(View v) {
        lic_img = v.findViewById(R.id.lic_img);
        edt_lic_no = v.findViewById(R.id.edt_lic_no);
        edt_lic_issued_date = v.findViewById(R.id.edt_lic_issued_date);
        edt_lic_expiry_date = v.findViewById(R.id.edt_lic_expiry_date);
        btn_lic_register = v.findViewById(R.id.btn_lic_register);
        radio_group = v.findViewById(R.id.radio_group);
        radio_angle = v.findViewById(R.id.radio_angle);
        radio_with_car_angle = v.findViewById(R.id.radio_with_car_angle);
    }
}
