package com.mtlogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.mtlogic.x12.X12Message;
import com.mtlogic.x12.X12Segment;
import com.mtlogic.x12.exception.InvalidX12MessageException;

@Path("/claim")
public class ClaimStatus {
	@Path("/inquiry")
	@POST
	@Consumes("text/plain")
	@Produces("text/plain")
	public Response transmitClaimInquiry(String inputMessage) throws JSONException 
	{	
		Response response = null;
		X12Message claimInquiry = null;
		String claimStatusResponse = null;
		ClaimStatusService claimStatusService = null;
		try {
			System.out.println(inputMessage);
			claimStatusService = new ClaimStatusService(inputMessage);
			claimStatusService.updateMessageWithAlveoPayerCode();
			System.out.println(claimStatusService.getClaimInquiry().toString());
		} catch (InvalidX12MessageException ixme) {
			System.out.println(ixme.getMessage());
			response = Response.status(422).entity(ixme.getMessage()).build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			response = Response.status(422).entity("Message could not be processed: " + e.getMessage()).build();
		}
		
		if (response == null) {
			try {
				claimStatusResponse = claimStatusService.postInquiryToEmdeon(claimInquiry.toString());
			} catch (Exception e) {
				System.out.println(e.getMessage());
				response = Response.status(422).entity("Could not connect to Emdeon: " + e.getMessage()).build();
			}
		}
		
		if (response == null) {
			response = Response.status(200).entity(claimStatusResponse.toString()).build();
		}
		return response;
	}
	
}
