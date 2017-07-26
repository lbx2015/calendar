package net.riking.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.ModelAmlCorptrn;
import net.riking.entity.model.ModelAmlInditrn;

@Service("extractDataService")
public class ExtractDataServiceImpl {
		
	public BigAmount extractBig(ModelAmlCorptrn tm) {
        BigAmount bigAmount = new BigAmount();
        bigAmount.setRicd(tm.getBgjgbm());
        bigAmount.setFinc(tm.getWddm());
        bigAmount.setRlfc(tm.getJrjgykhdgx());
        bigAmount.setHtdt(tm.getJyrq());
        bigAmount.setCsnm(tm.getKhbh());
        bigAmount.setCitp(tm.getZjlx());
        bigAmount.setCtid(tm.getZjhm());
        bigAmount.setCtnt(tm.getZbszdgj());
        bigAmount.setCtvc(tm.getKhhydm());
        bigAmount.setCctl(tm.getLxdh());
        bigAmount.setCcei(tm.getLxfs());
        bigAmount.setCtar(tm.getJzdz());
        bigAmount.setCtnm(tm.getKhzwmc());
        if(StringUtils.isBlank(bigAmount.getCtnm())){
			bigAmount.setCtnm(tm.getKhywmc());
		}
        bigAmount.setCatp(tm.getZhlx());
        bigAmount.setCtac(tm.getZh());
        bigAmount.setOatm(tm.getKhkhrq());
        bigAmount.setCbct(tm.getYhklx());
        bigAmount.setCbcn(tm.getYhkhm());
        bigAmount.setTbnm(tm.getDbrmc());
        bigAmount.setTbit(tm.getDbrzjlx());
        bigAmount.setTbid(tm.getDbrzjhm());
        bigAmount.setTbnt(tm.getDbrgj());
        bigAmount.setTstm(tm.getJyrq());
        bigAmount.setTrcd(tm.getJyfsd());
        bigAmount.setTicd(tm.getJylsh());
        bigAmount.setRpmt(tm.getSfkfpphlx());
        bigAmount.setRpmn(tm.getSfkfpph());
		//交易方式
		if(StringUtils.isNotBlank(tm.getJyfs()) && tm.getJyfs().length() > 2&&(!tm.getJyfs().substring(0, 2).equals("00"))){
			bigAmount.setTstp(tm.getJyfs().substring(0,2)+"_"+tm.getJyfs());
		}else{
			bigAmount.setTstp(tm.getJyfs());
		}
        bigAmount.setOctt(tm.getFgtjyfs());
        bigAmount.setOcec(tm.getFgtjyfssbdm());
        bigAmount.setBptc(tm.getYhyzfjgzjdywbm());
        bigAmount.setTsct(tm.getJybm());
        bigAmount.setCrpp(tm.getZjyt());
		if ("C".equals(tm.getJdbj())) {
			bigAmount.setTsdr("01");
		}else if ("D".equals(tm.getJdbj())) {
			bigAmount.setTsdr("02");
		}else{
			bigAmount.setTsdr(tm.getJdbj());
		}
        bigAmount.setCrtp(tm.getBz());
        bigAmount.setCrat(tm.getJyje());
        bigAmount.setCfin(tm.getJydsyhmc());
        bigAmount.setCfct(tm.getJydswdlx());
        bigAmount.setCfic(tm.getJydsyhdm());
        bigAmount.setCfrc(tm.getJydsyhgjdm());
        bigAmount.setTcnm(tm.getJydsmc());
        bigAmount.setTcit(tm.getJydszjlx());
        bigAmount.setTcid(tm.getJydszjhm());
        bigAmount.setTcat(tm.getJydszhlx());
        bigAmount.setTcac(tm.getJydszh());
		bigAmount.setCrpp(tm.getZjyt());
        bigAmount.setRotf1(tm.getJyxxbz1()==null?"@N":tm.getJyxxbz1());
        bigAmount.setRotf2(tm.getJyxxbz2()==null?"@N":tm.getJyxxbz2());
		bigAmount.setStartState("PRE_RECROD");
		bigAmount.setDeleteState("1");
		bigAmount.setAptp("01");
		bigAmount.setSfmz("01");
		bigAmount.setBwzt("xzbw");
		bigAmount.setJgbm(tm.getJgbm());
		bigAmount.setSubmitType("Y");
		bigAmount.setMirs("@N");
		bigAmount.setReportType("N");
		//其他说明
		bigAmount.setOitp1("@N");
		bigAmount.setOitp2("@N");
		bigAmount.setOitp3("@N");
		bigAmount.setOoct("@N");
		bigAmount.setOcbt("@N");
		
        return bigAmount;
    }

