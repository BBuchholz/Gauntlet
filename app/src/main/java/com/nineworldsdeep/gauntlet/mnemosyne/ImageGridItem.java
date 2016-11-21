package com.nineworldsdeep.gauntlet.mnemosyne;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

/**
 * Created by brent on 11/20/16.
 */
public class ImageGridItem {

    private Bitmap mImage;
	private String mTagString;
    private File mFile = null;

	public ImageGridItem(File imageFile, Bitmap image, String tagString) {
		super();

        this.mFile = imageFile;
		this.mImage = image;
		this.mTagString = tagString;
	}

	public ImageGridItem(FileListItem fli){
		this(fli.getFile(),
                BitmapFactory.decodeFile(fli.getFile().getAbsolutePath()),
				fli.getTags());
	}

	public Bitmap getImage() {

		return mImage;
	}

	public String getTags() {

		return mTagString;
	}

    public File getFile() {

        return mFile;
    }

}
