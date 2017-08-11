package com.goockr.nakedeyeguard.view.FirstUsePage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.goockr.nakedeyeguard.Base.BaseFragment;
import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.view.SettingPage.WifiPage.WifiActivity;

/**
 * Created by CMQ on 2017/6/6.
 */

public class OneFragment extends BaseFragment {

    private WifiActivity activty;

    @Override
    protected int getLoyoutId() {
        return R.layout.one_fragment;
    }

    @Override
    protected void onCusCreate(View view) {

        view.findViewById(R.id.bt_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaFragment(new BindingFragment());//跳转扫描
            }
        });
        activty = (WifiActivity) getActivity();
        activty.isHideArrow(false);
        activty.getBackBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishFragment();
            }
        });
    }

    public void replaFragment(Fragment fragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(
                R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out,
                R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fl_WifiConnect, fragment);
        transaction.addToBackStack("OneFragment");
        transaction.commit();
    }

//    private void finishFragment() {
//       /* InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
//        }
//        getActivity().getSupportFragmentManager().popBackStack();*/
//
//        Intent intent= new Intent(getActivity(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

    //   }
    private void finishFragment() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }


}
