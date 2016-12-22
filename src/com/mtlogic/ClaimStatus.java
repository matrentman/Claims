package com.mtlogic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mtlogic.x12.exception.InvalidX12MessageException;

@Path("/claim")
public class ClaimStatus {
	final Logger logger = LoggerFactory.getLogger(ClaimStatus.class);
	
	@Path("/inquiry")
	@POST
	@Consumes("text/plain")
	@Produces("text/plain")
	public Response transmitClaimInquiry(String inputMessage) throws JSONException 
	{	
		logger.info("Entered transmit claimInquiry(" + inputMessage + ")");
		Response response = null;
		String claimStatusResponse = null;
		ClaimStatusService claimStatusService = null;
		
		try {
			logger.debug("Input message - " + inputMessage);
			claimStatusService = new ClaimStatusService(inputMessage);
			claimStatusService.updateMessageWithAlveoPayerCode();
			logger.debug("Parsed message - " + claimStatusService.getClaimInquiry().toString());
		} catch (InvalidX12MessageException ixme) {
			logger.error("Could not parse incoming message! - " + ixme.getMessage());
			response = Response.status(422).entity(ixme.getMessage()).build();
		} catch (Exception e) {
			logger.error("Message could not be processed: " + e.getMessage());
			response = Response.status(422).entity("Message could not be processed: " + e.getMessage()).build();
		}
		
		if (response == null) {
			try {
				claimStatusResponse = claimStatusService.postInquiryToEmdeon(claimStatusService.getClaimInquiry().toString());
			} catch (Exception e) {
				logger.error("Could not connect to Emdeon: " + e.getMessage());
				response = Response.status(422).entity("Could not connect to Emdeon: " + e.getMessage()).build();
			}
		}
		
		if (response == null) {
			response = Response.status(200).entity(claimStatusResponse.toString()).build();
		}
		logger.info("Exited transmit claimInquiry(" + inputMessage + ")");
		return response;
	}
	
}
