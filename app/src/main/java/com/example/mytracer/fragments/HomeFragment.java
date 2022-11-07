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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytracer.Constants;
import com.example.mytracer.MyLocation2;
import com.example.mytracer.R;
import com.example.mytracer.activities.LocationActivity;
import com.example.mytracer.activities.LockScreen;
import com.example.mytracer.service.MyService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {


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
        Intent intent = new Intent(getActivity(), MyService.class);
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
                LocationManager.GPS_PROVIDER, 2000, 2, locationListener);
    }

    private void getLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location",location.toString());
                Log.i("GetAccuracy",""+location.getAccuracy());

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