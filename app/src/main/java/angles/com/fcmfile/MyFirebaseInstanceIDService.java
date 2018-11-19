package angles.com.fcmfile;

import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import angles.com.utils.PreferenceHelper;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //private static final String TAG = "MyFirebaseIIDService";

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Displaying token on logcat
        Log.d(TAG, "***  Refreshed token: " + refreshedToken);
        new PreferenceHelper(this).putFirebaseToken(refreshedToken);


     //   sendRegistrationToServer(refreshedToken);

    }

}
