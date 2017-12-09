package com.wwj.bannerlearn;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int NEXT_PAGE = 20;
    private LinearLayout indicator;
    private int indicatorSize;
    private Spinner spinner;
    private BannerViewPager bannerViewPager;
    private ArrayList<ImageView> imageviews = new ArrayList<>();
    private int images[] = {R.mipmap.banner_a, R.mipmap.banner_b, R.mipmap.banner_c};
    private int count = images.length; //有几张图片
    private int currentItem = 1;
    private int lastItem = currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        indicator = findViewById(R.id.indicator);
        bannerViewPager = findViewById(R.id.bannerViewPager);
        spinner = findViewById(R.id.spinner);

        indicatorSize = (int) getResources().getDimension(R.dimen.indicatorSize);


        for (int i = 0; i < count; i++) {
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


        for (int i = 0; i <= count + 1; i++) {
            int url;
            if (i == 0) {
                url = count - 1;
            } else if (i == count + 1) {
                url = 0;
            } else {
                url = i - 1;
            }
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
            imageView.setImageResource(images[url]);
            imageviews.add(imageView);
        }

        bannerViewPager.setAdapter(new BannerAdpater());
        bannerViewPager.setCurrentItem(1);
        bannerViewPager.addOnPageChangeListener(this);

        mHandler.sendEmptyMessageDelayed(NEXT_PAGE, 2000);
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NEXT_PAGE) {
                currentItem = currentItem % (count + 1) + 1;
                bannerViewPager.setCurrentItem(currentItem);
                mHandler.sendEmptyMessageDelayed(NEXT_PAGE, 2000);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;

        ImageView imageView = (ImageView) indicator.getChildAt((lastItem - 1 + count) % count);
        imageView.setImageResource(R.drawable.indicator_unselect);
        imageView = (ImageView) indicator.getChildAt((currentItem - 1 + count) % count);
        imageView.setImageResource(R.drawable.indicator_select);

        lastItem = currentItem;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:  //页面滚动状态处于空闲
                if (currentItem == 0) {
                    bannerViewPager.setCurrentItem(count, false);
                } else if (currentItem == 4) {
                    bannerViewPager.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:  //页面滚动状态拖动中
                if (currentItem == 0) {
                    bannerViewPager.setCurrentItem(count, false);
                } else if (currentItem == 4) {
                    bannerViewPager.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_SETTLING:  //页面滚动状态停止拖动
                break;
        }
    }

    private class BannerAdpater extends PagerAdapter {

        @Override
        public int getCount() {
            return imageviews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageviews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}