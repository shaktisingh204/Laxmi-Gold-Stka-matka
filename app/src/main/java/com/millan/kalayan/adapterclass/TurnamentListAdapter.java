package com.millan.kalayan.adapterclass;

import static com.millan.kalayan.R.drawable.blueback;
import static com.millan.kalayan.R.drawable.greenback;
import static com.millan.kalayan.R.drawable.redback;
import static com.millan.kalayan.R.drawable.yellowback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.R;
import com.millan.kalayan.activityclass.TableActivity;
import com.millan.kalayan.responseclass.DataGameList;
import com.millan.kalayan.shareprefclass.SharPrefClass;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;

public class TurnamentListAdapter extends RecyclerView.Adapter<TurnamentListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataGameList.Data data, View itemView);
    }
    Context context;
    private final ArrayList<DataGameList.Data> datalArrayList;

    private final OnItemClickListener listener;

    public TurnamentListAdapter(Context context, ArrayList<DataGameList.Data> datalArrayList, OnItemClickListener listener) {
        this.context = context;
        this.datalArrayList = datalArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TurnamentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnamentListAdapter.ViewHolder holder, int position) {
        holder.attach(datalArrayList.get(position), listener, context, position);
        setRoundedCorners(holder.mainline, "00000000", "00000000",12, 0);
        setRoundedCorners(holder.marketOpen, "FFFFFF", "00000000",360, 0);
        if (position % 1 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, greenback));
            holder.sideline.setBackgroundColor(Color.parseColor("#45957a"));
            setRoundedCorners(holder.imagebac, "45957a", "00000000",360, 0);
            setRoundedCorners(holder.chartback, "45957a", "00000000",360, 0);
        }
        if (position % 2 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, redback));
            holder.sideline.setBackgroundColor(Color.parseColor("#f5726a"));
            setRoundedCorners(holder.imagebac, "f5726a", "00000000",360, 0);
            setRoundedCorners(holder.chartback, "f5726a", "00000000",360, 0);

        }
        if (position % 3 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, yellowback));
            holder.sideline.setBackgroundColor(Color.parseColor("#c99041"));
            setRoundedCorners(holder.imagebac, "c99041", "00000000",360, 0);
            setRoundedCorners(holder.chartback, "c99041", "00000000",360, 0);

        }
        if (position % 4 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, blueback));
            holder.sideline.setBackgroundColor(Color.parseColor("#31425e"));
            setRoundedCorners(holder.imagebac, "31425e", "00000000",360, 0);
            setRoundedCorners(holder.chartback, "31425e", "00000000",360, 0);

        }
    }

    @Override
    public int getItemCount() {
        return datalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RelativeLayout mainline;
        private final MaterialTextView eventNumber;
        private final MaterialTextView openingTime;
        private final MaterialTextView closingTime;
        private final MaterialTextView marketOpen;
        private final ShapeableImageView chartTable;
        private final ShapeableImageView eventStatus;
        private final ShimmerTextView eventType;
        private final LinearLayout sideline = itemView.findViewById(R.id.sideline);
        private final LinearLayout imagebac = itemView.findViewById(R.id.imagebac);
        private final LinearLayout chartback = itemView.findViewById(R.id.chartback);
        private final ImageView playic = itemView.findViewById(R.id.playic);
        private final TextView playtext = itemView.findViewById(R.id.playtext);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.eventType);
            eventNumber = itemView.findViewById(R.id.eventNumber);
            eventStatus = itemView.findViewById(R.id.eventStatus);
            openingTime = itemView.findViewById(R.id.openingTime);
            closingTime = itemView.findViewById(R.id.closingTime);
            chartTable = itemView.findViewById(R.id.chartTable);
            marketOpen = itemView.findViewById(R.id.marketOpen);
            mainline = itemView.findViewById(R.id.mainline);
            Shimmer shimmer = new Shimmer();
            shimmer.start(eventType);

        }

        public void attach(DataGameList.Data data, OnItemClickListener listener, Context context, int position) {
            if (!SharPrefClass.getLiveUser(context)){
                eventStatus.setImageResource(R.drawable.chart_icon);
                eventStatus.setOnClickListener(v -> {

                    Intent intent = new Intent(context, TableActivity.class);
                    intent.putExtra(context.getString(R.string.chart), data.getChart_url());
                    context.startActivity(intent);
                });
                marketOpen.setVisibility(View.GONE);

            }else {
                if (data.isMarket_open() && data.isPlay()){
                   // eventStatus.setImageResource(R.drawable.play_icon);
                    marketOpen.setText("Market is Running");
                    marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
                    playic.setVisibility(View.VISIBLE);
                    playtext.setText("Play Now");

                }else {
                    //eventStatus.setImageResource(R.drawable.close_icon);
                    marketOpen.setText("Market Closed");
                    marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
                    playic.setVisibility(View.GONE);
                    playtext.setText("Closed");
                }
                userDataMethod(data);
            }

            eventType.setText(data.getName());
            openingTime.setText(data.getOpen_time());
            closingTime.setText(data.getClose_time());
            eventNumber.setText(data.getResult());
            itemView.setOnClickListener(v ->{
                if(SharPrefClass.getLiveUser(context)){
                    listener.onItemClick(data, v);
                }
            });

            chartTable.setOnClickListener(v -> {
                Intent intent = new Intent(context, TableActivity.class);
                intent.putExtra(context.getString(R.string.chart), data.getChart_url());
                context.startActivity(intent);
            });


        }

        private void userDataMethod(DataGameList.Data data) {
            eventType.setText(data.getName());
            openingTime.setText(data.getOpen_time());
            closingTime.setText(data.getClose_time());
            eventNumber.setText(data.getResult());

            if (data.isMarket_open() && data.isPlay()){
              //  eventStatus.setImageResource(R.drawable.play_icon);
                marketOpen.setText("Market is Running");
                marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
                playic.setVisibility(View.VISIBLE);
                playtext.setText("Play Now");
            }else {
               // eventStatus.setImageResource(R.drawable.close_icon);
                marketOpen.setText("Market Closed");
                marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
                playic.setVisibility(View.GONE);
                playtext.setText("Closed");
            }
            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.move);
            eventNumber.setAnimation(animation);
        }
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

}