	public BigAmount extractBig(ModelAmlInditrn im) {
		BigAmount bigAmount = new BigAmount();
		bigAmount.setRicd(im.getBgjgbm());
		bigAmount.setFinc(im.getWddm());
		bigAmount.setRlfc(im.getJrjgykhdgx());
		bigAmount.setHtdt(im.getJyrq());
		bigAmount.setCsnm(im.getKhbh());
		bigAmount.setCitp(im.getZjlx());
		bigAmount.setCtid(im.getZjhm());
		bigAmount.setCtnt(im.getKhgj());
		bigAmount.setCtvc(im.getZy());
		bigAmount.setCctl(im.getLxdh());
		bigAmount.setCcei(im.getLxfs());
		bigAmount.setCtar(im.getJzdz());
		bigAmount.setCtnm(im.getKhzwmc());
		if(StringUtils.isBlank(bigAmount.getCtnm())){
			bigAmount.setCtnm(im.getKhywmc());
		}
		bigAmount.setCatp(im.getZhlx());
		bigAmount.setCtac(im.getZh());
		bigAmount.setOatm(im.getKhkhrq());
		bigAmount.setCbct(im.getYhklx());
		bigAmount.setCbcn(im.getYhkhm());
		bigAmount.setTbnm(im.getDbrmc());
		bigAmount.setTbit(im.getDbrzjlx());
		bigAmount.setTbid(im.getDbrzjhm());
		bigAmount.setTbnt(im.getDbrgj());
		bigAmount.setTstm(im.getJyrq());
		bigAmount.setTrcd(im.getJyfsd());
		bigAmount.setTicd(im.getJylsh());
		bigAmount.setRpmt(im.getSfkfpphlx());
		bigAmount.setRpmn(im.getSfkfpph());
		if(StringUtils.isNotBlank(im.getJyfs()) && im.getJyfs().length() > 2&&(!im.getJyfs().substring(0, 2).equals("00"))){
			bigAmount.setTstp(im.getJyfs().substring(0,2)+"_"+im.getJyfs());
		}else{
			bigAmount.setTstp(im.getJyfs());
		}
		bigAmount.setOctt(im.getFgtjyfs());
		bigAmount.setOcec(im.getFgtjyfssbdm());
		bigAmount.setBptc(im.getYhyzfjgzjdywbm());
		bigAmount.setTsct(im.getJybm());
		bigAmount.setCrpp(im.getZjyt());
		if ("C".equals(im.getJdbj())) {
			bigAmount.setTsdr("01");
		}else if ("D".equals(im.getJdbj())) {
			bigAmount.setTsdr("02");
		}else{
			bigAmount.setTsdr(im.getJdbj());
		}
		bigAmount.setCrtp(im.getBz());
		bigAmount.setCrat(im.getJyje());
		bigAmount.setCfin(im.getJydsyhmc());
		bigAmount.setCfct(im.getJydswdlx());
		bigAmount.setCfic(im.getJydsyhdm());
		bigAmount.setCfrc(im.getJydsyhgjdm());
		bigAmount.setTcnm(im.getJydsmc());
		bigAmount.setTcit(im.getJydszjlx());
		bigAmount.setTcid(im.getJydszjhm());
		bigAmount.setTcat(im.getJydszhlx());
		bigAmount.setTcac(im.getJydszh());
		bigAmount.setRotf1(im.getJyxxbz1()==null?"@N":im.getJyxxbz1());
		bigAmount.setRotf2(im.getJyxxbz2()==null?"@N":im.getJyxxbz2());
		bigAmount.setStartState("PRE_RECROD");
		bigAmount.setDeleteState("1");
		bigAmount.setAptp("01");
		bigAmount.setSfmz("01");
		bigAmount.setBwzt("xzbw");
		bigAmount.setJgbm(im.getJgbm());
		bigAmount.setReportType("N");
		bigAmount.setSubmitType("Y");
		bigAmount.setMirs("@N");
		//其他说明
		bigAmount.setOitp1("@N");
		bigAmount.setOitp2("@N");
		bigAmount.setOitp3("@N");
		bigAmount.setOoct("@N");
		bigAmount.setOcbt("@N");
		return bigAmount;
	}

