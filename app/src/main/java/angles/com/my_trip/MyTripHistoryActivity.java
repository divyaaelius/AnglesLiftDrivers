package angles.com.my_trip;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import angles.com.R;

public class MyTripHistoryActivity extends AppCompatActivity {

    Context context;
    RecyclerView rec_my_trip_hist;
    MyTriHistAdpter adpter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trip_history);

        init();
        setUpAdapter();

    }

    public void FragmentTransfer() {

        MyTripHistDetailsFragment frag = new MyTripHistDetailsFragment();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contain_myhist,frag).addToBackStack(null).commit();
    }

    private void setUpAdapter() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rec_my_trip_hist.setLayoutManager(mLayoutManager);
        rec_my_trip_hist.setItemAnimator(new DefaultItemAnimator());
        adpter = new MyTriHistAdpter(this);
        rec_my_trip_hist.setAdapter(adpter);
        adpter.notifyDataSetChanged();

    }

    private void init() {

        context=this;
        rec_my_trip_hist=findViewById(R.id.rec_my_trip_hist);

    }
}
