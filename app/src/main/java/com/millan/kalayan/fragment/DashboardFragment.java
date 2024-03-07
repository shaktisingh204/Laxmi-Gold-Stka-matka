package com.millan.kalayan.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.R;
import com.millan.kalayan.activityclass.AddCoinActivity;
import com.millan.kalayan.activityclass.DashboardActivity;
import com.millan.kalayan.activityclass.DisawarActivity;
import com.millan.kalayan.activityclass.TakeOutActivity;
import com.millan.kalayan.activityclass.TurnamentActivity;
import com.millan.kalayan.activityclass.UserInfoActivity;
import com.millan.kalayan.activityclass.WebSiteActivity;
import com.millan.kalayan.adapterclass.TurnamentListAdapter;
import com.millan.kalayan.adapterclass.ViewPagerAdapter;
import com.millan.kalayan.responseclass.DataGameList;
import com.millan.kalayan.shareprefclass.SharPrefClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment {

    public static MaterialTextView marqueText, user_name;
    private MaterialCardView whatsappBtn, callBtn,user_profile;
    ViewPager mViewPager;
    ViewPagerAdapter mViewPagerAdapter;
    static FrameLayout mViewPagFrame;
    static FrameLayout stripLayout;
    static RelativeLayout phoneLyt;
    static LinearLayout aaa;
    private static RecyclerView recyView;
    private static TurnamentListAdapter turnamentListAdapter;
    public static List<DataGameList.Data> mDataList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private Context context;
    private static Context mContext;
    private static Vibrator mVibe;
    int mCurrentPage = 0;
    long DELAY_MS = 1000;
    long PERIOD_MS = 2000;
    private String[] mImages;

    TextView whatsppTv;
    private static RelativeLayout dpboss_rl;
    private RelativeLayout whatsapp_item;
    private RelativeLayout telegram_item;
    private static RelativeLayout addpoints_item;
    private static RelativeLayout starline_item;
    private static RelativeLayout galli_diswar_item;
    private   CardView addfundscard, withdrawcard, desawarcard, whatsappcard;
    private CardView phonecard, telecard;
    private static CardView starcard;
    private TextView textwa, textcall;
    private static LinearLayout addfundscardLyt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mContext = view.getContext();
        addfundscardLyt =  view.findViewById(R.id.addfundscardLyt);

        intVariables(view);
        loadData(context);
        clickListeners(context);
        confViewPager(context, mImages);
       /* textcall.setText(SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_PHONE_NUMBER1));
        textwa.setText(SharPrefClass.getContactObject(context, SharPrefClass.KEY_WHATSAP_NUMBER));*/
        return view;
    }

    private void clickListeners(Context context) {

        whatsppTv.setText(SharPrefClass.getContactObject(context, SharPrefClass.KEY_WHATSAP_NUMBER).substring(3,13).toString());
        desawarcard.setOnClickListener(v -> {
            String msg = "Hello Sir\nMy Name : " +
                    SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_USER_NAME) +
                    "\nMy Number : " +
                    SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_PHONE_NUMBER);

            String url = "https://api.whatsapp.com/send?phone="+SharPrefClass.getContactObject(context, SharPrefClass.KEY_WHATSAP_NUMBER)+"&text="+msg;
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.setData(Uri.parse(url));
            startActivity(i);
        });

        dpboss_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebSiteActivity.class);
                context.startActivity(intent);

            }
        });

        addfundscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addFunds = new Intent(context, AddCoinActivity.class);
                startActivity(addFunds);
            }
        });
        withdrawcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent withdrawPoints = new Intent(context, TakeOutActivity.class);
                startActivity(withdrawPoints);
            }
        });
        telecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "Hello Sir\nMy Name : " +
                        SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_USER_NAME) +
                        "\nMy Number : " +
                        SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_PHONE_NUMBER);
/*
                String url = SharPrefClass.getContactObject(context, SharPrefClass.KEY_PHONE_NUMBER2);
*/
                String url = "https://t.me/"+SharPrefClass.getContactObject(context, SharPrefClass.KEY_PHONE_NUMBER2);

                Intent i = new Intent(Intent.ACTION_VIEW);

                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        starline_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*DashboardActivity.viewPager.setCurrentItem(1,true);*/
                final com.google.android.material.bottomsheet.BottomSheetDialog bottomSheetDialog = new com.google.android.material.bottomsheet.BottomSheetDialog(getActivity());

                View bottomSheetView; bottomSheetView = getLayoutInflater().inflate(R.layout.bottomgames,null );
                bottomSheetDialog.setContentView(bottomSheetView);

                bottomSheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
                LinearLayout galigame = bottomSheetView.findViewById(R.id.galigame);
                LinearLayout starlinegame = bottomSheetView.findViewById(R.id.starlinegame);
                galigame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), DisawarActivity.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                starlinegame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DashboardActivity.viewPager.setCurrentItem(1,true);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.setCancelable(true);
                bottomSheetDialog.show();
            }
        });

        addpoints_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCoinActivity.class);
                startActivity(intent);
            }
        });

       /* desawarcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DashboardActivity.viewPager.setCurrentItem(4,true);

                Intent intent = new Intent(getActivity(), DisawarActivity.class);
                startActivity(intent);
            }
        });
*/
        whatsappBtn.setOnClickListener(v -> {
            String msg = "Hello Sir\nMy Name : " +
                    SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_USER_NAME) +
                    "\nMy Number : " +
                    SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_PHONE_NUMBER);
            String url = "https://api.whatsapp.com/send?phone="+SharPrefClass.getContactObject(context, SharPrefClass.KEY_WHATSAP_NUMBER)+"&text="+msg;
            Intent i = new Intent(Intent.ACTION_VIEW);

            i.setData(Uri.parse(url));
            startActivity(i);
        });

