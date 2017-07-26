package net.riking.service.repo;

import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.ModelAmlInditrn;

import java.util.Date;
import java.util.List;

/**
 * Created by bing.xun on 2017/5/26.
 */
public interface ModelAmlInditrnDao {
	List<ModelAmlInditrn> getWithTime(String rule, Date startDate, Date endDate);

	int updateIndvByAif(BaseAif baseAif);

	int updateIndvByTrn(BaseTrn BaseTrn);
	

	int updateIndvByIndv(BaseIndvCust baseIndvCust);
}
