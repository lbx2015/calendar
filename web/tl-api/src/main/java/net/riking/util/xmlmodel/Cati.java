package net.riking.util.xmlmodel;

import java.util.List;

public class Cati {
	private Cbif CBIF;
	private String HTDT;
	private List<Htcr> HTCRs;
	public Cbif getCBIF() {
		return CBIF;
	}
	public void setCBIF(Cbif cBIF) {
		CBIF = cBIF;
	}
	public String getHTDT() {
		return HTDT;
	}
	public void setHTDT(String hTDT) {
		HTDT = hTDT;
	}
	public List<Htcr> getHTCRs() {
		return HTCRs;
	}
	public void setHTCRs(List<Htcr> hTCRs) {
		HTCRs = hTCRs;
	}
	
	
	
}
