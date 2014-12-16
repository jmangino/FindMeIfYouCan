package com.example.joey.findmeifyoucan;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.CircleOptionsCreator;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Timer;
import java.util.TimerTask;




public class Hider extends Activity implements GestureDetector.OnGestureListener {

    private TextView timeRemaining;

    private long timeend;

    private int radius;

    private GestureDetector detector;

    private ViewSwitcher switcher;

    private GoogleMap map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hider);
        detector = new GestureDetector(this, this);

        int players = this.getIntent().getIntExtra("numplayers",4);
        int time = this.getIntent().getIntExtra("timelimit",10);
        radius = this.getIntent().getIntExtra("radius",1000);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.hider_map)).getMap();

        setUpMap(map);

        startCountdown(time);

        switcher = (ViewSwitcher) this.findViewById(R.id.hider_switcher);


    }

    @Override
    protected void onPause(){
        Notification.Builder b = new Notification.Builder(this);
        b.setContentTitle("FMIYC");
        b.setContentText("Game Still Running");
        b.setSmallIcon(R.drawable.ic_launcher);
        Notification notification = b.build();
        NotificationManager nm =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1,notification);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void giveUp (View view){
        map.clear();
        Intent intent = new Intent(this, DefaultActivity.class);
        startActivity(intent);
    }

    private void setUpMap(GoogleMap map){
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
        Log.d("loc",loc.toString());
        LatLng latlng = new LatLng(loc.getLatitude(),loc.getLongitude());

        CircleOptions circleOptions = new CircleOptions()
                .center(latlng)
                .strokeColor(Color.argb(255,250,30,30))
                .fillColor(Color.argb(100,250,30,30))
                .radius(1000); // In meters


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,13));
        map.addCircle(circleOptions);

    }

    private void startCountdown(int time){
        timeRemaining = (TextView) this.findViewById(R.id.hider_time_remaining_count);
        timeRemaining.setText(String.valueOf(time)+":00");
        //START TIMER
        timeend = System.currentTimeMillis() + time * 1000 * 60 ;
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Hider.this.runOnUiThread(new Runnable() {
                    public void run(){
                        long remaining = Math.max(0, timeend - System.currentTimeMillis());
                        int minutes = (int) (remaining / (60 * 1000));
                        int seconds = (int) (remaining % (60 * 1000) / 1000);
                        String sm = String.valueOf(minutes);
                        String ss = (seconds >= 10) ? String.valueOf(seconds) : "0" + String.valueOf(seconds);
                        StringBuilder sb = new StringBuilder();
                        sb.append(sm);
                        sb.append(':');
                        sb.append(ss);
                        TextView timeremaining = (TextView) Hider.this.findViewById(R.id.hider_time_remaining_count);
                        timeremaining.setText(sb.toString());
                    }
                });//run on ui thread
                if(timeend-System.currentTimeMillis() < 0){
                    //time is over

                    //end repaint time thread if time is done
                    this.cancel();
                }
            }//close run
        },0,333);//delay 0 repeat w 333ms
    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        //relay touch events to gesture events
        detector.onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please return to the game.")
                .setCancelable(false)
                .setTitle("Out of Bounds")
                .setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float intensity = Math.abs(e1.getRawX() - e2.getRawX());
        float direction = intensity / Math.abs(e1.getRawY() - e2.getRawY());
        if(intensity < 20 && direction > 1){
            return false;
        }
        if(e1.getRawX() > e2.getRawX()){
            switcher.showNext();
        }else{
            switcher.showPrevious();
        }
        return false;
    }

    private void addMessage(String s){
        TextView text_top = (TextView) this.findViewById(R.id.hider_message1);
        SimpleDateFormat format = new SimpleDateFormat("h:mm:ss");
        text_top.append("\n\n"+new Formatter()+format.format(new Date())+"\n"+s);
    }
}
