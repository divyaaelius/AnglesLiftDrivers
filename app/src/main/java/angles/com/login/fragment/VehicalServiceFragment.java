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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import angles.com.MainActivity;
import angles.com.R;
import angles.com.utils.ConstMethod;
import angles.com.utils.FilePath;

import static android.app.Activity.RESULT_OK;
import static angles.com.utils.Const.PICK_IMAGE_REQUEST;

/**
 * A simple {@link Fragment} subclass.
 */
public class VehicalServiceFragment extends Fragment {

    ImageView veh_img;
    EditText edt_veh_no, edt_veh_doc, edt_veh_detalis, edt_veh_seater;
    Button btn_driver_register;
    private Uri filePath;
    String realFilePath="";

    public VehicalServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_vehical, container, false);

        initId(v);
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
