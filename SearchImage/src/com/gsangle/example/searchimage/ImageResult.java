package com.gsangle.example.searchimage;

import java.io.Serializable;
import java.util.ArrayList;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This is the Model part of View Model
 * @author gsangle
 *
 */
public class ImageResult implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1266059278818003513L;
	private String fullUrl;
	private String thumbUrl;
	
	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			// could not parse the object properly, should we just crash ?
			e.printStackTrace();
		}
	}
	
	public String getFullUrl() {
		return fullUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public String toString() {
		return thumbUrl;
	}

	public static ArrayList<ImageResult> fromJSONArray(
			JSONArray imgJsonResults) {
		
		ArrayList<ImageResult> imgResults = new ArrayList<ImageResult>();
		for (int i = 0; i < imgJsonResults.length(); ++i) {
			try {
				imgResults.add(new ImageResult(imgJsonResults.getJSONObject(i)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("DEBUG", "Could not convert json img object to ImageResult");
				e.printStackTrace();
			}
		}
		
		return imgResults;
	}

}
