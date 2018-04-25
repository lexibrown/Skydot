package com.comp.lexi.skydot.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp.lexi.skydot.R;
import com.comp.lexi.skydot.utils.IconUtil;

public class BaseImageAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    public BaseImageAdapter(Context c) {
        mInflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return IconUtil.getNumIcons();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {  // if it's not recycled,
            convertView = mInflater.inflate(R.layout.image_content, null);
            convertView.setLayoutParams(new GridView.LayoutParams(90, 90));
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.categoryText);
            holder.icon = (ImageView) convertView.findViewById(R.id.categoryimage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.icon.setAdjustViewBounds(true);
        holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.title.setText(IconUtil.getIconName(position));
        holder.icon.setImageResource(IconUtil.getIcon(position));
        return convertView;
    }

    private class ViewHolder {
        TextView title;
        ImageView icon;
    }

}