package net.riking.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import net.riking.config.ExportConfig;
import net.riking.core.entity.model.Job;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;
import net.riking.util.parserXmlUtil;
@Service("feedBackServiceImpl")
public class FeedBackServiceImpl {

	@Autowired
	BigAmountRepo amountRepo;
	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;
	@Autowired
	ExportConfig exportConfig;

	public List<BigAmount> getFailBigAmount(String path, Map<String, String> nodes) {
		BigAmount bigAmount = new BigAmount();
		try {
			String zipDir = exportConfig.getExportBigamountNowDir();
			bigAmount = parserXmlUtil.parserXml(bigAmount, nodes, zipDir  + path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bigAmount.setDeleteState("1");
		Job job = new Job();
		job.setCurJobState("PRE_EXPORTOVER");
		bigAmount.setJob(job);
		return amountRepo.findAll(Example.of(bigAmount, ExampleMatcher.matchingAll()));
	}

	public List<AmlSuspicious> getFailAmlSuspicious(String path, Map<String, String> nodes) {
		AmlSuspicious susp = new AmlSuspicious();
		String zipDir = exportConfig.getExportShadinessNowDir();
		try {
			susp = parserXmlUtil.parserXml(susp, nodes, zipDir + path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		susp.setDeleteState("1");
		Job job = new Job();
		job.setCurJobState("PRE_EXPORTOVER");
		susp.setJob(job);
		return amlSuspiciousRepo.findAll(Example.of(susp, ExampleMatcher.matchingAll()));
	}

}
