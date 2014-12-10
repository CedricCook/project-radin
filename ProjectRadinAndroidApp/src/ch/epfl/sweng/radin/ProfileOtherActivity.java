package ch.epfl.sweng.radin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

/**
 * @author Fabien Zellweger
 */
public class ProfileOtherActivity extends DashBoardActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_profile_other);
		setHeader(getString(R.string.title_activity_profile_other), true, true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_other, menu);
		return true;
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
