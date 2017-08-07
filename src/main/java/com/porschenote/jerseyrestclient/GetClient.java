package com.porschenote.jerseyrestclient;

import javax.ws.rs.core.MediaType;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class GetClient {

	public static void main(String[] args) {

		String url = "http://localhost:8085/person/10"; // Input URL

		try {

			Client client = Client.create();

			WebResource webResource = client.resource(url);

			ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Response from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}