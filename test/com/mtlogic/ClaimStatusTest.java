package com.mtlogic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.mockito.Mockito;

public class ClaimStatusTest {
	
	//public static final ClaimStatusService claimStatusServiceMock = Mockito.mock(ClaimStatusService.class);
	
	String valid276 = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	String valid276Response = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*00211~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	String msg276WithMissingISASegment = "GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	String msg276WithMissingIEASegment = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~";
	String msg276WithMissingGSSegment = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	String msg276WithMissingGESegment = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~IEA*1*000004251~";
	String msg276WithMissingSTSegment = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	String msg276WithMissingSESegment = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*:~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~GE*1*4251~IEA*1*000004251~";
	
	
	String test = "ISA*00*          *00*          *ZZ*341559999      *ZZ*CONSULT        *151005*1043*^*00501*000004251*0*P*|~GS*HR*341559999*CONSULT*20151005*1043*4251*X*005010X212~ST*276*000000001*005010X212~BHT*0010*13*0918151000*20151005*1043~HL*1**20*1~NM1*PR*2*MEDICAL MUTUAL OF OHIO*****PI*29076~HL*2*1*21*1~NM1*41*2*PATRICK J SKAROTE, MD, INC*****46*341559999~HL*3*2*19*1~NM1*1P*2*PATRICK J SKAROTE MD INC*****FI*550809531~HL*4*3*22*1~NM1*IL*1*TALLMAN*DARYN****MI*562912567807~HL*5*4*23~DMG*D8*19780729*F~NM1*QC*1*TALLMAN*JENNIFER~TRN*1*5660-18903.0-12~AMT*T3*0~DTP*472*RD8*20150604-20150604~SE*17*000000001~GE*1*4251~IEA*1*000004251~";
	
	@Test
	public void testClaimStatusWithValid276() {
		/*ClaimStatus claimStatus = new ClaimStatus();
		
		Response response = claimStatus.transmitClaimInquiry(valid276);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.SUCCESSFUL);
		assertEquals(response.getEntity().toString(), valid276.replaceAll("276", "277"));
		assertTrue(response.getEntity().toString().contains(valid276.replaceAll("276", "277")));*/
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingISASegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingISASegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Missing segment: ISA!]"));
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingIEASegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingIEASegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Mismatched segment: ISA/IEA]"));
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingGSSegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingGSSegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Mismatched segment: GS/GE]"));
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingGESegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingGESegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Mismatched segment: GS/GE]"));
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingSTSegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingSTSegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Mismatched segment: ST/SE]"));
	}
	
	@Test
	public void testClaimStatusWithInvalid276MissingSESegment() {
		ClaimStatus claimStatus = new ClaimStatus();
		Response response = claimStatus.transmitClaimInquiry(msg276WithMissingSESegment);
		
		assertNotNull(response);
		assertEquals(response.getStatusInfo().getFamily(), Response.Status.Family.CLIENT_ERROR);
		assertTrue(response.getEntity().toString().contains("[Mismatched segment: ST/SE]"));
	}
}
