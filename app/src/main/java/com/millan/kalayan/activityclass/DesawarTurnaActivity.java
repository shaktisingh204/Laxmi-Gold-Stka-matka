package com.millan.kalayan.activityclass;


import static com.millan.kalayan.shareprefclass.Utility.BroadCastStringForAction;
import static com.millan.kalayan.shareprefclass.Utility.myReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.R;
import com.millan.kalayan.shareprefclass.Utility;
import com.millan.kalayan.shareprefclass.YourService;

public class DesawarTurnaActivity extends AppCompatActivity {

    private Intent intent;
    private IntentFilter mIntentFilter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desawar_turna);
        intVariables();
        loadData();
    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void intVariables() {
        MaterialTextView dataConText = findViewById(R.id.internet_text);
        toolbar = findViewById(R.id.tool_bar);
        Utility utility = new Utility(dataConText);
        String games = getIntent().getStringExtra(getString(R.string.game));
        intent = new Intent(this, DesawarBPActivity.class);
        intent.putExtra("games", games);
    }

    public void leftDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 12);
        startActivity(intent);
    }

    public void rightDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 13);
        startActivity(intent);
    }

    public void jodiDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 14);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

}