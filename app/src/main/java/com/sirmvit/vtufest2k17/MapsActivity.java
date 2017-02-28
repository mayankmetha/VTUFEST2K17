package com.sirmvit.vtufest2k17;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback , GoogleMap.OnMarkerClickListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    //Data that is shared between the two activities.
    public static Marker Data = null;
    private PrefManager mPrefManager;
    Boolean isInitLaunch;

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
        if(id == R.id.action_reset) {
            //reset SharedPreference
            mPrefManager.setFirstTimeLaunch(true);
            //restart app
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        return id == R.id.action_setting || super.onOptionsItemSelected(item);

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

        //Place Markers
        final List<MapsItem> list = MapsContent.ITEMS;
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

    private Bitmap getMarker(String str){
        //text style
        IconGenerator iconfactory = new IconGenerator(this);
        iconfactory.setTextAppearance(R.style.MapLabel);
        iconfactory.setBackground(null);
        //get marker bitmap
        Bitmap base = BitmapFactory.decodeResource(getResources(),R.drawable.marker_vector);
        //generate text as bitmap
        Bitmap text = iconfactory.makeIcon(str);
        //final marker
        //Bitmap send = Bitmap.createBitmap(text.getWidth(),base.getHeight(),base.getConfig()); - old, DON'T DELETE
        Bitmap send = Bitmap.createBitmap(text.getWidth(),(text.getHeight()+base.getHeight()),base.getConfig());
        //overlay text on base
        Canvas canvas = new Canvas(send);
        float left = (float) (text.getWidth()*0.5-base.getWidth()*0.5);
        //float top = (float) (base.getHeight()*0.5-text.getHeight()*0.5); - old, DON'T DELETE
        float top = (float) (text.getHeight()*0.5);
        canvas.drawBitmap(base,left,top,null);
        canvas.drawBitmap(text, new Matrix(), null);
        return send;
    }

    @Override
    public boolean onMarkerClick(Marker arg0) {
        Data = arg0;
        //Start an activity when a marker is selected.
        Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
        startActivity(i);
        Toast.makeText(MapsActivity.this, arg0.getTitle(), Toast.LENGTH_SHORT).show();// display toast
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MapsActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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
            Toast.makeText(MapsActivity.this, "First Run", Toast.LENGTH_SHORT).show();
        }
        mPrefManager.setFirstTimeLaunch(false);
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

}
