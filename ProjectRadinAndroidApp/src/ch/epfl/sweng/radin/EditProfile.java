package ch.epfl.sweng.radin;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ch.epfl.sweng.radin.callback.RadinListener;
import ch.epfl.sweng.radin.callback.StorageManagerRequestStatus;
import ch.epfl.sweng.radin.storage.UserModel;
import ch.epfl.sweng.radin.storage.managers.UserStorageManager;
/**
 *
 * @author topali2
 * Allows to edit user's informations.
 *
 */
public class EditProfile extends Activity {

	private UserModel mNewProfileModel = null;
	private boolean mProfileOK = false;
	private List<UserModel> mUserModelList = new ArrayList<UserModel>();
	private UserStorageManager mUserStorageManager;
	private int mUserId;
	private SharedPreferences mPrefs;

	/**
	 * 
	 * @author Fabien Zellweger
	 * enum to suppress magic number for OnClickListener
	 */
	private static enum ProfileDeleteTextField {
		FIRST_NAME,
		LAST_NAME,
		USER_NAME,
		ADDRESS,
		EMAIL,
		IBAN,
		BICS_WIFT;	
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_change);

		mPrefs = getSharedPreferences(LoginActivity.PREFS, MODE_PRIVATE);

		mUserId = Integer.parseInt(mPrefs.getString(getString(R.string.username), "-1"));

		mUserStorageManager = UserStorageManager.getStorageManager();

		Button buttonSave = (Button) findViewById(R.id.profileButtonSaveChanges);
		ImageButton deleteButtonFirstName = (ImageButton) findViewById(R.id.delete_button_firstName);
		ImageButton deleteButtonLastName = (ImageButton) findViewById(R.id.delete_button_lastName);
		ImageButton deleteButtonUsername = (ImageButton) findViewById(R.id.delete_button_username);
		ImageButton deleteButtonAddress = (ImageButton) findViewById(R.id.delete_button_address);
		ImageButton deleteButtonEmail = (ImageButton) findViewById(R.id.delete_button_email);
		ImageButton deleteButtonIban = (ImageButton) findViewById(R.id.delete_button_iBan);
		ImageButton deleteButtonBicSwift = (ImageButton) findViewById(R.id.delete_button_bicwift);


		buttonSave.setOnClickListener(profileChangeButtonListener);
		deleteButtonFirstName.setOnClickListener(deleteButtonListener);
		deleteButtonLastName.setOnClickListener(deleteButtonListener);
		deleteButtonUsername.setOnClickListener(deleteButtonListener);
		deleteButtonAddress.setOnClickListener(deleteButtonListener);
		deleteButtonEmail.setOnClickListener(deleteButtonListener);
		deleteButtonIban.setOnClickListener(deleteButtonListener);
		deleteButtonBicSwift.setOnClickListener(deleteButtonListener);

