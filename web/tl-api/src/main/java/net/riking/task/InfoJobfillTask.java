package net.riking.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import net.riking.config.Config;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.model.Job;
import net.riking.core.service.repo.CommonRepo;
import net.riking.core.workflow.EventResult;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.model.BaseAif;
import net.riking.entity.model.BaseCorpCust;
import net.riking.entity.model.BaseIndvCust;
import net.riking.entity.model.BaseTrn;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BaseAifRepo;
import net.riking.service.repo.BaseCorpCustRepo;
import net.riking.service.repo.BaseIndvCustAddRepo;
import net.riking.service.repo.BaseIndvCustRepo;
import net.riking.service.repo.BaseTrnRepo;
import net.riking.service.repo.BigAmountRepo;
import net.riking.service.repo.UpdateJobRepo;

@Component("infoJobfill")
public class InfoJobfillTask implements Runnable {

	@Autowired
	BaseCorpCustRepo baseCorpCustRepo;
	@Autowired
	BaseIndvCustRepo baseIndvCustRepo;
	@Autowired
	BaseIndvCustAddRepo baseIndvCustAddRepo;
	@Autowired
	BaseAifRepo baseAifRepo;
	@Autowired
	BaseTrnRepo baseTrnRepo;
	@Autowired
	WorkflowMgr workflowMgr;
	@Autowired
	UpdateJobRepo updateJobRepo;
	@Autowired
	Config config;
	@Autowired
	CommonRepo commonRepo;
	@Autowired
	BigAmountRepo bigAmountRepo;
	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;

