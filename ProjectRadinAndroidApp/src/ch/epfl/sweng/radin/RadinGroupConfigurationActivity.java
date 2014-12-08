package ch.epfl.sweng.radin;

import ch.epfl.sweng.radin.storage.RadinGroupModel;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.LinearLayout;

/**
 * 
 * @author Fabien Zellweger
 *
 */
public class RadinGroupConfigurationActivity extends DashBoardActivity {
	private RadinGroupModel mCurrentRadinGroupModel;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_radingroup_configuration);
		setHeader(getString(R.string.configuration), true, true);
		
		Bundle extras = getIntent().getExtras();
		mCurrentRadinGroupModel = ActionBar.getRadinGroupModelFromBundle(extras);
		
		LinearLayout thisLayout = (LinearLayout) findViewById(R.id.configurationRadinGroupLayout);
		ActionBar.addActionBar(this, thisLayout, mCurrentRadinGroupModel);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.radingroup_stats, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_home:
	        	Intent intent = new Intent(this, HomeActivity.class);
	        	startActivity(intent);
	            return true;
	        case R.id.action_settings:
	         
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
