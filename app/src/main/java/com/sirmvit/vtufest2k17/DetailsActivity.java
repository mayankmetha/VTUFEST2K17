package com.sirmvit.vtufest2k17;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity{
    String loc;
    String jsonLoc;
    List<EventItem> data;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    final List<MapsItem> mMapsItems = MapsContent.ITEMS;
    Bitmap image;
    String map;
    CollapsingToolbarLayout mActionBarToolbar;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        loc = MapsActivity.Data.getTitle();
        image = getImage(loc);
        imageView = (ImageView) findViewById(R.id.toolbarImage);
        imageView.setImageBitmap(image);

        jsonLoc = MapsActivity.jsonData;

        try {
            JSONObject jObject = new JSONObject(jsonLoc);
            JSONArray jArray = jObject.getJSONArray(loc);
            data = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                EventItem mEventItem = new EventItem();
                mEventItem.name = jArray.getJSONObject(i).getString("eventName");
                mEventItem.day = jArray.getJSONObject(i).getString("eventDay");
                mEventItem.time = jArray.getJSONObject(i).getString("eventTime");
                data.add(mEventItem);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //////////////////////////////////////////////////////////////

        mActionBarToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        mActionBarToolbar.setTitle(loc);



        //fab
        final FloatingActionButton navifab = (FloatingActionButton) findViewById(R.id.nav_button);
        navifab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latlng = getLoc(loc);
                map = "http://maps.google.co.in/maps?q=loc:"+latlng.latitude+","+latlng.longitude+"("+loc+")";
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(map));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);

            }
        });

        //recyclerView
        recyclerView = (RecyclerView)findViewById(R.id.event_list);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new EventListAdapter(DetailsActivity.this,data);
        recyclerView.setAdapter(mAdapter);


    }

    //get image
    Bitmap getImage(String str) {
        //default image
        int res=-1;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.collegelogo);
        for (int i = 0; i < mMapsItems.size(); i++) {
            MapsItem current = mMapsItems.get(i);
            //get image for specific loc
            if (current.title.equals(str)) {
                res = current.id;
                break;
            }
        }
        switch(res) {
            case 0 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.collegelogo); break;
            case 1 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sciblk); break;
            case 2 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mainblk); break;
            case 3 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.newaudi); break;
            case 4 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.oldaudi); break;
            case 5 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.library); break;
            case 6 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.parking); break;
            case 7 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.coffee); break;
            case 8 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.rolls); break;
            case 9 : bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mechblk); break;
            case 10: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.civblk); break;
            case 11: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mba); break;
            case 12: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dental); break;
            case 13: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.work); break;
            case 14: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.gnd); break;
            case 15: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.caneshop); break;
            case 16: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.atm); break;
            case 17: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.canteen); break;
            case 18: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.busdpo); break;
            case 19: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ht); break;
            case 20: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.la); break;
            case 21: bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.mess); break;
            case 22: bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.food1); break;
            case 23: bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stall); break;
            case 24: bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.badcourt); break;
            case 25: bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.men); break;
            case 26: bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ladhostel); break;
        }
        return bitmap;
    }

    LatLng getLoc(String str) {
        int res=-1;
        for(int i = 0; i< mMapsItems.size();i++) {
            MapsItem current = mMapsItems.get(i);
            if(current.title.equals(str)) {
                res = current.id;
                break;
            }
        }
        return mMapsItems.get(res).navi;
    }

}
