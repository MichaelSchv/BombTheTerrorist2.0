package com.example.hw2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2.Interface.ButtonRecordClicked;
import com.example.hw2.Interface.Score_Callback;
import com.example.hw2.Model.User;
import com.example.hw2.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class Score_Adapter extends RecyclerView.Adapter<Score_Adapter.RecordViewHolder> {

    private Context context;
    ArrayList<User> users;
    private Score_Callback scoreCallback;
    ButtonRecordClicked buttonRecordClicked;

    public Score_Adapter(Context context, ArrayList<User> users)
    {
        this.context = context;
        this.users = users;
    }


    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent,false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        User user = getItem(position);
        holder.score_LBL_place.setText((position+1) + "#:");
        holder.score_LBL_score.setText(user.getScore()+"");
        holder.score_BTN_show.setOnClickListener(v->{
            if(scoreCallback != null)
                scoreCallback.showButtonClicked(user);
        });
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }
    private User getItem(int position){
        return users.get(position);
    }

    public Score_Adapter setScoreListener(ButtonRecordClicked buttonRecordClicked){
        this.buttonRecordClicked = buttonRecordClicked;
        return this;
    }
    public class RecordViewHolder extends RecyclerView.ViewHolder{
        private MaterialTextView score_LBL_place;
        private MaterialTextView score_LBL_score;
        private MaterialButton score_BTN_show;
        public RecordViewHolder(View itemView) {
            super(itemView);
            score_LBL_place = itemView.findViewById(R.id.score_LBL_place);
            score_LBL_score = itemView.findViewById(R.id.score_LBL_score);
            score_BTN_show = itemView.findViewById(R.id.score_BTN_show);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User user = getItem(getAdapterPosition());
                    buttonRecordClicked.buttonRecordClicked(user, getAdapterPosition());
                }
            });
        }
    }
}
