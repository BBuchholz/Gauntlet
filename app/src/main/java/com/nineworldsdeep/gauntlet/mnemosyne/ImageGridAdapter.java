package com.nineworldsdeep.gauntlet.mnemosyne;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineworldsdeep.gauntlet.R;

import java.util.ArrayList;

/**
 * Created by brent on 11/20/16.
 */

public class ImageGridAdapter extends ArrayAdapter {

    private Context mContext;
	private int mLayout;
	private ArrayList<ImageGridItem> mImageGridItems = new ArrayList<>();

	public ImageGridAdapter(Context ctx,
                            int layout,
                            ArrayList<ImageGridItem> imageGridItems) {

		super(ctx, layout, imageGridItems);
		this.mLayout = layout;
		this.mContext = ctx;
		this.mImageGridItems = imageGridItems;
	}

    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			row = inflater.inflate(mLayout, parent, false);
			holder = new ViewHolder();
			holder.tvTagString = (TextView) row.findViewById(R.id.txtTagLabel);
			holder.ivImage = (ImageView) row.findViewById(R.id.ivImage);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		ImageGridItem item = mImageGridItems.get(position);
		holder.tvTagString.setText(item.getTags());

		if(item.getImage() != null) {

			holder.ivImage.setImageBitmap(item.getImage());

		}else{

			holder.ivImage.setImageResource(R.mipmap.ic_nwd_junction);
		}

		return row;
	}

	@Override
	public ImageGridItem getItem(int position) {
        return mImageGridItems.get(position);
    }

	static class ViewHolder {
		TextView tvTagString;
		ImageView ivImage;
	}
}
