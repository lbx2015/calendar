package net.riking.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import net.riking.core.service.repo.CommonRepo;
import net.riking.util.JdbcBatch;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Config;
import net.riking.core.entity.Target;
import net.riking.core.entity.model.BaseFlowJob;
import net.riking.core.entity.model.Job;
import net.riking.core.entity.model.JobState;
import net.riking.core.workflow.Workflow;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.core.workflow.repo.JobRepo;
import net.riking.core.workflow.repo.JobStateRepo;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BigAmount;
import net.riking.entity.model.CheckResult;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BigAmountRepo;

import javax.persistence.*;

/**
 * Created by bing.xun on 2017/6/20.
 */
@Service("workflowServiceImpl")
public class WorkflowServiceImpl {

	@Autowired
	JobRepo jobRepo;

	@Autowired
	JobStateRepo jobStateRepo;

	@Autowired
	private WorkflowMgr workflowMgr;

	@Autowired
	private BigAmountRepo bigAmountRepo;

	@Autowired
	private Config config;

	@Autowired
	private AmlSuspiciousRepo amlSuspiciousRepo;

	@Autowired
	CommonRepo commonRepo;

	@Autowired
	private BigSuspCheckServiceImpl bigSuspCheckService;

	@Autowired
	JdbcBatch jdbcBatch;

	public Integer saveAmlSuspiciousList(List<AmlSuspicious> amlSuspiciousList) throws Exception {
		Integer count = 0;
		for (int i = 0; i < amlSuspiciousList.size(); i += 5000) {
			if (amlSuspiciousList.size() - i > 5000) {
				count += saveBigAmountOrAmlSuspiciousJobJob(amlSuspiciousList.subList(i, i + 5000), (byte) 2);
				// workflowMgr.addJobs(config.getAmlWorkId(),amlSuspiciousList.subList(i,i+1000));
			} else {
				if (amlSuspiciousList.size() > i) {
					count += saveBigAmountOrAmlSuspiciousJobJob(amlSuspiciousList.subList(i, amlSuspiciousList.size()),
							(byte) 2);
					// workflowMgr.addJobs(config.getAmlWorkId(),amlSuspiciousList.subList(i,i+1000));
				}
			}
		}
		return count;
	}

	public Integer saveBigAmountList(List<BigAmount> bigAmountList) throws Exception {
		Integer count = 0;
		for (int i = 0; i < bigAmountList.size(); i += 5000) {
			if (bigAmountList.size() - i > 5000) {
				count += saveBigAmountOrAmlSuspiciousJobJob(bigAmountList.subList(i, i + 5000), (byte) 1);
			} else {
				if (bigAmountList.size() > i) {
					count += saveBigAmountOrAmlSuspiciousJobJob(bigAmountList.subList(i, bigAmountList.size()),
							(byte) 1);
				}
			}
		}
		return count;
	}

	public void initBigAmount(List<BigAmount> bigAmountList) {
		for (BigAmount bigAmount : bigAmountList) {
			CheckResult checkResult = bigSuspCheckService.checkBigAmount(bigAmount, true);
			if (StringUtils.isEmpty(checkResult.getReason())) {
				bigAmount.setStartState("PRE_SUBMIT");
				bigAmount.setFlowState("PRE_SUBMIT");
			} else {
				bigAmount.setStartState("PRE_RECROD");
				bigAmount.setFlowState("PRE_RECROD");
			}
			bigAmount.setFlowOwner("");
		}
	}

	public void initAmlSuspicious(List<AmlSuspicious> amlSuspiciousList) {
		for (AmlSuspicious amlSuspicious : amlSuspiciousList) {
			amlSuspicious.setStartState("SISP_PRE_DEAL");
			amlSuspicious.setFlowState("SISP_PRE_DEAL");
			amlSuspicious.setFlowOwner("");
		}
	}

	@SuppressWarnings("unchecked")
	public int saveBigAmountOrAmlSuspiciousJobJob(Collection<? extends BaseFlowJob> flowJobs, byte type) throws Exception {
		if (flowJobs == null)
			return 0;
		List<JobState> jobStates = new ArrayList<>();
		List<Job> jobs = new ArrayList<>();
		int len = flowJobs.size();
		Workflow workflow = workflowMgr.getWorkflow(config.getAmlWorkId());
		for (BaseFlowJob fjob : flowJobs) {
			if (fjob.getJob() == null) {
				continue;
			}
			JobState firstState = new JobState();
			String state = fjob.getJob().getStartState();
			if (StringUtils.isEmpty(state)) {
				state = workflow.getStates().get(0).getState();
			}
			firstState.setId(UUID.randomUUID().toString().replace("-",""));
			firstState.setState(state);
			firstState.setJobId(fjob.getJob().getJobId());

			Job job = fjob.getJob();
			job.setFlowId(config.getAmlWorkId());
			job.setTarget(Target.of(fjob.getClass()).toString());
			job.setStartState(state);
			job.setCurJobState(state);
			jobs.add(job);

			firstState.setDateCreate(new Date());
			if (firstState.getComments() == null) {
				firstState.setComments("init");
			}
			jobStates.add(firstState);
			fjob.setStartState(firstState.getState());
			fjob.setFlowState(firstState.getState());
			fjob.setFlowOwner(firstState.getOwner());
		}
		bactchInsert("net.riking.core.entity.model.Job",jobs);
		if(type == 1) {
			bactchInsert("net.riking.entity.model.BigAmount",flowJobs);
		}else{
			bactchInsert("net.riking.entity.model.AmlSuspicious",flowJobs);
		}
		bactchInsert("net.riking.core.entity.model.JobState",jobStates);

		return 0;
	}

