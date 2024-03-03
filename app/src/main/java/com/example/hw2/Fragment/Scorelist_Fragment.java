package com.example.hw2.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hw2.Adapters.Score_Adapter;
import com.example.hw2.Interface.ButtonRecordClicked;
import com.example.hw2.Interface.Score_Callback;
import com.example.hw2.Model.User;
import com.example.hw2.R;
import com.example.hw2.Utilities.SharedPreferencesManager;

import java.util.ArrayList;

public class Scorelist_Fragment extends Fragment {
    private RecyclerView scores_LST_scores;
    private Score_Callback scoreCallback;
    Score_Adapter scoreAdapter;
    private AppCompatActivity activity;
    ArrayList<User> users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_score_list, container,false);
        findViews(view);
        createList();
        handleAdapter();
        return view;
    }

    private void handleAdapter() {
        scoreAdapter.setScoreListener(new ButtonRecordClicked() {
            @Override
            public void buttonRecordClicked(User user, int position) {
                if(scoreCallback != null){
                    Log.d("Scorelist_Fragment", "buttonRecordClicked lat: "+ user.getLat() + ", lon: " + user.getLon());
                    scoreCallback.showButtonClicked(user);
                }
            }
        });
    }

    private void createList() {
        users = SharedPreferencesManager.getInstance().getTopTen();
        scoreAdapter = new Score_Adapter(activity, users);
        scores_LST_scores.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        scores_LST_scores.setHasFixedSize(true);
        scores_LST_scores.setItemAnimator(new DefaultItemAnimator());
        scores_LST_scores.setAdapter(scoreAdapter);
    }

    private void findViews(View view) {
        scores_LST_scores = view.findViewById(R.id.scores_LST_scores);
    }


    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }

    public void setScoreCallback(Score_Callback scoreCallback){
        this.scoreCallback = scoreCallback;
    }


}

