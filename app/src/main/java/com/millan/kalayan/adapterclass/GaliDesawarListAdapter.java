package com.millan.kalayan.adapterclass;

import static com.millan.kalayan.R.drawable.blueback;
import static com.millan.kalayan.R.drawable.greenback;
import static com.millan.kalayan.R.drawable.redback;
import static com.millan.kalayan.R.drawable.yellowback;

import android.content.Context;
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
import com.millan.kalayan.responseclass.DataDesawarList;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

public class GaliDesawarListAdapter extends RecyclerView.Adapter<GaliDesawarListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataDesawarList.Data.GaliDesawarGame starlineGame, View itemView);
    }
    Context context;
    private final List<DataDesawarList.Data.GaliDesawarGame> galiDesawarGameList;

    private final OnItemClickListener listener;

    public GaliDesawarListAdapter(Context context, List<DataDesawarList.Data.GaliDesawarGame> galiDesawarGameList, OnItemClickListener listener) {
        this.context = context;
        this.galiDesawarGameList = galiDesawarGameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.g_d_turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(galiDesawarGameList.get(position), listener, context, position);
        setRoundedCorners(holder.mainline, "00000000", "00000000",12, 0);
        setRoundedCorners(holder.marketOpen, "FFFFFF", "00000000",360, 0);
        holder.mainline.setBackground(ContextCompat.getDrawable(context, greenback));
        holder.gameName.setBackgroundColor(Color.parseColor("#45957a"));
        setRoundedCorners(holder.imagebac, "45957a", "00000000",360, 0);
        if (position % 1 == 0){

            holder.mainline.setBackground(ContextCompat.getDrawable(context, greenback));
            holder.gameName.setBackgroundColor(Color.parseColor("#45957a"));
            setRoundedCorners(holder.imagebac, "45957a", "00000000",360, 0);
        }
        if (position % 2 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, redback));
            holder.gameName.setBackgroundColor(Color.parseColor("#f5726a"));
            setRoundedCorners(holder.imagebac, "f5726a", "00000000",360, 0);

        }
        if (position % 3 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, yellowback));
            setRoundedCorners(holder.imagebac, "c99041", "00000000",360, 0);

        }
        if (position % 4 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, blueback));
            holder.gameName.setBackgroundColor(Color.parseColor("#31425e"));
            setRoundedCorners(holder.imagebac, "31425e", "00000000",360, 0);

        }
    }

    @Override
    public int getItemCount() {
        return galiDesawarGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView marketOpen;
        private final MaterialTextView gameResult;
        private final MaterialTextView closeTime;
        private final ShapeableImageView gamePlay;
        private final RelativeLayout mainline;
        private final ShimmerTextView gameName = itemView.findViewById(R.id.eventType);
        private final LinearLayout imagebac = itemView.findViewById(R.id.imagebac);
        private final ImageView playic = itemView.findViewById(R.id.playic);
        private final TextView playtext = itemView.findViewById(R.id.playtext);

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameResult = itemView.findViewById(R.id.eventNumber);
            gamePlay = itemView.findViewById(R.id.gamePlay);
            closeTime = itemView.findViewById(R.id.closingTime);
            marketOpen = itemView.findViewById(R.id.marketOpen);
            mainline = itemView.findViewById(R.id.mainline);


        }

        public void bind(DataDesawarList.Data.GaliDesawarGame galiDesawarGame, OnItemClickListener listener, Context context, int position) {

            closeTime.setText("Close : " +galiDesawarGame.getTime());
            gameName.setText(galiDesawarGame.getName());
            gameResult.setText(galiDesawarGame.getResult());
            if(galiDesawarGame.isPlay()) {
                Animation  rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
                /*gamePlay.startAnimation(rotate);
                gamePlay.setImageResource(R.drawable.play_icon);*/
                marketOpen.setText("Market is Running");
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
                playic.setVisibility(View.VISIBLE);
                playtext.setText("Play Now");
            }
            else {
/*
                gamePlay.setImageResource(R.drawable.close_icon);
*/
                playic.setVisibility(View.GONE);
                playtext.setText("Closed");
                marketOpen.setText("Market is Closed");
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
            }

            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.run);
            gameResult.setAnimation(animation);
            itemView.setOnClickListener(v ->{
                listener.onItemClick(galiDesawarGame, v);
            });
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
