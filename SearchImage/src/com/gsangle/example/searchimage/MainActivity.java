package com.gsangle.example.searchimage;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {

	
	private GridView gvResults;
	MenuItem searchItem;


	private ArrayList<ImageResult> imgResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageResultAdapter;
	private SearchView searchView = null;
	
	private String jsonQueryPrefix = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0";

	private final int SETTINGS_REQUEST = 1;
	private SearchFilters filters = new SearchFilters();

	private int curr_page = 0;
	private final int MAX_PAGES = 8;
	
	private String curr_query;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// question, why not setup imgResults here ?

		setUpViews();
		imageResultAdapter = new ImageResultArrayAdapter(this, imgResults);
		gvResults.setAdapter(imageResultAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imgResult = imgResults.get(position);
				i.putExtra("image", imgResult);
				startActivity(i);
			}
		});
		gvResults.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				// Triggered only when new data needs to be appended to the list
				// Add whatever code is needed to append new items to your AdapterView
				customLoadMoreDataFromApi(page); 
				// or customLoadMoreDataFromApi(totalItemsCount); 
			}

		});
	}


	private void customLoadMoreDataFromApi(int page) {
		if (page > MAX_PAGES) {
			return;
		}
		curr_page = page;
		performSearch();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.search_settings, menu);
		

		MenuItem searchItem = menu.findItem(R.id.miaction_search);
		searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				curr_page = 0;
				if (curr_page == 0 && curr_query != null && curr_query.equals(query)) {
					return true;
					// we are being invoked twice, ignore;
				}
				// User wants to start a fresh query
				curr_page = 0;
				curr_query = new String(query);
				performSearch();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);


	}

	void setUpViews() {
		//etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
	}  


	public void onClickSettings(MenuItem  item) {
		switch (item.getItemId()) {
		case R.id.miSettings :
			showSettingsPage();
		default:
			return;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode) {
		case SETTINGS_REQUEST:
			filters = (SearchFilters) data.getSerializableExtra("filters");
		default:
			return;
		}
	}  

	private void showSettingsPage() {
		Intent i = new Intent(getBaseContext(), SettingsActivity.class);
		i.putExtra("filters", filters);
		startActivityForResult(i, SETTINGS_REQUEST);
	}

	public void performSearch() {
		String input_query = searchView.getQuery().toString();
		String query = jsonQueryPrefix + "&start=" + curr_page*MAX_PAGES + "&q=" + Uri.encode(input_query);

		// Add query filters
		if (filters.getSize().length() > 0) {
			query += "&imgsz=" + filters.getSize();
		}  
		if (filters.getColor().length() > 0) {
			query += "&imgcolor=" + filters.getColor();
		}
		if (filters.getType().length() > 0) {
			query += "&imgtype=" + filters.getType();
		}
		if (filters.getSite().length() > 0) {
			query += "&as_sitesearch=" + Uri.encode(filters.getSite());
		}

		AsyncHttpClient client = new AsyncHttpClient();
		Log.d("DEBUG", "Query: " + query);
		client.get(query,
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject resp) {
				JSONArray imgJsonResults = null;
				try {
					imgJsonResults = resp.getJSONObject("responseData").getJSONArray("results");
					Log.d("Debug", "Size of the result is " + imgJsonResults.length());
  
					if (curr_page == 0) {
						imageResultAdapter.clear();
					}
					imageResultAdapter.addAll(
							ImageResult.fromJSONArray(imgJsonResults));
					
  
					// Log information for debugging purposes
					Log.d("DEBUG", "img result aa size is " + imageResultAdapter.getCount());
					Log.d("DEBUG", "img result size is " + imgResults.size());
				} catch (JSONException e) {
					// TODO : Try to indicate to the user that search query failed
					Log.d("Debug", "Search query failed");
					e.printStackTrace();
				}
			}});
	}
}
