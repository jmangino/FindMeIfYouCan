package com.example.joey.findmeifyoucan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


public class CreateGameActivity extends Activity {

    private static int MAXTIME=20, MINTIME = 1;
    private static int MAXPLAYERS = 10, MINPLAYERS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_game);
        init();
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
        SeekBar playersbar = (SeekBar) this.findViewById(R.id.seekBar);
        SeekBar timebar = (SeekBar) this.findViewById(R.id.seekBar2);
        intent.putExtra("numplayers",playersbar.getProgress());
        intent.putExtra("timelimit",timebar.getProgress());
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
    }//end init
}
