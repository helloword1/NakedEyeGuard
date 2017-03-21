package com.goockr.nakedeyeguard.TipsPage;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by JJT-ssd on 2017/3/16.
 */

public class GuideAdapter extends PagerAdapter {

    List<ImageView> images;

    public GuideAdapter(List<ImageView> images)
    {
        this.images=images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
//    @Override
//    public void destroyItem(View container, int position, Object object) {
//        // TODO Auto-generated method stub
//        //((ViewPag.er)container).removeView((View)object);
//        ((ViewPager)container).removeView(images.get(position));
//
//    }

//    @Override
//    public Object instantiateItem(View container, int position) {
//        // TODO Auto-generated method stub
//        ((ViewPager)container).addView(images.get(position));
//        return images.get(position);
//    }


    @Override public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override public Object instantiateItem(ViewGroup view, int position) {
//        TextView textView = new TextView(view.getContext());
//        textView.setText(String.valueOf(position + 1));
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(Color.WHITE);
//        textView.setTextSize(48);
//        view.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT,
          //      ViewGroup.LayoutParams.MATCH_PARENT);
        view.addView(images.get(position));
        return images.get(position);
     //   return textView;
    }

    public void addItem() {

        notifyDataSetChanged();
    }

    public void removeItem() {
        notifyDataSetChanged();
    }
}
