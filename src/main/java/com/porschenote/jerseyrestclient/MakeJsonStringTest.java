package com.porschenote.jerseyrestclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObjectBuilder;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

public class MakeJsonStringTest {

	public static void main(String[] args) {

		String prettyJsonString = null;
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter prettyPrinter = mapper.writerWithDefaultPrettyPrinter();

		try {
			JsonNode rootNode = mapper.readTree(makeJsonString());
			prettyJsonString = prettyPrinter.writeValueAsString(rootNode);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(prettyJsonString);
	}

	public static String makeJsonString() {

		String resultString = null;

		try {
			Map<String, String> keyValueMap = new HashMap<String, String>();
			JsonBuilderFactory factory = Json.createBuilderFactory(keyValueMap);
			JsonObjectBuilder obj_1 = factory.createObjectBuilder();
			JsonObjectBuilder obj_2 = factory.createObjectBuilder();
			JsonObjectBuilder obj_3 = factory.createObjectBuilder();
			JsonObjectBuilder obj_4 = factory.createObjectBuilder();
			JsonArrayBuilder ab = factory.createArrayBuilder();

			obj_1.add("firstName", "Sunil121");
			obj_1.add("lastName", "Pandey121");
			obj_1.add("Title", "Master");
			//obj_2.add("child1", "Satvik");
			//obj_1.add("child", obj_2);
			/*obj_2.add("language", "Java");
			obj_2.add("grade", "Advanced");
			obj_3.add("language", "C/C++");
			obj_3.add("grade", "Advanced");
			ab.add(obj_2);
			ab.add(obj_3);

			obj_1.add("languageskill", ab);

			obj_4.add("db1", "Oracle");
			obj_4.add("db2", "MySQL");
			ab.add(obj_4);

			obj_1.add("database", ab);

			obj_1.add("laptop", "MacBookPro");*/

			resultString = obj_1.build().toString();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return resultString;
	}

}
