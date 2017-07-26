package net.riking.util.xmlmodel;

import java.util.List;
import java.util.Map;

public class Tsdt {
	private String OCNM;
	private String OTDT;
	private String OTCD;
	private String OTIC;
	private Map<String, String> RINI;
	private Map<String, String> ATIF;
	private Map<String, String> TBIF;
	private Map<String, String> TSIF;
	private Map<String, String> TCIF;
	private List<String> ROTFs;

	public String getOCNM() {
		return OCNM;
	}

	public void setOCNM(String oCNM) {
		OCNM = oCNM;
	}

	public String getOTDT() {
		return OTDT;
	}

	public void setOTDT(String oTDT) {
		OTDT = oTDT;
	}

	public String getOTCD() {
		return OTCD;
	}

	public void setOTCD(String oTCD) {
		OTCD = oTCD;
	}

	public String getOTIC() {
		return OTIC;
	}

	public void setOTIC(String oTIC) {
		OTIC = oTIC;
	}

	public Map<String, String> getRINI() {
		return RINI;
	}

	public void setRINI(Map<String, String> rINI) {
		RINI = rINI;
	}

	public Map<String, String> getATIF() {
		return ATIF;
	}

	public void setATIF(Map<String, String> aTIF) {
		ATIF = aTIF;
	}

	public Map<String, String> getTBIF() {
		return TBIF;
	}

	public void setTBIF(Map<String, String> tBIF) {
		TBIF = tBIF;
	}

	public Map<String, String> getTSIF() {
		return TSIF;
	}

	public void setTSIF(Map<String, String> tSIF) {
		TSIF = tSIF;
	}

	public Map<String, String> getTCIF() {
		return TCIF;
	}

	public void setTCIF(Map<String, String> tCIF) {
		TCIF = tCIF;
	}

	public List<String> getROTFs() {
		return ROTFs;
	}

	public void setROTFs(List<String> rOTFs) {
		ROTFs = rOTFs;
	}

}
