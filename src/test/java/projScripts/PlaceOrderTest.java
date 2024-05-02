package projScripts;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PlaceOrderTest {
  
  
  @DataProvider(name="placeOrder")
  public String[][] getData() throws IOException, ParseException{
	String path=System.getProperty("user.dir")+"//src//test//resources//testFiles//testData.json";
	FileReader reader= new FileReader(path);
	JSONParser parser= new JSONParser();
	Object obj= parser.parse(reader);
	JSONObject jsonobj= (JSONObject) obj;
	JSONArray userArray=(JSONArray) jsonobj.get("PlaceOrderDets");
	String arr[][]= new String[userArray.size()][];
    for(int i=0; i< userArray.size(); i++)
    {
     JSONObject user=(JSONObject)userArray.get(i);
     String strName=(String)user.get("Name");
     String strCountry=(String)user.get("Country");
     String strCity=(String)user.get("City");
     String strCard=(String)user.get("CreditCard");
     String strMonth=(String)user.get("Month");
     String strYear=(String)user.get("Year");
	 String record[]= { strName, strCountry, strCity, strCard, strMonth, strYear};
	 arr[i]=record;
    }
	return arr;
  }
}
