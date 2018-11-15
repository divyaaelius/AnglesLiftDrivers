package angles.com;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import angles.com.Setting.ChangePasswordActivity;
import angles.com.Setting.SettingsActivity;
import angles.com.home.MapFragment;
import angles.com.job.JobFragment;
import angles.com.my_trip.MyTripHistoryActivity;
import angles.com.utils.ConstMethod;
import ng.max.slideview.SlideView;


import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static angles.com.utils.Const.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    boolean isGpsDialogShowing = false;
    private LocationManager manager;
    AlertDialog gpsAlertDialog;
    AlertDialog alv;


    ProgressBar progressBarView;
    TextView tv_time;
    EditText et_timer;
    int progress=0;
    CountDownTimer countDownTimer;
    int endTime = 250;
    SlideView btn_accept_req,btn_reject_req;

  //  BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // apply the drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // find and apply the nevigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // check the internet is on or not
        if (ConstMethod.isInternetOn(this)) {
            manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            intentFilter = new IntentFilter();
            intentFilter.addAction("ALERT_BROADCAST");
            registerReceiver(broadcastReceiver,intentFilter);


        } else {
            ConstMethod.NetworkAlert(this);

        }


    }

    // change the main fragement in to activity
    public void gotoMapFragmenta() {
        MapFragment frag = new MapFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, frag).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ConstMethod.isInternetOn(this)) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //  mLocationPermissionGranted = true;
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    ShowGpsDialog();

                } else {
                    removeGpsDialog();
                    gotoMapFragmenta();
                }
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }

        } else {
            ConstMethod.NetworkAlert(this);

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        //    mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //      mLocationPermissionGranted = true;

                }else{
                    finish();
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }/*else if(id==R.id.switchId){


return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent();
            intent.setAction("ALERT_BROADCAST");
            sendBroadcast(intent);
           // DialogCalling();
            // Handle the camera action
        } else if (id == R.id.nav_trp_hist) {
            startActivity(new Intent(this,MyTripHistoryActivity.class));

        } else if (id == R.id.nav_sche_tip) {

        } else if (id == R.id.nav_setting) {
            startActivity(new Intent(this,SettingsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // it show the gps alert when gps is not on in app
    private void ShowGpsDialog() {

        isGpsDialogShowing = true;
        AlertDialog.Builder gpsBuilder = new AlertDialog.Builder(
                this);
        gpsBuilder.setCancelable(false);
        gpsBuilder
                .setTitle(getString(R.string.dialog_no_gps))
                .setMessage(getString(R.string.dialog_no_gps_messgae))
                .setPositiveButton(getString(R.string.dialog_enable_gps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                                removeGpsDialog();
                                gotoMapFragmenta();
                            }
                        })

                .setNegativeButton(getString(R.string.dialog_exit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                removeGpsDialog();
                                finish();
                            }
                        });
        gpsAlertDialog = gpsBuilder.create();
        gpsAlertDialog.show();
    }

    private void removeGpsDialog() {
        if (gpsAlertDialog != null && gpsAlertDialog.isShowing()) {
            gpsAlertDialog.dismiss();
            isGpsDialogShowing = false;
            gpsAlertDialog = null;

        }
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"This is the broadcast",Toast.LENGTH_SHORT).show();
            DialogCalling();
        }
    };

    private void DialogCalling() {


        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View promptView = layoutInflater.inflate(R.layout.request_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);
        // setup a dialog window
        alertDialogBuilder.setCancelable(true);

        btn_reject_req = (SlideView) promptView.findViewById(R.id.btn_reject_req);
        btn_accept_req = (SlideView) promptView.findViewById(R.id.btn_accept_req);
        progressBarView = (ProgressBar) promptView.findViewById(R.id.view_progress_bar);
        tv_time= (TextView)promptView.findViewById(R.id.tv_timer);

        progressBarView.setProgress(progress);

        fn_countdown();

        // sliding button
        btn_accept_req.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Toast.makeText(MainActivity.this, "You Accept Request", Toast.LENGTH_SHORT).show();
                // vibrate the device
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);
                progressBarView.setProgress(0);
                countDownTimer.cancel();
                alv.dismiss();
                JobFragment fragment=new JobFragment();
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame,fragment).addToBackStack(null).commit();

            }
        });
        btn_reject_req.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                Toast.makeText(MainActivity.this, "You Reject Request", Toast.LENGTH_SHORT).show();

                // vibrate the device
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(100);

                countDownTimer.cancel();
                progressBarView.setProgress(0);
                // go to a new activity
                alv.dismiss();
            }
        });

        // create an alert dialog
        alv= alertDialogBuilder.create();
        alv.show();
    }

    private void fn_countdown() {


            String timeInterval ="60";
            progress = 1;
            endTime = Integer.parseInt(timeInterval); // up to finish time

        Log.e("End time cecking","=============>"+endTime);

            countDownTimer = new CountDownTimer(endTime * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                   /* setProgress(progress, endTime);
                    progress = progress + 1;*/
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);

                    Log.e("progressbar","-------->"+progress);

                    progress++;
                    progressBarView.setProgress((int)progress*100/(endTime * 1000/1000));


                    String newtime = minutes + ":" + seconds;
                    tv_time.setText(""+String.format("%d : %d ",
                            TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                }

                @Override
                public void onFinish() {
                    Log.e("progressbar2","-------->"+progress);
                    progress++;
                    progressBarView.setProgress(100);
                    Log.e("progressbar3","-------->"+progress);
                   /* setProgress(progress, endTime);*/
                    countDownTimer.cancel();
                    alv.dismiss();

                }
            };
            countDownTimer.start();


    }

    public void setProgress(int startTime, int endTime) {

        Log.e("Star time progress ","----->"+startTime);
        Log.e("end time progress","----->"+endTime);

       /* progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);*/

    }

}
