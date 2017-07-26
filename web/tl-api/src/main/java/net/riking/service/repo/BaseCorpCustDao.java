package net.riking.service.repo;

import java.util.List;

import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseTrn;

@Repository
public interface BaseCorpCustDao {
	
	List<BaseCorpCust> getMore(String khbh);
	
	List<String> getMore(List<String> csnms);
	
	List<BaseCorpCust> getBykhbh(String khbh);
	
	List<BaseCorpCust> getByshtyzxdm(String shtyzxdm);
	
	List<BaseCorpCust> getByyhzh(String yhzh);
	
	List<BaseCorpCust> getBynsrzbh(String nsrzbh);

	List<BaseTrn> getMoreBaseTrn();
	
	
}
