/**
 * 
 */
package ch.epfl.sweng.radin.test.storage;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import android.test.AndroidTestCase;
import ch.epfl.sweng.radin.storage.parsers.UserJSONParser;
import ch.epfl.sweng.radin.storage.UserModel;

/**
 * @author topali2
 *
 */
public class UserJSONParserTest extends AndroidTestCase{

	private static final String FIRSTNAME = "Julie";
	private static final String LASTNAME = "Djeffal";
	private static final String USERNAME = "julied20";
	private static final String EMAIL = "julie.djeffal@epfl.ch";
	private static final String ADDRESS = "quelquepart";
	private static final String IBAN = "CH246465 6464 6564";
	private static final String BICSWIFT = "CH802";
	private static final String PICTURE = "img/haha.png";
	private static final int ID = 2;

	private static final String FIRSTNAME2 = "Cedric";
	private static final String LASTNAME2 = "Cook";
	private static final String USERNAME2 = "Cookie";
	private static final String EMAIL2 = "cedrik.cook@agepoly.ch";
	private static final String ADDRESS2 = "chez lui";
	private static final String IBAN2 = "CH34 0026 5265 6399 1140 A";
	private static final String BICSWIFT2 = "CH801";
	private static final String PICTURE2 = "img/hihi.gif";
	private static final int ID2 = 3;
	
	private static final String FIRSTNAME3 = "Timothé";
	private static final String LASTNAME3 = "Lottaz";
	private static final String USERNAME3 = "Tim";
	private static final String PASSWORD3 = "nicetry";
	private static final String EMAIL3 = "timothe.lottaz@agepoly.ch";
	private static final String ADDRESS3 = "ici";
	private static final String IBAN3 = "CH34 A";
	private static final String BICSWIFT3 = "CH803";
	private static final String PICTURE3 = "img/hoho.gif";
	private static final int ID3 = 4;

	private static JSONObject json;
	private static String jsonString;
	private static List<UserModel> modelList; 


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		jsonString = "{"
				+ "     \"user\": ["
				+ "    {" 
				+ "                \"U_firstName\": \""+FIRSTNAME+"\","
				+ "                \"U_lastName\": \""+LASTNAME+"\","
				+ "                \"U_username\": \""+USERNAME+"\","
				+ "                \"U_email\": \""+EMAIL+"\","
				+ "                \"U_address\" : \""+ADDRESS+"\"," 
				+ "                \"U_iban\" : \""+IBAN+"\"," 
				+ "                \"U_bicSwift\" : \""+BICSWIFT+"\","
				+ "                \"U_picture\" : \""+PICTURE+"\","            
				+ "                \"U_ID\": "+ID+"" 
				+ "    }," 
				+ "    {" 
				+ "                \"U_firstName\": \""+FIRSTNAME2+"\","
				+ "                \"U_lastName\": \""+LASTNAME2+"\","
				+ "                \"U_username\": \""+USERNAME2+"\","
				+ "                \"U_email\": \""+EMAIL2+"\","
				+ "                \"U_address\" : \""+ADDRESS2+"\"," 
				+ "                \"U_iban\" : \""+IBAN2+"\","
				+ "                \"U_bicSwift\" : \""+BICSWIFT2+"\","
				+ "                \"U_picture\" : \""+PICTURE2+"\","            
				+ "                \"U_ID\": "+ID2+"" 
				+ "    },"
				+ "     {" 
				+ "                \"U_firstName\": \""+FIRSTNAME3+"\","
				+ "                \"U_lastName\": \""+LASTNAME3+"\","
				+ "                \"U_username\": \""+USERNAME3+"\","
				+ "                \"U_password\": \""+PASSWORD3+"\","
				+ "                \"U_email\": \""+EMAIL3+"\","
				+ "                \"U_address\" : \""+ADDRESS3+"\"," 
				+ "                \"U_iban\" : \""+IBAN3+"\","
			    + "                \"U_bicSwift\" : \""+BICSWIFT3+"\","
				+ "                \"U_picture\" : \""+PICTURE3+"\","            
				+ "                \"U_ID\": "+ID3+"" 
				+ "    }"  
				+ "  ]" 
				+ "}"; 

		json = new JSONObject(jsonString);
		modelList = new ArrayList<UserModel>();
		modelList.add(new UserModel(FIRSTNAME, LASTNAME, USERNAME, EMAIL, 
				ADDRESS, IBAN, BICSWIFT, PICTURE, ID));
		modelList.add(new UserModel(FIRSTNAME2, LASTNAME2, USERNAME2, EMAIL2, 
				ADDRESS2, IBAN2, BICSWIFT2, PICTURE2, ID2));
		modelList.add(new UserModel(FIRSTNAME3, LASTNAME3, USERNAME3, PASSWORD3,
				EMAIL3, ADDRESS3, IBAN3, BICSWIFT3, PICTURE3, ID3));

	}

	public void testJsonToModelList() throws JSONException {

		UserJSONParser user = new UserJSONParser();

		List<UserModel> modelListTest = user.getModelsFromJson(json);

		assertEquals(modelList, modelListTest);

	}

	public void testModeListToJson() throws JSONException {

		UserJSONParser user = new UserJSONParser();

		JSONObject jsonTest = user.getJsonFromModels(modelList);

		JSONAssert.assertEquals(json, jsonTest, true);


	}
}
