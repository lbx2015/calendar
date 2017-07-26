package net.riking.util.xmlmodel;

import java.util.List;

public class Htcr {
	private String CRCD;
	private String TTNM;
	private List<Ccif_t> CCIFs;
	public String getCRCD() {
		return CRCD;
	}
	public void setCRCD(String cRCD) {
		CRCD = cRCD;
	}
	
	public String getTTNM() {
		return TTNM;
	}
	public void setTTNM(String tTNM) {
		TTNM = tTNM;
	}
	public List<Ccif_t> getCCIFs() {
		return CCIFs;
	}
	public void setCCIFs(List<Ccif_t> cCIFs) {
		CCIFs = cCIFs;
	}
	
}
