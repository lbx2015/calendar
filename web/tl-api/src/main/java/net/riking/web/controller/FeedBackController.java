package net.riking.web.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.riking.core.annos.AuthPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.core.entity.JobEvent;
import net.riking.core.entity.Resp;
import net.riking.core.log.InModLog;
import net.riking.core.workflow.WorkflowMgr;
import net.riking.entity.PageQuery;
import net.riking.entity.model.AmlSuspicious;
import net.riking.entity.model.BaseReceipt;
import net.riking.entity.model.BigAmount;
import net.riking.service.FeedBackServiceImpl;
import net.riking.service.repo.AmlSuspiciousRepo;
import net.riking.service.repo.BaseReceiptRepo;
import net.riking.service.repo.BigAmountRepo;

@InModLog(modName="反馈报文")
@RestController
@RequestMapping(value = "/Receipt")
public class FeedBackController {

	@Autowired
	FeedBackServiceImpl     feedBackServiceImpl;
	
	@Autowired
	BaseReceiptRepo baseReceiptRepo;

	@Autowired
	Config config;

	@Autowired
	BigAmountRepo bigAmountRepo;

	@Autowired
	WorkflowMgr workflowMgr;

	@Autowired
	AmlSuspiciousRepo amlSuspiciousRepo;

	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore(@ModelAttribute PageQuery query, @ModelAttribute BaseReceipt baseReceipt) {
		Sort sort = new Sort(Direction.DESC, "hzsj");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), sort);
		Example<BaseReceipt> example = Example.of(baseReceipt, ExampleMatcher.matchingAll());
		Page<BaseReceipt> page = baseReceiptRepo.findAll(example, pageable);
		return new Resp(page, CodeDef.SUCCESS);
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") Long id) {
		BaseReceipt baseReceipt = baseReceiptRepo.findOne(id);
		return new Resp(baseReceipt, CodeDef.SUCCESS);
	}
	@AuthPass
	@RequestMapping(value = "/receiptImport", method = RequestMethod.POST, produces ="text/plain;charset=utf-8 ")
	public String addMore(HttpServletRequest request) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		String Username = request.getParameter("user");
		MultipartFile mFile = multipartRequest.getFile("bw");
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String startDate = df.format(calendar.getTime());
		String fileName = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("\\")+1);
		String type = fileName.substring(0, 4);
		String state = fileName.substring(5, 8);
		List<BaseReceipt> listes = baseReceiptRepo.findByHzmc(fileName);
		int rs = 1;
		if (listes.size() > 0) {
			rs = 0;
			return rs+"";
		}
		// 报文类型：新增报文（N），修改报文（C），删除报文（D）; 大额（BH）、可疑（BS）
		if (fileName.length() > 39) {
			if (type.equals(config.getCorrectReceipt())) {
				if (state.substring(1).equals("BH")) {
					bigAmountcorrectReceipt(mFile, Username);
				} else if (state.substring(1).equals("BS")) {
					supCorrectReceipt(mFile, Username);
				}
			} else if (type.equals(config.getModifyReceipt())) {
				if (state.equals("NBH")) {
					bigAmountModifyReceipt(mFile, Username);
				}
				if (state.equals("NBS")) {
					supModifyReceipt(mFile, 1, Username);
				}
			} else if (type.equals(config.getWrongReceipt())) {
				if (state.equals("DBH")) {
					bigAmountDeleteWoring(mFile, Username);
				}
				if (state.equals("ABH") || state.equals("SBH")) {
					bigAmountXGBWWoring(mFile, Username);
				}
				if (state.equals("NBH") || state.equals("CBH")) {
					bigAmountXZBWWoring(mFile, Username);
				}
				if (state.equals("NBS")) {
					supModifyReceipt(mFile, 2, Username);
				}
			}
		} else {
			state = fileName.substring(0, 3);
			if (state.equals("ABH")) {
				bigamountCorrectionsReceipt(mFile, Username);
			}
			if (state.equals("SBH")) {

			}
			if (state.equals("ABS")) {

			}
		}
		InputStream in = mFile.getInputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		String path1 = config.getReceipt();
		fileName = path1 + "\\" + startDate + "\\" + fileName;// 文件最终上传的位置
		File file = new File(fileName);
		File fileParent = file.getParentFile();
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		OutputStream out = new FileOutputStream(fileName);
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();
		return rs+"";
	}
	
	private List<BaseReceipt> bigAmountcorrectReceipt(MultipartFile mFile, String userName) throws Exception {
		// String userName = TokenHolder.get().getUserName();
		// System.err.println(userName);
		String Hzmc = mFile.getOriginalFilename();
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		BaseReceipt baseReceipt = null;
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		// 返回documentBuilderFactory对象
		dbf = DocumentBuilderFactory.newInstance();
		// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNodes = element.getChildNodes();
		String RPID = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node1 = childNodes.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
		}
		Set<Long> ides = bigAmountRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides.size() < 1) {
			return null;
		}
		if (ides.size() > 0) {
			List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ides);
			for (int i = 0; i < bigAmounts.size(); i++) {
				BigAmount bigAmount = bigAmounts.get(i);
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx("正确回执");
				baseReceipt.setXgnr("已正确入库");
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("02");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(bigAmount.getCrcd());
				baseReceipt.setOldcsnm(bigAmount.getCsnm());
				baseReceipt.setOldticd(bigAmount.getTicd());
				baseReceipt.setOldtstm(bigAmount.getTstm());
				baseReceipt.setOldId(bigAmount.getId());
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "ADD_STORAGE", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;
	}

	private List<BaseReceipt> bigamountCorrectionsReceipt(MultipartFile mFile, String userName) throws Exception {
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String Hzmc = mFile.getOriginalFilename();
		List<BaseReceipt> list = new ArrayList<>();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String hzjs = dfs.format(calendar.getTime());
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		// 返回documentBuilderFactory对象
		dbf = DocumentBuilderFactory.newInstance();
		// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNodes = element.getChildNodes();
		String Requirement = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node1 = childNodes.item(i);
			if ("BSIF".equals(node1.getNodeName())) {
				Node node2 = childNodes.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if ("TMLM".equals(node3.getNodeName())) {
						Requirement = "更正完成时限:" + node3.getTextContent() + "；";
					}
					if ("RQDS".equals(node3.getNodeName())) {
						Requirement = Requirement + "更正填报要求:" + node3.getTextContent() + "；";
					}
				}
			}
			if ("TSDTs".equals(node1.getNodeName())) {
				Node node2 = childNodes.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if ("TSDT".equals(node3.getNodeName())) {
						BaseReceipt baseReceipt = new BaseReceipt();
						baseReceipt.setHzsj(dfs.parse(hzjs));
						baseReceipt.setHzState("01");
						baseReceipt.setHzmc(Hzmc);
						baseReceipt.setHzlx("人工补正通知");
						StringBuffer Xgnr = new StringBuffer();
						Xgnr.append(Requirement);
						Node node4 = childNodes1.item(j);
						NodeList childNodes2 = node4.getChildNodes();
						for (int k = 0; k < childNodes2.getLength(); k++) {
							Node node5 = childNodes2.item(k);
							if ("OCNM".equals(node5.getNodeName())) {
								baseReceipt.setOldcsnm(node5.getTextContent());
							}
							if ("OTDT".equals(node5.getNodeName())) {
								baseReceipt.setOldtstm(df.parse(node5.getTextContent()));
							}
							if ("OTCD".equals(node5.getNodeName())) {
								baseReceipt.setOldcrcd(node5.getTextContent());
							}
							if ("OTIC".equals(node5.getNodeName())) {
								baseReceipt.setOldticd(node5.getTextContent());
							}
							if ("ITEMS".equals(node5.getNodeName())) {
								Node node6 = childNodes2.item(k);
								NodeList childNodes4 = node6.getChildNodes();
								int q = 0;
								for (int l = 0; l < childNodes4.getLength(); l++) {
									Node node7 = childNodes4.item(l);
									if ("ITEM".equals(node7.getNodeName())) {
										q++;
										Xgnr.append("待变更字段" + q + node7.getTextContent()+"；");
									}
								}
								baseReceipt.setXgnr(Xgnr.toString());
							}
						}
						list.add(baseReceipt);
					}
				}
			}
		}
		Set<Long> ides = new HashSet<>();
		for (int j = 0; j < list.size(); j++) {
			BaseReceipt baseReceipt = list.get(j);
			Long oldId ;
			oldId=bigAmountRepo.findByTicdAndRpdtAndCrcdAndDeleteState(baseReceipt.getOldticd(),
					baseReceipt.getOldtstm(), baseReceipt.getOldcrcd(), "1");
			ides.add(oldId);
			baseReceipt.setOldId(oldId);
		}
		if (ides.size()>0) {
		List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ides);
		for (int i = 0; i < bigAmounts.size(); i++) {
			BaseReceipt baseReceipt = list.get(i);
			BigAmount bigAmount =bigAmounts.get(i);
			bigAmount.setBwzt("rggz");
			bigAmount.setReportType("C");
			bigAmountRepo.save(bigAmount);
			JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
			baseReceiptRepo.save(baseReceipt);
			workflowMgr.sendEvents(Arrays.asList(jobEvent));
		}
	}
		return null;

	}

	private List<BaseReceipt> supModifyReceipt(MultipartFile mFile, int state, String userName) throws Exception {
		String Hzmc = mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("\\")+1);
		String Hzlx;
		if (state == 1) {
			Hzlx = "补正回执";
		} else {
			Hzlx = "错误回执";
		}
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		BaseReceipt baseReceipt = null;
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		// 返回documentBuilderFactory对象
		dbf = DocumentBuilderFactory.newInstance();
		// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNodes = element.getChildNodes();
		String RPID = null;
		List<String>  ERLC = new ArrayList<>();
		List<String> Xgnr = new ArrayList<>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node1 = childNodes.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
			if ("FCERs".equals(node1.getNodeName())) {
				Node node2 = childNodes.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if ("FCER".equals(node3.getNodeName())) {
						Node node4 = childNodes1.item(i);
						NodeList childNodes2 = node4.getChildNodes();
						for (int k = 0; k < childNodes2.getLength(); k++) {
							Node node5 = childNodes2.item(k);
							if ("ERLC".equals(node5.getNodeName())) {
								ERLC.add(node5.getTextContent());
							}
							if ("ERRS".equals(node5.getNodeName())) {
								Xgnr.add(node5.getTextContent());
							}
						}
					}
				}
			}
		}
		List<AmlSuspicious> list =new ArrayList<>();
		int n = 0;
		for (int i = 0; i < ERLC.size(); i++) {
			String main = ERLC.get(i);
			int before = 0 ;
			int after = 0 ;
			int w = 0;
			int m =0 ;
			for (int j = 0; j < main.length(); j++) {
				 if (main.substring(j, j + 1).equals("["))   
				   { 
					 before = j;
					 w=w+1;
				   }
				 if (main.substring(j, j + 1).equals("]")) {
					 after = j+1;
					 m=m+1;
				}
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("STIF"+main.substring(before, after), "CSNM,TSTM,TICD");
			List<AmlSuspicious> amlSuspicious = feedBackServiceImpl.getFailAmlSuspicious(RPID+".ZIP", map);
			if (amlSuspicious.size()>0) {
				list.add(amlSuspicious.get(0));
				n++;
			}else {
				Xgnr.remove(n);			}
		}
		/*Set<Long> ides = amlSuspiciousRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides.size() < 1) {
			return null;
		}*/
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				AmlSuspicious amlSuspiciou = list.get(i);
				if (amlSuspiciou.getReportType().equals("N")) {
					amlSuspiciou.setBwzt("错误回执");
					amlSuspiciousRepo.save(amlSuspiciou);
				}
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx(Hzlx);
				baseReceipt.setXgnr(Xgnr.get(i));
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("01");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(amlSuspiciou.getStcr());
				baseReceipt.setOldcsnm(amlSuspiciou.getCsnm1());
				baseReceipt.setOldticd(amlSuspiciou.getTicd());
				baseReceipt.setOldtstm(amlSuspiciou.getTstm());
				baseReceipt.setOldId(amlSuspiciou.getId());
				if (amlSuspiciou.getId() != null) {
					JobEvent jobEvent = new JobEvent(amlSuspiciou.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;

	}

	private List<BaseReceipt> bigAmountModifyReceipt(MultipartFile mFile, String userName) throws Exception {
		List<BaseReceipt> list = new ArrayList<>();
		String Hzmc =mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("\\")+1);
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNode = element.getChildNodes();
		String RPID = null;
		StringBuffer Xgnr = null;
		for (int i = 0; i < childNode.getLength(); i++) {
			Node node1 = childNode.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
			if ("FCRCs".equals(node1.getNodeName())) {
				Node node2 = childNode.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if (!node3.getNodeName().equals("#text")) {
						BaseReceipt baseReceipt = new BaseReceipt();
						baseReceipt.setBwmc(RPID);
						baseReceipt.setHzlx("补正回执");
						baseReceipt.setHzsj(dfs.parse(zhjs));
						baseReceipt.setHzState("01");
						baseReceipt.setHzmc(Hzmc);
						Xgnr = new StringBuffer();
						int q = 1;
						if ("FCRC".equals(node3.getNodeName())) {
							NodeList childNodes3 = node3.getChildNodes();
							for (int k = 0; k < childNodes3.getLength(); k++) {
								Node node4 = childNodes3.item(k);
								if ("OCNM".equals(node4.getNodeName())) {
									baseReceipt.setOldcsnm(node4.getTextContent());
								}
								if ("OTDT".equals(node4.getNodeName())) {
									baseReceipt.setOldtstm(df.parse(node4.getTextContent()));
								}
								if ("OTCD".equals(node4.getNodeName())) {
									baseReceipt.setOldcrcd(node4.getTextContent());
								}
								if ("OTIC".equals(node4.getNodeName())) {
									baseReceipt.setOldticd(node4.getTextContent());
								}
								if ("FCERs".equals(node4.getNodeName())) {
									NodeList childNodes4 = node4.getChildNodes();
									for (int l = 0; l < childNodes4.getLength(); l++) {
										Node node5 = childNodes4.item(l);
										if (!node5.getNodeName().equals("#text")) {
											if ("FCER".equals(node5.getNodeName())) {
												NodeList childNodes5 = node5.getChildNodes();
												for (int m = 0; m < childNodes5.getLength(); m++) {
													Node node6 = childNodes5.item(m);
													if ("ERLC".equals(node6.getNodeName())) {
														Xgnr.append(q + "." + node6.getTextContent() + "：");
														q++;
													}
													if ("ERRS".equals(node6.getNodeName())) {
														Xgnr.append(node6.getTextContent() + "；");
													}
												}
											}
										}
									}
								}
							}
						}
						baseReceipt.setXgnr(Xgnr.toString());
						list.add(baseReceipt);
					}
				}
			}
		}
		Set<Long> ides = bigAmountRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides.size() < 1) {
			return null;
		}
		BaseReceipt baseReceipt = null;
		Set<Long> idess = new HashSet<Long>();
		for (int i = 0; i < list.size(); i++) {
			baseReceipt = list.get(i);
			idess.add(bigAmountRepo.findByTicdAndRpdtAndCrcdAndDeleteStates(baseReceipt.getOldticd(),
					baseReceipt.getOldtstm(), baseReceipt.getOldcrcd(), "1"));
		}
		Iterator<Long> it = ides.iterator();
		List<Long> idesss = new ArrayList<>();
		for (int i = 0; i < ides.size(); i++) {
			Long name = it.next();
			Iterator<Long> its = idess.iterator();
			for (int j = 0; j < idess.size(); j++) {
				Long names = its.next();
				if (name.equals(names)) {
					idesss.add(name);
				}
			}
		}
		int sum = idess.size();
		if (idess.size() > ides.size()) {
			sum = ides.size();
		}
		for (int i = 0; i < sum; i++) {
			ides.remove(idesss.get(i));
		}
		if (idess.size() > 0) {
			List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(idess);
			for (int i = 0; i < bigAmounts.size(); i++) {
				BigAmount bigAmount = bigAmounts.get(i);
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				list.get(i).setOldId(bigAmount.getId());
				baseReceiptRepo.save(list.get(i));
			}
		}
		if (ides.size() > 0) {
			List<BigAmount> bigAmountes = bigAmountRepo.findByIdIn(ides);
			for (int i = 0; i < bigAmountes.size(); i++) {
				BigAmount bigAmount = bigAmountes.get(i);
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx("正确回执");
				baseReceipt.setXgnr("已正确入库");
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("02");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(bigAmount.getCrcd());
				baseReceipt.setOldcsnm(bigAmount.getCsnm());
				baseReceipt.setOldticd(bigAmount.getTicd());
				baseReceipt.setOldtstm(bigAmount.getTstm());
				baseReceipt.setOldId(bigAmount.getId());
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "ADD_STORAGE", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;
	}

	private List<BaseReceipt> bigAmountXZBWWoring(MultipartFile mFile, String userName) throws Exception {
		String Hzmc =mFile.getOriginalFilename().substring(mFile.getOriginalFilename().lastIndexOf("\\")+1);
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		BaseReceipt baseReceipt = null;
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		// 返回documentBuilderFactory对象
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNodes = element.getChildNodes();
		String RPID = null;
		List<String>  ERLC = new ArrayList<>();
		List<String> Xgnr = new ArrayList<>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node1 = childNodes.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
			if ("FCERs".equals(node1.getNodeName())) {
				Node node2 = childNodes.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if ("FCER".equals(node3.getNodeName())) {
						Node node4 = childNodes1.item(j);
						NodeList childNodes2 = node4.getChildNodes();
						for (int k = 0; k < childNodes2.getLength(); k++) {
							Node node5 = childNodes2.item(k);
							if ("ERLC".equals(node5.getNodeName())) {
								ERLC.add(node5.getTextContent());
							}
							if ("ERRS".equals(node5.getNodeName())) {
								Xgnr.add(node5.getTextContent());
							}
						}
					}
				}
			}
		}
		List<BigAmount> list =new ArrayList<>();
		int n = 0;
		for (int i = 0; i < ERLC.size(); i++) {
			String main = ERLC.get(i);
			int[] before = new int[3];
			int[] after = new int[3];
			int w = 0;
			int m =0 ;
			for (int j = 0; j < main.length(); j++) {
				 if (main.substring(j, j + 1).equals("["))   
				   { 
					 before[w] = j;
					 w=w+1;
				   }
				 if (main.substring(j, j + 1).equals("]")) {
					 after[m] = j+1;
					 m=m+1;
				}
			}
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("CATI"+main.substring(before[0], after[0]), "CSNM,HTDT");
			map.put("HTCR"+main.subSequence(before[1], after[1]), "CRCD");
			map.put("TSDT"+main.subSequence(before[2], after[2]), "TICD");
			List<BigAmount> bigAmounts = feedBackServiceImpl.getFailBigAmount(RPID+".ZIP", map);
			if (bigAmounts.size()>0) {
				list.add(bigAmounts.get(0));
				n++;
			}else {
				Xgnr.remove(n);			}
		}
		/*Set<Long> ides = bigAmountRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides.size() < 1) {
			return null;
		}*/
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				BigAmount bigAmount = list.get(i);
				if (bigAmount.getReportType().equals("N")) {
					bigAmount.setBwzt("错误回执");
					bigAmountRepo.save(bigAmount);
				}
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx("错误回执");
				baseReceipt.setXgnr(Xgnr.get(i));
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("01");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(bigAmount.getCrcd());
				baseReceipt.setOldcsnm(bigAmount.getCsnm());
				baseReceipt.setOldticd(bigAmount.getTicd());
				baseReceipt.setOldtstm(bigAmount.getTstm());
				baseReceipt.setOldId(bigAmount.getId());
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;
	}

	private List<BaseReceipt> bigAmountXGBWWoring(MultipartFile mFile, String userName) throws Exception {
		List<BaseReceipt> list = new ArrayList<>();
		String Hzmc = mFile.getOriginalFilename();
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNode = element.getChildNodes();
		String RPID = null;
		StringBuffer Xgnr = null;
		for (int i = 0; i < childNode.getLength(); i++) {
			Node node1 = childNode.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
			if ("FCRCs".equals(node1.getNodeName())) {
				Node node2 = childNode.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int j = 0; j < childNodes1.getLength(); j++) {
					Node node3 = childNodes1.item(j);
					if (!node3.getNodeName().equals("#text")) {
						BaseReceipt baseReceipt = new BaseReceipt();
						baseReceipt.setBwmc(RPID);
						baseReceipt.setHzlx("错误回执");
						baseReceipt.setHzsj(dfs.parse(zhjs));
						baseReceipt.setHzState("01");
						baseReceipt.setHzmc(Hzmc);
						Xgnr = new StringBuffer();
						int q = 1;
						if ("FCRC".equals(node3.getNodeName())) {
							NodeList childNodes3 = node3.getChildNodes();
							for (int k = 0; k < childNodes3.getLength(); k++) {
								Node node4 = childNodes3.item(k);
								if ("OCNM".equals(node4.getNodeName())) {
									baseReceipt.setOldcsnm(node4.getTextContent());
								}
								if ("OTDT".equals(node4.getNodeName())) {
									baseReceipt.setOldtstm(df.parse(node4.getTextContent()));
								}
								if ("OTCD".equals(node4.getNodeName())) {
									baseReceipt.setOldcrcd(node4.getTextContent());
								}
								if ("OTIC".equals(node4.getNodeName())) {
									baseReceipt.setOldticd(node4.getTextContent());
								}
								if ("FCERs".equals(node4.getNodeName())) {
									NodeList childNodes4 = node4.getChildNodes();
									for (int l = 0; l < childNodes4.getLength(); l++) {
										Node node5 = childNodes4.item(l);
										if (!node5.getNodeName().equals("#text")) {
											if ("FCER".equals(node5.getNodeName())) {
												NodeList childNodes5 = node5.getChildNodes();
												for (int m = 0; m < childNodes5.getLength(); m++) {
													Node node6 = childNodes5.item(m);
													if ("ERLC".equals(node6.getNodeName())) {
														Xgnr.append(q + "." + node6.getTextContent() + "：");
														q++;
													}
													if ("ERRS".equals(node6.getNodeName())) {
														Xgnr.append(node6.getTextContent() + "；");
													}
												}
											}
										}
									}
								}
							}
						}
						baseReceipt.setXgnr(Xgnr.toString());
						list.add(baseReceipt);
					}
				}
			}
		}
		Set<Long> ides1 = bigAmountRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides1.size() < 1) {
			return null;
		}
		Set<Long> ides2 = new HashSet<Long>();
		BaseReceipt baseReceipt = null;
		for (int i = 0; i < list.size(); i++) {
			baseReceipt = list.get(i);
			ides2.add(bigAmountRepo.findByTicdAndRpdtAndCrcdAndDeleteStates(baseReceipt.getOldticd(),
					baseReceipt.getOldtstm(), baseReceipt.getOldcrcd(), "1"));
		}
		Iterator<Long> it = ides1.iterator();
		List<Long> ides3 = new ArrayList<>();
		for (int i = 0; i < ides1.size(); i++) {
			Long name = it.next();
			Iterator<Long> its = ides2.iterator();
			for (int j = 0; j < ides2.size(); j++) {
				Long names = its.next();
				if (name.equals(names)) {
					ides3.add(name);
				}
			}
		}
		int sum = ides2.size();
		if (ides2.size() > ides1.size()) {
			sum = ides1.size();
		}
		for (int i = 0; i < sum; i++) {
			ides1.remove(ides3.get(i));
		}
		if (ides2.size() > 0) {
			List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ides2);
			for (int i = 0; i < bigAmounts.size(); i++) {
				BigAmount bigAmount = bigAmounts.get(i);
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				list.get(i).setOldId(bigAmount.getId());
				baseReceiptRepo.save(list.get(i));
			}
		}
		if (ides1.size() > 0) {
			List<BigAmount> bigAmountes = bigAmountRepo.findByIdIn(ides1);
			for (int i = 0; i < bigAmountes.size(); i++) {
				BigAmount bigAmount = bigAmountes.get(i);
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx("错误回执");
				baseReceipt.setXgnr("同批次有数据错误");
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("02");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(bigAmount.getCrcd());
				baseReceipt.setOldcsnm(bigAmount.getCsnm());
				baseReceipt.setOldticd(bigAmount.getTicd());
				baseReceipt.setOldtstm(bigAmount.getTstm());
				baseReceipt.setOldId(bigAmount.getId());
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;
	}

	private List<BaseReceipt> bigAmountDeleteWoring(MultipartFile mFile, String userName) throws Exception {
		List<BaseReceipt> list1 = new ArrayList<>();
		List<BaseReceipt> list2 = new ArrayList<>();
		String Hzmc = mFile.getOriginalFilename();
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNode = element.getChildNodes();
		String RPID = null;
		for (int i = 0; i < childNode.getLength(); i++) {
			Node node1 = childNode.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
			if ("SDTKs".equals(node1.getNodeName())) {
				Node node2 = childNode.item(i);
				NodeList childNodes1 = node2.getChildNodes();
				for (int q = 0; q < childNodes1.getLength(); q++) {
					Node node3 = childNodes1.item(q);
					if (!node3.getNodeName().equals("#text")) {
						BaseReceipt baseReceipt = new BaseReceipt();
						baseReceipt.setBwmc(RPID);
						baseReceipt.setHzlx("错误回执");
						baseReceipt.setHzsj(dfs.parse(zhjs));
						baseReceipt.setHzState("02");
						baseReceipt.setHzmc(Hzmc);
						baseReceipt.setXgnr("已删除正确入库");
						if ("SDTK".equals(node3.getNodeName())) {
							NodeList childNodes2 = node3.getChildNodes();
							for (int k = 0; k < childNodes2.getLength(); k++) {
								Node node4 = childNodes2.item(k);
								if ("CSNM".equals(node4.getNodeName())) {
									baseReceipt.setOldcsnm(node4.getTextContent());
								}
								if ("HTDT".equals(node4.getNodeName())) {
									baseReceipt.setOldtstm(df.parse(node4.getTextContent()));
								}
								if ("CRCD".equals(node4.getNodeName())) {
									baseReceipt.setOldcrcd(node4.getTextContent());
								}
								if ("TICD".equals(node4.getNodeName())) {
									baseReceipt.setOldticd(node4.getTextContent());
								}
							}
						}
						list1.add(baseReceipt);
					}
				}
			}
			if ("UMTKs".equals(node1.getNodeName())) {
				Node node5 = childNode.item(i);
				NodeList childNodes3 = node5.getChildNodes();
				for (int q = 0; q < childNodes3.getLength(); q++) {
					Node node6 = childNodes3.item(q);
					if (!node6.getNodeName().equals("#text")) {
						BaseReceipt baseReceipt = new BaseReceipt();
						baseReceipt.setBwmc(RPID);
						baseReceipt.setHzlx("错误回执");
						baseReceipt.setHzsj(dfs.parse(zhjs));
						baseReceipt.setHzState("01");
						baseReceipt.setHzmc(Hzmc);
						baseReceipt.setXgnr("删除失败");
						if ("UMTK".equals(node6.getNodeName())) {
							NodeList childNodes4 = node6.getChildNodes();
							for (int k = 0; k < childNodes4.getLength(); k++) {
								Node node7 = childNodes4.item(k);
								if ("CSNM".equals(node7.getNodeName())) {
									baseReceipt.setOldcsnm(node7.getTextContent());
								}
								if ("HTDT".equals(node7.getNodeName())) {
									baseReceipt.setOldtstm(df.parse(node7.getTextContent()));
								}
								if ("CRCD".equals(node7.getNodeName())) {
									baseReceipt.setOldcrcd(node7.getTextContent());
								}
								if ("TICD".equals(node7.getNodeName())) {
									baseReceipt.setOldticd(node7.getTextContent());
								}
							}
						}
						list2.add(baseReceipt);
					}
				}
			}
		}
		BaseReceipt baseReceipt = null;
		Set<Long> ides1 = new HashSet<Long>();
		Set<Long> ides2 = new HashSet<Long>();
		for (int i = 0; i < list1.size(); i++) {
			baseReceipt = list1.get(i);
			ides1.add(bigAmountRepo.findByTicdAndRpdtAndCrcdAndDeleteStates(baseReceipt.getOldticd(),
					baseReceipt.getOldtstm(), baseReceipt.getOldcrcd(), "1"));
		}
		for (int i = 0; i < list2.size(); i++) {
			baseReceipt = list2.get(i);
			ides2.add(bigAmountRepo.findByTicdAndRpdtAndCrcdAndDeleteStates(baseReceipt.getOldticd(),
					baseReceipt.getOldtstm(), baseReceipt.getOldcrcd(), "1"));
		}
		if (ides1.size() > 0) {
			List<BigAmount> bigAmountes = bigAmountRepo.findByIdIn(ides1);
			for (int i = 0; i < bigAmountes.size(); i++) {
				BigAmount bigAmount = bigAmountes.get(i);
				baseReceipt.setOldId(bigAmount.getId());
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "ADD_STORAGE", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				list1.get(i).setOldId(bigAmount.getId());
				baseReceiptRepo.save(list1.get(i));
			}
		}
		if (ides2.size() > 0) {
			List<BigAmount> bigAmounts = bigAmountRepo.findByIdIn(ides2);
			for (int i = 0; i < bigAmounts.size(); i++) {
				BigAmount bigAmount = bigAmounts.get(i);
				if (bigAmount.getId() != null) {
					JobEvent jobEvent = new JobEvent(bigAmount.getJob().getJobId(), "REPORT_AGAIN", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				list2.get(i).setOldId(bigAmount.getId());
				baseReceiptRepo.save(list2.get(i));
			}
		}
		return null;
	}

	private List<BaseReceipt> supCorrectReceipt(MultipartFile mFile, String userName) throws Exception {
		String Hzmc = mFile.getOriginalFilename();
		DateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String zhjs = dfs.format(calendar.getTime());
		BaseReceipt baseReceipt = null;
		Element element = null;
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		// 返回documentBuilderFactory对象
		dbf = DocumentBuilderFactory.newInstance();
		// 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
		db = dbf.newDocumentBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(mFile.getInputStream(), "utf-8"));
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = br.readLine()) != null) {
			buffer.append(line);
		}
		InputStream is = new ByteArrayInputStream(buffer.toString().getBytes());
		Document dt = db.parse(is);
		element = dt.getDocumentElement();
		NodeList childNodes = element.getChildNodes();
		String RPID = null;
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node1 = childNodes.item(i);
			if ("RPID".equals(node1.getNodeName())) {
				RPID = node1.getTextContent();
			}
		}
		Set<Long> ides = amlSuspiciousRepo.findByTnameAndDeleteState(RPID, "1");
		if (ides.size() < 1) {
			return null;
		}
		if (ides.size() > 0) {
			List<AmlSuspicious> amlSuspicious = amlSuspiciousRepo.findByIdIn(ides);
			for (int i = 0; i < amlSuspicious.size(); i++) {
				AmlSuspicious amlSuspiciou = amlSuspicious.get(i);
				baseReceipt = new BaseReceipt();
				baseReceipt.setBwmc(RPID);
				baseReceipt.setHzlx("正确回执");
				baseReceipt.setXgnr("已正确入库");
				baseReceipt.setHzsj(dfs.parse(zhjs));
				baseReceipt.setHzState("02");
				baseReceipt.setHzmc(Hzmc);
				baseReceipt.setOldcrcd(amlSuspiciou.getStcr());
				baseReceipt.setOldcsnm(amlSuspiciou.getCsnm1());
				baseReceipt.setOldticd(amlSuspiciou.getTicd());
				baseReceipt.setOldtstm(amlSuspiciou.getTstm());
				baseReceipt.setOldId(amlSuspiciou.getId());
				if (amlSuspiciou.getId() != null) {
					JobEvent jobEvent = new JobEvent(amlSuspiciou.getJob().getJobId(), "ADD_STORAGE", userName);
					workflowMgr.sendEvents(Arrays.asList(jobEvent));
				}
				baseReceiptRepo.save(baseReceipt);
			}
		}
		return null;
	}
}
