package com.android.turntable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.turntable.R;
import com.android.turntable.applock.AppInfo;
import com.android.turntable.utils.PreferencesUtils;

import java.util.ArrayList;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppInfo> appList;
    private Context context;

    public AppListAdapter(Context context, ArrayList<AppInfo> appList) {
        this.context = context;
        this.appList = appList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        AppInfo appInfo = appList.get(position);
        holder.appIcon.setImageDrawable(appInfo.getIcon());
        holder.appName.setText(appInfo.getName());
        // 为 CheckBox 设置状态变化监听器
        holder.checkBox.setChecked(PreferencesUtils.getBoolean(context, appList.get(position).getPackageName(), false));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 更新数据模型中的选中状态

                // 处理 CheckBox 状态变化的逻辑
                if (isChecked) {
                    // CheckBox 被选中时的处理
                    PreferencesUtils.putBoolean(context, appList.get(position).getPackageName(), true);
                } else {
                    // CheckBox 取消选中时的处理
                    PreferencesUtils.putBoolean(context, appList.get(position).getPackageName(), false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView appIcon;
        TextView appName;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            checkBox = itemView.findViewById(R.id.app_checkbox);
        }
    }
}

