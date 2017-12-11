package com.wwj.bannerlearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wwj.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {


    private Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banner=findViewById(R.id.banner);

        List<Integer> list=new ArrayList<>();
        list.add(R.mipmap.banner_a);
        list.add(R.mipmap.banner_b);
        banner.setData(list);
        banner.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        banner.stopPlay();
    }




}