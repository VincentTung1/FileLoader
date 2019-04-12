package com.vincent.loadfilelibrary.engine.zip.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincent.loadfilelibrary.R;
import com.vincent.loadfilelibrary.engine.x5.utils.FileUtils;
import com.vincent.loadfilelibrary.engine.zip.manager.ResourceTypeManager;

import java.io.File;
import java.util.ArrayList;

public class ZipPreviewAdapter extends BaseAdapter {


    private final Context mContext;

    private ArrayList<File> mFiles;

    public ZipPreviewAdapter(Context context,ArrayList<File> files) {
        mContext = context;
        mFiles = files;
    }

    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public Object getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.lv_zip_preview_item,null);
        ImageView ivIcon = root.findViewById(R.id.mIcon);
        TextView tvName = root.findViewById(R.id.mName);


        File file = mFiles.get(position);

        String expansion = FileUtils.getExspansion(file.getName());

        Integer resource = ResourceTypeManager.get().getMimeDrawable(expansion);

        if (resource != -1){
            ivIcon.setImageResource(resource);
        }

        tvName.setText(file.getName());

        return root;
    }
}
