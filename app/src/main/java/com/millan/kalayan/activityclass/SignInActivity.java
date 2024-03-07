package com.millan.kalayan.activityclass;

import static com.millan.kalayan.shareprefclass.Utility.BroadCastStringForAction;
import static com.millan.kalayan.shareprefclass.Utility.myReceiver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.R;
import com.millan.kalayan.apiclass.ApiClass;
import com.millan.kalayan.responseclass.DataApp;
import com.millan.kalayan.responseclass.DataLogIN;
import com.millan.kalayan.shareprefclass.SharPrefClass;
import com.millan.kalayan.shareprefclass.Utility;
import com.millan.kalayan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText inNum, inPass;
    private ShapeableImageView pToggle;
    private ProgressBar progressBar;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    Utility utility;
    private ImageView
            whatsappic, callic, teleic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        intVariables();
        loadData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        whatsappic = findViewById(R.id.whatsappic);
        callic = findViewById(R.id.callic);
        teleic = findViewById(R.id.teleic);
       /* whatsappic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://api.whatsapp.com/send?phone="+SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_WHATSAP_NUMBER)+"&text="+"hii";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        callic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(RegistrationActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 100);
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" +SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_PHONE_NUMBER1)));
                    startActivity(callIntent);
                }
            }
        });
        teleic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://t.me/"+SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_WHATSAP_NUMBER);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });*/
        getAppInfoMethod(SignInActivity.this);

    }

    private void loadData() {
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    public void SignInBtn(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String number = inNum.getText().toString();
        String password = inPass.getText().toString();
        if (TextUtils.isEmpty(number)){
            Snackbar.make(view, getString(R.string.please_enter_mobile_number),2000).show();
            return;
        }
        if (number.length()!=10){
            Snackbar.make(view, getString(R.string.please_enter_valid_mobile_number),2000).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Snackbar.make(view, getString(R.string.please_enter_password),2000).show();
            return;
        }
        if (password.length()<4){
            Snackbar.make(view, getString(R.string.please_enter_min_4_digits_password),2000).show();
            return;
        }
        if (YourService.isOnline(this))
        signInMethod(number,password);
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();


    }


    private void signInMethod(String number, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().getSignIn(number, password);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(@NonNull Call<DataLogIN> call, @NonNull Response<DataLogIN> response) {
                if (response.isSuccessful()){
                    DataLogIN dataLogIN = response.body();
                    assert dataLogIN != null;
                    if (dataLogIN.getStatus().equals("success")){
                        SharPrefClass.setSigninTkn(SignInActivity.this, dataLogIN.getData().getToken());
                        Intent intent = new Intent(SignInActivity.this, SPinActivity.class);
                        intent.putExtra(getString(R.string.phone_number), number);
                        intent.putExtra(getString(R.string.pin_reset), 200);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignInActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Log.e("SignInActivity" , "onResponse: "+response.code());
                    Toast.makeText(SignInActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.on_api_failure), Toast.LENGTH_LONG).show();
                System.out.println("getSignUp OnFailure "+t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void passToggle(View view) {
        if (inPass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            inPass.setTransformationMethod(new SingleLineTransformationMethod());
            pToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            inPass.setTransformationMethod(new PasswordTransformationMethod());
            pToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
        }

        inPass.setSelection(inPass.getText().length());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }
    public void forPass(View view) {
        Intent intent = new Intent(this, ForPassActivity.class);
        if (!TextUtils.isEmpty(inNum.getText().toString())){
            intent.putExtra(getString(R.string.phone_number), inNum.getText().toString());
        }
        startActivity(intent);
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
    private void intVariables() {
        inNum = findViewById(R.id.phone_num);
        inPass = findViewById(R.id.in_pass);
        pToggle = findViewById(R.id.pass_toggle);
        progressBar = findViewById(R.id.progressBar);
        mDataConText = findViewById(R.id.dataConText);
    }

    public void registerClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
    private void getAppInfoMethod(SignInActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataApp> call = ApiClass.getClient().getAppInfo("");
        call.enqueue(new Callback<DataApp>() {
            @Override
            public void onResponse(@NonNull Call<DataApp> call, @NonNull Response<DataApp> response) {
                if (response.isSuccessful()) {
                    DataApp dataApp = response.body();
                    if (dataApp.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataApp.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    if (dataApp.getStatus().equalsIgnoreCase(getString(R.string.success))) {
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_MAR_TXT, dataApp.getData().getBanner_marquee());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_PHONE_NUMBER1, "+91" + dataApp.getData().getContact_details().getMobile_no_1());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_PHONE_NUMBER2,  dataApp.getData().getContact_details().getTelegram_channel_link());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_WHATSAP_NUMBER, "+91" + dataApp.getData().getContact_details().getWhatsapp_no());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_REACH_US_EMAIL, dataApp.getData().getContact_details().getEmail_1());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES1, dataApp.getData().getBanner_image().getBanner_img_1());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES2, dataApp.getData().getBanner_image().getBanner_img_2());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES3, dataApp.getData().getBanner_image().getBanner_img_3());
                        whatsappic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://api.whatsapp.com/send?phone="+SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_WHATSAP_NUMBER)+"&text="+"hii";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                        callic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(SignInActivity.this,
                                            new String[]{Manifest.permission.CALL_PHONE}, 100);
                                } else {
                                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    callIntent.setData(Uri.parse("tel:" +SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_PHONE_NUMBER1)));
                                    startActivity(callIntent);
                                }
                            }
                        });
                        teleic.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "https://t.me/"+SharPrefClass.getContactObject(getApplicationContext(), SharPrefClass.KEY_PHONE_NUMBER2);
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        });
                    } else
                        Toast.makeText(activity, dataApp.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataApp> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("getAppDetails error " + t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();

            }
        });

    }


}