		retrieveUserInformation();

	}

	private OnClickListener deleteButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			EditText deletedText = null;
			ProfileDeleteTextField index = ProfileDeleteTextField.values()[Integer.valueOf((String) v.getTag())];

			switch (index) {
				case FIRST_NAME:
					deletedText = (EditText) findViewById(R.id.editProfileFirstName);
					break;
				case LAST_NAME:
					deletedText = (EditText) findViewById(R.id.editProfileLastName);
					break;
				case USER_NAME:
					deletedText = (EditText) findViewById(R.id.editProfileUsername);
					break;
				case ADDRESS:
					deletedText = (EditText) findViewById(R.id.editProfileAddress);
					break;
				case EMAIL:
					deletedText = (EditText) findViewById(R.id.editProfileEmail);
					break;
				case IBAN:
					deletedText = (EditText) findViewById(R.id.editProfileIBan);
					break;
				case BICS_WIFT:
					deletedText = (EditText) findViewById(R.id.editProfileBicSwift);
					break;

				default:
					break;
			}

			deletedText.getText().clear();
			deletedText.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(deletedText, InputMethodManager.SHOW_IMPLICIT);

		}
	};


	private OnClickListener profileChangeButtonListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			setAllEditText();

			if (mProfileOK) {

				mUserModelList.add(mNewProfileModel);

				mUserStorageManager.update(mUserModelList, new RadinListener<UserModel>() {

					@Override
					public void callback(List<UserModel> items, StorageManagerRequestStatus status) {
						if (status == StorageManagerRequestStatus.SUCCESS) {

							Intent profile = new Intent(getApplicationContext(), ProfileActivity.class);
							startActivity(profile);
						} else {

							displayErrorToast(getString(R.string.sending_user_data_error));

						}

					}

				});

			} else {
				retrieveUserInformation();	
			}

		};
	};

	/**
	 * 
	 */
	private void retrieveUserInformation() {

		if (mUserId != -1) {

			mUserStorageManager.getById(mUserId, new RadinListener<UserModel>() {

				@Override
				public void callback(List<UserModel> items, StorageManagerRequestStatus status) {
					if (status == StorageManagerRequestStatus.SUCCESS) {
						if (items.size() == 1) {						
							mNewProfileModel = items.get(0);
							mProfileOK = true;
							initializeEditText();
						} else {
							displayErrorToast(getString(R.string.retrieving_user_error));
						}

					} else {

						displayErrorToast(getString(R.string.server_general_error));


					}


				}
			});
		} else {
			displayErrorToast("please relogin again, error getting userID");
		}
	}

	/**
	 * 
	 * @param message of the error
	 */
	private void displayErrorToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private void initializeEditText() {

		EditText firstName = (EditText) findViewById(R.id.editProfileFirstName);
		firstName.setText(mNewProfileModel.getFirstName());

		EditText lastName = (EditText) findViewById(R.id.editProfileLastName);
		lastName.setText(mNewProfileModel.getLastName());


		EditText username = (EditText) findViewById(R.id.editProfileUsername);
		username.setText(mNewProfileModel.getUsername());


		EditText address = (EditText) findViewById(R.id.editProfileAddress);
		address.setText(mNewProfileModel.getAddress());


		EditText email = (EditText) findViewById(R.id.editProfileEmail);
		email.setText(mNewProfileModel.getEmail());


		EditText iBan = (EditText) findViewById(R.id.editProfileIBan);
		iBan.setText(mNewProfileModel.getIban());


		EditText bicSwift = (EditText) findViewById(R.id.editProfileBicSwift);
		bicSwift.setText(mNewProfileModel.getBicSwift());

	}
	private void setAllEditText() {

		mProfileOK = true;

		EditText newFirstName = (EditText) findViewById(R.id.editProfileFirstName);
		mNewProfileModel.setFirstName(newFirstName.getText().toString());

		EditText newLastName = (EditText) findViewById(R.id.editProfileLastName);
		mNewProfileModel.setLastName(newLastName.getText().toString());


		EditText newUsername = (EditText) findViewById(R.id.editProfileUsername);
		mNewProfileModel.setUsername(newUsername.getText().toString());


		EditText newAddress = (EditText) findViewById(R.id.editProfileAddress);
		mNewProfileModel.setAddress(newAddress.getText().toString());


		EditText newEmail = (EditText) findViewById(R.id.editProfileEmail);
		if (newEmail.getText().toString().contains("@")) {
			mNewProfileModel.setEmail(newEmail.getText().toString());			
		} else {
			mProfileOK = false;
			displayErrorToast("invalid email");
		}


		EditText newIBan = (EditText) findViewById(R.id.editProfileIBan);
		mNewProfileModel.setIban(newIBan.getText().toString());


		EditText newBicSwift = (EditText) findViewById(R.id.editProfileBicSwift);
		mNewProfileModel.setBicSwift(newBicSwift.getText().toString());

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_change, menu);
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
