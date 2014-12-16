package com.example.joey.findmeifyoucan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;


public class CreateGameActivity extends Activity {

    private static int MAXTIME=20, MINTIME = 1;
    private static int MAXPLAYERS = 10, MINPLAYERS = 2;
    private static int MAXRAD=1200, MINRAD=100;

    private GoogleMap map;

    private Circle circle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        init();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        drawMap(map);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void goToGame(View view){

        SeekBar playersbar = (SeekBar) this.findViewById(R.id.seekBar);
        SeekBar timebar = (SeekBar) this.findViewById(R.id.seekBar2);
        SeekBar radbar = (SeekBar)  this.findViewById(R.id.seekBar3);

        int players = (int)(MINPLAYERS+((double)playersbar.getProgress()/playersbar.getMax())*(MAXPLAYERS-MINPLAYERS));
        int time = (int)(MINTIME+((double)timebar.getProgress()/timebar.getMax())*(MAXTIME-MINTIME));
        int rad = (int)(MINRAD+((double)radbar.getProgress()/radbar.getMax())*(MAXRAD-MINRAD));
        Intent intent = (time%2==0)?new Intent(this,Seeker.class):new Intent(this,Hider.class);

        intent.putExtra("numplayers",players);
        intent.putExtra("timelimit",time);
        intent.putExtra("radius",rad);


        this.startActivity(intent);
    }

    public void init(){//init block
        //set up numplayers bar
        SeekBar playersbar = (SeekBar) this.findViewById(R.id.seekBar);
        playersbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int players = seekBar.getProgress();
                players = (int)(MINPLAYERS+((double)players/seekBar.getMax())*(MAXPLAYERS-MINPLAYERS));
                ((TextView)(CreateGameActivity.this.findViewById(R.id.textShowPlayers))).setText(String.valueOf(players));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //setup time bar
        SeekBar timebar = (SeekBar) this.findViewById(R.id.seekBar2);
        timebar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int time = seekBar.getProgress();
                time = (int)(MINTIME+((double)time/seekBar.getMax())*(MAXTIME-MINTIME));
                ((TextView)(CreateGameActivity.this.findViewById(R.id.textShowTime))).setText(String.valueOf(time));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });


        //setup radius bar
        SeekBar radbar = (SeekBar) this.findViewById(R.id.seekBar3);
        radbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int rad = seekBar.getProgress();
                rad = (int)(MINRAD+((double)rad/seekBar.getMax())*(MAXRAD-MINRAD));
                circle.setRadius(rad);
                ((TextView)(CreateGameActivity.this.findViewById(R.id.textShowRadius))).setText(String.valueOf(rad));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar){ }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }//end init

    private void drawMap(GoogleMap map){
        map.setPadding(5,5,5,5);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setMyLocationEnabled(true);

        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location
        Location loc = locationManager.getLastKnownLocation(provider);
        Log.d("loc", loc.toString());
        LatLng latlng = new LatLng(loc.getLatitude(),loc.getLongitude());

        CircleOptions circleOptions = new CircleOptions()
                .center(latlng)
                .strokeColor(Color.argb(255, 250, 30, 30))
                .fillColor(Color.argb(100,250,30,30))
                .radius(100); // In meters


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 13));
        circle = map.addCircle(circleOptions);
    }
}
