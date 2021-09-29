import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Utils {
	public static JsonPath rawToJson(String rawString) {
		
		JsonPath jp = new JsonPath(rawString);
		return jp;
	}
	
	
	public static RequestSpecification ReqSpec() {
		
		RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		return req;
	}
	
	
	public static ResponseSpecification RespSpec() {
		
		ResponseSpecification resp = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		return resp;
	}
	
	
}
