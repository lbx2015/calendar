package net.riking.util.xmlmodel;

import java.util.List;

public class Ccif_t {
	private String CTNM;
	private String CITP;
	private String OITP;
	private String CTID;
	private List<Tsdt> TSDTs;
	public String getCTNM() {
		return CTNM;
	}
	public void setCTNM(String cTNM) {
		CTNM = cTNM;
	}
	public String getCITP() {
		return CITP;
	}
	public void setCITP(String cITP) {
		CITP = cITP;
	}
	public String getOITP() {
		return OITP;
	}
	public void setOITP(String oITP) {
		OITP = oITP;
	}
	public String getCTID() {
		return CTID;
	}
	public void setCTID(String cTID) {
		CTID = cTID;
	}
	public List<Tsdt> getTSDTs() {
		return TSDTs;
	}
	public void setTSDTs(List<Tsdt> tSDTs) {
		TSDTs = tSDTs;
	}
	
}
