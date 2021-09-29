import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.AddPlace;
import pojo.AddPlaceLocation;
import pojo.GetPlace;
import pojo.UpdatePlace;

public class SecondDemo {

	public static void main(String[] args) {
		
		////////////////////////ADD PLACE GET API \\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		//Setting all the values to POJO SETTERS (USING SERIALIZATION)
		AddPlace ap = new AddPlace();
		ap.setAccuracy(50);
		ap.setName("Frontline house");
		ap.setPhone_number("(+1) 444 888 7777");
		ap.setAddress("29, side layout, cohen 09");
		ap.setWebsite("http://google.com");
		ap.setLanguage("French-IN");
		
		
		AddPlaceLocation apl = new AddPlaceLocation();
		apl.setLat(-38.383494);
		apl.setLng(33.427362);
		ap.setLocation(apl);
		
		List<String> typeList = new ArrayList<String>();
		typeList.add("shoe park");
		typeList.add(" shop");
		ap.setTypes(typeList);
		
		//Saving response to String variable
		String addPlaceResp = 
				given()
					.spec(Utils.ReqSpec())
					.body(ap)
				.when()
					.post("/maps/api/place/add/json")
				.then()
					.spec(Utils.RespSpec())
				.extract().response().asString();
		
		JsonPath addPlaceJp = Utils.rawToJson(addPlaceResp);
		String place_id = addPlaceJp.getString("place_id");
		System.out.println(place_id);
		
		
		////////////////////////GET PLACE GET API \\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		//(USING DESERIALIZATION)
		GetPlace gp = 
				given()
					.spec(Utils.ReqSpec())
					.queryParam("place_id", place_id)
					.expect().defaultParser(Parser.JSON)
				.when()
					.get("/maps/api/place/get/json")
				.as(GetPlace.class);
				
		

		//Using TESTNG validations
		Assert.assertEquals(gp.getLocation().getLatitude(), ap.getLocation().getLat());
		Assert.assertEquals(gp.getLocation().getLongitude(), ap.getLocation().getLng());
		Assert.assertEquals(gp.getAccuracy(), ap.getAccuracy());
		Assert.assertEquals(gp.getName(), ap.getName());
		Assert.assertEquals(gp.getPhone_number(), ap.getPhone_number());
		Assert.assertEquals(gp.getAddress(), ap.getAddress());
		Assert.assertEquals(gp.getWebsite(), ap.getWebsite());
		Assert.assertEquals(gp.getLanguage(), ap.getLanguage());
	
		////////////////////////// UPDATE PLACE API \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
		//Setting all the values to POJO SETTERS
		UpdatePlace up =  new UpdatePlace();
		up.setPlace_id(place_id);
		up.setAddress("UPDATED ADDRESS");
		up.setKey("qaclick123");
		
		String updatePlaceResponse = 
				given().log().all()
					.spec(Utils.ReqSpec())
					.body(up)
				.when()
					.put("/maps/api/place/update/json")
				.then()
					.spec(Utils.RespSpec())	
				.extract().response().asString();
		
		//Using JsonPath to parse String response and get JSON elements
		JsonPath updateJp = Utils.rawToJson(updatePlaceResponse);
		String updateMsg = updateJp.getString("msg");
		
		//Using TESTNG validations
		Assert.assertEquals("Address successfully updated", updateMsg);
		
		
		//////////////////////SECOND GET PLACE GET API REQUEST \\\\\\\\\\\\\\\\\\\\\\\\\
		//GetPlace getPlaceResp2 = 
		GetPlace gp2 = 
				given()
					.spec(Utils.ReqSpec())
					.queryParam("place_id", place_id)
					.expect().defaultParser(Parser.JSON)
				.when()
					.get("https://rahulshettyacademy.com/maps/api/place/get/json")
				.as(GetPlace.class);
		
		//Using TESTNG validations
		//Validating address updated in UPDATE API is retrieving back in GET API
		Assert.assertEquals(gp2.getAddress(), up.getAddress());
		//Validating updated address from second GET response with first GET response.
		Assert.assertNotEquals(gp2.getAddress(), gp.getAddress());
		//Validating rest of the values matches with old GET response.
		Assert.assertEquals(gp2.getLocation().getLatitude(), gp.getLocation().getLatitude() );
		Assert.assertEquals(gp2.getLocation().getLongitude(), gp.getLocation().getLongitude());
		Assert.assertEquals(gp2.getAccuracy(), gp.getAccuracy());
		Assert.assertEquals(gp2.getName(), gp.getName());
		Assert.assertEquals(gp2.getPhone_number(), gp.getPhone_number());
		Assert.assertEquals(gp2.getTypes(), gp.getTypes());
		Assert.assertEquals(gp2.getWebsite(), gp.getWebsite());
		Assert.assertEquals(gp2.getLanguage(), gp.getLanguage());
		
	}

}
