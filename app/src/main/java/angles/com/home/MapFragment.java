package angles.com.home;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import angles.com.MainActivity;
import angles.com.R;
import angles.com.home.adapter.PlacesAutoCompleteAdapter;
import angles.com.home.model.Route;
import angles.com.home.model.Step;
import angles.com.utils.Const;
import angles.com.utils.LocationHelper;
import angles.com.utils.PolyLineUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static angles.com.utils.Const.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationHelper.OnLocationReceived {

    String TAG = "MapFragment";
    private GoogleMap mMap;
    private View view;
    MainActivity activity;
    private float currentZoom = -1;
    boolean mLocationPermissionGranted;
    private LocationHelper locHelper;
    private Location myLocation;
    private LatLng curretLatLng;
    ImageButton btn_mylocation;
    private Marker markerDestination, markerSource;
    LatLng destlatLang = new LatLng(21.5222,
            70.4579);
    Route route;
    private ArrayList<LatLng> points;
    private PolylineOptions lineOptions;
    private Polyline polyLine;
    private PlacesAutoCompleteAdapter adapterDestination;
    private PlacesAutoCompleteAdapter adapterSource;
    private AutoCompleteTextView edtPickupAdd, edtDestinationAdd;
    boolean isSource;
    Switch check_login;
    TextView txt_login;
    int islogin;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        activity = (MainActivity) getActivity();
        getLocationPermission();
        intiId(view);
        bindMethode();
        return view;
    }
    private void bindMethode() {
        btn_mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myLocation != null) {
                    LatLng latLang = new LatLng(myLocation.getLatitude(),
                            myLocation.getLongitude());

                    if(markerSource==null){
                        setMarker(latLang);
                    }
                    animateCameraToMarker(latLang, true);

                    drawPath(latLang, destlatLang);
                }
            }
        });



        check_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean bChecked) {
                if (bChecked) {
                    txt_login.setText("online");
                    islogin=1;
                } else {
                    txt_login.setText("offline");
                    islogin=0;
                }
            }
        });


    }

    public void setMarker(LatLng latLng) {
        if (mMap!=null)
        { markerSource = mMap.addMarker(new MarkerOptions()
                .position(
                        new LatLng(latLng.latitude,
                                latLng.longitude))
                .title(activity.getResources().getString(
                        R.string.text_source_pin_title))
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.pin_client)));

        }

    }

    private void setMarker(LatLng latLng, boolean isSource) {
        if (!MapFragment.this.isVisible())
            return;
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            // inputMethodManager.hideSoftInputFromWindow(getActivity()
            // .getCurrentFocus().getWindowToken(), 0);
            //activity.hideKeyboard();
        }

        if (latLng != null && mMap != null) {
            if (isSource) {
                if (markerSource == null) {
                    markerSource = mMap.addMarker(new MarkerOptions()
                            .position(
                                    new LatLng(latLng.latitude,
                                            latLng.longitude))
                            .title(activity.getResources().getString(
                                    R.string.text_source_pin_title))
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.pin_client)));
                    // markerSource.setDraggable(true);
                } else {
                    markerSource.setPosition(latLng);
                }
                CameraUpdateFactory.newLatLng(latLng);

            } else {
                if (markerDestination == null) {
                    MarkerOptions opt = new MarkerOptions();
                    opt.position(latLng);
                    opt.title(activity.getResources().getString(
                            R.string.text_destination_pin_title));
                    opt.icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.pin_des));
                    markerDestination = mMap.addMarker(opt);

                    markerDestination.setDraggable(true);

                    if (markerSource != null) {
                        LatLngBounds.Builder bld = new LatLngBounds.Builder();

                        bld.include(new LatLng(
                                markerSource.getPosition().latitude,
                                markerSource.getPosition().longitude));
                        bld.include(new LatLng(
                                markerDestination.getPosition().latitude,
                                markerDestination.getPosition().longitude));
                        LatLngBounds latLngBounds = bld.build();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                                latLngBounds, 30));
                    } else {
                        CameraUpdateFactory.newLatLng(latLng);
                    }

                } else {
                    markerDestination.setPosition(latLng);
                }
            }

            // getAddressFromLocation(markerSource.getPosition(), etSource, activity);
        } else {
            Toast.makeText(getActivity(),
                    activity.getString(R.string.dialog_no_location_service),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void intiId(View view) {
        btn_mylocation = view.findViewById(R.id.btn_mylocation);
        check_login=view.findViewById(R.id.check_login);
        txt_login=view.findViewById(R.id.txt_login);
       // mapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locHelper = new LocationHelper(getContext());
        locHelper.setLocationReceivedLister(this);
        locHelper.onStart();
    }



    private void BindMap(View view) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFrg);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        //  LatLng sydney = new LatLng(22.2296, 70.8685);
        // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //  mLocationPermissionGranted = true;
            BindMap(view);
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);


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

                }
            }
        }
        //  updateLocationUI();
    }


    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                // mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onLocationReceived(LatLng latlong) {
        Log.d(TAG, "*** onLocationReceived ");

    }

    @Override
    public void onLocationReceived(Location location) {
        if (location != null) {
            Log.d(TAG, "*** onLocationReceived location" + location);
            // drawTrip(latlong);
            myLocation = location;
        }
    }

    @Override
    public void onConntected(Bundle bundle) {
        Log.d(TAG, "*** onConntected ");

    }

    @Override
    public void onConntected(Location location) {
        if (location != null) {
            Log.d(TAG, "*** onConntected location" + location);

            myLocation = location;

            // isLocationEnable = true;
            LatLng latLang = new LatLng(location.getLatitude(),
                    location.getLongitude());
            curretLatLng = latLang;
            Log.d(TAG, "*** onConntected curretLatLng" + curretLatLng);
            setMarker(latLang);
            animateCameraToMarker(curretLatLng,false);
            //  getAllProviders(latLang);
            animateCameraToMarker(latLang, false);
            //   getAddressFromLocation(latLang, etSource, activity);
        } else {
            //  activity.showLocationOffDialog();
        }
    }

    private void animateCameraToMarker(LatLng latLng, boolean isAnimate) {
        try {
            Log.d(TAG, "***  animateCameraToMarker" + latLng);

            //etSource.setFocusable(false);
            //  etSource.setFocusableInTouchMode(false);
            CameraUpdate cameraUpdate = null;

            cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            if (cameraUpdate != null && mMap != null) {
                if (isAnimate)
                    mMap.animateCamera(cameraUpdate);
                else
                    mMap.moveCamera(cameraUpdate);
            }
            //    etSource.setFocusable(true);
            // etSource.setFocusableInTouchMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMarkerOnRoad(LatLng sourceLat, LatLng destinationLat) {
        Log.d(TAG, "*** drawPath " + sourceLat + " desination lat" + destinationLat);



    }



    public void drawPath(LatLng sourceLat, LatLng destinationLat) {
        Log.d(TAG, "*** drawPath " + sourceLat + " desination lat" + destinationLat);


        String url = getDirectionsUrl(sourceLat, destinationLat);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    class DownloadTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            Log.d(TAG, "*** DownloadTask url" + url);

            try {
                data = downloadUrl(url[0]);
                Log.d(TAG, "*** DownloadTask data" + data);

            } catch (Exception e) {
                Log.d(TAG, "Background Task" + e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, String> {

        // Parsing the data in non-ui thread
        @Override
        protected String doInBackground(String... jsonData) {

            JSONObject jObject;
            String routes = "";

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d(TAG, "*** ParserTask" + jObject.toString());
                routes=jObject.toString();
              /*  DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);*/
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "*** ParserTask result" + result);

            if (!result.isEmpty()) {
                Log.d(TAG, "*** ParserTask result" + result.toString());

                route = new Route();
                parseRoute(result, route);

                final ArrayList<Step> step = route.getListStep();
                System.out.println("**** step size=====> " + step.size());
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                for (int i = 0; i < step.size(); i++) {

                    List<LatLng> path = step.get(i).getListPoints();
                    System.out.println("*** step =====> " + i + " and "
                            + path.size());
                    points.addAll(path);

                }

                if (polyLine != null)
                    polyLine.remove();
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(activity.getResources().getColor(R.color.green)); // #00008B
                // rgb(0,0,139)

                if (lineOptions != null && mMap != null) {
                    polyLine = mMap.addPolyline(lineOptions);

                 /*   LatLngBounds.Builder bld = new LatLngBounds.Builder();

                    bld.include(markerSource.getPosition());
                    bld.include(markerDestination.getPosition());
                    LatLngBounds latLngBounds = bld.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                            latLngBounds, 30));*/
                }
            }


        }
    }

    public Route parseRoute(String response, Route routeBean) {

        try {
            Step stepBean;
            JSONObject jObject = new JSONObject(response);
            JSONArray jArray = jObject.getJSONArray(Const.Params.ROUTES);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject innerjObject = jArray.getJSONObject(i);
                if (innerjObject != null) {
                    JSONArray innerJarry = innerjObject.getJSONArray(Const.Params.LEGS);
                    for (int j = 0; j < innerJarry.length(); j++) {

                        JSONObject jObjectLegs = innerJarry.getJSONObject(j);
                        routeBean.setDistanceText(jObjectLegs.getJSONObject(
                                Const.Params.DISTANCE).getString(Const.Params.TEXT));
                        routeBean.setDistanceValue(jObjectLegs.getJSONObject(
                                Const.Params.DISTANCE).getInt(Const.Params.VALUE));

                        routeBean.setDurationText(jObjectLegs.getJSONObject(
                                Const.Params.DURATION).getString(Const.Params.TEXT));
                        routeBean.setDurationValue(jObjectLegs.getJSONObject(
                                Const.Params.DURATION).getInt(Const.Params.VALUE));

                        routeBean.setStartAddress(jObjectLegs
                                .getString(Const.Params.START_ADDRESS));
                        routeBean.setEndAddress(jObjectLegs
                                .getString(Const.Params.END_ADDRESS));

                        routeBean.setStartLat(jObjectLegs.getJSONObject(
                                Const.Params.START_LOCATION).getDouble(Const.Params.LATITUDE));
                        routeBean.setStartLon(jObjectLegs.getJSONObject(
                                Const.Params.START_LOCATION).getDouble(Const.Params.LONGITUDE));

                        routeBean.setEndLat(jObjectLegs.getJSONObject(
                                Const.Params.END_LOCATION).getDouble(Const.Params.LATITUDE));
                        routeBean.setEndLon(jObjectLegs.getJSONObject(
                                Const.Params.END_LOCATION).getDouble(Const.Params.LONGITUDE));

                        JSONArray jstepArray = jObjectLegs.getJSONArray(Const.Params.STEPS);
                        if (jstepArray != null) {
                            for (int k = 0; k < jstepArray.length(); k++) {
                                stepBean = new Step();
                                JSONObject jStepObject = jstepArray
                                        .getJSONObject(k);
                                if (jStepObject != null) {

                                    stepBean.setHtml_instructions(jStepObject
                                            .getString(Const.Params.HTML_INSTRUCTIONS));
                                    stepBean.setStrPoint(jStepObject
                                            .getJSONObject(Const.Params.POLYLINE).getString(
                                                    Const.Params.POINTS));
                                    stepBean.setStartLat(jStepObject
                                            .getJSONObject(Const.Params.START_LOCATION)
                                            .getDouble(Const.Params.LATITUDE));
                                    stepBean.setStartLon(jStepObject
                                            .getJSONObject(Const.Params.START_LOCATION)
                                            .getDouble(Const.Params.LONGITUDE));
                                    stepBean.setEndLat(jStepObject
                                            .getJSONObject(Const.Params.END_LOCATION)
                                            .getDouble(Const.Params.LATITUDE));
                                    stepBean.setEndLong(jStepObject
                                            .getJSONObject(Const.Params.END_LOCATION)
                                            .getDouble(Const.Params.LONGITUDE));

                                    stepBean.setListPoints(new PolyLineUtils()
                                            .decodePoly(stepBean.getStrPoint()));
                                    routeBean.getListStep().add(stepBean);
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return routeBean;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";
        String mapKey = getString(R.string.google_maps_key);

        // Building the url to the web service +"&key="+mapKey
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&key="+mapKey;
        Log.d(TAG, "*** getDirectionsUrl " + url);


        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private LatLng getLocationFromAddress(final String place) {
        LatLng loc = null;
        Geocoder gCoder = new Geocoder(getActivity());
        try {
            final List<Address> list = gCoder.getFromLocationName(place, 1);
            if (list != null && list.size() > 0) {
                loc = new LatLng(list.get(0).getLatitude(), list.get(0)
                        .getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loc;
    }

}