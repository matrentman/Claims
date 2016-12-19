package com.mtlogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
	public Response transmitClaimInquiry(String claimText) throws JSONException 
	{	
		Response response = null;
		X12Message claim = null;
		try {
			claim = new X12Message(claimText);
	
			claim.validate();
			System.out.println(claim.toString());
			String payerCode = getPayerCode(claim);
			String alveoPayerCode = lookupAlveoPayerCode(payerCode);
			setPayerCode(claim, alveoPayerCode);
			System.out.println(claim.toString());
		}
		catch (InvalidX12MessageException ixme) {
			System.out.println(ixme.getMessage());
			response = Response.status(422).entity(ixme.getMessage()).build();
		}
		
		if (response == null) {
			response = Response.status(200).entity(claim.toString()).build();
		}
		return response;
	}
	
	private String getPayerCode(X12Message claim) {
		String payerCode = null;
		X12Segment loop2100a = claim.getInterchangeControlList().get(0).getFunctionalGroupEnvelopes().get(0).getTransactionSetEnvelopes().get(0).getSegments().get(2);
		payerCode = loop2100a.getElements()[9];
		return payerCode;
	}
	
	private void setPayerCode(X12Message claim, String payerCode) {
		X12Segment loop2100a = claim.getInterchangeControlList().get(0).getFunctionalGroupEnvelopes().get(0).getTransactionSetEnvelopes().get(0).getSegments().get(2);
		loop2100a.getElements()[9] = payerCode;
	}
	
	private String lookupAlveoPayerCode(String payerCode) {
		String alveoPayerCode = null;
		
		try {
			URL url = new URL("http://192.0.0.71/ClaimStatusServices_Test/api/ClaimStatus/GetOutboundPayerCodeFromAlveoPayerCode/"+payerCode);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/plain");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				alveoPayerCode = output.replaceAll("\"", "");
			}

			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		return alveoPayerCode;
	}
}
