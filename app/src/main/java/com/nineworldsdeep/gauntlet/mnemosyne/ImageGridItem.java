package com.nineworldsdeep.gauntlet.mnemosyne;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by brent on 11/20/16.
 */
public class ImageGridItem {

    private Bitmap mImage;
	private String mTagString;

	public ImageGridItem(Bitmap image, String tagString) {
		super();
		this.mImage = image;
		this.mTagString = tagString;
	}

	public ImageGridItem(FileListItem fli){
		this(BitmapFactory.decodeFile(fli.getFile().getAbsolutePath()),
				fli.getTags());
	}

	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap mImage) {
		this.mImage = mImage;
	}

	public String getTags() {
		return mTagString;
	}

	public void setTags(String mTagsLabel) {
		this.mTagString = mTagsLabel;
	}
}
