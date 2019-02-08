package com.qmetry.qaf.example.steps;

import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matchers;
import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.core.MessageTypes;
import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.util.Reporter;
import com.qmetry.qaf.automation.util.Validator;
import com.qmetry.qaf.automation.ws.RestWSTestCase;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;

public class WebServiceTest extends RestWSTestCase {

	@QAFTestStep(description="user send request {0}")
	public void getRequest(String request){
		Map<String, Object > map=new HashMap<>();
		map.put("Content-Type", "application/json");
		String tokenActual=(String)ConfigurationManager.getBundle().getProperty("token");
		RestAssured.baseURI=(String)ConfigurationManager.getBundle().getProperty("baseURI");
		Response resp=RestAssured.given().headers(map).body(tokenActual).post(request).andReturn();
		ConfigurationManager.getBundle().setProperty("resp", resp);
	}

	@QAFTestStep(description="response should contain {0}")
	public void verifyResponse(String result){
		@SuppressWarnings("unchecked")
		String str =((ResponseOptions<Response>) ConfigurationManager.getBundle().getProperty("resp")).getBody().asString();
		Validator.verifyThat(str, Matchers.containsString(result));
		Reporter.log(str, MessageTypes.Info);  
	}
}
