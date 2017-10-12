/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cosmic.mapsexample.activity;

import android.util.Log;
import android.widget.Toast;

import com.cosmic.mapsexample.model.Events;
import com.cosmic.mapsexample.utils.EventRenderer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONException;

import java.io.InputStream;
import java.util.List;

/**
 * Simple activity demonstrating ClusterManager.
 */
public class ClusteringDemoActivity extends BaseDemoActivity implements ClusterManager.OnClusterClickListener<Events>,
        ClusterManager.OnClusterInfoWindowClickListener<Events>,
        ClusterManager.OnClusterItemClickListener<Events>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Events>
    {
        private ClusterManager<Events> mClusterManager;

        @Override
        protected void startDemo() {
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.7749,-122.4194), 10));
            mClusterManager = new ClusterManager<Events>(this, getMap());
            mClusterManager.addItems(eventlist);
            setListeners();
            setMapUISettings();
            mClusterManager.setRenderer(new EventRenderer(getApplicationContext(),getMap(),mClusterManager));
            mClusterManager.cluster();


        }

        private void setListeners(){
            getMap().setOnCameraChangeListener(mClusterManager);
            getMap().setOnMarkerClickListener(mClusterManager);
            getMap().setOnInfoWindowClickListener(mClusterManager);
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        }

        private void setMapUISettings(){
            getMap().getUiSettings().setZoomControlsEnabled(true);
            getMap().getUiSettings().setRotateGesturesEnabled(false);
            getMap().getUiSettings().setScrollGesturesEnabled(true);
            getMap().getUiSettings().setTiltGesturesEnabled(false);
        }


        @Override
        public boolean onClusterClick(Cluster<Events> cluster) {

            String firstName = cluster.getItems().iterator().next().getTitle();
            Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();
            Log.i("Cluster","onClusterClick "+cluster.getItems().size());
            return false;
        }

        @Override
        public void onClusterInfoWindowClick(Cluster<Events> cluster) {
            // Does nothing, but you could go to a list of the users.
        }

        @Override
        public boolean onClusterItemClick(Events event) {
            Toast.makeText(this, event.getTitle(), Toast.LENGTH_SHORT).show();

            return false;
        }

        @Override
        public void onClusterItemInfoWindowClick(Events event) {

        }



    }