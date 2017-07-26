package net.riking.service.repo;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.riking.entity.model.Sdcurrpd;

@Repository
public interface SdcurrpdDao {
	
	List<Sdcurrpd> getMore(String ccy,Date effectDate);
	
}
