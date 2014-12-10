package com.example.joey.findmeifyoucan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class Seeker extends Activity {

    private TextView timeRemaining;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeker);

        //time and players are produced on a 0 to 100 scale from view
        int players = this.getIntent().getIntExtra("numplayers",4);
        int time = this.getIntent().getIntExtra("timelimit",10);

       // time = MINTIME+(time/100)*(MAXTIME-MINTIME);

        timeRemaining = (TextView) this.findViewById(R.id.time_remaining_count);
        timeRemaining.setText(String.valueOf(time)+":00");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seeker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void giveUp (View view){
        Intent intent = new Intent(this, DefaultActivity.class);
        startActivity(intent);
    }
}
