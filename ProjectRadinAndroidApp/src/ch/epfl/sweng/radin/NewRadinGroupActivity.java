package ch.epfl.sweng.radin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import ch.epfl.sweng.radin.callback.RadinListener;
import ch.epfl.sweng.radin.callback.StorageManagerRequestStatus;
import ch.epfl.sweng.radin.storage.RadinGroupModel;
import ch.epfl.sweng.radin.storage.UserModel;
import ch.epfl.sweng.radin.storage.managers.RadinGroupStorageManager;
import ch.epfl.sweng.radin.storage.managers.UserStorageManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity allows the user to create a new list of expenses.
 * The user must provide a name for the list and the names of the people that he wants share this list with. 
 *
 */
public class NewRadinGroupActivity extends Activity {
	public static final int FIFTEEN_SECS = 10000; //RadinGroupViewActivity.TEN_SECS ? 
	
	private final int mClientID = 1; //will be propagated from LoginActivity?
	private EditText mNameEdit;
	private boolean[] checkedItems;
	private ArrayList<UserModel> mFriendsModel;
	private  HashMap<String, UserModel> mNamesAndModel;
	private String[] mFriends;
	private ArrayList<UserModel> mParticipants;
	private int mRadinGroupId;
	private Activity mCurrentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_radingroup);
        retrieveData();
		
        mNameEdit = (EditText) findViewById(R.id.edit_name);
    }

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.new_radingroup, menu);
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
	
	/**
	 * Shows AlertDialog containing friends to select
	 *
	 */
	public void showDialog(View view) {
		if (mFriends != null) {
			createDialog().show();
		} else {
			Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * Retrieves the data (user's friends) from StorageManager
	 */
	private void retrieveData() {
		UserStorageManager usrStorageManager = UserStorageManager.getStorageManager();
		usrStorageManager.getFriendsOfUserWithId(mClientID, new RadinListener<UserModel>() {
			@Override
			public void callback(List<UserModel> items, StorageManagerRequestStatus status) {
				if (status == StorageManagerRequestStatus.SUCCESS) {
					affectDataRetrieved(items);
				} else {
					Toast.makeText(getApplicationContext(),	R.string.server_error3, Toast.LENGTH_SHORT).show();
					mCurrentActivity.finish();
				}
			}
			
		});
	}
	
	private void affectDataRetrieved(List<UserModel> items) {
		mFriendsModel = new ArrayList<UserModel>(items);
		mNamesAndModel = new HashMap<String, UserModel>();
		mFriends = new String[mFriendsModel.size()];
		
		for (int i = 0; i < mFriendsModel.size(); i++) {
			UserModel currentUser = mFriendsModel.get(i);
			String fullName = currentUser.getFirstName() + " " + currentUser.getLastName();
			mNamesAndModel.put(fullName, currentUser);
			mFriends[i] = fullName;
		}
		//initially false (default value)
		checkedItems = new boolean[mFriends.length];
	}
	
	/**
	 * Create a dialog that display friends to check to add to the new RadiGroup
	 * @param friendNames Client's Friends
	 * @return AlertDialog ready to be shown
	 */
	private AlertDialog createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.multi_friend);

		final ListView listView = new ListView(this);
		StableArrayAdapter<String> adapter = new StableArrayAdapter<String>(this, 
																			android.R.layout.select_dialog_multichoice,
																			mFriends);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		for (int i = 0; i < checkedItems.length; i++) {
			listView.setItemChecked(i, checkedItems[i]);
		}
		builder.setView(listView);

		// Set OK button
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				long[] checkedIds = listView.getCheckedItemIds();
				mParticipants = new ArrayList<UserModel>();

				checkedItems = new boolean[mFriends.length];
				for (int i = 0; i < checkedIds.length; i++) {
					checkedItems[(int) checkedIds[i]] = true;
					mParticipants.add(mNamesAndModel.get(mFriends[(int) checkedIds[i]]));
					// Log.i("participant" + i, mFriends[(int) checkedIds[i]]);
				}
				String participants = getResources().getString(R.string.participants);
				for (UserModel usr : mParticipants) {
					participants += usr.getFirstName() +" ";
				}
				TextView participantsTextView = (TextView) findViewById(R.id.participants_in_radin_group);
				participantsTextView.setText(participants);
			}
		});
		// Set CANCEL button
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// nothing to do
			}
		});
		return builder.create();
	}
	
	public void createRadinGroup(View view) {
		String rdGrpName = mNameEdit.getText().toString();
    	if ((rdGrpName == null) || rdGrpName.equals("")) {
    		Toast.makeText(getBaseContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
        } else if (mParticipants == null || mParticipants.isEmpty()) {
    		Toast.makeText(getBaseContext(), R.string.invalid_participants, Toast.LENGTH_SHORT).show();
        } else {
        	//valid data
        	sendRadinGroup(rdGrpName);
        }
    }
	
	private void sendRadinGroup(String name) {
		RadinGroupModel rdinGrpModel = new RadinGroupModel(-1, DateTime.now(), name, "", "");
    	RadinGroupStorageManager rdGrpStorageManager = RadinGroupStorageManager.getStorageManager();
    	ArrayList<RadinGroupModel> rdGroupToCreate = new ArrayList<RadinGroupModel>();
    	rdGroupToCreate.add(rdinGrpModel);
    	rdGrpStorageManager.create(rdGroupToCreate, new RadinListener<RadinGroupModel>() {
			@Override
			public void callback(List<RadinGroupModel> items, StorageManagerRequestStatus status) {
				if (status == StorageManagerRequestStatus.SUCCESS) {
					mRadinGroupId = items.get(0).getRadinGroupID();
					timer();
					sendParticipants();
					mCurrentActivity.finish();
					Toast.makeText(getBaseContext(), R.string.rd_group_created, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
				}
			}
    	});
	}
	
	private void sendParticipants() {
		if (!mParticipants.isEmpty()) {
			UserStorageManager usrStorageManager = UserStorageManager.getStorageManager();
			ArrayList<UserModel> usrList = new ArrayList<UserModel>();
			usrList.add(mParticipants.get(0));
			usrStorageManager.postMemberToRadinGroup(mRadinGroupId, usrList,	new RadinListener<UserModel>() {
				@Override
				public void callback(List<UserModel> items, StorageManagerRequestStatus status) {
					if (status == StorageManagerRequestStatus.SUCCESS) {
						mParticipants.remove(0);
					}
					sendParticipants();
				}
			});
		}
	}
	private void timer() {
    	ProgressDialog.show(this, "", "Sending. Please wait...", true);
    	new CountDownTimer(FIFTEEN_SECS, FIFTEEN_SECS) {
    		public void onTick(long millisUntilFinished) {
    		}
    		public void onFinish() {
				Toast.makeText(getBaseContext(), R.string.server_error2, Toast.LENGTH_SHORT).show();
				mCurrentActivity.finish();
			}
    	}.start();
	}
}