package com.mahao.flowlayoutdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Penghy on 2017/7/10.
 */


public class FlowLayoutDemo extends ViewGroup implements View.OnClickListener {

   private ViewClickListener mClickListener;

    private static final int CENTER_NORMAL = -2;
    private static final int CENTRER_VERTICAL = 1;

    //记录每一行的view
    ArrayList<View> horView = new ArrayList<>();

    //记录每一整个布局的View
    ArrayList<ArrayList<View>> allViews = new ArrayList<>();

    //每一行的宽度
    ArrayList<Integer> listWidths = new ArrayList<>();

    //每一行的高度
    ArrayList<Integer> lsitHeights = new ArrayList<>();

    private Context mContext;
    private int mGravity;

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

    /**
     *
     * @param context   初始化数据
     * @param set
     */
    private void initData(Context context,AttributeSet set) {

        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(set, R.styleable.FlowLayoutDemo);
        int selectId = typedArray.getInt(R.styleable.FlowLayoutDemo_selector, -1);
        mGravity = typedArray.getInt(R.styleable.FlowLayoutDemo_gravity,CENTER_NORMAL);

        typedArray.recycle();
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

            //设置点击事件---和回调数据
            String content="mahao";
            if(view instanceof  TextView){

                TextView txtView = (TextView) view;
                content = (String) txtView.getText();
            }

            TagBean bean = new TagBean(i,content,this,false);
            view.setTag(bean);
            view.setOnClickListener(this);

            //强制测量ziview;
            measureChild(view,widthMeasureSpec,heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();

            int measuredHeight = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int measureWidth = view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            if(lineWidth + measureWidth > showWidth - getPaddingLeft() - getPaddingRight()){

                //计算新的高度
                totalHeight += lineHeight;

                //换行初始值
                lineWidth = measureWidth;
                lineHeight = measuredHeight;

                //每一行计算完成----计算最大宽度
                totalWidth  = Math.max(totalWidth,lineWidth); //实际自定义view用的宽度

            }else{

                lineWidth = lineWidth + measureWidth;

                //取出当前行中，最高的一个
                lineHeight = Math.max(lineHeight,measuredHeight);
            }

            if(i == count-1){

                totalWidth  = Math.max(totalWidth,lineWidth); //实际自定义view用的宽度
                totalHeight += lineHeight;
            }
        }
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? showWidth : totalWidth + getPaddingLeft() + getPaddingRight()
                ,totalHeight + getPaddingTop()+getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        //布局完成之后---清空list,防止多次onmeasure 和 onlayout
        horView.clear();
        allViews.clear();

        //获取测量的宽度
        int width = getMeasuredWidth();

        //每一行的宽度
        int lineWidth = 0;
        int lineHeight = 0;
        int count = getChildCount();

        for(int i = 0; i < count; i++){

            View view = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();

          //  int measuredHeight = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            int measureWidth = view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            if(lineWidth + measureWidth > width - getPaddingLeft() - getPaddingRight()){

                lineWidth = 0;
                allViews.add(horView);
                horView = new ArrayList<>();

            }
            //属于一行的view
            horView.add(view);
            lineWidth += measureWidth;
        }
        allViews.add(horView);

        int currentLeft = getPaddingLeft();
        int currentTop = getPaddingTop();

        Log.i("mahao",allViews.size()+".....all_view.."); //2

        //获取每一行的view
        for(int i = 0; i < allViews.size(); i++){

            ArrayList<View> views = allViews.get(i);

            Log.i("mahao",views.size()+".....hor_view.."); //8
            //遍历每一行的每一个view

            int preTop = 0;
            for(int j = 0; j < views.size(); j++){

                View view = views.get(j);

              /*  switch (mGravity){
                    case CENTRER_VERTICAL: //水平居中
                        //获取最大值，
                        if(j == 0){
                           // currentTop = getMaxHight(views)/2 + preTop ;
                        }
                        break;
                    case CENTER_NORMAL:  //默认显示
                        break;
                }*/
                MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
                int measuredWidth = view.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
                int measureHeight = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

                int left = currentLeft + lp.leftMargin;
              //  int top = currentTop + lp.topMargin ;//+ getMaxHight(views)/2;
                int top = currentTop + getMaxHight(views)/2 - view.getMeasuredHeight()/2;
                int right =left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();

                view.layout(left,top,right,bottom);

                //累加计算下一个left
                currentLeft = currentLeft + measuredWidth ;

                //计算每一个高度
                preTop = Math.max(measureHeight,preTop);

            }
            currentLeft = getPaddingLeft();
            currentTop += preTop; //高叠加
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {

        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

    /**
     *
     * @param list   获取最大的值
     * @return
     */
    public int  getMaxHight(ArrayList<View> list){

        int maxHight = 0;
        for(int i = 0; i < list.size(); i++){

            View view = list.get(i);
            MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
            int measureHeight = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            if(measureHeight > maxHight){

                maxHight = measureHeight;
            }
        }
        Log.i("mahao",maxHight+".....");
        return maxHight;
    }

    /**
     *
      * @param clickListener   设置监听事件
     */
    public void setOnclickListener(ViewClickListener clickListener){

        this.mClickListener = clickListener;
    }


    //传递view的点击事件给----listener
    @Override
    public void onClick(View v) {

        TagBean bean  = (TagBean) v.getTag();
        boolean selected = bean.isSelected();
        if(selected == false){

          //  selected = true;
            bean.setSelected(true);
            v.setTag(bean);

        }else {

            bean.setSelected(false);
            v.setTag(bean);
        }
        mClickListener.getOnclickListener(v,bean.getPosition(),bean.getContent(),bean.getLayoutDemo(),bean.isSelected());
    }

    /**
     *   view 点击事件；
     */
    interface  ViewClickListener{

        void getOnclickListener(View view,int position,String content,FlowLayoutDemo parent,boolean flag);
    }


    /**
     *  获取点击事件传递----第二种方式
     *
     *  1 : 从ontouchEvent获取event 获取x,y 找到对应的view;
     *
     *  2 ： 通过view找到对应的position
     *
     *  3 : 重写performClick方法 ,返回true ，回调自己的接口
     */

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    /**
     *
     * @param x  依据x,y获取对应的View;
     * @param y
     * @return  The hit rectangle of the view.
     */
    private TextView findChild(int x, int y)
    {
        final int cCount = getChildCount();
        for (int i = 0; i < cCount; i++)
        {
            TextView v = (TextView) getChildAt(i);
            if (v.getVisibility() == View.GONE) continue;
            Rect outRect = new Rect();

            //获得控件的矩形坐标---- onlayour方法之后才能调用
            v.getHitRect(outRect);
            if (outRect.contains(x, y))
            {
                return v;
            }
        }
        return null;
    }

}
