package com.example.mytracer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mytracer.Constants;
import com.example.mytracer.R;
import com.example.mytracer.service.TimeService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    /**
     * FLOW OF THE APP.
     *
     */

    private TextView textView, myLocationTv, myLocationTv2;

    //public  static so that we can call timeServiceTv from MyService.java without necessarily creating object instance
    public  static TextView timeServiceTv;


    LocationManager locationManager;
    LocationListener locationListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        textView = view.findViewById(R.id.textView);
        myLocationTv=view.findViewById(R.id.myLocationTv);
        myLocationTv2=view.findViewById(R.id.myLocationTv2);
        timeServiceTv = view.findViewById(R.id.timeServiceTv);

        getLocation();

        getLocation2();
        
        startTimeService();
        return view;
    }

    private void startTimeService() {
        timeServiceTv.setText("Current Time: " + new Date());
        Intent intent = new Intent(getActivity(), TimeService.class);
        getActivity().startService(intent);// start Service
        //getActivity().stopService(intent);//stop Service

    }

    private void getLocation2() {
        myLocationTv2.setText("Loading location....");

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                //check location after 2 second.
                //if location changes by a distance of 2 metre, invoke locationListener
                LocationManager.GPS_PROVIDER, 2000, 2, locationListener);
    }

    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                Log.i("GetAccuracy",""+location.getAccuracy());

                //save location to db whenever it changes
                //saveUserToDB(latitude1, longitude1);

                myLocationTv.setText("Location: "+location.toString()+"\nGetAccuracy: "+location.getAccuracy()+"\nLatitude: "+location.getLatitude()+" Longitude: "+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    private void saveUserToDB(double latitude, double longitude) {
        String baseUrl = Constants.baseUrl;
        String url = baseUrl+"/locations/location/";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "Response: "+response.toString(), Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomeFragment", "Sending Location Error: "+error.getMessage() );
                Toast.makeText(getActivity(), "Sending Location Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                /**
                 {
                 "latitude": "28.8374",
                 "longitude": "38.283",
                 "date": "09-11-2022",
                 "time": "05:52 PM",
                 "user": 1
                 }
                 */


                //get date
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate = simpleDateFormat.format(date);

                String time = timeServiceTv.getText().toString();

                hashMap.put("latitude", ""+latitude);
                hashMap.put("longitude", ""+longitude);
                hashMap.put("date",currentDate);
                hashMap.put("time", time);
                hashMap.put("user", ""+Constants.userLocationID); //Constants.userLocationID

//                hashMap.put("latitude", "55.8374");
//                hashMap.put("longitude", "55.256");
//                hashMap.put("date","09-11-2022");
//                hashMap.put("time", "09:49");
//                hashMap.put("user", 7+""); //Constants.userLocationID

                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }


    /**-------------------------------------------------------------**/
    /*---------- Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {

        Context context;
        @Override
        public void onLocationChanged(Location loc) {


            myLocationTv2.setText("Loading location....");
            //pb.setVisibility(View.INVISIBLE);
            Toast.makeText(
                    getActivity(),
                    "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                            + loc.getLongitude(), Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("THIS", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("THIS", latitude);

            double latitude1 = loc.getLatitude();
            double longitude1 = loc.getLongitude();

            //save location to db whenever it changes
            saveUserToDB(latitude1, longitude1);

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    System.out.println(addresses.get(0).getLocality());
                    cityName = addresses.get(0).getLocality();
                }
            }
            catch (IOException e) {

                e.printStackTrace();
            }
            String s =  "Longitude: " +longitude + "\n" + "Latitude: " +latitude + "\n\nMy Current City is: "
                    + cityName;
            myLocationTv2.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }


}