package angles.com.login.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import angles.com.MainActivity;
import angles.com.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtpFragment extends Fragment {

    Button  otp_submit,otp_resend;
    EditText otp_no;

    public OtpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_otp, container, false);

        initId(v);
        otp_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
                }
        });

        return v;
    }

    private void initId(View v) {

        otp_no=v.findViewById(R.id.otp_no);
        otp_submit=v.findViewById(R.id.otp_submit);
        otp_resend=v.findViewById(R.id.otp_resend);

    }

}
