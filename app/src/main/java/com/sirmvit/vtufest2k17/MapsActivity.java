package com.sirmvit.vtufest2k17;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    //Data that is shared between the two activities.
    public static Marker Data = null;
    public static String jsonData = null;
    private PrefManager mPrefManager;
    Boolean isInitLaunch;
    PackageInfo packageInfo;
    private static double newVersion;
    private static double curVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefManager = new PrefManager(this);
        isInitLaunch = mPrefManager.isFirstTimeLaunch();
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
            return true;
        }
        /*
        if(id == R.id.action_reset) {
            //reset SharedPreference
            mPrefManager.setFirstTimeLaunch(true);
            //restart app
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        }
        */
        if (id == R.id.update) {
            updater();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near College.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMap.setOnMarkerClickListener(this);
        googleMap.clear();
        final List<MapsItem> list = MapsContent.ITEMS;
        //final List<MapsItem> hts = MapsContent.HTS;
        //style Map
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json_retro));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        for (int i = 0; i < list.size(); i++) {
            MapsItem current = list.get(i);
            //style Marker
            googleMap.addMarker(new MarkerOptions().position(current.position)
                    .title(current.title)
                    .icon(BitmapDescriptorFactory.fromBitmap(getMarker(current.title))));
            if (i == 0) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(current.position)
                        .zoom(18)
                        .bearing(270)
                        .tilt(60)
                        .build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
    }

    private Bitmap getMarker(String str) {
        //text style
        IconGenerator iconfactory = new IconGenerator(this);
        iconfactory.setTextAppearance(R.style.MapLabel);
        iconfactory.setBackground(null);
        //get marker bitmap
        Bitmap base = BitmapFactory.decodeResource(getResources(), R.drawable.marker_vector);
        //generate text as bitmap
        Bitmap text = iconfactory.makeIcon(str);
        //final marker
        //Bitmap send = Bitmap.createBitmap(text.getWidth(),base.getHeight(),base.getConfig()); - old, DON'T DELETE
        Bitmap send = Bitmap.createBitmap(text.getWidth(), (text.getHeight() + base.getHeight()), base.getConfig());
        //overlay text on base
        Canvas canvas = new Canvas(send);
        float left = (float) (text.getWidth() * 0.5 - base.getWidth() * 0.5);
        //float top = (float) (base.getHeight()*0.5-text.getHeight()*0.5); - old, DON'T DELETE
        float top = (float) (text.getHeight() * 0.5);
        canvas.drawBitmap(base, left, top, null);
        canvas.drawBitmap(text, new Matrix(), null);
        return send;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        Data = arg0;
        jsonData = loadJSONFromAsset();

        //Start an activity when a marker is selected.
        Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
        startActivity(i);
        return true;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (android.os.Build.VERSION.SDK_INT >= 21) {
                            finishAndRemoveTask();
                        } else {
                            finish();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        if (isInitLaunch) {
            startActivity(new Intent(MapsActivity.this, FirstRunActivity.class));
            finish();
        }
        isNewUpdatePresent();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getApplicationContext().getResources().openRawResource(R.raw.event_details);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void updater() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            isNewUpdatePresent();
            new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            try {
                packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            curVersion = Double.parseDouble(packageInfo.versionName);
            if (newVersion > curVersion) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("New update available. Want do download now?")
                        .setCancelable(false)
                        .setPositiveButton("Download & Install", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("No new updates")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No Internet Access")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void isNewUpdatePresent() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            try {
                URL url = new URL("https://raw.githubusercontent.com/mayankmetha/VTUFEST2K17/master/docs/version");
                new checkUpdate().execute(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private class checkUpdate extends AsyncTask<URL, Void, String> {
        @Override
        protected String doInBackground(URL... urls) {
            String str="";
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(urls[0].openStream()));
                str = in.readLine();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        @Override
        protected void onPostExecute(String result) {
            newVersion = Double.parseDouble(result);
        }
    }

}
