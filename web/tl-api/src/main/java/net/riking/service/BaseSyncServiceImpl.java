package net.riking.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Config;
import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.BaseTrn;
import net.riking.service.repo.BaseAifRepo;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.service.repo.BaseTrnRepo;
import net.riking.service.repo.ModelAmlCorptrnRepo;
import net.riking.service.repo.ModelAmlInditrnRepo;

@Service("baseSyncService")
public class BaseSyncServiceImpl {

	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;
	@Autowired
	BaseAifRepo baseAifRepo;
	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	BaseTrnRepo baseTrnRepo;
	@Autowired
	ModelAmlCorptrnRepo modelAmlCorptrnRepo;
	@Autowired
	ModelAmlInditrnRepo modelAmlInditrnRepo;
	@Autowired
	Config config;

	public void sncyInformation(Date snycTimes) {
		if ("true".equals(config.getSnyc())){
			Long snycTime = snycTimes.getTime() - 2 * 24 * 60 * 60 * 1000L;
			List<BaseAif> baseAifs = baseAifRepo.findBySyncLastTime(snycTime);
			if (baseAifs.size() > 0) {
				for (int i = 0; i < baseAifs.size(); i++) {
					BaseAif baseAif = baseAifs.get(i);
					if (modelAmlCorptrnRepo.getByZh(baseAif.getZh()).size() > 0) {
						modelAmlCorptrnRepo.updateCorpByAif(baseAif);
					} else {
						modelAmlInditrnRepo.updateIndvByAif(baseAif);
					}
				}
			}
			List<BaseTrn> baseTrns = baseTrnRepo.findBySyncLastTime(snycTime);
			if (baseTrns.size() > 0) {
				for (int i = 0; i < baseTrns.size(); i++) {
					if (modelAmlCorptrnRepo.getByJylsh(baseTrns.get(i).getJylsh()).size() > 0) {
						modelAmlCorptrnRepo.updateCorpByTrn(baseTrns.get(i));
					} else {
						modelAmlInditrnRepo.updateIndvByTrn(baseTrns.get(i));
					}
				}
			}
			List<BaseCorpCust> baseCorpCusts = baseCorpCustRepo.findBySyncLastTime(snycTime);
			if (baseCorpCusts.size() > 0) {
				for (int i = 0; i < baseCorpCusts.size(); i++) {
					if (modelAmlCorptrnRepo.getByKhbh(baseCorpCusts.get(i).getKhbh()).size() > 0) {
						modelAmlCorptrnRepo.updateCorpByCorp(baseCorpCusts.get(i));
					}
				}
			}
			List<BaseIndvCust> baseIndvCusts = baseIndvCustRepo.findBySyncLastTime(snycTime);
			if (baseIndvCusts.size() > 0) {
				for (int i = 0; i < baseIndvCusts.size(); i++) {
					if (modelAmlInditrnRepo.getByKhbh(baseIndvCusts.get(i).getKhbh()).size() > 0) {
						modelAmlInditrnRepo.updateIndvByIndv(baseIndvCusts.get(i));
					}
				}
			}
		}
	}
}
