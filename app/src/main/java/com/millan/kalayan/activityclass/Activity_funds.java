package com.millan.kalayan.activityclass;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.appbar.MaterialToolbar;
import com.millan.kalayan.R;

public class Activity_funds extends AppCompatActivity {
    private CardView card1, card2, card3, card4, card5, card42, card43;
    private MaterialToolbar toolbar;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funds);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#05020b"));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        card1 = findViewById(R.id.card1);
        card2 = findViewById(R.id.card2);
        card3 = findViewById(R.id.card3);
        card4 = findViewById(R.id.card4);
        card5 = findViewById(R.id.card5);
        card42 = findViewById(R.id.card42);
        card43 = findViewById(R.id.card43);
        toolbar = findViewById(R.id.toolbar);
        ToolbarMethod();
        card1.setOnClickListener(v -> {
            Intent addFunds = new Intent(Activity_funds.this, AddCoinActivity.class);
            startActivity(addFunds);
        });
        card2.setOnClickListener(v -> {
            Intent withdrawPoints = new Intent(Activity_funds.this, TakeOutActivity.class);
            startActivity(withdrawPoints);
        });
        card3.setOnClickListener(v -> {
            Intent manageBank = new Intent(Activity_funds.this, BActivity.class);
            startActivity(manageBank);
        });
        card4.setOnClickListener(v -> {
            Intent managePaytm = new Intent(Activity_funds.this, UPIDActivity.class);
            managePaytm.putExtra(getString(R.string.upi), 1);
            startActivity(managePaytm);
        });
        card42.setOnClickListener(v -> {
            Intent manageGooglePay = new Intent(Activity_funds.this, UPIDActivity.class);
            manageGooglePay.putExtra(getString(R.string.upi),3);
            startActivity(manageGooglePay);
        });
        card43.setOnClickListener(v -> {
            Intent managePhonePay = new Intent(Activity_funds.this, UPIDActivity.class);
            managePhonePay.putExtra(getString(R.string.upi), 2);
            startActivity(managePhonePay);
        });
        card5.setOnClickListener(v -> {
            Intent transferPoints = new Intent(Activity_funds.this, TCoinActivity.class);
            startActivity(transferPoints);
        });
    }
    private void ToolbarMethod() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