	public AmlSuspicious extractShadiness(ModelAmlCorptrn tm) {

		AmlSuspicious amlSuspicious = new AmlSuspicious();
		//报告机构编码
		amlSuspicious.setRicd(tm.getBgjgbm());
		//上报网点代码
		amlSuspicious.setRpnc(tm.getBgjgbm());
		//金融机构网点代码
		amlSuspicious.setFinc(tm.getWddm());
		//金融机构与客户的关系
		amlSuspicious.setRlfc(tm.getJrjgykhdgx());
		//可疑交易报告紧急程度
		amlSuspicious.setDetr("");
		//客户号 (可疑主体客户号)
		amlSuspicious.setCsnm1(tm.getKhbh());
		//可疑主体职业（对私）或行业（对公）
		amlSuspicious.setSevc(tm.getKhhydm());
		//可疑主体姓名名称
		amlSuspicious.setSenm(tm.getKhzwmc());
		//可疑主体身份证件证明文件类型
		amlSuspicious.setSetp(tm.getZjlx());
		//可疑主体身份证件证明文件号码
		amlSuspicious.setSeid(tm.getZjhm());
		//可疑主体国籍
		amlSuspicious.setStnt(tm.getZbszdgj());
		//可疑主体联系电话 1
		amlSuspicious.setSctl(tm.getLxdh());
		//可疑主体住址经营地址 1
		amlSuspicious.setSear(tm.getJzdz());
		//可疑主体其他联系方式 1
		amlSuspicious.setSeei(tm.getLxfs());
		//可疑主体法定代表人姓名
		amlSuspicious.setSrnm(tm.getFrxm());
		//可疑主体法定代表人身份证件类型
		amlSuspicious.setSrit(tm.getFrzjlx());
		//可疑主体法定代表人身份证件号码
		amlSuspicious.setSrid(tm.getFrzjhm());
		//可疑主体控股股东或实际控制人名称
		amlSuspicious.setScnm(tm.getGdmc());
		//可疑主体控股股东或实际控制人身份证件证明文件类型
		amlSuspicious.setScit(tm.getGdzjlx());
		//客户姓名名称
		amlSuspicious.setCtnm(tm.getKhzwmc());
		if(StringUtils.isBlank(amlSuspicious.getCtnm())){
			amlSuspicious.setCtnm(tm.getKhywmc());
		}
		//客户身份证件证明文件类型
		amlSuspicious.setCitp(tm.getZjlx());
		//客户身份证件证明文件号码
		amlSuspicious.setCtid(tm.getZjhm());
		//客户号
		amlSuspicious.setCsnm2(tm.getKhbh());
		//客户账户类型
		amlSuspicious.setCatp(tm.getZhlx());
		//客户账号
		amlSuspicious.setCtac(tm.getZh());
		//客户账户开立时间
		amlSuspicious.setOatm(tm.getKhkhrq());
		//客户账户销户时间
		amlSuspicious.setCatm(tm.getKhxhrq());
		//客户银行卡类型
		amlSuspicious.setCbct(tm.getYhklx());
		//客户银行卡号码
		amlSuspicious.setCbcn(tm.getYhkhm());
		//交易代办人姓名
		amlSuspicious.setTbnm(tm.getDbrmc());
		//交易代办人身份证件证明文件类型
		amlSuspicious.setTbit(tm.getDbrzjlx());
		//交易代办人身份证件证明文件号码
		amlSuspicious.setTbid(tm.getDbrzjhm());
		//交易代办人国籍
		amlSuspicious.setTbnt(tm.getDbrgj());
		//交易时间
		amlSuspicious.setTstm(tm.getJyrq());
		//交易发生地
		amlSuspicious.setTrcd(tm.getJyfsd());
		//业务标识号
		amlSuspicious.setTicd(tm.getJylsh());
		//收付款方匹配号类型
		amlSuspicious.setRpmt(tm.getSfkfpphlx());
		//收付款方匹配号
		amlSuspicious.setRpmn(tm.getSfkfpph());
		//交易方式
		if(StringUtils.isNotBlank(tm.getJyfs()) && tm.getJyfs().length() > 2&&(!tm.getJyfs().substring(0, 2).equals("00"))){
			amlSuspicious.setTstp(tm.getJyfs().substring(0,2)+"_"+tm.getJyfs());
		}else{
			amlSuspicious.setTstp(tm.getJyfs());
		}
		//非柜台交易方式
		amlSuspicious.setOctt(tm.getFgtjyfs());
		//非柜台交易方式的设备代码
		amlSuspicious.setOcec(tm.getFgtjyfssbdm());
		//银行与支付机构之间的业务交易编码
		amlSuspicious.setBptc(tm.getYhyzfjgzjdywbm());
		//涉外收支交易分类与代码
		amlSuspicious.setTsct(tm.getJybm());
		//资金收付标志
		amlSuspicious.setTsdr(tm.getJdbj());
		//资金来源和用途
		amlSuspicious.setCrsp(tm.getZjyt());
		if ("C".equals(tm.getJdbj())) {
			amlSuspicious.setTsdr("01");
		}else if ("D".equals(tm.getJdbj())) {
			amlSuspicious.setTsdr("02");
		}else{
			amlSuspicious.setTsdr(tm.getJdbj());
		}
		//交易币种
		amlSuspicious.setCrtp(tm.getBz());
		//交易金额
		amlSuspicious.setCrat(tm.getJyje());
		//对方金融机构网点名称
		amlSuspicious.setCfin(tm.getJydsyhmc());
		//对方金融机构网点代码类型
		amlSuspicious.setCfct(tm.getJydswdlx());
		//对方金融机构网点代码
		amlSuspicious.setCfic(tm.getJydsyhdm());
		//对方金融机构网点行政区划代码
		amlSuspicious.setCfrc(tm.getJydsyhgjdm());
		//交易对手姓名名称
		amlSuspicious.setTcnm(tm.getJydsmc());
		//交易对手身份证件证明文件类型
		amlSuspicious.setTcit(tm.getJydszjlx());
		//交易对手身份证件证明文件号码
		amlSuspicious.setTcid(tm.getJydszjhm());
		//交易对手账户类型
		amlSuspicious.setTcat(tm.getJydszhlx());
		//交易对手账号
		amlSuspicious.setTcac(tm.getJydszh());
		//资金来源于用途
		amlSuspicious.setCrsp(tm.getZjyt());
		//交易信息备注 1
		amlSuspicious.setRotf1(tm.getJyxxbz1()==null?"@N":tm.getJyxxbz1());
		//交易信息备注 2
		amlSuspicious.setRotf2(tm.getJyxxbz2()==null?"@N":tm.getJyxxbz2());

		amlSuspicious.setStartState("SISP_PRE_DEAL");
		amlSuspicious.setDeleteState("1");
		amlSuspicious.setAptp("01");
		amlSuspicious.setBwzt("xzbw");
		amlSuspicious.setJgbm(tm.getJgbm());
		amlSuspicious.setReportType("N");
		amlSuspicious.setSubmitType("Y");
		amlSuspicious.setMirs("@N");
		//报送次数标志
		amlSuspicious.setTorp("1");
		//初次报送报文名
		amlSuspicious.setOrxn("@N");
		//其他类型说明
		amlSuspicious.setOitp1("@N");
		amlSuspicious.setOitp2("@N");
		amlSuspicious.setOitp3("@N");
		amlSuspicious.setOitp4("@N");
		amlSuspicious.setOrit("@N");
		amlSuspicious.setOcit("@N");
		amlSuspicious.setOcbt("@N");
		amlSuspicious.setOoct("@N");
		amlSuspicious.setOdrp("@N");
		amlSuspicious.setOtpr("@N");
		return amlSuspicious;
	}

