package com.gsangle.example.searchimage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	private Spinner spSize;
	private Spinner spColor;
	private Spinner spType;
	private EditText etSite;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		spSize = (Spinner) findViewById(R.id.spSize);
		spColor = (Spinner) findViewById(R.id.spSettingsColor);
		spType = (Spinner) findViewById(R.id.spSettingsType);
		etSite = (EditText) findViewById(R.id.etSettingsSite);
		btnSave = (Button) findViewById(R.id.btnSearch);

		// get the previously selected filters
		Intent i = getIntent();
		SearchFilters f = (SearchFilters) i.getSerializableExtra("filters");
		
		ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.settings_size, 
				android.R.layout.simple_spinner_item);
		sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spSize.setAdapter(sizeAdapter);
		spSize.setSelection(sizeAdapter.getPosition(f.getSize()));
		
		ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.settings_color, 
				android.R.layout.simple_spinner_item);
		colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spColor.setAdapter(colorAdapter);
		spColor.setSelection(colorAdapter.getPosition(f.getColor()));

		ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.settings_type, 
				android.R.layout.simple_spinner_item);
		typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spType.setAdapter(typeAdapter);
		spType.setSelection(typeAdapter.getPosition(f.getType()));
		        
	}
   

	public void onClickSave(View v) {

		Intent i = new Intent();

		// Return the filters to the main activity
		SearchFilters f = new SearchFilters();
		f.setColor(spColor.getSelectedItem().toString());
		f.setSize(spSize.getSelectedItem().toString());
		f.setType(spType.getSelectedItem().toString());

		f.setSite(etSite.getText().toString());

		i.putExtra("filters", f);
		setResult(RESULT_OK, i);
		finish();
	}
}
