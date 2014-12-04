package com.example.joey.findmeifyoucan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;


public class CreateGameActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
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
        Intent intent = new Intent(this, Seeker.class);
        SeekBar players = (SeekBar) this.findViewById(R.id.seekBar);
        SeekBar time = (SeekBar) this.findViewById(R.id.seekBar2);
        intent.putExtra("numplayers",players.getProgress());
        intent.putExtra("timelimit",time.getProgress());
        this.startActivity(intent);
    }
}
