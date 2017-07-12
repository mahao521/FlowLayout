package com.mahao.flowlayoutdemo;

import android.view.View;

/**
 * Created by Penghy on 2017/7/12.
 */


public class TagBean {

    private int position ;

    private String  content;

    private FlowLayoutDemo mLayoutDemo;

    private boolean selected;



    public TagBean(int position, String content, FlowLayoutDemo layoutDemo, boolean selected) {
        this.position = position;
        this.content = content;
        mLayoutDemo = layoutDemo;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FlowLayoutDemo getLayoutDemo() {
        return mLayoutDemo;
    }

    public void setLayoutDemo(FlowLayoutDemo layoutDemo) {
        mLayoutDemo = layoutDemo;
    }
}
