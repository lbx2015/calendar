package net.riking.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.riking.core.cache.RCache;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.entity.BigAmountMz;
import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.Sdcurrpd;
import net.riking.service.repo.AmlRuleEngineRepo;
import net.riking.service.repo.BigAmountRepo;

@Service("bigCheckService")
public class BigCheckServiceImpl {

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	SdcurrpdServiceImpl sdcurrpdService;

    @Autowired
    private AmlRuleEngineRepo amlRuleEngineRepo;

    private static RCache<List<BigAmount>> cacheBig = new RCache<List<BigAmount>>( 4);

    private static RCache<AmlRuleEngine> cacheRuleEngine = new RCache<AmlRuleEngine>( 4);

    private  List<BigAmount> getBigAmountList(String ctid, Date rpdt,String crcd){
        List<BigAmount> temp = new ArrayList<BigAmount>();
        if(null == ctid || null == rpdt || null == crcd){
            return temp;
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String timeStr = sf.format(rpdt);
        List<BigAmount> list =  cacheBig.get(timeStr);
        if(null == list || list.size() == 0){
            list = bigAmountRepo.findallByRpdt(rpdt);
            cacheBig.put(timeStr,list);
        }

        for(BigAmount bigAmount : list){
            if(ctid.equals(bigAmount.getCtid()) && crcd.equals(bigAmount.getCrcd())){
                temp.add(bigAmount);
            }
        }
        return temp;
    }

    private AmlRuleEngine getAmlRuleEngine(String crcd){
        if(null == crcd){
            return null;
        }
        AmlRuleEngine ruleEngine = cacheRuleEngine.get(crcd);
        if(null == ruleEngine){
            List<AmlRuleEngine> amlRuleEngineList = amlRuleEngineRepo.findAll();
            if(amlRuleEngineList.size() == 0) {
                return null;
            }
            for(AmlRuleEngine amlRuleEngine : amlRuleEngineList){
                cacheRuleEngine.put(amlRuleEngine.getRuleNo(),amlRuleEngine);
                if(crcd.equals(amlRuleEngine.getRuleNo())){
                    ruleEngine = amlRuleEngine;
                }
            }
        }
        return ruleEngine;
    }

	//判断是否满足
	public BigAmountMz getBigamountSfmz(BigAmount amount) throws Exception {
		String ctid = amount.getCtid();
		BigAmountMz mz = new BigAmountMz();

		BigDecimal sCNYTotal = new BigDecimal(0);
        BigDecimal zCNYTotal = new BigDecimal(0);
        BigDecimal sUSDTotal = new BigDecimal(0);
        BigDecimal zUSDTotal = new BigDecimal(0);
        mz.setSfmz("02");
        mz.setCtid(ctid);
        mz.setCnyzczje(BigDecimal.valueOf(0));
        mz.setUsdsrzje(BigDecimal.valueOf(0));
        mz.setCnysrzje(BigDecimal.valueOf(0));
        mz.setUsdzczje(BigDecimal.valueOf(0));
        Boolean isSrCNY = true;
        Boolean isZcCNY = true;
        mz.setIsSrCNY(isSrCNY);
        mz.setIsZcCNY(isZcCNY);
        //没有客户号或可疑特征代码无法判断
        if(StringUtils.isBlank(ctid) || StringUtils.isBlank(amount.getCrcd())){
            return mz;
        }
        AmlRuleEngine amlRuleEngine = getAmlRuleEngine(amount.getCrcd());
		if(null == amlRuleEngine) {
		    return mz;
        }
        Map<String, Sdcurrpd> dcurrpdMap = sdcurrpdService.getSdcurrpd(amount.getRpdt());

		Date rpdt = amount.getRpdt();
		BigDecimal sumZ = BigDecimal.valueOf(0);
		BigDecimal sumS = BigDecimal.valueOf(0);
		List<BigAmount> BigAmountList = getBigAmountList(ctid, rpdt,amount.getCrcd());
        boolean hasSrCNY = false;
        boolean hasZcCNY = false;
		for(BigAmount bigAmount:BigAmountList){
			if(!"CNY".equals(bigAmount.getCrtp()) && "01".equals(bigAmount.getTsdr())) {//非人民币交易
                isSrCNY = false;
			}
            if("CNY".equals(bigAmount.getCrtp()) && "01".equals(bigAmount.getTsdr())) {//非人民币交易
                isSrCNY = true;
            }
            if(!"CNY".equals(bigAmount.getCrtp()) && "02".equals(bigAmount.getTsdr())) {//非人民币交易
                isZcCNY = false;
            }
            if("CNY".equals(bigAmount.getCrtp()) && "02".equals(bigAmount.getTsdr())) {//非人民币交易
                hasZcCNY = true;
            }
		}
        mz.setIsSrCNY(isSrCNY);
        mz.setIsZcCNY(isZcCNY);
        if(null != amlRuleEngine.getCreditAmount() && amlRuleEngine.getCreditAmount().compareTo(BigDecimal.valueOf(0)) > 0){
            sCNYTotal = amlRuleEngine.getCreditAmount();
        }
        if(null != amlRuleEngine.getDebitAmount()&& amlRuleEngine.getDebitAmount().compareTo(BigDecimal.valueOf(0)) > 0){
            zCNYTotal = amlRuleEngine.getDebitAmount();
        }
        if (null != amlRuleEngine.getDebitAmount() && amlRuleEngine.getDebitAmount().compareTo(new BigDecimal(0)) > 0 && null != amlRuleEngine.getDollarDebitAmount() && amlRuleEngine.getDollarDebitAmount().compareTo(new BigDecimal(0))>0) {
            zUSDTotal = compareCnyAndUsd(amlRuleEngine.getDebitAmount(),amlRuleEngine.getDollarDebitAmount(),dcurrpdMap)||!hasSrCNY?amlRuleEngine.getDollarDebitAmount():getUSDAmount("CNY",amlRuleEngine.getDebitAmount(),dcurrpdMap);
        }else{
            zUSDTotal = amlRuleEngine.getDollarDebitAmount();
        }
        if (null != amlRuleEngine.getCreditAmount() && amlRuleEngine.getCreditAmount().compareTo(new BigDecimal(0)) > 0 && null != amlRuleEngine.getDollarCreditAmount() && amlRuleEngine.getDollarCreditAmount().compareTo(new BigDecimal(0))>0) {
            sUSDTotal = compareCnyAndUsd(amlRuleEngine.getCreditAmount(),amlRuleEngine.getDollarCreditAmount(),dcurrpdMap)||!hasSrCNY?amlRuleEngine.getDollarCreditAmount():getUSDAmount("CNY",amlRuleEngine.getCreditAmount(),dcurrpdMap);
        }else{
            sUSDTotal = amlRuleEngine.getDollarCreditAmount();
        }
		if(isSrCNY){
            for(BigAmount bigAmount:BigAmountList) {
                if( "01".equals(bigAmount.getTsdr())){
                    sumS = sumS.add(bigAmount.getCrat());
                }
            }
            mz.setCnysrzje(sumS);
            if(sumS.compareTo(sCNYTotal)>-1){
                mz.setSfmz("01");
            }
        }else{
            for(BigAmount bigAmount:BigAmountList) {
                if("USD".equals(bigAmount.getCrtp())){
                    if( "01".equals(bigAmount.getTsdr())){
                        sumS = sumS.add(bigAmount.getCrat());
                    }
                }else{
                    Sdcurrpd sdcurrpd;
                    if(StringUtils.isNotBlank(bigAmount.getCrtp())) {
                        sdcurrpd = dcurrpdMap.get(bigAmount.getCrtp());
                    }else{
                        sdcurrpd = dcurrpdMap.get("CNY");
                    }
                    if (null != sdcurrpd) {
                        if ("01".equals(bigAmount.getTsdr())) {
                            if ("D".equals(sdcurrpd.getMethod())) {
                                sumS = bigAmount.getCrat().divide(sdcurrpd.getRate(), 4, RoundingMode.HALF_UP).add(sumS);
                            } else {
                                sumS = bigAmount.getCrat().multiply(sdcurrpd.getRate()).add(sumS);
                            }
                        }
                    }
                }
            }
            mz.setUsdsrzje(sumS);
            if(sumS.compareTo(sUSDTotal)>-1){
                mz.setSfmz("01");;
            }
        }
        if(isZcCNY){
            for(BigAmount bigAmount:BigAmountList) {
                if( "02".equals(bigAmount.getTsdr())){
                    sumZ = sumZ.add(bigAmount.getCrat());
                }
            }
            mz.setCnyzczje(sumZ);
            if(sumZ.compareTo(zCNYTotal)>-1){
                mz.setSfmz("01");
            }
        }else{
            for(BigAmount bigAmount:BigAmountList) {
                if("USD".equals(bigAmount.getCrtp())){
                    if( "02".equals(bigAmount.getTsdr())){
                        sumZ = sumZ.add(bigAmount.getCrat());
                    }
                }else{
                    Sdcurrpd sdcurrpd;
                    if(StringUtils.isNotBlank(bigAmount.getCrtp())) {
                        sdcurrpd = dcurrpdMap.get(bigAmount.getCrtp());
                    }else{
                        sdcurrpd = dcurrpdMap.get("CNY");
                    }
                    if (null != sdcurrpd) {
                        if ("02".equals(bigAmount.getTsdr())) {
                            if ("D".equals(sdcurrpd.getMethod())) {
                                sumZ = bigAmount.getCrat().divide(sdcurrpd.getRate(), 4, RoundingMode.HALF_UP).add(sumZ);
                            } else {
                                sumZ = bigAmount.getCrat().multiply(sdcurrpd.getRate()).add(sumZ);
                            }
                        }
                    }
                }
            }
            mz.setUsdzczje(sumZ);
            if(sumZ.compareTo(zUSDTotal)>-1){
                mz.setSfmz("01");;
            }
        }
        if(!mz.getIsSrCNY()){
            if(!compareCnyAndUsd(amlRuleEngine.getCreditAmount(),amlRuleEngine.getDollarCreditAmount(),dcurrpdMap)){
                mz.setIsSrCNY(true);
                if(null != mz.getUsdsrzje()&&mz.getUsdsrzje().compareTo(BigDecimal.valueOf(0))>0){
                    mz.setCnysrzje(getCNYAmount("USD",mz.getUsdsrzje(),dcurrpdMap));
                }
            }
        }
        if(!mz.getIsZcCNY()){
            if(!compareCnyAndUsd(amlRuleEngine.getDebitAmount(),amlRuleEngine.getDollarDebitAmount(),dcurrpdMap)){
                mz.setIsZcCNY(true);
                if(null != mz.getUsdzczje()&&mz.getUsdzczje().compareTo(BigDecimal.valueOf(0))>0){
                    mz.setCnyzczje(getCNYAmount("USD",mz.getUsdzczje(),dcurrpdMap));
                }
            }
        }
		return mz;
	}

	public String checkBig(BigAmount amount) throws Exception {
        BigAmountMz mz = this.getBigamountSfmz(amount);
        if(null == mz || null == mz.getIsSrCNY()){
            return "";
        }
		if (amount.getTsdr().equals("02")) {
            if(mz.getIsZcCNY()){
                if(mz.getCnyzczje().compareTo(BigDecimal.valueOf(2000000))>-1){
                    return "0502";
                }else if(mz.getCnyzczje().compareTo(BigDecimal.valueOf(2000000))>-1){
                    return "0503";
                }else if(mz.getCnyzczje().compareTo(BigDecimal.valueOf(200000))>-1){
                    return "0501";
                }else {
                    return "";
                }
            }else{
                if (amount.getCfrc().equals("")) {
                    if (mz.getUsdzczje().compareTo(BigDecimal.valueOf(10000)) > -1) {
                        return "0504";
                    } else {
                        return "";
                    }
                }
                if(mz.getUsdzczje().compareTo(BigDecimal.valueOf(200000))>-1){
                    return "0502";
                }else if(mz.getUsdzczje().compareTo(BigDecimal.valueOf(100000))>-1){
                    return "0503";
                }else if(mz.getUsdzczje().compareTo(BigDecimal.valueOf(10000))>-1){
                    return "0501";
                }else {
                    return "";
                }
            }
		} else {
            if(mz.getIsSrCNY()){
                if(mz.getCnysrzje().compareTo(BigDecimal.valueOf(2000000))>-1){
                    return "0502";
                }else if(mz.getCnysrzje().compareTo(BigDecimal.valueOf(2000000))>-1){
                    return "0503";
                }else if(mz.getCnysrzje().compareTo(BigDecimal.valueOf(200000))>-1){
                    return "0501";
                }else {
                    return "";
                }
            }else{
                if (amount.getCfrc().equals("")) {
                    if (mz.getUsdsrzje().compareTo(BigDecimal.valueOf(10000)) > -1) {
                        return "0504";
                    } else {
                        return "";
                    }
                }
                if(mz.getUsdsrzje().compareTo(BigDecimal.valueOf(200000))>-1){
                    return "0502";
                }else if(mz.getUsdsrzje().compareTo(BigDecimal.valueOf(100000))>-1){
                    return "0503";
                }else if(mz.getUsdsrzje().compareTo(BigDecimal.valueOf(10000))>-1){
                    return "0501";
                }else {
                    return "";
                }
            }
		}
	}

    //比较人名币金额是否大于美元金额
    private Boolean compareCnyAndUsd(BigDecimal cny,BigDecimal usd,Map<String,Sdcurrpd> dcurrpdMap){
        Sdcurrpd sdcurrpd  = dcurrpdMap.get("CNY");
        BigDecimal cnyToUsd = BigDecimal.valueOf(0);
        if(null != sdcurrpd && null !=sdcurrpd.getRate() && sdcurrpd.getRate().compareTo(BigDecimal.valueOf(0))>0){
            if("D".equals(sdcurrpd.getMethod())){
                cnyToUsd = cny.divide(sdcurrpd.getRate(),4, RoundingMode.HALF_UP);
            }else{
                cnyToUsd = cny.multiply(sdcurrpd.getRate());
            }
        }
        return cnyToUsd.compareTo(usd) > 0;
    }

    //转换成美元
    private BigDecimal getUSDAmount(String bz,BigDecimal jyje,Map<String,Sdcurrpd> dcurrpdMap){
        if("USD".equals(bz)){
            return jyje;
        }else{
            Sdcurrpd sdcurrpd;
            if(StringUtils.isNotBlank(bz)) {
                sdcurrpd = dcurrpdMap.get(bz);
            }else{
                sdcurrpd = dcurrpdMap.get("CNY");
            }
            if(null != sdcurrpd){
                if("D".equals(sdcurrpd.getMethod())){
                    return jyje.divide(sdcurrpd.getRate(),4, RoundingMode.HALF_UP);
                }else{
                    return jyje.multiply(sdcurrpd.getRate());
                }
            }
        }
        return BigDecimal.valueOf(0);
    }

    //转换成人民币
    private BigDecimal getCNYAmount(String bz,BigDecimal jyje,Map<String,Sdcurrpd> dcurrpdMap){
        BigDecimal usdAmount = getUSDAmount(bz,jyje,dcurrpdMap);
        Sdcurrpd sdcurrpd = dcurrpdMap.get("CNY");
        if(null != sdcurrpd && null !=sdcurrpd.getRate() && sdcurrpd.getRate().compareTo(BigDecimal.valueOf(0))>0){
            if("M".equals(sdcurrpd.getMethod())){
                return usdAmount.divide(sdcurrpd.getRate(),4, RoundingMode.HALF_UP);
            }else{
                return usdAmount.multiply(sdcurrpd.getRate());
            }
        }
        return BigDecimal.valueOf(0);
    }
}
