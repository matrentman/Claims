package com.mtlogic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;

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
				claimStatusResponse = claimStatusService.postInquiryToEmdeon(claimStatusService.getClaimInquiry().toString());
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
