package net.riking.web.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.TopicQuestionRepo;
import net.riking.dao.repo.TopicRepo;
import net.riking.entity.VerifyParamModel;
import net.riking.entity.model.Topic;
import net.riking.service.AppUserService;
import net.riking.service.QuestionKeyWordService;
import net.riking.service.TopicService;

@RestController
@RequestMapping(value = "/topicController")
public class TopicController {

	@Autowired
	TopicRepo topicRepo;

	@Autowired
	AppUserService appUserService;

	@Autowired
	TopicService topicService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	QuestionKeyWordService questionKeyWordService;

	@Autowired
	TopicQuestionRepo topicQuestionRepo;

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		Topic topic = topicRepo.findOne(id);
		return new Resp(topic, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute Topic topic) {
		query.setSort("modifiedTime_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if (null == topic.getIsDeleted()) {
			topic.setIsDeleted(1);
		}
		// 截取资源访问路径
		// String projectPath = StringUtil.getProjectPath(request.getRequestURL().toString());
		Example<Topic> example = Example.of(topic, ExampleMatcher.matchingAll());
		Page<Topic> page = topicRepo.findAll(example, pageable);
		List<Topic> list = page.getContent();
		// List<Topic> listNew = new ArrayList<Topic>();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).settId(list.get(i).getId());
			list.get(i).setSerialNum(i + 1);
			// list.get(i).setTopicUrl(appUserService.getPhotoUrlPath(Const.TL_TOPIC_PHOTO_PATH)+
			// list.get(i).getTopicUrl());
			// listNew.add(list.get(i));
		}
		// Page<Topic> modulePage = new PageImpl<Topic>(listNew, pageable, page.getTotalElements());
		return new Resp(page, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "添加或者更新信息", notes = "POST")
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Resp save_(@RequestBody Topic topic) {
		if (StringUtils.isEmpty(topic.getId())) {
			topic.setIsDeleted(1);
			topic.setIsAduit(0);
		}
		topic.setIsAduit(0);
		// topic.setTopicUrl(topic.getContent().split("alt=")[1].split(">")[0].replace("\"", ""));
		// Pattern pattern = Pattern.compile("(?<=alt\\=\")(.+?)(?=\")");
		// Matcher matcher = pattern.matcher(topic.getContent());
		// while (matcher.find()) {
		// topic.setTopicUrl(matcher.group());
		// break;
		// }
		if (StringUtils.isBlank(topic.getTopicUrl())) {
			return new Resp(null, CodeDef.ERROR);
		}
		topicService.moveFile(topic);
		Topic save = topicRepo.save(topic);
		return new Resp(save, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "批量删除", notes = "POST")
	@RequestMapping(value = "/delMore", method = RequestMethod.POST)
	public Resp delMore_(@RequestBody Set<String> ids) {
		int rs = 0;
		if (ids.size() > 0) {
			rs = topicRepo.deleteById(ids);
		}
		if (rs > 0) {
			return new Resp().setCode(CodeDef.SUCCESS);
		} else {
			return new Resp().setCode(CodeDef.ERROR);
		}
	}

	@ApiOperation(value = "批量审核", notes = "POST")
	@RequestMapping(value = "/verifyMore", method = RequestMethod.POST)
	public Resp verifyMore_(@RequestBody VerifyParamModel verifyParamModel) {
		if (verifyParamModel.getIds() == null || verifyParamModel.getIds().size() < 1) {
			return new Resp("参数有误", CodeDef.ERROR);
		}
		int rs = 0;
		List<Topic> datas = topicRepo.findAll(verifyParamModel.getIds());
		// successCount表示删除成功的条数
		Integer successCount = 0;
		// failCount表示删除失败的条数
		Integer failCount = 0;
		for (Topic topic : datas) {
			// 已提交才可以进行审批
			if (Const.ADUIT_NO == topic.getIsAduit()) {
				switch (verifyParamModel.getEvent()) {
					case "VERIFY_NOT_PASS":
						// 如果审批不通过
						if (verifyParamModel.getIds().size() > 0) {
							rs = topicRepo.verifyNotPassById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					// 如果审批通过
					case "VERIFY_PASS":
						if (verifyParamModel.getIds().size() > 0) {
							rs = topicRepo.verifyById(verifyParamModel.getIds());
						}
						if (rs > 0) {
							successCount += 1;
						} else {
							failCount += 1;
						}
						break;
					default:
						failCount += 1;
						break;
				}
			} else {
				failCount += 1;
			}
		}
		// 如果数据只有一条且失败返回失败
		if (datas.size() == 1 && failCount == 1) {
			return new Resp("审批失败", CodeDef.ERROR);
		} else if (datas.size() == 1 && successCount == 1) {
			return new Resp("审批成功", CodeDef.SUCCESS);
		} else {
			return new Resp("操作成功!成功" + successCount + "条" + "失败" + failCount + "条", CodeDef.SUCCESS);
		}

	}
}
