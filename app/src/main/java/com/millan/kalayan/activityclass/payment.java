package com.millan.kalayan.activityclass;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.millan.kalayan.R;
import com.millan.kalayan.apiclass.ApiClass;
import com.millan.kalayan.responseclass.DataMain;
import com.millan.kalayan.shareprefclass.SharPrefClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class payment extends AppCompatActivity {
    private TextView mTextView;
    private ImageView copy;
    private MaterialButton submit;
    private EditText utr;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#05020b"));
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        mTextView = findViewById(R.id.upi_id);
        mTextView.setText( SharPrefClass.getAddAmountUpiId(this, SharPrefClass.KEY_ADD_COINS_BHIM_ID));
        submit = findViewById(R.id.submit);
        utr = findViewById(R.id.utr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utr.getText().toString().length() > 10) {
                addCoinMethod(payment.this, getIntent().getStringExtra("amount"), "Payed with UPIID :"+utr.getText().toString());
            }else {
                    utr.setError("Enter UTR number");
                }}
        });
        copy = findViewById(R.id.copy);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ClipboardManager
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                // Copy the text into clipboard
                clipboard.setText(mTextView.getText().toString());
                // Snack Bar to show the text is copied
             Toast.makeText(getApplicationContext(), "Upi Copied", Toast.LENGTH_SHORT).show();
                }
        });
    }
    private void addCoinMethod(payment activity, String amount, String orderID) {
        Call<DataMain> call = ApiClass.getClient().AddCoins(SharPrefClass.getLoginInToken(activity), amount,"pending",orderID);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()){
                    DataMain dataMain = response.body();
                    if (dataMain.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataMain.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        Toast.makeText(payment.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(payment.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();}
                    Toast.makeText(payment.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("addFund Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