	public AmlSuspicious extractShadiness(ModelAmlInditrn tm) {

		AmlSuspicious amlSuspicious = new AmlSuspicious();
		//报告机构编码
		amlSuspicious.setRicd(tm.getBgjgbm());
		//上报网点代码
		amlSuspicious.setRpnc(tm.getBgjgbm());
		//金融机构网点代码
		amlSuspicious.setFinc(tm.getWddm());
		//金融机构与客户的关系
		amlSuspicious.setRlfc(tm.getJrjgykhdgx());
		//可疑交易报告紧急程度
		amlSuspicious.setDetr("");
		//客户号 (可疑主体客户号)
		amlSuspicious.setCsnm1(tm.getKhbh());
		//可疑主体职业（对私）或行业（对公）
		amlSuspicious.setSevc(tm.getZy());
		//可疑主体姓名名称
		amlSuspicious.setSenm(tm.getKhzwmc());
		//可疑主体身份证件证明文件类型
		amlSuspicious.setSetp(tm.getZjlx());
		//可疑主体身份证件证明文件号码
		amlSuspicious.setSeid(tm.getZjhm());
		//可疑主体国籍
		amlSuspicious.setStnt(tm.getKhgj());
		//可疑主体联系电话 1
		amlSuspicious.setSctl(tm.getLxdh());
		//可疑主体住址经营地址 1
		amlSuspicious.setSear(tm.getJzdz());
		//可疑主体其他联系方式 1
		amlSuspicious.setSeei(tm.getLxfs());
		//客户姓名名称
		amlSuspicious.setCtnm(tm.getKhzwmc());
        if(StringUtils.isBlank(amlSuspicious.getCtnm())){
            amlSuspicious.setCtnm(tm.getKhywmc());
        }
		//客户身份证件证明文件类型
		amlSuspicious.setCitp(tm.getZjlx());
		//客户身份证件证明文件号码
		amlSuspicious.setCtid(tm.getZjhm());
		//客户号
		amlSuspicious.setCsnm2(tm.getKhbh());
		//客户账户类型
		amlSuspicious.setCatp(tm.getZhlx());
		//客户账号
		amlSuspicious.setCtac(tm.getZh());
		//客户账户开立时间
		amlSuspicious.setOatm(tm.getKhkhrq());
		//客户账户销户时间
		amlSuspicious.setCatm(tm.getKhxhrq());
		//客户银行卡类型
		amlSuspicious.setCbct(tm.getYhklx());
		//客户银行卡号码
		amlSuspicious.setCbcn(tm.getYhkhm());
		//交易代办人姓名
		amlSuspicious.setTbnm(tm.getDbrmc());
		//交易代办人身份证件证明文件类型
		amlSuspicious.setTbit(tm.getDbrzjlx());
		//交易代办人身份证件证明文件号码
		amlSuspicious.setTbid(tm.getDbrzjhm());
		//交易代办人国籍
		amlSuspicious.setTbnt(tm.getDbrgj());
		//交易时间
		amlSuspicious.setTstm(tm.getJyrq());
		//交易发生地
		amlSuspicious.setTrcd(tm.getJyfsd());
		//业务标识号
		amlSuspicious.setTicd(tm.getJylsh());
		//收付款方匹配号类型
		amlSuspicious.setRpmt(tm.getSfkfpphlx());
		//收付款方匹配号
		amlSuspicious.setRpmn(tm.getSfkfpph());
		//交易方式
		if(StringUtils.isNotBlank(tm.getJyfs()) && tm.getJyfs().length() > 2&&(!tm.getJyfs().substring(0, 2).equals("00"))){
			amlSuspicious.setTstp(tm.getJyfs().substring(0,2)+"_"+tm.getJyfs());
		}else{
			amlSuspicious.setTstp(tm.getJyfs());
		}
		//非柜台交易方式
		amlSuspicious.setOctt(tm.getFgtjyfs());
		//非柜台交易方式的设备代码
		amlSuspicious.setOcec(tm.getFgtjyfssbdm());
		//银行与支付机构之间的业务交易编码
		amlSuspicious.setBptc(tm.getYhyzfjgzjdywbm());
		//涉外收支交易分类与代码
		amlSuspicious.setTsct(tm.getJybm());
		//资金来源于用途
		amlSuspicious.setCrsp(tm.getZjyt());
		//资金收付标志
		amlSuspicious.setTsdr(tm.getJdbj());
		//资金来源和用途
		amlSuspicious.setCrsp(tm.getZjyt());
		if ("C".equals(tm.getJdbj())) {
			amlSuspicious.setTsdr("01");
		}else if ("D".equals(tm.getJdbj())) {
			amlSuspicious.setTsdr("02");
		}else{
			amlSuspicious.setTsdr(tm.getJdbj());
		}
		//交易币种
		amlSuspicious.setCrtp(tm.getBz());
		//交易金额
		amlSuspicious.setCrat(tm.getJyje());
		//对方金融机构网点名称
		amlSuspicious.setCfin(tm.getJydsyhmc());
		//对方金融机构网点代码类型
		amlSuspicious.setCfct(tm.getJydswdlx());
		//对方金融机构网点代码
		amlSuspicious.setCfic(tm.getJydsyhdm());
		//对方金融机构网点行政区划代码
		amlSuspicious.setCfrc(tm.getJydsyhgjdm());
		//交易对手姓名名称
		amlSuspicious.setTcnm(tm.getJydsmc());
		//交易对手身份证件证明文件类型
		amlSuspicious.setTcit(tm.getJydszjlx());
		//交易对手身份证件证明文件号码
		amlSuspicious.setTcid(tm.getJydszjhm());
		//交易对手账户类型
		amlSuspicious.setTcat(tm.getJydszhlx());
		//交易对手账号
		amlSuspicious.setTcac(tm.getJydszh());
		//交易信息备注 1
		amlSuspicious.setRotf1(tm.getJyxxbz1()==null?"@N":tm.getJyxxbz1());
		//交易信息备注 2
		amlSuspicious.setRotf2(tm.getJyxxbz2()==null?"@N":tm.getJyxxbz2());
		amlSuspicious.setStartState("SISP_PRE_DEAL");
		amlSuspicious.setDeleteState("1");
		amlSuspicious.setAptp("01");
		amlSuspicious.setBwzt("xzbw");
		amlSuspicious.setJgbm(tm.getJgbm());
		amlSuspicious.setReportType("N");
		amlSuspicious.setSubmitType("Y");
		amlSuspicious.setMirs("@N");
		//报送次数标志
		amlSuspicious.setTorp("1");
		//初次报送报文名
		amlSuspicious.setOrxn("@N");
		//其他类型说明
		amlSuspicious.setOitp1("@N");
		amlSuspicious.setOitp2("@N");
		amlSuspicious.setOitp3("@N");
		amlSuspicious.setOitp4("@N");
		amlSuspicious.setOrit("@N");
		amlSuspicious.setOcit("@N");
		amlSuspicious.setOcbt("@N");
		amlSuspicious.setOoct("@N");
		amlSuspicious.setOdrp("@N");
		amlSuspicious.setOtpr("@N");
		return amlSuspicious;
	}
}
