package com.millan.kalayan.adapterclass;

import static com.millan.kalayan.R.drawable.blueback;
import static com.millan.kalayan.R.drawable.greenback;
import static com.millan.kalayan.R.drawable.redback;
import static com.millan.kalayan.R.drawable.yellowback;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.millan.kalayan.R;
import com.millan.kalayan.responseclass.DataStarlineGameList;

import java.util.List;

public class SLListAdapter extends RecyclerView.Adapter<SLListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataStarlineGameList.Data.StarlineGame starlineGame, View itemView);
    }
    Context context;
    private final List<DataStarlineGameList.Data.StarlineGame> starlineGameList;

    private final OnItemClickListener listener;

    public SLListAdapter(Context context, List<DataStarlineGameList.Data.StarlineGame> starlineGameList, OnItemClickListener listener) {
        this.context = context;
        this.starlineGameList = starlineGameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SLListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.s_l_turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SLListAdapter.ViewHolder holder, int position) {
        holder.bind(starlineGameList.get(position), listener, context, position);
        setRoundedCorners(holder.mainline, "00000000", "00000000",12, 0);
        setRoundedCorners(holder.marketOpen, "FFFFFF", "00000000",360, 0);
        if (position % 1 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, greenback));
            //holder.sideline.setBackgroundColor(Color.parseColor("#45957a"));
            setRoundedCorners(holder.imagebac, "45957a", "00000000",360, 0);

        }
        if (position % 2 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, redback));
            //holder.sideline.setBackgroundColor(Color.parseColor("#f5726a"));
            setRoundedCorners(holder.imagebac, "f5726a", "00000000",360, 0);


        }
        if (position % 3 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, yellowback));
          //  holder.sideline.setBackgroundColor(Color.parseColor("#c99041"));
            setRoundedCorners(holder.imagebac, "c99041", "00000000",360, 0);

        }
        if (position % 4 == 0){
            holder.mainline.setBackground(ContextCompat.getDrawable(context, blueback));
           // holder.sideline.setBackgroundColor(Color.parseColor("#31425e"));
            setRoundedCorners(holder.imagebac, "31425e", "00000000",360, 0);

        }
    }

    @Override
    public int getItemCount() {
        return starlineGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView gameName;
        private final MaterialTextView gameResult;
        private final ShapeableImageView gamePlay;
        private final RelativeLayout mainline;
        private final LinearLayout imagebac = itemView.findViewById(R.id.imagebac);
        private final MaterialTextView marketOpen;
        private final MaterialTextView closeTime = itemView.findViewById(R.id.closeTime);


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
            gameResult = itemView.findViewById(R.id.gameResult);
            gamePlay = itemView.findViewById(R.id.gamePlay);
            mainline = itemView.findViewById(R.id.mainline);
            marketOpen = itemView.findViewById(R.id.marketOpen);
        }

        public void bind(DataStarlineGameList.Data.StarlineGame starlineGame, OnItemClickListener listener, Context context, int position) {
            gameName.setText(starlineGame.getName());
            gameResult.setText(starlineGame.getResult());
            if(starlineGame.isPlay())  {marketOpen.setText("Market is Running");
            marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));}
            else {marketOpen.setText("Market Closed");
            marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));}
            /*if(starlineGame.isPlay()) gamePlay.setImageResource(R.drawable.play_icon);
            else gamePlay.setImageResource(R.drawable.close_icon);*/
            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.move);
            gameResult.setAnimation(animation);
            itemView.setOnClickListener(v ->{
                listener.onItemClick(starlineGame, v);
            });
            closeTime.setText(starlineGame.getTime());
            Log.d("time", "time : "+starlineGame.getTime());
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
