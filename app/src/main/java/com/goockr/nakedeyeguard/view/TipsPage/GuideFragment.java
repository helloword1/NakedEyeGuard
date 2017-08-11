package com.goockr.nakedeyeguard.view.TipsPage;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.view.HealingProcessPage.CourseOfTreatmentFragment;
import com.goockr.nakedeyeguard.view.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.data.Model.UserDetailBean;
import com.goockr.nakedeyeguard.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JJT-ssd on 2017/3/16.
 */

public class GuideFragment extends BaseFragment implements View.OnClickListener {

    int[] imagesRes = {R.drawable.guide_one, R.drawable.guide_two, R.drawable.guide_three};
    UserDetailBean userInfo;
    private List<ImageView> images;
    Button bt_GuideNext;
    ViewPager vp_GuidePage;
    HealingProcessActivity activity;

    @Override
    protected int getLoyoutId() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void onCusCreate(View view) {
        setupUI(view);
    }


    private void setupUI(final View view) {
        userInfo = ((HealingProcessActivity) getActivity()).userInfo;
        setData();
        vp_GuidePage = (ViewPager) view.findViewById(R.id.vp_GuidePage);
        bt_GuideNext = (Button) view.findViewById(R.id.bt_GuideNext);
        bt_GuideNext.setOnClickListener(this);
        GuideAdapter guideAdapter = new GuideAdapter(images);
        vp_GuidePage.setAdapter(guideAdapter);
        activity = (HealingProcessActivity) getActivity();
        activity.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getSupportFragmentManager().popBackStack();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                bt_GuideNext.setEnabled(true);
                bt_GuideNext.setVisibility(View.VISIBLE);
                setAnimation(bt_GuideNext, true);
            }
        }, 600);


    }

    ScaleAnimation scaleAnimation;

    private void setAnimation(View v, boolean isShow) {
        if (isShow)
            scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        else
            scaleAnimation = new ScaleAnimation(0.0f, 0.0f, 0.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(800);//动画时间
        scaleAnimation.setFillAfter(true);//动画结束后停留在当前位置
        v.startAnimation(scaleAnimation);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_GuideNext:
                bt_GuideNext.setEnabled(false);
                replaFragment(new CourseOfTreatmentFragment());

                break;
        }
    }

    public void setData() {

        images = new ArrayList<>();

        ImageView mImageView = new ImageView(getActivity());
        UserDetailBean.TreatmentBean treatmentBean = userInfo.getTreatment();
        if (treatmentBean.getType().equals("A")) mImageView.setBackgroundResource(imagesRes[0]);
        else if (treatmentBean.getType().equals("B"))
            mImageView.setBackgroundResource(imagesRes[1]);
        else if (treatmentBean.getType().equals("C"))
            mImageView.setBackgroundResource(imagesRes[2]);
        else mImageView.setBackgroundResource(imagesRes[0]);
        images.add(mImageView);

    }
    public void replaFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_HPFLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