/*
        phonecard.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE}, 100);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +SharPrefClass.getContactObject(context, SharPrefClass.KEY_PHONE_NUMBER1)));
                startActivity(callIntent);
            }
        });
*/

        user_profile.setOnClickListener(v -> {
            Intent profile = new Intent(context, UserInfoActivity.class);
            startActivity(profile);
        });



    }

    private void loadData(Context context) {
        marqueText.setText(SharPrefClass.getPrfrnceinfo(context, SharPrefClass.KEY_MAR_TXT));
        marqueText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        marqueText.setSelected(true);
        marqueText.setSingleLine(true);
        marqueText.setMarqueeRepeatLimit(-1);
        mVibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mImages = new String[]{SharPrefClass.getPosterImage(context, SharPrefClass.KEY_POSTER_IMAGES1),
                SharPrefClass.getPosterImage(context, SharPrefClass.KEY_POSTER_IMAGES2),
                SharPrefClass.getPosterImage(context, SharPrefClass.KEY_POSTER_IMAGES3)};
    }

    private void intVariables(View view) {
        context = view.getContext();
        starcard = view.findViewById(R.id.starcard);
        mViewPager = view.findViewById(R.id.viewPager);
        addfundscard = view.findViewById(R.id.addfundscard);
        mViewPagFrame = view.findViewById(R.id.viewPagerFrame);
        recyView = view.findViewById(R.id.recyclerView);
        whatsappBtn = view.findViewById(R.id.whatsappBtn);
        callBtn = view.findViewById(R.id.callBtn);
        withdrawcard = view.findViewById(R.id.withdrawcard);
        marqueText = view.findViewById(R.id.text_marque);
        mProgressBar = view.findViewById(R.id.progressBar);
        user_name = view.findViewById(R.id.user_name);
        user_profile = view.findViewById(R.id.user_profile);
        stripLayout = view.findViewById(R.id.stripLayout);
        phoneLyt = view.findViewById(R.id.phoneLyt);
        dpboss_rl = view.findViewById(R.id.dpboss_rl);
        whatsapp_item= view.findViewById(R.id.whatsapp_item);
        whatsppTv= view.findViewById(R.id.whats_app_n);
        desawarcard = view.findViewById(R.id.desawarcard);
        telegram_item= view.findViewById(R.id.telegram_item);
        telecard = view.findViewById(R.id.telegramcard);
        addpoints_item= view.findViewById(R.id.addpoints_item);
        starline_item= view.findViewById(R.id.starline_item);
        galli_diswar_item= view.findViewById(R.id.galli_item);
        textwa = view.findViewById(R.id.textwa);
        textcall = view.findViewById(R.id.textcall);
        aaa = view.findViewById(R.id.aaa);
        confiView();
    }

    private static void confiView() {
        if (SharPrefClass.getSharedBooleanStatus(mContext, SharPrefClass.KEY_DEVELOPER_MODE)){
            phoneLyt.setVisibility(View.GONE);
            mViewPagFrame.setVisibility(View.GONE);
            stripLayout.setVisibility(View.GONE);
            aaa.setVisibility(View.GONE);
        }else {
            phoneLyt.setVisibility(View.GONE);
            aaa.setVisibility(View.VISIBLE);
            //  phoneLyt.setVisibility(View.VISIBLE);
            mViewPagFrame.setVisibility(View.VISIBLE);
            stripLayout.setVisibility(View.VISIBLE);
        }
        if(SharPrefClass.getLiveUser(mContext)){
            galli_diswar_item.setVisibility(View.VISIBLE);
            starcard.setVisibility(View.VISIBLE);
            addpoints_item.setVisibility(View.VISIBLE);
            dpboss_rl.setVisibility(View.VISIBLE);
            addfundscardLyt.setVisibility(View.VISIBLE);

        }else{
            galli_diswar_item.setVisibility(View.GONE);
            starcard.setVisibility(View.GONE);
            addpoints_item.setVisibility(View.GONE);
            addfundscardLyt.setVisibility(View.GONE);
            dpboss_rl.setVisibility(View.GONE);
        }
    }

    public void confViewPager(Context context, String[] images) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        double imageHeight = width*0.40;
        mViewPagFrame.getLayoutParams().height= (int) imageHeight;

        mViewPagerAdapter = new ViewPagerAdapter(context, images);
        mViewPager.setAdapter(mViewPagerAdapter);
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (mCurrentPage == 3) {
                mCurrentPage = 0;
            }
            mViewPager.setCurrentItem(mCurrentPage++, true);
        };

        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public static void confRecyView(Context activity) {
        turnamentListAdapter = new TurnamentListAdapter(activity, (ArrayList<DataGameList.Data>) mDataList, (data, itemView) -> {
            if (!data.isPlay()){
                ObjectAnimator
                        .ofFloat(itemView, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                        .setDuration(700)
                        .start();
                mVibe.vibrate(500);
            }else {
                Intent intent = new Intent(activity, TurnamentActivity.class);
                intent.putExtra(activity.getString(R.string.game), data.getId());
                intent.putExtra("open",data.isOpen());
                activity.startActivity(intent);
            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(activity.getApplicationContext(), 2);
        recyView.setLayoutManager(layoutManager);
        recyView.setAdapter(turnamentListAdapter);
    }

    public static void recall() {
        confiView();
    }
}