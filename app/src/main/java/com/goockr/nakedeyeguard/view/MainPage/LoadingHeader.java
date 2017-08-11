package com.goockr.nakedeyeguard.view.MainPage;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.goockr.nakedeyeguard.R;
import com.goockr.nakedeyeguard.Tools.LoadingAnimation;
import com.shizhefei.view.coolrefreshview.CoolRefreshView;
import com.shizhefei.view.coolrefreshview.PullHeader;

/**
 * Created by JJT-ssd on 2017/2/27.
 */

public class LoadingHeader implements PullHeader {
    private View headView;
    private ImageView iv_Loading;

    @Override
    public View createHeaderView(final CoolRefreshView refreshView) {
        headView = LayoutInflater.from(refreshView.getContext()).inflate(R.layout.loading_headerview, refreshView, false);
        iv_Loading = (ImageView) headView.findViewById(R.id.iv_Loading);
        return headView;
    }

    /**
     * 开始拖动
     */
    @Override
    public void onPullBegin(CoolRefreshView refreshView) {
        headView.setVisibility(View.VISIBLE);
    }

    /**
     * 位置变化
     *
     * @param refreshView
     * @param status          状态 /没有任何操作
     *                        public final static byte PULL_STATUS_INIT = 1;
     *                        //开始下拉
     *                        public final static byte PULL_STATUS_TOUCH_MOVE = 2;
     *                        //回到原始位置
     *                        public final static byte PULL_STATUS_RESET = 3;
     *                        //刷新中
     *                        public final static byte PULL_STATUS_REFRESHING = 4;
     *                        //刷新完成
     *                        public final static byte PULL_STATUS_COMPLETE = 5;
     * @param dy              下拉事件的位移
     * @param currentDistance 当前位移的距离
     */
    @Override
    public void onPositionChange(CoolRefreshView refreshView, int status, int dy, int currentDistance) {
        int offsetToRefresh = getConfig().offsetToRefresh(refreshView, headView);
        if (status == CoolRefreshView.PULL_STATUS_TOUCH_MOVE) {
            if (currentDistance < offsetToRefresh) {

                if (!isDownArrow) {
                    LoadingAnimation.rotateDictAnimation(iv_Loading, currentDistance);
                    isDownArrow = true;
                } else {
                    LoadingAnimation.rotateDictAnimation(iv_Loading, -currentDistance);
                    isDownArrow = false;
                }
            }
        }
        else {
                if (isDownArrow) {
                    isDownArrow = false;
                }
            }

    }

    private boolean isDownArrow= false;

    /**
     * 刷新中
     */
    @Override
    public void onRefreshing(CoolRefreshView refreshView) {
        headView.setVisibility(View.VISIBLE);
        LoadingAnimation.rotateAnimation(iv_Loading);
    }

    /**
     * 没有刷新的释放回去
     */
    @Override
    public void onReset(CoolRefreshView refreshView, boolean pullRelease) {
        refreshView.setRefreshing(false);
    }

    /**
     * 设置刷新完成，并且释放回去
     */
    @Override
    public void onPullRefreshComplete(CoolRefreshView frame) {
        headView.setVisibility(View.GONE);
    }

    @Override
    public Config getConfig() {
        return config;
    }

    //使用默认的配置
    private DefaultConfig config = new DefaultConfig();

}