package angles.com.Setting;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import angles.com.AsynceTask.ServiceAsync;
import angles.com.AsynceTask.UploadImageAsync;
import angles.com.R;
import angles.com.utils.Const;
import angles.com.utils.ConstMethod;
import angles.com.utils.FilePath;
import angles.com.utils.PreferenceHelper;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static angles.com.utils.Const.PICK_IMAGE_REQUEST;
import static angles.com.utils.Const.STORAGE_PERMISSION_CODE;
import static angles.com.utils.Const.UrlClient.PROFILE_IMG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener{

    String TAG="ProfileFragment";

    CircleImageView profile_img;

    EditText edt_signup_first_nm, edt_signup_mno, edt_signup_email,edt_signup_address;
    EditText edt_veh_no, edt_veh_doc, edt_veh_detalis;
    EditText edt_lic_no;

    Button btn_signup_register;

    TextView img_details,tv_signup_dob,tv_lic_img,tv_car_img,img_txt;
    TextView edt_lic_issued_date, edt_lic_expiry_date;

    RadioGroup radio_group;
    RadioButton radio_angle,radio_with_car_angle;

    int imagType = 0;
    int status;

    private Uri filePath,filePathcar;
    String realFilePath="",realFilePath_car="";


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        init(v);
        SetupData();
        onClickmehod();

        return v;
    }
    private void checkedRadio() {
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.pref_radio_angle){
                    status=1;
                    Toast.makeText(getContext(), "angele", Toast.LENGTH_SHORT).show();
                    Log.e("status","================>"+status);
                    // btn_lic_register.setText("Register");

                }else if(checkedId==R.id.pref_radio_with_car_angle){
                    status=2;
                    Toast.makeText(getContext(), "angle with care", Toast.LENGTH_SHORT).show();
                    Log.e("status","================>"+status);
                    //btn_lic_register.setText("Continue");

                }
            }
        });
    }

    private void SetupData() {

        final ProgressDialog myDialog = ConstMethod.showProgressDialog(getActivity(), getResources().getString(R.string.please_wait));

        String id = new PreferenceHelper(getContext()).getuserDetailsid();
        Uri.Builder builder = new Uri.Builder();

        Log.e(TAG, Const.UrlClient.GET_PROFILE_DATA+id);

        new ServiceAsync(Const.UrlClient.GET_PROFILE_DATA + id, new ServiceAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                Log.e("JSON", "DATA " + result);
                myDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString(Const.Params.STATUS).equals(Const.Params.TRUE)) {
                        //String str = jsonObject.getString("status");
                        JSONObject object = jsonObject.getJSONObject(Const.Params.PROFILEDETAILS);

                        edt_signup_first_nm.setText(object.getString(Const.Params.DW_NAME));
                        edt_signup_mno.setText(object.getString(Const.Params.DW_MOBILENO));
                        edt_signup_address.setText(object.getString(Const.Params.DW_ADDRESS));
                        edt_signup_email.setText(object.getString(Const.Params.DW_EMAIL));
                        tv_signup_dob.setText(object.getString(Const.Params.DW_DOB));
                        edt_lic_no.setText(object.getString(Const.Params.DW_LICENSE_NUMBER));
                        edt_lic_issued_date.setText(object.getString(Const.Params.DW_LC_DATE));
                        edt_lic_expiry_date.setText(object.getString(Const.Params.DW_EXPIRE_DATE));
                        edt_veh_no.setText(object.getString(Const.Params.DW_CAR_NO));
                        edt_veh_doc.setText(object.getString(Const.Params.DW_CAR_DOCUMENT));
                        edt_veh_detalis.setText(object.getString(Const.Params.DW_MODEL));
                        status= Integer.parseInt(object.getString(Const.Params.DW_TYPE));




                        Picasso.with(getContext()).load(PROFILE_IMG+Const.Params.DW_IMAGE)
                                .placeholder(R.drawable.angel_bg)
                                .error(R.drawable.calender)
                                .into(profile_img);



                    } else {
                        Toast.makeText(getContext(), "Login fail, please try again", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Incorrect Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String result) {
                myDialog.dismiss();
                Toast.makeText(getContext(), "failed" + result, Toast.LENGTH_SHORT).show();
            }

        }, builder).execute();
    }

    private void init(View v) {

        //image
        profile_img = v.findViewById(R.id.pref_profile_img);

        // edit text

        edt_signup_first_nm = v.findViewById(R.id.pref_edt_first_nm);
        edt_signup_mno = v.findViewById(R.id.pref_edt_mno);
        edt_signup_email = v.findViewById(R.id.pref_edt_email);
        edt_signup_address = v.findViewById(R.id.pref_edt_address);
        edt_lic_no = v.findViewById(R.id.pref_edt_lic_no);

        edt_veh_detalis=v.findViewById(R.id.pref_edt_veh_detalis);
        edt_veh_doc=v.findViewById(R.id.pref_edt_veh_doc);
        edt_veh_no=v.findViewById(R.id.pref_edt_veh_no);

        //text view
        img_txt = v.findViewById(R.id.img_txt);
        tv_signup_dob = v.findViewById(R.id.pref_tv_dob);
        tv_lic_img = v.findViewById(R.id.pref_lic_img);
        tv_car_img = v.findViewById(R.id.pref_car_img);
        edt_lic_issued_date = v.findViewById(R.id.pref_edt_lic_issued_date);
        edt_lic_expiry_date = v.findViewById(R.id.pref_edt_lic_expiry_date);

        // radio
        radio_group = v.findViewById(R.id.pref_radio_group);
        radio_angle = v.findViewById(R.id.pref_radio_angle);
        radio_with_car_angle = v.findViewById(R.id.pref_radio_with_car_angle);

        // button
        btn_signup_register = v.findViewById(R.id.pref_btn_register);



    }
