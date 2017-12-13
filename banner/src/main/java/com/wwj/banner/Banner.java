package com.wwj.banner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/10 0010.
 */

public class Banner extends FrameLayout implements ViewPager.OnPageChangeListener {

    private static final long INTERVAL_TIME = 5000;
    private final Context mContext;
    private int indicator_height;
    private int indicator_width;
    private int indicator_unselect, indicator_select;
    private int indicator_margin;


    private static final int NEXT_PAGE = 20;
    private LinearLayout indicator;
    private Spinner spinner;
    private BannerViewPager bannerViewPager;
    private ArrayList<ImageView> imageviews = new ArrayList<>();
    private int count = 0; //有几张图片
    private int currentItem = 1;
    private int lastItem = currentItem;
    private List<?> dataList;
    private BannerAdpater bannerAdapter;
    private int resource_id;
    private boolean isAutoPlay = true; //默认自动播放轮播图
    private boolean scrollable = true; //默认可以滑动轮播图

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        initAttribute(attrs);

    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.banner);

        int indicatorSize = (int) getResources().getDimension(R.dimen.indicatorSize);

        indicator_height = (int) typedArray.getDimension(R.styleable.banner_indicator_height, indicatorSize);
        indicator_width = (int) typedArray.getDimension(R.styleable.banner_indicator_width, indicatorSize);
        indicator_margin = (int) typedArray.getDimension(R.styleable.banner_indictor_margin, indicatorSize);
        indicator_unselect = typedArray.getResourceId(R.styleable.banner_indicator_unselect, R.drawable.indicator_unselect);
        indicator_select = typedArray.getResourceId(R.styleable.banner_indicator_select, R.drawable.indicator_select);
        resource_id = typedArray.getResourceId(R.styleable.banner_resource_id, R.layout.banner);
        isAutoPlay = typedArray.getBoolean(R.styleable.banner_isAutoPlay, isAutoPlay);
        scrollable = typedArray.getBoolean(R.styleable.banner_scrollable, scrollable);

        typedArray.recycle();

        initView();

    }

    public void setData(List<?> dataList) {
        this.dataList = dataList;
        count = dataList.size(); //有几张图片

    }

    public void start() {
        createImageView();
        createIndicator();
        setAdapter();
    }


    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(resource_id, null);
        addView(view);

        indicator = view.findViewById(R.id.indicator);
        bannerViewPager = view.findViewById(R.id.bannerViewPager);
        spinner = view.findViewById(R.id.spinner);

        bannerViewPager.setScrollable(scrollable);

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

    private void createImageView() {
        imageviews.clear();
        for (int i = 0; i <= count + 1; i++) {
            Object url;
            if (i == 0) {
                url = dataList.get(count - 1);
            } else if (i == count + 1) {
                url = dataList.get(0);
            } else {
                url = dataList.get(i - 1);
            }
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(mContext).load(url).into(imageView);
            imageviews.add(imageView);
        }
    }

    private void createIndicator() {
        indicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(indicator_width, indicator_height);
            layoutParams.leftMargin = indicator_margin;
            layoutParams.rightMargin = indicator_margin;
            imageView.setLayoutParams(layoutParams);
            if (i == 0) {
                imageView.setImageResource(R.drawable.indicator_select);
            } else {
                imageView.setImageResource(R.drawable.indicator_unselect);
            }
            indicator.addView(imageView);
        }
    }

    private void setAdapter() {
        if (null == bannerAdapter) {
            bannerAdapter = new BannerAdpater();
            bannerViewPager.setAdapter(bannerAdapter);
            bannerViewPager.addOnPageChangeListener(this);
        } else {
            bannerAdapter.notifyDataSetChanged();
        }
        bannerViewPager.setCurrentItem(1);

        startPlay();
    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == NEXT_PAGE) {
                if (!isAutoPlay) {
                    return;
                }
                currentItem = currentItem % (count + 1) + 1;
                bannerViewPager.setCurrentItem(currentItem);
                mHandler.sendEmptyMessageDelayed(NEXT_PAGE, INTERVAL_TIME);
            }
        }
    };


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        currentItem = position;
        Log.i("tag","=======positon="+position);
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
                } else if (currentItem == count+1) {
                    bannerViewPager.setCurrentItem(1, false);
                }
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:  //页面滚动状态拖动中
                if (currentItem == 0) {
                    bannerViewPager.setCurrentItem(count, false);
                } else if (currentItem == count+1) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mHandler.removeCallbacksAndMessages(null);
        } else if (ev.getAction() == MotionEvent.ACTION_UP ||
                ev.getAction() == MotionEvent.ACTION_CANCEL ||
                ev.getAction() == MotionEvent.ACTION_OUTSIDE
                ) {
            startPlay();
        }
        return super.dispatchTouchEvent(ev);
    }

    public void startPlay() {
        if (isAutoPlay) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(NEXT_PAGE, INTERVAL_TIME);
        }
    }

    /**
     * 停止自动轮播
     */
    public void stopPlay() {
        mHandler.removeCallbacksAndMessages(null);
    }

    public void setIndicator_height(int indicator_height) {
        this.indicator_height = indicator_height;
    }

    public void setIndicator_width(int indicator_width) {
        this.indicator_width = indicator_width;
    }

    public void setIndicator_unselect(int indicator_unselect) {
        this.indicator_unselect = indicator_unselect;
    }

    public void setIndicator_select(int indicator_select) {
        this.indicator_select = indicator_select;
    }

    public void setIndicator_margin(int indicator_margin) {
        this.indicator_margin = indicator_margin;
    }

    public void setAutoPlay(boolean autoPlay) {
        isAutoPlay = autoPlay;
    }

    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        bannerViewPager.setScrollable(scrollable);
    }
}
