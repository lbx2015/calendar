package net.riking.util.xmlmodel;

import java.util.Set;

public class Cbif {
	private String CSNM;
	private Set<String> CTNTs;
	private String CTVC;
	private Ccif_c CCIF;
	
	
	public String getCSNM() {
		return CSNM;
	}
	public void setCSNM(String cSNM) {
		CSNM = cSNM;
	}
	public String getCTVC() {
		return CTVC;
	}
	public void setCTVC(String cTVC) {
		CTVC = cTVC;
	}
	public Ccif_c getCCIF() {
		return CCIF;
	}
	public void setCCIF(Ccif_c cCIF) {
		CCIF = cCIF;
	}
	public Set<String> getCTNTs() {
		return CTNTs;
	}
	public void setCTNTs(Set<String> cTNTs) {
		CTNTs = cTNTs;
	}
	
	
	
		
}