/*
    private void checkedRadio() {

    }
*/

    public void onClickmehod() {
      /*  profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagType = 1;
                SelectFile();
            }
        });*/
         profile_img.setOnClickListener(this);
        tv_lic_img.setOnClickListener(this);
        tv_car_img.setOnClickListener(this);

        tv_signup_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstMethod.dateForamtYYYMMDDWithDesh(getContext(), tv_signup_dob);
            }
        });
      /*  tv_lic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagType = 2;
                SelectFile();
            }
        });*/

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

        checkedRadio();

        /*tv_car_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagType = 3;
                SelectFile();
            }
        });*/
        btn_signup_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceCall();
            }
        });

    }

    private void ServiceCall() {
        String path_img = "",img_path_car = "";

            path_img=img_txt.getText().toString();
            ConstMethod.LodDebug(TAG, " path--->" + path_img);

             img_path_car= tv_car_img.getText().toString();
            ConstMethod.LodDebug(TAG, "car path--->" + img_path_car);





        if(status==2){
            final ProgressDialog myDialog = ConstMethod.showProgressDialog(getContext(), getResources().getString(R.string.please_wait));
            String id = new PreferenceHelper(getContext()).getuserDetailsid();

            HashMap<String, String> hashMap = new HashMap<>();
            HashMap<String, File> hashMap1 = new HashMap<>();

            hashMap.put(Const.Params.DW_ID,id);
            hashMap.put(Const.Params.DW_EMAIL, edt_signup_email.getText().toString());
            hashMap.put(Const.Params.DW_ADDRESS, edt_signup_address.getText().toString());

            hashMap.put(Const.Params.DW_CAR_NO, edt_veh_no.getText().toString());
            hashMap.put(Const.Params.DW_CAR_DOCUMENT, edt_veh_doc.getText().toString());
            hashMap.put(Const.Params.DW_MODEL, edt_veh_detalis.getText().toString());

            hashMap1.put(Const.Params.DW_IMAGE, new File(path_img));
            hashMap1.put(Const.Params.DW_CAR_IMAGE, new File(img_path_car));

            ConstMethod.LodDebug(TAG, "url --->" + Const.UrlClient.SET_PROFILE_DATA+id);
            ConstMethod.LodDebug(TAG, "text hashMap --->" + hashMap.toString());
            ConstMethod.LodDebug(TAG, "file path hashMap1 --->" + hashMap1.toString());

            UploadImageAsync imageAsync = new UploadImageAsync(Const.UrlClient.SET_PROFILE_DATA+id, hashMap, hashMap1, new UploadImageAsync.OnAsyncResult() {
                @Override
                public void onSuccess(String result) {
                    myDialog.dismiss();
                    ConstMethod.LodDebug(TAG, "Result--->" + result);

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.getString(Const.Params.STATUS).equals(Const.Params.TRUE)) {

                            ConstMethod.showToast(getContext(), "Profile Update successfully");

                        }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(String result) {
                    myDialog.dismiss();
                    ConstMethod.LodDebug(TAG, "Result failed--->" + result);

                }
            });
            imageAsync.execute();
        }

    }
    public void SelectFile() {

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
    // permission for access gallary
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        ConstMethod.SelectFile(getActivity());
                    }
                }

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            ConstMethod.LodDebug("File path", "------------>" + filePath);
            realFilePath = FilePath.getPath(getActivity(), filePath);
            ConstMethod.LodDebug("real path", "------------>" + realFilePath);
            try {
                if (imagType == 1) {
                    Picasso.with(getContext())
                            .load(filePath)
                            .error(R.drawable.icon_trans)
                            .placeholder(R.drawable.icon_trans)
                            .into(profile_img);
                    img_txt.setText(realFilePath);

                }else if(imagType==2){
                    tv_lic_img.setText(realFilePath);
                }else if(imagType==3){
                    tv_car_img.setText(realFilePath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.pref_profile_img){
            imagType = 1;
            SelectFile();
        }else if(v.getId()==R.id.pref_car_img){
            imagType = 3;
            SelectFile();
        }else if(v.getId()==R.id.pref_lic_img){
            imagType = 2;
            SelectFile();
        }
    }
}
