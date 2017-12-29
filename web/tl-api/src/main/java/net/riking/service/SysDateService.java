package net.riking.service;

import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.Period;
import net.riking.entity.model.SysDaysTemp;

public interface SysDateService {
	public String getDate();

	public Map<String, Set<String>> getMounthWeek(String date);

	public Period getDate(String date, String type);

	/********************* WEB ***************/

	Page<SysDaysTemp> findAllToPage(SysDaysTemp sysDaysTemp, PageRequest pageable);

	/******************** WEB END ***********/
}