	public void run() {
		PageRequest pageable = new PageRequest(0, 1000);
		while (true) {
			try {
				/*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String start = "2017-01-01";
				String end = "2017-07-22";
				Set<String> jgbm  = new HashSet<>();
				jgbm.add("310000151101");
				try {
					Long result = bigAmountRepo.findAllReport(format.parse(start), format.parse(end), null, jgbm);
					Long result2 = amlSuspiciousRepo.findAllReport(format.parse(start), format.parse(end), null, jgbm);
					List<CustomerReport> list = bigAmountRepo.findByCtnmAndCsnm("", "05300012", format.parse(start),
							format.parse(end), null);
					List<CustomerReport> list2 = amlSuspiciousRepo.findBySenmAndCsnm("", "1", format.parse(start),
							format.parse(end), null);
					List<QueryResult> list3 = bigAmountRepo.findReportByCrcd(format.parse(start), format.parse(end),
							null);
					CrimeReport crimeReport = amlSuspiciousRepo.findReportByTosc(format.parse(start), format.parse(end),
							"101", null);
					System.err.println(result + "   " + result2);
					for (int i = 0; i < list3.size(); i++) {
						System.err.println(list3.get(i));
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				Thread.sleep(1 * 60 * 1000);
				Specification<BaseCorpCust> s1 = null;
				s1 = new Specification<BaseCorpCust>() {
					@Override
					public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(root.get("jobId").as(Job.class).isNull());
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseCorpCust> pagee = baseCorpCustRepo.findAll(Specifications.where(s1), pageable);
					List<BaseCorpCust> baseCorpCusts = pagee.getContent();
					List<BaseCorpCust> baseCorp = new ArrayList<>();
					Set<String> basecorps = new HashSet<>();
					for (int j = 0; j < baseCorpCusts.size(); j++) {
						BaseCorpCust baseCorpCust1 = baseCorpCusts.get(j);
						if (baseCorpCust1.getJobId() == null) {
							if (baseCorpCust1.getConfirmStatus() != null
									&& baseCorpCust1.getConfirmStatus().equals("101001")) {
								baseCorpCust1.setStartState("PRE_APPROVE");
							} else {
								Calendar cal = Calendar.getInstance();
								baseCorpCust1.setSyncLastTime(cal.getTimeInMillis());
								baseCorpCust1.setStartState("APPROVED");
								baseCorpCust1.setConfirmStatus("101002");
							}
							basecorps.add(baseCorpCust1.getJobId());
							baseCorp.add(baseCorpCust1);
						}
					}
					workflowMgr.addJobs(config.getBaseInfoWorkId(), baseCorp);
					baseCorpCustRepo.save(baseCorp);
					if (basecorps.size() > 0) {
						updateJobRepo.updateStartState(basecorps);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				Specification<BaseIndvCust> s2 = null;
				s2 = new Specification<BaseIndvCust>() {
					@Override
					public Predicate toPredicate(Root<BaseIndvCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(root.get("jobId").as(Job.class).isNull());
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				pageable = new PageRequest(0, 1000);
				while (true) {
					Page<BaseIndvCust> pagee = baseIndvCustRepo.findAll(Specifications.where(s2), pageable);
					List<BaseIndvCust> baseIndvCusts = pagee.getContent();
					List<BaseIndvCust> baseIndv = new ArrayList<>();
					Set<String> baseIndvs = new HashSet<>();
					for (int j = 0; j < baseIndvCusts.size(); j++) {
						BaseIndvCust baseIndvCust1 = baseIndvCusts.get(j);
						if (baseIndvCust1.getJobId() == null) {
							if (baseIndvCust1.getConfirmStatus() != null
									&& baseIndvCust1.getConfirmStatus().equals("101001")) {
								baseIndvCust1.setStartState("PRE_APPROVE");
							} else {
								Calendar cal = Calendar.getInstance();
								baseIndvCust1.setSyncLastTime(cal.getTimeInMillis());
								baseIndvCust1.setStartState("APPROVED");
								baseIndvCust1.setConfirmStatus("101002");
							}
							baseIndvs.add(baseIndvCust1.getJobId());
							baseIndv.add(baseIndvCust1);
						}
					}
					workflowMgr.addJobs(config.getBaseInfoWorkId(), baseIndv);
					if (baseIndv.size() > 0) {
						updateJobRepo.updateStartState(baseIndvs);
					}
					baseIndvCustRepo.save(baseIndv);
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				pageable = new PageRequest(0, 1000);
				Specification<BaseAif> s3 = null;
				s3 = new Specification<BaseAif>() {
					@Override
					public Predicate toPredicate(Root<BaseAif> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(root.get("jobId").as(Job.class).isNull());
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseAif> pagee = baseAifRepo.findAll(Specifications.where(s3), pageable);
					List<BaseAif> baseAifs = pagee.getContent();
					List<BaseAif> baseAifs2 = new ArrayList<>();
					Set<String> baseAifs3 = new HashSet<>();
					for (int j = 0; j < baseAifs.size(); j++) {
						BaseAif baseAif1 = baseAifs.get(j);
						if (baseAif1.getJobId() == null) {
							if (baseAif1.getConfirmStatus() != null && baseAif1.getConfirmStatus().equals("101001")) {
								baseAif1.setStartState("PRE_APPROVE");
							} else {
								Calendar cal = Calendar.getInstance();
								baseAif1.setSyncLastTime(cal.getTimeInMillis());
								baseAif1.setStartState("APPROVED");
								baseAif1.setConfirmStatus("101002");
							}
							baseAifs3.add(baseAif1.getJobId());
							baseAifs2.add(baseAif1);
						}
					}
					workflowMgr.addJobs(config.getBaseInfoWorkId(), baseAifs2);
					baseAifRepo.save(baseAifs2);
					if (baseAifs3.size() > 0) {
						updateJobRepo.updateStartState(baseAifs3);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				pageable = new PageRequest(0, 1000);
				Specification<BaseTrn> s4 = null;
				s4 = new Specification<BaseTrn>() {
					@Override
					public Predicate toPredicate(Root<BaseTrn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(root.get("jobId").as(Job.class).isNull());
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseTrn> pagee = baseTrnRepo.findAll(Specifications.where(s4), pageable);
					List<BaseTrn> baseTrns = pagee.getContent();
					List<BaseTrn> baseTrns2 = new ArrayList<>();
					Set<String> ids = new HashSet<String>();
					for (int j = 0; j < baseTrns.size(); j++) {
						BaseTrn baseTrn2 = baseTrns.get(j);
						if (baseTrn2.getJobId() == null) {
							if (baseTrn2.getConfirmStatus() != null && baseTrn2.getConfirmStatus().equals("101001")) {
								baseTrn2.setStartState("PRE_APPROVE");
							} else {
								Calendar cal = Calendar.getInstance();
								baseTrn2.setSyncLastTime(cal.getTimeInMillis());
								baseTrn2.setStartState("APPROVED");
								baseTrn2.setConfirmStatus("101002");
							}
							ids.add(baseTrn2.getJobId());
							baseTrns2.add(baseTrn2);
						}
					}
					workflowMgr.addJobs(config.getBaseInfoWorkId(), baseTrns2);
					baseTrnRepo.save(baseTrns2);
					if (ids.size() > 0) {
						updateJobRepo.updateStartState(ids);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				Specification<BaseCorpCust> s5 = null;
				s5 = new Specification<BaseCorpCust>() {
					@Override
					public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101003"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				pageable = new PageRequest(0, 1000);
				while (true) {
					Page<BaseCorpCust> pagee = baseCorpCustRepo.findAll(Specifications.where(s5), pageable);
					List<BaseCorpCust> baseCorpCusts = pagee.getContent();
					List<BaseCorpCust> baseCorp = new ArrayList<>();
					for (int j = 0; j < baseCorpCusts.size(); j++) {
						BaseCorpCust baseCorpCust1 = baseCorpCusts.get(j);
						if (baseCorpCust1.getConfirmStatus().equals("101003")) {
							baseCorpCust1.setConfirmStatus("101001");
							baseCorp.add(baseCorpCust1);
						}
					}
					baseCorpCustRepo.save(baseCorp);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseCorp.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseCorp.get(j).getJobId(), "UNAPPROVE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						List<EventResult> results = workflowMgr.sendEvents(jobEvents);
						for (EventResult eventResult : results) {
							System.err.println(eventResult.getData());
						}
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}

				}
				Specification<BaseIndvCust> s6 = null;
				s6 = new Specification<BaseIndvCust>() {
					@Override
					public Predicate toPredicate(Root<BaseIndvCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101003"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				pageable = new PageRequest(0, 1000);
				while (true) {
					Page<BaseIndvCust> pagee = baseIndvCustRepo.findAll(Specifications.where(s6), pageable);
					List<BaseIndvCust> baseIndvCusts = pagee.getContent();
					List<BaseIndvCust> baseIndv = new ArrayList<>();
					for (int j = 0; j < baseIndvCusts.size(); j++) {
						BaseIndvCust baseIndvCust1 = baseIndvCusts.get(j);
						if (baseIndvCust1.getConfirmStatus().equals("101003")) {
							baseIndvCust1.setConfirmStatus("101001");
							baseIndv.add(baseIndvCust1);
						}
					}
					baseIndvCustRepo.save(baseIndv);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseIndv.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseIndv.get(j).getJobId(), "UNAPPROVE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				pageable = new PageRequest(0, 1000);
				Specification<BaseAif> s7 = null;
				s7 = new Specification<BaseAif>() {
					@Override
					public Predicate toPredicate(Root<BaseAif> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101003"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseAif> pagee = baseAifRepo.findAll(Specifications.where(s7), pageable);
					List<BaseAif> baseAifs = pagee.getContent();
					List<BaseAif> baseAifs2 = new ArrayList<>();
					for (int j = 0; j < baseAifs.size(); j++) {
						BaseAif baseAif1 = baseAifs.get(j);
						if (baseAif1.getConfirmStatus().equals("101003")) {
							baseAif1.setConfirmStatus("101001");
							baseAifs2.add(baseAif1);
						}
					}
					baseAifRepo.save(baseAifs2);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseAifs2.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseAifs2.get(j).getJobId(), "UNAPPROVE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				pageable = new PageRequest(0, 1000);
				Specification<BaseTrn> s8 = null;
				s8 = new Specification<BaseTrn>() {
					@Override
					public Predicate toPredicate(Root<BaseTrn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101003"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseTrn> pagee = baseTrnRepo.findAll(Specifications.where(s8), pageable);
					List<BaseTrn> baseTrns = pagee.getContent();
					List<BaseTrn> baseTrns2 = new ArrayList<>();
					for (int j = 0; j < baseTrns.size(); j++) {
						BaseTrn baseTrn2 = baseTrns.get(j);
						if (baseTrn2.getConfirmStatus().equals("101003")) {
							baseTrn2.setConfirmStatus("101001");
							baseTrns2.add(baseTrn2);
						}
					}
					baseTrnRepo.save(baseTrns2);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseTrns2.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseTrns2.get(j).getJobId(), "UNAPPROVE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				Specification<BaseCorpCust> s9 = null;
				s9 = new Specification<BaseCorpCust>() {
					@Override
					public Predicate toPredicate(Root<BaseCorpCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101004"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				pageable = new PageRequest(0, 1000);
				while (true) {
					Page<BaseCorpCust> pagee = baseCorpCustRepo.findAll(Specifications.where(s9), pageable);
					List<BaseCorpCust> baseCorpCusts = pagee.getContent();
					List<BaseCorpCust> baseCorp = new ArrayList<>();
					for (int j = 0; j < baseCorpCusts.size(); j++) {
						BaseCorpCust baseCorpCust1 = baseCorpCusts.get(j);
						if (baseCorpCust1.getConfirmStatus().equals("101004")) {
							baseCorpCust1.setConfirmStatus("101001");
							baseCorp.add(baseCorpCust1);
						}
					}
					baseCorpCustRepo.save(baseCorp);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseCorp.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseCorp.get(j).getJobId(), "UPDATE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}

				}
				Specification<BaseIndvCust> s10 = null;
				s10 = new Specification<BaseIndvCust>() {
					@Override
					public Predicate toPredicate(Root<BaseIndvCust> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101004"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				pageable = new PageRequest(0, 1000);
				while (true) {
					Page<BaseIndvCust> pagee = baseIndvCustRepo.findAll(Specifications.where(s10), pageable);
					List<BaseIndvCust> baseIndvCusts = pagee.getContent();
					List<BaseIndvCust> baseIndv = new ArrayList<>();
					for (int j = 0; j < baseIndvCusts.size(); j++) {
						BaseIndvCust baseIndvCust1 = baseIndvCusts.get(j);
						if (baseIndvCust1.getConfirmStatus().equals("101004")) {
							baseIndvCust1.setConfirmStatus("101001");
							baseIndv.add(baseIndvCust1);
						}
					}
					baseIndvCustRepo.save(baseIndv);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseIndv.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseIndv.get(j).getJobId(), "UPDATE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}

				pageable = new PageRequest(0, 1000);
				Specification<BaseAif> s11 = null;
				s11 = new Specification<BaseAif>() {
					@Override
					public Predicate toPredicate(Root<BaseAif> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101004"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseAif> pagee = baseAifRepo.findAll(Specifications.where(s11), pageable);
					List<BaseAif> baseAifs = pagee.getContent();
					List<BaseAif> baseAifs2 = new ArrayList<>();
					for (int j = 0; j < baseAifs.size(); j++) {
						BaseAif baseAif1 = baseAifs.get(j);
						if (baseAif1.getConfirmStatus().equals("101004")) {
							baseAif1.setConfirmStatus("101001");
							baseAifs2.add(baseAif1);
						}
					}
					baseAifRepo.save(baseAifs2);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseAifs2.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseAifs2.get(j).getJobId(), "UPDATE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				pageable = new PageRequest(0, 1000);
				Specification<BaseTrn> s12 = null;
				s12 = new Specification<BaseTrn>() {
					@Override
					public Predicate toPredicate(Root<BaseTrn> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
						List<Predicate> list = new ArrayList<Predicate>();
						list.add(cb.equal((root.get("confirmStatus").as(String.class)), "101004"));
						Predicate[] p = new Predicate[list.size()];
						return cb.or(list.toArray(p));
					}
				};
				while (true) {
					Page<BaseTrn> pagee = baseTrnRepo.findAll(Specifications.where(s12), pageable);
					List<BaseTrn> baseTrns = pagee.getContent();
					List<BaseTrn> baseTrns2 = new ArrayList<>();
					for (int j = 0; j < baseTrns.size(); j++) {
						BaseTrn baseTrn2 = baseTrns.get(j);
						if (baseTrn2.getConfirmStatus().equals("101004")) {
							baseTrn2.setConfirmStatus("101001");
							baseTrns2.add(baseTrn2);
						}
					}
					baseTrnRepo.save(baseTrns2);
					List<JobEvent> jobEvents = new ArrayList<>();
					for (int j = 0; j < baseTrns2.size(); j++) {
						JobEvent jobEvent = new JobEvent(baseTrns2.get(j).getJobId(), "UPDATE", "");
						jobEvent.setFireEvent(false);
						jobEvents.add(jobEvent);
					}
					if (jobEvents.size() > 0) {
						workflowMgr.sendEvents(jobEvents);
					}
					if (pagee.hasNext()) {
						pageable.next();
					} else {
						break;
					}
				}
				Thread.sleep(2 * 60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
}