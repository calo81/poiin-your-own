package com.poiin.yourown.social;

import android.graphics.Bitmap;

public interface ProfilePictureRetriever {
	Bitmap retrieveBitmap();
	void retrieveToImageView();
}
