package com.cosmic.mapsexample.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cosmic.mapsexample.R;
import com.cosmic.mapsexample.model.Events;
import com.google.android.gms.maps.model.LatLng;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TestingJSON extends AppCompatActivity {

    ArrayList<Events> eventsList;

    Context ctx;
    AsyncHttpClient client;


    public static final String URL = "http://sf.funcheap.com/feed/json/?secret=k5S4y87359Ot=&paged=1";
    public static final String MAP_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    public static final String TAG = "TestingJSON";
     @Override
    protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_testing_json);

         eventsList = new ArrayList<>();
         ctx = TestingJSON.this;


         client = new AsyncHttpClient();
         client.get(this, URL, null, new JsonHttpResponseHandler() {
             @Override
             public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                 Log.i(TAG, response.toString());
                 try {
                     eventsList.addAll(Events.fromJSON(response.getJSONArray("events"), ctx));
                     getlatlngFilled();

                     Log.i(TAG, "" + eventsList.size());
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 Log.i(TAG, errorResponse.toString());
             }

             @Override
             public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                 Log.i(TAG, responseString);
             }
         });

         Log.i(TAG, "" + eventsList.size());
     }


     private void getlatlngFilled(){
         for (int i = 0; i < eventsList.size(); i++) {

             final int pos = i;

             RequestParams params = new RequestParams();
             params.add("address", eventsList.get(i).getVenue().getAddress());
             params.add("key", "AIzaSyCEOZZElqbmWcXYrTmL80QShR50VHfeg1E");

             client.get(this, MAP_URL, params, new JsonHttpResponseHandler() {
                 @Override
                 public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                     Log.i(TAG, response.toString());
                     try {

                         double lat = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                         double lng = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                         LatLng latlng = new LatLng(lat,lng);
                         eventsList.get(pos).setLatitude(lat);
                         eventsList.get(pos).setLongitude(lng);
                         eventsList.get(pos).setPosition(latlng);
                         Log.i(TAG, ""+lat);
                         Log.i(TAG, ""+lng);

                         if(pos==eventsList.size()-1){
                             Intent intent = new Intent();
                             Bundle bundle = new Bundle();
                             bundle.putParcelableArrayList("arraylist",eventsList);
                             intent.putExtra("bundle",bundle);
                             intent.setClass(ctx,ClusteringDemoActivity.class);
                             startActivity(intent);
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                     Log.i(TAG, errorResponse.toString());
                 }

                 @Override
                 public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                     Log.i(TAG, responseString);
                 }
             });


         }


     }






}
