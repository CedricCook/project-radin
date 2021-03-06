/**
 * 
 */
package ch.epfl.sweng.radin.storage.managers;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import ch.epfl.sweng.radin.callback.RadinListener;
import ch.epfl.sweng.radin.storage.RequestType;
import ch.epfl.sweng.radin.storage.UserModel;
import ch.epfl.sweng.radin.storage.parsers.JSONParser;
import ch.epfl.sweng.radin.storage.parsers.UserJSONParser;

/**
 * @author CedricCook
 * A storage manager for the model type UserModel
 */
public final class UserStorageManager extends StorageManager<UserModel> {
	private static UserStorageManager userStorageManager = null;
	private UserStorageManager() {

	}
	
    /**
     * Singleton constructor that returns the sole UserStorageManager
     * @return the singleton UserStorageManager
     */
	public static UserStorageManager getStorageManager() {
		if (userStorageManager == null) {
			userStorageManager = new UserStorageManager();
		}		
		return userStorageManager;
	}
	
    /* (non-Javadoc)
     * @see ch.epfl.sweng.radin.storage.StorageManager#getJSONParser()
     */
    @Override
    public JSONParser<UserModel> getJSONParser() {
        return new UserJSONParser();
    }

    /* (non-Javadoc)
     * @see ch.epfl.sweng.radin.storage.StorageManager#getTypeUrl()
     */
    @Override
    protected String getTypeUrl() {
        return "users";
    }

    /**
     * Check if the user is logged in
     * @author CedricCook
     * @param username the username to log in with
     * @param password the password for the username
     * @param callback will be called with the user who logged in, if correct
     */
	public void verifyLogin(String username, String password, RadinListener<UserModel> callback) {
		if (isConnected()) {
			if (!isHashMatchServer()) {
				ServerConnectionTask<UserModel> connTask = 
					 getConnectionFactory().createTask(callback, RequestType.POST,
								SERVER_BASE_URL + "login/" + username, getJSONParser());
				connTask.execute("{\"password\": \"" + password + "\"}");
			}
		}
	}
    
    /**
     * Gets a list of users who are friends with the user with ID userId
     * @param userId ID of the users that we want the friends of.
     * @param callback UserModel callback
     */
    public void getFriendsOfUserWithId(int userId, RadinListener<UserModel> callback) {
    	final String accessUrl = "userRelationships";
		if (isConnected()) {
			if (!isHashMatchServer()) {
				ServerConnectionTask<UserModel> connTask = getConnectionFactory().createTask(callback, RequestType.GET,
				        SERVER_BASE_URL + accessUrl + "/" + String.valueOf(userId), getJSONParser());
				//Example url: http://radin.epfl.ch/userRelationships/1
				connTask.execute();
				return;
			}
		}
    }
    
    /**
     * Get all members of the radinGroup with id radinGroupId.
     * @param radinGroupId
     * @param callback
     */
    public void getAllForGroupId(int radinGroupId, RadinListener<UserModel> callback) {
		final String accessUrl = "radingroups";
		if (isConnected()) {
			if (!isHashMatchServer()) {
				ServerConnectionTask<UserModel> connTask = getConnectionFactory().createTask(callback, RequestType.GET,
				        SERVER_BASE_URL + accessUrl + "/" + String.valueOf(radinGroupId) + "/" + getTypeUrl(), 
				        getJSONParser());
				//Example url: http://radin.epfl.ch/radingroups/1/users
				connTask.execute();
				return;
			}
		}
    }
    
    /**
     * Post one user as a member of the radinGroup with id radinGroupId and user ID
     * @param radinGroupId RadinGroup's ID, to which the user will be added
     * @param userId Used to add the user to the RadinGroup 
     * @param callback callback
     */
    public void postMemberToRadinGroup(int radinGroupId, int userId, RadinListener<UserModel> callback) {
    	final String accessUrl = "radingroups";
		if (isConnected()) {
			if (!isHashMatchServer()) {
				ServerConnectionTask<UserModel> connTask = getConnectionFactory().createTask(
								callback,
								RequestType.POST,
								SERVER_BASE_URL + accessUrl + "/" + String.valueOf(radinGroupId) + "/" 
								+ "adduser" + "/" + userId,
								getJSONParser());
				//Example url: http://radin.epfl.ch/radingroups/:radinId/adduser/:userId
				JSONObject json;
		    	List<UserModel> user = new ArrayList<UserModel>();
		    	user.add(new UserModel());
	            try {
	                json = (JSONObject) getJSONParser().getJsonFromModels(user);
	                connTask.execute(json.toString());
	            } catch (JSONException e) {
	                // TODO Handle error
	                e.printStackTrace();
	            }
				return;
			}
		}
    }
    
    /**
     * Add a new relationship between two users
     * @author CedricCook
     * @param userId the iniator of the friend request
     * @param friendUserName the user to become friends with
     * @param callback
     */
    public void addNewFriend(int userId, String friendUserName, RadinListener<UserModel> callback) {
    	final String accessUrl = "userRelationships/";
    	List<UserModel> user = new ArrayList<UserModel>();
    	user.add(new UserModel());
    	if (isConnected()) {
    		if (!isHashMatchServer()) {
    			ServerConnectionTask<UserModel> connTask = 
    			        getConnectionFactory().createTask(callback, RequestType.POST, 
    				  	SERVER_BASE_URL + accessUrl + userId + "/" + friendUserName, getJSONParser());
    			//Example url: http://radin.epfl.ch/userRalationships/1/uname
    			JSONObject json;
    			try {
    				json = (JSONObject) getJSONParser().getJsonFromModels(user);
    				connTask.execute(json.toString());
    			} catch (JSONException e) {
    				e.printStackTrace();
    			}
    			return;
    		}
    	}
    }
}

