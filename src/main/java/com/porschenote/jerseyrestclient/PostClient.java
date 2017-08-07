package com.porschenote.jerseyrestclient;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class PostClient {

	public static void main(String[] args) {

		String url = "http://localhost:8085/person"; // Input URL
		//String json = "{\"id\":3,\"loginId\":\"sss\",\"firstName\":\"gggg\",\"lastName\":\"Gautam\",\"currentAddress\":\"Dallas\"}"; // Input Json String
		//String url = "http://localhost:8199/app/records";
				String json =MakeJsonStringTest.makeJsonString();
		try {

			Client client = Client.create();

			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, json);

			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			System.out.println("Response from Server .... \n");
			String output = response.getEntityTag().getValue();
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}