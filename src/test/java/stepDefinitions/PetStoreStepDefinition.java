package stepDefinitions;

import com.mysql.cj.util.Util;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import utils.Utils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class PetStoreStepDefinition extends AbstractStepDefinition {

    SoftAssert softAssert=new SoftAssert();

    @Given("API is set to variable")
    public void setAPIKey() {
        API_Key="4432";
    }

    @When("user adds a new pet information using {string} API")
    public void addNewPet(String scenario) throws IOException {
        String id,name, url, status;
        String baseURL= Utils.getPropertyData("baseURL")+"/pet";
        addPetReq=Utils.parseJSONFile("src/test/resources/testDataJson/petStore.json")
                .getJSONObject(scenario).toString();
        if(scenario.equals("Add-PET")){
            id="1";
            name="Bird";
            url="testbirdpic.org";
            status="Available";
        }else{
            throw new IllegalStateException("Unexpected value during pet addition" +scenario);
        }
        addPetReq=addPetReq.replace("$id", id)
                .replace("$name",name)
                .replace("$url",url)
                .replace("$status",status);

        addPetResp= given()
                .contentType("application/json")
                .header("api_key",API_Key)
                .body(addPetReq).log().all().when()
                .post(baseURL)
                .then().log().all().extract().response();
    }

    @Then("the response status code of {string} API is {int}")
    public void verifyResponseCode(String action, int code) {
        Response response;
        switch (action){
            case "PET-POST":
                response=addPetResp;
                break;
            default:
                throw new IllegalStateException("Unexpected value during response code validation" +action);
        }
        Utils.responseCodeValidation(response,code);
    }

    @And("validate {string} API response")
    public void validateAPIResponse(String action) {
        JsonPath js=new JsonPath(addPetResp.asString());
        switch (action){
            case "PET-POST":
                softAssert.assertEquals(js.getString("id"),"1", "ID field is not matching");
                softAssert.assertEquals(js.getString("category.name"),"Dogs", "Name value is not matching");
                break;
            default:
                throw new IllegalStateException("Unexpected value during response validation" +action);
        }
        softAssert.assertAll();
        System.out.println("Asserttion is completed");
    }
}
