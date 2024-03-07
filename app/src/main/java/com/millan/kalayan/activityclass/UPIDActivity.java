package com.millan.kalayan.activityclass;

import static com.millan.kalayan.shareprefclass.Utility.BroadCastStringForAction;
import static com.millan.kalayan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.apiclass.ApiClass;
import com.millan.kalayan.responseclass.DataMain;
import com.millan.kalayan.shareprefclass.SharPrefClass;
import com.millan.kalayan.shareprefclass.Utility;
import com.millan.kalayan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UPIDActivity extends AppCompatActivity {

    private TextInputEditText setTextUPI;
    private MaterialToolbar toolbar;
    private int anInt = 0;
    private Call<DataMain> call;
    private ProgressBar progressBar;
    private IntentFilter mIntentFilter;
    private LinearLayout layoutWithdrawPoints;
    private ImageView money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.millan.kalayan.R.layout.activity_u_p_i_d);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#05020b"));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        intVariables();
        loadData();
        ToolbarMethod();
        layoutWithdrawPoints = findViewById(com.millan.kalayan.R.id.layoutWithdrawPoints);
        money = findViewById(com.millan.kalayan.R.id.money);
        setRoundedCorners(layoutWithdrawPoints, "FFFFFF", "FFFFFF", 360, 0);
        setRoundedCorners(money, "FDB827", "FFFFFF", 360, 0);

    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        anInt = getIntent().getIntExtra(getString(com.millan.kalayan.R.string.upi),0);
        switch (anInt){
            case 1:
                setTextUPI.setText(SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_P_UPI_ID));
                break;
            case 2:
                setTextUPI.setText(SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_PP_UPI_ID));
                break;
            case 3:
                setTextUPI.setText(SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_G_PAY_UPI_ID));
                break;
        }
    }

    private void intVariables() {
        setTextUPI = findViewById(com.millan.kalayan.R.id.in_txt_upi);
        toolbar = findViewById(com.millan.kalayan.R.id.toolbar);
        progressBar = findViewById(com.millan.kalayan.R.id.progressBar);

        MaterialTextView dataConText = findViewById(com.millan.kalayan.R.id.dataConText);
        Utility utility = new Utility(dataConText);
    }

    public void sUp(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(setTextUPI.getText().toString())){
            Snackbar.make(view, getString(com.millan.kalayan.R.string.enter_valid_number), 2000).show();
            return;
        }
        if (setTextUPI.getText().toString().length()<10){
            Snackbar.make(view, getString(com.millan.kalayan.R.string.enter_valid_number), 2000).show();
            return;
        }
        if (YourService.isOnline(this))
            UpiUpMethod();
        else Toast.makeText(this, getString(com.millan.kalayan.R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();

    }

    private void UpiUpMethod() {
        progressBar.setVisibility(View.VISIBLE);
        switch (anInt){
            case 1:
                call = ApiClass.getClient().UpgrdePytm(SharPrefClass.getLoginInToken(this), setTextUPI.getText().toString());
                break;
            case 2:
                call = ApiClass.getClient().UpgradePhnePe(SharPrefClass.getLoginInToken(this), setTextUPI.getText().toString());
                break;
            case 3:
                call = ApiClass.getClient().upgradeGpay(SharPrefClass.getLoginInToken(this), setTextUPI.getText().toString());
                break;
        }
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(@NonNull Call<DataMain> call, @NonNull Response<DataMain> response) {
                if (response.isSuccessful()){
                    DataMain dataMain = response.body();

                    if (dataMain.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(UPIDActivity.this);
                        Toast.makeText(UPIDActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UPIDActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataMain.getStatus().equalsIgnoreCase(getString(com.millan.kalayan.R.string.success))){
                        progressBar.setVisibility(View.GONE);
                        switch (anInt){
                            case 1:
                                SharPrefClass.setPrefrenceStrngData(UPIDActivity.this, SharPrefClass.KEY_P_UPI_ID, setTextUPI.getText().toString());
                                break;
                            case 2:
                                SharPrefClass.setPrefrenceStrngData(UPIDActivity.this, SharPrefClass.KEY_PP_UPI_ID, setTextUPI.getText().toString());
                                break;
                            case 3:
                                SharPrefClass.setPrefrenceStrngData(UPIDActivity.this, SharPrefClass.KEY_G_PAY_UPI_ID, setTextUPI.getText().toString());
                                break;
                        }
                        onBackPressed();
                    }
                    Toast.makeText(UPIDActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UPIDActivity.this, getString(com.millan.kalayan.R.string.response_error), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<DataMain> call, @NonNull Throwable t) {
                System.out.println("UPI Update Error "+t);
                Toast.makeText(UPIDActivity.this, getString(com.millan.kalayan.R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void setRoundedCorners(View linearLayout, String color, String scolor, float radius, int storck) {
        // Create a new GradientDrawable
        GradientDrawable gradientDrawable = new GradientDrawable();
        // Set the shape to a rectangle
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);

        // Set the corner radius (adjust as needed)
        gradientDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});

        // Set the background color (adjust as needed)
        gradientDrawable.setColor(Color.parseColor("#" + color));

        // Set the stroke (optional)
        gradientDrawable.setStroke(storck, Color.parseColor("#" + scolor));

        // Set the gradient type (optional)
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);

        // Set the orientation of the gradient (optional)
        gradientDrawable.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);

        // Set the background drawable for the LinearLayout
        linearLayout.setBackground(gradientDrawable);
        linearLayout.setElevation(5);
        linearLayout.setClipToOutline(true);
    }

    private void ToolbarMethod() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}