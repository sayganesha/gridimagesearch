package com.gsangle.example.searchimage;



import android.app.Activity;
import android.os.Bundle;

import com.loopj.android.image.SmartImageView;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
	
		// get the data passed to this activity using getIntent
		ImageResult img = (ImageResult) getIntent().getSerializableExtra("image");
		
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(img.getFullUrl());

	}
}
