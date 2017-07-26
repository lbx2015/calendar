package net.riking.util.suspxmlmodel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Seif {
	private String CSNM;
	private String SEVC;
	private List<Siif> SIIFs;
	private Set<String> STNTs;
	private Scif SCIF;
	private Map<String, String> SRIF;
	public String getCSNM() {
		return CSNM;
	}
	public void setCSNM(String cSNM) {
		CSNM = cSNM;
	}
	
	public String getSEVC() {
		return SEVC;
	}
	public void setSEVC(String sEVC) {
		SEVC = sEVC;
	}
	public List<Siif> getSIIFs() {
		return SIIFs;
	}
	public void setSIIFs(List<Siif> sIIFs) {
		SIIFs = sIIFs;
	}
	public Set<String> getSTNTs() {
		return STNTs;
	}
	public void setSTNTs(Set<String> sTNTs) {
		STNTs = sTNTs;
	}
	public Scif getSCIF() {
		return SCIF;
	}
	public void setSCIF(Scif sCIF) {
		SCIF = sCIF;
	}
	public Map<String, String> getSRIF() {
		return SRIF;
	}
	public void setSRIF(Map<String, String> sRIF) {
		SRIF = sRIF;
	}
	
	
}
