package angles.com.job;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import angles.com.MainActivity;
import angles.com.R;
import angles.com.invoice.InvoiceFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.max.slideview.SlideView;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobFragment extends Fragment {

    CircleImageView cst_call,cst_img;
    TextView cust_nm,pickup_km,pickup_time,cust_pickup_add;
    SlideView btn_change_status;
   int status=0;
    public JobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_job, container, false);

        initId(v);  // fetch all id at here

        cst_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:+919106839608"));

                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},123);
                }
                else
                {
                    startActivity(callIntent);
                }
            }

        });
        btn_change_status.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                if(status==0){
                    status=1;
                    btn_change_status.setText("Pickup");

                }else  if(status==1){
                    status=2;
                    btn_change_status.setText("Drop");

                    InvoiceFragment fragment=new InvoiceFragment();
                    FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.content_frame,fragment).commit();
                    getActivity().getSupportFragmentManager().beginTransaction().remove(JobFragment.this).commit();
                }
            }
        });

        return v;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+919106839608"));
                    startActivity(intent);
                }
                else
                { }
                return;
            }
        }
    }
    private void initId(View v) {
        cst_call=v.findViewById(R.id.cst_call);
        cst_img=v.findViewById(R.id.cst_img);
        cust_nm=v.findViewById(R.id.cust_nm);
        pickup_km=v.findViewById(R.id.pickup_km);
        pickup_time=v.findViewById(R.id.pickup_time);
        cust_pickup_add=v.findViewById(R.id.cust_pickup_add);
        btn_change_status=v.findViewById(R.id.btn_change_status);

    }

}
