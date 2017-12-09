package com.wwj.bannerlearn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity  {

    private LinearLayout indicator;
    private int indicatorSize;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = findViewById(R.id.indicator);

        spinner = findViewById(R.id.spinner);

        indicatorSize = (int) getResources().getDimension(R.dimen.indicatorSize);


        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicatorSize, indicatorSize);
            layoutParams.leftMargin = indicatorSize;
            layoutParams.rightMargin = indicatorSize;
            imageView.setLayoutParams(layoutParams);
            if (i == 0) {
                imageView.setImageResource(R.drawable.indicator_select);
            } else {
                imageView.setImageResource(R.drawable.indicator_unselect);
            }
            indicator.addView(imageView);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {  //left
                    indicator.setGravity(Gravity.LEFT);
                } else if (position == 1) { //center
                    indicator.setGravity(Gravity.CENTER);
                } else if (position == 2) {  //right
                    indicator.setGravity(Gravity.RIGHT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}