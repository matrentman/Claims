package com.mtlogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.mtlogic.x12.X12Message;
import com.mtlogic.x12.X12Segment;
import com.mtlogic.x12.exception.InvalidX12MessageException;

public class ClaimStatusService {
	private String originalClaimInquiry = null;
	private X12Message claimInquiry = null;
	
	public ClaimStatusService(String originalClaimInquiry) throws InvalidX12MessageException {
		super();
		this.originalClaimInquiry = originalClaimInquiry;
		claimInquiry = new X12Message(originalClaimInquiry);
		claimInquiry.validate();
	}
	
	public String getPayerCode(X12Message claim) {
		String payerCode = null;
		X12Segment loop2100a = claim.getInterchangeControlList().get(0).getFunctionalGroupEnvelopes()
				.get(0).getTransactionSetEnvelopes().get(0).getSegments().get(2);
		payerCode = loop2100a.getElements()[9];
		return payerCode;
	}

	public void setPayerCode(X12Message claim, String payerCode) {
		X12Segment loop2100a = claim.getInterchangeControlList().get(0).getFunctionalGroupEnvelopes()
				.get(0).getTransactionSetEnvelopes().get(0).getSegments().get(2);
		loop2100a.getElements()[9] = payerCode;
	}
	
	public void updateMessageWithAlveoPayerCode() {
		String payerCode = getPayerCode(claimInquiry);
		String alveoPayerCode = lookupAlveoPayerCode(payerCode);
		if (alveoPayerCode != null && !alveoPayerCode.isEmpty()) {
			setPayerCode(claimInquiry, alveoPayerCode);
		}
	}
	
	public String lookupAlveoPayerCode(String payerCode) {
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
	
	public String postInquiryToEmdeon(String claimStatusInquiry) {
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL("http://localhost:8080/Claims/api/test/echo");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/plain");
			
			OutputStream os = conn.getOutputStream();
			os.write(claimStatusInquiry.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
			
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
					System.out.println(output);
					sb.append(output);
			}
		
			conn.disconnect();
		
		} catch (MalformedURLException e) {
			e.printStackTrace();
		
		} catch (IOException e) {
			  e.printStackTrace();
		}
		
		return sb.toString();
	}

	public X12Message getClaimInquiry() {
		return claimInquiry;
	}

	public void setClaimInquiry(X12Message claimInquiry) {
		this.claimInquiry = claimInquiry;
	}

	public String getOriginalClaimInquiry() {
		return originalClaimInquiry;
	}
}
