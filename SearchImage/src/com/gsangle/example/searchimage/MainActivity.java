package com.gsangle.example.searchimage;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater.Filter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText etQuery;
	private GridView gvResults;
	private Button btnSearch;

	private ArrayList<ImageResult> imgResults = new ArrayList<ImageResult>();
	private ImageResultArrayAdapter imageResultAdapter;

	private String jsonQueryPrefix = "https://ajax.googleapis.com/ajax/services/search/images?rsz=8&v=1.0";
	
	private final int SETTINGS_REQUEST = 1;
	private SearchFilters filters = new SearchFilters();
	
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_settings, menu);
		return true;//
	}

	void setUpViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
	}  


	public void onImageSearch(View v) {
		String query = jsonQueryPrefix + "&start=" + 0 + "&q=" + Uri.encode(etQuery.getText().toString());

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
		//Toast.makeText(this, "searching for " + query, Toast.LENGTH_LONG).show();

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

					imgResults.clear();
					imageResultAdapter.addAll(ImageResult.fromJSONArray(imgJsonResults));

					// Log information for debugging purposes
					Log.d("DEBUG", imgResults.toString());
				} catch (JSONException e) {
					// TODO : Try to indicate to the user that search query failed
					Log.d("Debug", "Search query failed");
					e.printStackTrace();
				}
			}});
	}


	public void onClickSettings(MenuItem  item) {
		Toast.makeText(getApplicationContext(), "Id: " + item.getItemId(), Toast.LENGTH_LONG).show();
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
			Toast.makeText(getApplicationContext(), filters.getColor() + " " + filters.getSite() + " ", Toast.LENGTH_LONG).show();
		default:
			return;
		}
	}

	private void showSettingsPage() {
		// TODO Auto-generated method stub
		Intent i = new Intent(getBaseContext(), SettingsActivity.class);
		i.putExtra("filters", filters);
		startActivityForResult(i, SETTINGS_REQUEST);
	}
}
