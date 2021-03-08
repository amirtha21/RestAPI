package restassured;

import org.junit.Test;
import java.util.List;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class Usercreation {
   
	@Test
	public static void main(String[] args) {
    	
        //creating user and validating the response code
        String userid =
         given()
                        .accept("application/json")
                        .contentType("application/json") // defining the content type
                        .auth().oauth2(" <<user token>> ") // Since it is private token i have replaced the token with a variable name after successfully running it
                        // Message body with the name, gender,email and the status to create the user
                        .body("{\"name\":\"test quaadlis\", \"gender\":\"Female\", \"email\":\"test@qadualis.com\", \"status\":\"Active\"}") 
                        .post("https://gorest.co.in/public-api/users").then() // Hitting the end point
                        
                        .statusCode(200).extract().path("data.id").toString(); // checking the status code for success
        System.out.println(userid);


        //The following code will check if the created user persist
        given()
                .get("https://gorest.co.in/public-api/users/"+userid)
                .then()
                .statusCode(200)
                .extract().response().prettyPrint();

        //The following part of the code will update user name and email address of the already created user
        String updatedUsername = given()
                .accept("application/json")
                .contentType("application/json")
                .auth().oauth2("<<user token>>")
                .body("{\"name\":\"test update\", \"gender\":\"Male\", \"email\":\"test@update.com\", \"status\":\"Active\"}")
                .patch("https://gorest.co.in/public-api/users/"+userid).then()
                .statusCode(200).assertThat().extract().path("data.name"); // Assertion to check the presence of the created user

        System.out.println(updatedUsername);
        
        //The following code is for posting a comment with the body message
    		String comment = given()
    				.accept("application/json")
                    .contentType("application/json")
                    .auth().oauth2(" <<user token>> ")
                    .body("{\"title\" : \"sample\",\"body\":\"my post\"}")
                    .post("https://gorest.co.in/public-api/users").then()
                    .statusCode(200).extract().path("data.id").toString();
                    System.out.println(comment);
        
      //The following code checks if the comment persists and is related with the correct user.
                    
             given()
                    .get("https://gorest.co.in/public-api/users/"+userid)
                    .then()
                    .statusCode(200)
                    .extract().response().prettyPrint();


        //This part of the code is to delete the created user
        String deleteSuccess = given()
                .accept("application/json")
                .contentType("application/json")
                .auth().oauth2(" <<user token>> ")
                .delete("https://gorest.co.in/public-api/users/"+userid)
                .then()
                .statusCode(200).assertThat().extract().path("code").toString();

        System.out.println(deleteSuccess);
    }
}


