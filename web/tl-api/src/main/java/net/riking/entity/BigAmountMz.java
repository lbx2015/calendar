package net.riking.entity;

import java.math.BigDecimal;

/**
 * Created by bing.xun on 2017/5/10.
 * 大额是否满足报送，客户当天包含非人民币交易统一以美元计算
 */
public class BigAmountMz {

    //客户号
    private String ctid;

    //该客户收入是否都为人民币交易
    private Boolean isSrCNY;

    //该客户支出是否都为人民币交易
    private Boolean isZcCNY;

    //01-满足，02-不满足
    private String sfmz;

    //人民币收入总金额
    private BigDecimal cnysrzje;

    //人民币支出总金额
    private BigDecimal cnyzczje;

    //美元收入总金额
    private BigDecimal usdsrzje;

    //美元支出总金额
    private BigDecimal usdzczje;

    public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}


    public Boolean getIsSrCNY() {
        return isSrCNY;
    }

    public void setIsSrCNY(Boolean srCNY) {
        isSrCNY = srCNY;
    }

    public Boolean getIsZcCNY() {
        return isZcCNY;
    }

    public void setIsZcCNY(Boolean zcCNY) {
        isZcCNY = zcCNY;
    }

    public String getSfmz() {
        return sfmz;
    }

    public void setSfmz(String sfmz) {
        this.sfmz = sfmz;
    }

    public BigDecimal getCnysrzje() {
        return cnysrzje;
    }

    public void setCnysrzje(BigDecimal cnysrzje) {
        this.cnysrzje = cnysrzje;
    }

    public BigDecimal getCnyzczje() {
        return cnyzczje;
    }

    public void setCnyzczje(BigDecimal cnyzczje) {
        this.cnyzczje = cnyzczje;
    }

    public BigDecimal getUsdsrzje() {
        return usdsrzje;
    }

    public void setUsdsrzje(BigDecimal usdsrzje) {
        this.usdsrzje = usdsrzje;
    }

    public BigDecimal getUsdzczje() {
        return usdzczje;
    }

    public void setUsdzczje(BigDecimal usdzczje) {
        this.usdzczje = usdzczje;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ctid == null) ? 0 : ctid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BigAmountMz other = (BigAmountMz) obj;
		if (ctid == null) {
			if (other.ctid != null)
				return false;
		} else if (!ctid.equals(other.ctid))
			return false;
		return true;
	}
    
    
    
}
