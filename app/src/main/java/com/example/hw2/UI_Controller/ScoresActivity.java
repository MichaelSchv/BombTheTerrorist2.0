package com.example.hw2.UI_Controller;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.hw2.Fragment.Map_Fragment;
import com.example.hw2.Fragment.Scorelist_Fragment;
import com.example.hw2.Interface.Score_Callback;
import com.example.hw2.Model.User;
import com.example.hw2.R;

public class ScoresActivity extends AppCompatActivity {
    private FrameLayout scores_FRAME_list;
    private FrameLayout scores_FRAME_map;
    private Scorelist_Fragment scorelistFragment;
    private Map_Fragment mapFragment;
    FragmentTransaction fragmentTransaction;
    private Score_Callback scoreCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        findViews();
        handleCallback();

        if (savedInstanceState == null){
            mapFragment = new Map_Fragment();
            mapFragment.setActivity(this);
            //mapFragment.getMapAsync(this);
            fragmentTransaction = getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAME_map, mapFragment);
            fragmentTransaction.commit();
        }
        if(savedInstanceState == null){
            scorelistFragment = new Scorelist_Fragment();
            scorelistFragment.setActivity(this);
            scorelistFragment.setScoreCallback(scoreCallback);
            getSupportFragmentManager().beginTransaction().add(R.id.scores_FRAME_list, scorelistFragment).commit();
        }



    }

    private void handleCallback() {
        scoreCallback = new Score_Callback() {
            @Override
            public void showButtonClicked(User user) {
                Log.d("ScoresActivity", "buttonRecordClicked lat: "+ user.getLat() + ", lon: " + user.getLon());
                fragmentTransaction.add(R.id.scores_FRAME_map, mapFragment, null);
                mapFragment.setMarker(user.getLat(), user.getLon());
            }
        };
    }

    private void findViews() {
        scores_FRAME_list = findViewById(R.id.scores_FRAME_list);
        scores_FRAME_map = findViewById(R.id.scores_FRAME_map);
    }
}