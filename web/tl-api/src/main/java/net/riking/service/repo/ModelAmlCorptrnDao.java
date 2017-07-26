package net.riking.service.repo;

import java.util.Date;
import java.util.List;

import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseTrn;
import net.riking.entity.model.ModelAmlCorptrn;

public interface ModelAmlCorptrnDao{
	List<ModelAmlCorptrn> get(String rule,Date date);

	List<ModelAmlCorptrn> getWithTime(String rule,Date startDate ,Date endDate);
	
	int updateCorpByAif(BaseAif BaseAif);
	
	int updateCorpByTrn(BaseTrn BaseTrn);
	
	int updateCorpByCorp(BaseCorpCust BaseCorpCust);
}
