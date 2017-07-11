package com.mahao.flowlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by Penghy on 2017/7/10.
 */


public class FlowLayoutDemo extends ViewGroup {


    //记录每一行的view
    ArrayList<View> horView = new ArrayList<>();

    //记录每一整个布局的View
    ArrayList<ArrayList<View>> allViews = new ArrayList<>();

    //每一行的宽度
    ArrayList<Integer> listWidths = new ArrayList<>();

    //每一行的高度
    ArrayList<Integer> lsitHeights = new ArrayList<>();

    private Context mContext;
    public FlowLayoutDemo(Context context) {
        this(context,null);
    }

    public FlowLayoutDemo(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayoutDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initData(context,attrs);

    }

    private void initData(Context context,AttributeSet set) {

        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.FlowLayoutDemo);
        int selectId = typedArray.getInt(R.styleable.FlowLayoutDemo_selector, -1);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        int hightMode = MeasureSpec.getMode(heightMeasureSpec);


        int showWidth = 0;
        int showHeight = 0;

        //计算屏幕的宽度
        switch (widthMode){

            case MeasureSpec.EXACTLY : //match_parent-----20dp

                showWidth = widthSize;
                break;

            case MeasureSpec.AT_MOST: //wrap_content 不超过父控件大小

                //获取屏幕的值
                WindowManager  manager  = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display defaultDisplay = manager.getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                showWidth = point.x;
                break;

            case MeasureSpec.UNSPECIFIED://想要多大就有多大

                break;
        }

        //计算整体高度
        int totalHeight = 0;
        int totalWidth = 0;

        //每一行的宽度
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();

        for(int i = 0; i < count; i++){

            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);

            int measuredHeight = view.getMeasuredHeight();
            int measureWidth = view.getMeasuredWidth();

            if(lineWidth + measureWidth > showWidth - getPaddingLeft() - getPaddingRight()){

                lineWidth = measureWidth;
                totalHeight += lineHeight;

                //每一行计算完成----计算最大宽度
                totalWidth  = Math.max(totalWidth,lineWidth); //实际自定义view用的宽度

            }else{

                lineWidth = lineWidth + measureWidth;

                //取出当前行中，最高的一个
                lineHeight = Math.max(lineHeight,measuredHeight);

            }

            if(i == count-1){

                totalHeight += lineHeight;
            }
        }
        setMeasuredDimension(showWidth,totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        //布局完成之后---清空list,防止多次onmeasure 和 onlayout
        horView.clear();
        allViews.clear();

        //获取测量的宽度
        int width = getWidth();

        //每一行的宽度
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();

        for(int i = 0; i < count; i++){

            View view = getChildAt(i);
            int measuredHeight = view.getMeasuredHeight();
            int measureWidth = view.getMeasuredWidth();

            if(lineWidth + measureWidth > width - getPaddingLeft() - getPaddingRight()){

                lineWidth = measureWidth;

                horView.add(view);
                allViews.add(horView);
                horView = new ArrayList<>();

            }else{

                lineWidth = lineWidth + measureWidth;

                //取出当前行中，最高的一个
                lineHeight = Math.max(lineHeight,measuredHeight);

                //属于一行的view
                horView.add(view);
            }
        }


        int currentLeft = 0;
        int currentTop = 0;


        Log.i("mahao",allViews.size()+".....all_view..");

        //获取每一行的view
        for(int i = 0; i < allViews.size(); i++){

            ArrayList<View> views = allViews.get(i);

            Log.i("mahao",views.size()+".....hor_view..");
            //遍历每一行的每一个view
            for(int j = 0; j < views.size(); j++){

                View view = views.get(j);

                int measuredWidth = view.getMeasuredWidth();
                int measureHeight = view.getMeasuredHeight();

                int left = currentLeft;
                int top = view.getTop();
                int bottom = top + measureHeight;
                int right =left + measuredWidth;

                view.layout(left,top,right,bottom);

                currentLeft = left + measuredWidth;
            }
            currentTop += views.get(i).getMeasuredHeight(); //高叠加
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }


}
