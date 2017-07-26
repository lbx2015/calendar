package net.riking.task.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.model.TaskJobInfo;
import net.riking.core.task.IJobRunner;
import net.riking.entity.model.BaseIndvCust;
import net.riking.service.RisKInRateServiceImpl;
import net.riking.service.repo.BaseIndvCustRepo;

@Component("BaseIndvCustJob")
public class BaseIndvCustJob extends IJobRunner {
	
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private BaseIndvCustRepo customerRepo;
	@Autowired
	private RisKInRateServiceImpl risKInRateServiceImpl;

	public Map<String, Integer> getRatingCount() {
		Map<String, Integer> map = new HashMap<>();

		return map;
	}
	
	@Override
	public Short callback(TaskJobInfo jobinfo) throws Exception {
		BaseIndvCust customer = new BaseIndvCust();
		customer.setConfirmStatus("101002");
		customer.setEnabled("1");
		Pageable pageable = new PageRequest(0, 1000);
		while(true){	
			List<BaseIndvCust> customers = new ArrayList<>();// 0-100
			Example<BaseIndvCust> example = Example.of(customer, ExampleMatcher.matchingAll());
			Page<BaseIndvCust> page = customerRepo.findAll(example, pageable);
			customers = page.getContent();
			if (customers.size()>0) {
				risKInRateServiceImpl.baseIndvCustRiskRate(customers);
			}
			if (page.hasNext()) {
				pageable=pageable.next();
			} else {
				break;
			}
		}
		Map<String, String> jsonMap = new HashMap<String, String>();
		jobinfo.setResult(mapper.writeValueAsString(jsonMap));
		return STATUS_ENUM.SUCCESS;
	}
}