	public static String getBeanName(String bean) throws Exception {
		Class clz = Class.forName(bean);
		Table anno = (Table)clz.getAnnotation(Table.class);
		if(anno != null){
			return anno.name();
		}
		return "";
	}

	public static List<String> getBeanPropertyList(String bean){
		try {
			Class clz = Class.forName(bean);
			Field[] strs = clz.getDeclaredFields();
			List<String> propertyList = new ArrayList<String>();
			for (int i = 0; i < strs.length; i++) {
				String protype = strs[i].getType().toString();
				if("job".equals(strs[i].getName())){
					continue;
				}
				if("serialVersionUID".equals(strs[i].getName())){
					continue;
				}
				Transient tx = strs[i].getAnnotation(Transient.class);
				if(null == tx){
					String name = strs[i].getName();
					propertyList.add(protype.substring(protype.lastIndexOf(".")+1)+"`"+name);
				}
			}
			return propertyList;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String getBeanFieldsList(String bean){
		try {
			Class clz = Class.forName(bean);
			Field[] strsSub = clz.getDeclaredFields();
			Class clzSuper = Class.forName(bean).getSuperclass();
			List<Field> strs = new ArrayList<>();
			for(Field filed:strsSub){
				strs.add(filed);
			}
			if(null != clzSuper){
				Field[] StrsSuper = clzSuper.getDeclaredFields();
				for(Field filed:StrsSuper){
					strs.add(filed);
				}
			}

			StringBuffer sb = new StringBuffer();
			for (Field field:strs) {
				if(!field.isAccessible()){
					field.setAccessible(true);
				}
				if("job".equals(field.getName())){
					continue;
				}
				if("serialVersionUID".equals(field.getName())){
					continue;
				}
				if("idTo".equals(field.getName())){
					continue;
				}
				Transient tx = field.getAnnotation(Transient.class);
				if(null == tx){
					String name = field.getName();
					if(!bean.equals("net.riking.core.entity.model.JobState")&&"id".equals(name)){
						continue;
					}
					Column column = field.getAnnotation(Column.class);
					if(null != column && StringUtils.isNotBlank(column.name())){
						name = column.name();
					}
					/*if("jobId".equals(name)){
						if(bean.equals("net.riking.core.entity.model.Job")) {
							name = "id";
						}else if(!bean.equals("net.riking.core.entity.model.JobState")){
							name = "job_id";
						}
					}*/
					sb.append(name+",");
				}
			}
			sb.deleteCharAt(sb.toString().lastIndexOf(","));
			return sb.toString();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 生成插入语句
	 * @param bean
	 * @return
	 */
	public static String genInsertSql(String bean) throws Exception {
		String fieldsList =  getBeanFieldsList(bean);
		String[] field = fieldsList.split(",");
		String vlaues = "";
		for (int i = 0; i < field.length; i++) {
			if(i==field.length-1){
				vlaues += ":"+field[i];
			}else{
				vlaues += ":"+field[i] + ",";
			}
		}
		return "insert into "+getBeanName(bean)+"("+fieldsList+") values("+vlaues+")";
	}

	public void bactchInsert(String className,Collection<?> list) throws Exception {
		//"net.riking.entity.model.BigAmount"
		String sql = genInsertSql(className);
		List<Map<String,Object>> paramList = new ArrayList<>();
		for(Object object : list){
			Map paramsMap = transBean2Map(object);
			paramList.add(paramsMap);
		}
		jdbcBatch.batchInsert(sql,paramList);
	}

	public static Map<String, Object> transBean2Map(Object obj) {
		if(obj == null){
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			Class clz = obj.getClass();
			Field[] strsSub = clz.getDeclaredFields();
			Class clzSuper = clz.getSuperclass();
			List<Field> strs = new ArrayList<>();
			for(Field filed:strsSub){
				strs.add(filed);
			}
			if(null != clzSuper){
				Field[] StrsSuper = clzSuper.getDeclaredFields();
				for(Field filed:StrsSuper){
					strs.add(filed);
				}
			}
			Map<String,Field> mapFiled = new HashMap<>();
			for(Field field: strs){
				mapFiled.put(field.getName(),field);
			}

			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					if(null ==getter){
						continue;
					}
					Object value = getter.invoke(obj);
/*					if("jobId".equals(key)) {
						if (obj.getClass().getName().equals("net.riking.core.entity.model.Job")) {
							key = "id";
						} else if(!obj.getClass().getName().equals("net.riking.core.entity.model.JobState")){
							key = "job_id";
						}
					}*/
					if(null != mapFiled.get(key)){
						Column column = mapFiled.get(key).getAnnotation(Column.class);
						if(null != column && StringUtils.isNotBlank(column.name())){
							key = column.name();
						}
					}
					map.put(key, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
