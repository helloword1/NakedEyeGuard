package com.goockr.nakedeyeguard.view.MainPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.goockr.nakedeyeguard.view.HealingProcessPage.HealingProcessActivity;
import com.goockr.nakedeyeguard.data.Model.UserBean;
import com.goockr.nakedeyeguard.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by JJT-ssd on 2017/2/24.
 */

public class GridAdapter extends BaseAdapter {
    private Context context;
    private List<UserBean> models;
    private boolean isShowDelete;
    private UserBean user;

    public GridAdapter(Context context, List<UserBean> models){
        this.context=context;
        this.models=models;
    }
    //返回子项的个数
    @Override
    public int getCount() {
        return models.size();
    }
    //返回子项对应的对象
    @Override
    public Object getItem(int position) {
        return models.get(position);
    }
    //返回子项的下标
    @Override
    public long getItemId(int position) {
        return position;
    }
    //返回子项视图
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        user= (UserBean) getItem(position);
        View view;
        final ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(context).inflate(R.layout.user_grid_item,null);
            viewHolder=new ViewHolder();
            viewHolder.sceneImage=(CircleImageView)view.findViewById(R.id.iv_SceneIcon);
            viewHolder.sceneName=(TextView)view.findViewById(R.id.tv_SceneName);
            view.setTag(viewHolder);

        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        viewHolder.sceneName.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getHead_image(),viewHolder.sceneImage);
        viewHolder.sceneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentUser=new Intent(parent.getContext(), HealingProcessActivity.class);
                intentUser.putExtra("UserBean",models.get(position));
                parent.getContext().startActivity(intentUser);
            }
        });

        return view;
    }
    //创建ViewHolder类
    class ViewHolder{
        CircleImageView sceneImage;
        TextView sceneName;
    }


}