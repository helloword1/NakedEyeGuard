package com.goockr.nakedeyeguard.TipsPage;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.HealingProcessPage.CourseOfTreatmentFragment;
import com.goockr.nakedeyeguard.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.R;

import java.util.ArrayList;
import java.util.List;

import static com.goockr.nakedeyeguard.Tools.Common.replaFragment;

/**
 * Created by JJT-ssd on 2017/3/16.
 */

public class GuideFragment extends BaseFragment implements View.OnClickListener{

    int[] imagesRes={R.drawable.guide_one,R.drawable.guide_two,R.drawable.guide_three};
    //int[] imagesRes={R.drawable.guide1,R.drawable.guide2,R.drawable.guide3};
    private List<ImageView> images;
    Button bt_GuideNext;
    ViewPager vp_GuidePage;
    @Override
    protected int getLoyoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
        setWifiIcon(((HealingProcessActivity)getActivity()).getNetWorkState());
    }

    private void setupUI(final View view) {
        setData();
        vp_GuidePage=(ViewPager)view.findViewById(R.id.vp_GuidePage);
        bt_GuideNext=(Button)view.findViewById(R.id.bt_GuideNext);
        bt_GuideNext.setOnClickListener(this);
        GuideAdapter guideAdapter=new GuideAdapter(images);

        //CircleIndicator indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        vp_GuidePage.setAdapter(guideAdapter);
       // indicator.setViewPager(vp_GuidePage);
        //vp_GuidePage.setCurrentItem(0);


        getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        vp_GuidePage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (position==images.size()-1)
                {
                    bt_GuideNext.setEnabled(true);
                    bt_GuideNext.setVisibility(View.VISIBLE);
                    setAnimation(bt_GuideNext,true);
                }
                else {
                    bt_GuideNext.setEnabled(false);
                    setAnimation(bt_GuideNext,false);
                    bt_GuideNext.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    ScaleAnimation scaleAnimation;
    private void setAnimation(View v,boolean isShow)
    {
        if (isShow) scaleAnimation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        else scaleAnimation = new ScaleAnimation(0.0f,0.0f,0.0f,0.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(800);//动画时间
        scaleAnimation.setFillAfter(true);//动画结束后停留在当前位置
        v.startAnimation(scaleAnimation);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_GuideNext:
                replaFragment(getActivity(),new CourseOfTreatmentFragment(),R.id.fl_HPFLayout,true);
                break;
        }
    }
    public void setData()
    {
        images=new ArrayList<>();
        for(int i=0;i<imagesRes.length;i++)
        {
            ImageView mImageView=new ImageView(getActivity());
            mImageView.setBackgroundResource(imagesRes[i]);
            images.add(mImageView);
        }
    }


}
