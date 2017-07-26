package net.riking.entity.model;

public class Result {
	  private String csnm;
	  private String ctvc;
	  private Number sum;

	  public String getCsnm() {
	    return csnm;
	  }

	  public void setCsnm(String csnm) {
	    this.csnm = csnm;
	  }

	  public String getCtvc() {
	    return ctvc;
	  }

	  public void setCtvc(String ctvc) {
	    this.ctvc = ctvc;
	  }

	  public Number getSum() {
	    return sum;
	  }

	  public void setSum(Number sum) {
	    this.sum = sum;
	  }

	  @Override
	  public String toString() {
	    return "Result [csnm=" + csnm + ", ctvc=" + ctvc + ", sum=" + sum + "]";
	  }
}
