package net.riking.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.CheckResult;
import net.riking.service.repo.BaseCorpCustRepo;

@Service("check")
public class CheckUtil {

	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;

	private String checkRepeatCusts(BaseCorpCust baseCorpCust) {
		String list5;
		long id = 0L;
		if (null != baseCorpCust.getId()) {
			id = baseCorpCust.getId();
		}
		String khbh = baseCorpCust.getKhbh();
		String shtxxydm = baseCorpCust.getKhzjhm();
		String yhzh = baseCorpCust.getYhzh();
		String nsrsbm = baseCorpCust.getNsrsbm();
		List<BaseCorpCust> list1 = null;
		List<BaseCorpCust> list2 = null;
		List<BaseCorpCust> list3 = null;
		List<BaseCorpCust> list4 = null;
		if (StringUtils.isNotEmpty(khbh) && khbh != null) {
			list1 = baseCorpCustRepo.findByKhbhAndEnabledAndIdNot(khbh, "1", id);
		}

		if (StringUtils.isNotEmpty(shtxxydm) && shtxxydm != null) {
			list2 = baseCorpCustRepo.findByKhzjhmAndEnabledAndIdNot(shtxxydm, "1", id);
		}

		if (StringUtils.isNotEmpty(yhzh) && yhzh != null) {
			list3 = baseCorpCustRepo.findByYhzhAndEnabledAndIdNot(yhzh, "1", id);
		}

		if (StringUtils.isNotEmpty(nsrsbm) && nsrsbm != null) {
			list4 = baseCorpCustRepo.findByNsrsbmAndEnabledAndIdNot(nsrsbm, "1", id);
		}
		if (baseCorpCust.getOldId() != null) {
			if (list1 != null && list1.size() > 1) {
				list5 = "khbh:客户编号重复";
				return list5;
			}
			if (list2 != null && list2.size() > 1) {
				list5 = "khzjhm:客户证件号码重复";
				return list5;
			}

			if (list3 != null && list3.size() > 1) {
				list5 = "yhzh:银行账号重复";
				return list5;
			}

			if (list4 != null && list4.size() > 1) {
				list5 = "nsrsbm:纳税人识别号重复";
				return list5;
			}
		} else {
			if (list1 != null && list1.size() > 0) {
				list5 = "khbh:客户编号重复";
				return list5;
			}
			if (list2 != null && list2.size() > 0) {
				list5 = "khzjhm:客户证件号码重复";
				return list5;
			}
			if (list3 != null && list3.size() > 0) {
				list5 = "yhzh:银行账号重复";
				return list5;
			}
			if (list4 != null && list4.size() > 0) {
				list5 = "nsrsbm:纳税人识别号重复";
				return list5;
			}
		}
		return null;
	}

	public CheckResult checkCusts(BaseCorpCust baseCorpCust) {
		CheckResult result = new CheckResult();
		result.setCheckId(baseCorpCust.getId());
		result.setCheckDate(new Date());
		result.setFlag(2);
		result.setType("2");
		String substring = checkRepeatCusts(baseCorpCust);
		if (StringUtils.isNotBlank(substring)) {
			result.setReason(substring);
			return result;
		}
		return null;
	}

}
