package angles.com.login.fragment;


import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import angles.com.AsynceTask.UploadImageAsync;
import angles.com.MainActivity;
import angles.com.R;
import angles.com.utils.Const;
import angles.com.utils.ConstMethod;
import angles.com.utils.FilePath;

import static android.app.Activity.RESULT_OK;
import static angles.com.utils.Const.PICK_IMAGE_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicalServiceFragment extends Fragment {

    String TAG="VehicalServiceFragment";
    ImageView veh_img;
    EditText edt_veh_no, edt_veh_doc, edt_veh_detalis, edt_veh_seater;
    Button btn_driver_register;
    private Uri filePath;
    String realFilePath="";
    String name,mno,email,add,dob,pass,profile,lic_no,lic_img,lic_isses,lic_exp;

    public VehicalServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_vehical, container, false);

        initId(v);

        name=getArguments().getString   (String.valueOf(R.string.b_nm));
        mno=getArguments().getString    (String.valueOf(R.string.b_mno));
        email=getArguments().getString  (String.valueOf(R.string.b_email));
        add=getArguments().getString    (String.valueOf(R.string.b_add));
        dob=getArguments().getString    (String.valueOf(R.string.b_dob));
        pass=getArguments().getString   (String.valueOf(R.string.b_pass));
        profile=getArguments().getString(String.valueOf(R.string.b_profile));
        lic_no=getArguments().getString(String.valueOf(R.string.b_lic_no));
        lic_img=getArguments().getString(String.valueOf(R.string.b_lic_img));
        lic_isses=getArguments().getString(String.valueOf(R.string.b_issue_date));
        lic_exp=getArguments().getString(String.valueOf(R.string.b_exp_date));

        Log.d(TAG,"Bundls value ------->"+"name "+name+" mno "+mno+" email "+email+" add "+add+" dob "+dob
                +" pass "+pass+" profile "+profile+" lic_no "+lic_no+" lic_img "+lic_img+" lic_isses "+lic_isses
        +" lic_exp "+lic_exp);


        imageFetch();

        btn_driver_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vilidation();
            }
        });

        return v;
    }

    private void Vilidation() {

        if (ConstMethod.isInternetOn(getActivity())) {

            if (TextUtils.isEmpty(edt_veh_no.getText())) {
                edt_veh_no.requestFocus();
                edt_veh_no.setError("Enter Vehicle Number");

            } else if (TextUtils.isEmpty(edt_veh_doc.getText())) {
                edt_veh_doc.requestFocus();
                edt_veh_doc.setError("Enter Vehicle Document");

            } else if (TextUtils.isEmpty(edt_veh_detalis.getText())) {
                edt_veh_detalis.requestFocus();
                edt_veh_detalis.setError("Enter Vehicle Details");

               } else if (TextUtils.isEmpty(edt_veh_seater.getText())) {
                edt_veh_seater.requestFocus();
                edt_veh_seater.setError("Enter Vehicle Seater");
                }else{
                Log.e("Profile Real path", "===============>" + realFilePath);
                if (realFilePath.equals("") || realFilePath.length() == 0) {
                    Toast.makeText(getContext(), "Select Image", Toast.LENGTH_SHORT).show();
                } else {
                    VehicalRegister();
                }
            }

        } else {
            ConstMethod.NetworkAlert(getActivity());
        }
    }

    private void VehicalRegister() {

        final ProgressDialog myDialog = ConstMethod.showProgressDialog(getContext(), getResources().getString(R.string.please_wait));

        HashMap<String, String> hashMap = new HashMap<>();
        HashMap<String, File> hashMap1 = new HashMap<>();

        hashMap.put(Const.Params.DW_MOBILENO,mno);
        hashMap.put(Const.Params.DW_NAME, name);
        hashMap.put(Const.Params.DW_EMAIL, email);
        hashMap.put(Const.Params.DW_ADDRESS, add);
        hashMap.put(Const.Params.DW_DOB, dob);
       // hashMap.put(Const.Params.DW_IMAGE, profile);
        hashMap.put(Const.Params.DW_LICENSE_NUMBER, lic_no);
        //hashMap.put(Const.Params.DW_LICENSE_PICTURE, lic_img);
        hashMap.put(Const.Params.DW_LC_DATE, lic_isses);
        hashMap.put(Const.Params.DW_EXPIRE_DATE, lic_exp);
        hashMap.put(Const.Params.DW_CAR_NO, edt_veh_no.getText().toString());
        hashMap.put(Const.Params.DW_CAR_DOCUMENT, edt_veh_doc.getText().toString());
        hashMap.put(Const.Params.DW_MODEL, edt_veh_detalis.getText().toString());
        hashMap.put(Const.Params.DW_TYPE, String.valueOf(Const.MOBILETYPE));// 1 for driver with car
        hashMap.put(Const.Params.PASSWORD, pass);
        hashMap.put(Const.Params.MOBILE_TYPE, String.valueOf(Const.MOBILETYPE));



        hashMap1.put(Const.Params.DW_IMAGE, new File(profile));
        hashMap1.put(Const.Params.DW_CAR_IMAGE, new File(realFilePath));
        hashMap1.put(Const.Params.DW_LICENSE_PICTURE, new File(lic_img));


        ConstMethod.LodDebug(TAG, "url --->" + Const.UrlClient.REGISTER_URL);
        ConstMethod.LodDebug(TAG, "text hashMap --->" + hashMap.toString());
        ConstMethod.LodDebug(TAG, "file path hashMap1 --->" + hashMap1.toString());

        UploadImageAsync imageAsync = new UploadImageAsync(Const.UrlClient.REGISTER_URL, hashMap, hashMap1, new UploadImageAsync.OnAsyncResult() {
            @Override
            public void onSuccess(String result) {
                myDialog.dismiss();
                ConstMethod.LodDebug(TAG, "Result--->" + result);

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString(Const.Params.STATUS).equals(Const.Params.TRUE)) {

                        ConstMethod.showToast(getContext(), "Request Submit success");
                        replaceFragment();
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

    private void replaceFragment() {
        OtpFragment frag = new OtpFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.veh_frame, frag).addToBackStack(null).commit();
    }

    private void imageFetch() {
        veh_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectFile();
            }
        });
    }

    private void SelectFile() {
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
                        .into(veh_img);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initId(View v) {

        veh_img=v.findViewById(R.id.veh_img);
        edt_veh_seater=v.findViewById(R.id.edt_veh_seater);
        edt_veh_detalis=v.findViewById(R.id.edt_veh_detalis);
        edt_veh_doc=v.findViewById(R.id.edt_veh_doc);
        edt_veh_no=v.findViewById(R.id.edt_veh_no);
        btn_driver_register=v.findViewById(R.id.btn_driver_register);
    }

}
