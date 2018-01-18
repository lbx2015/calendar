package net.riking.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.core.entity.PageQuery;
import net.riking.core.entity.Resp;
import net.riking.dao.repo.AppUserDetailRepo;
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.News;
import net.riking.entity.model.NewsComment;
import net.riking.service.NewsCommentService;
import net.riking.service.NewsService;

@RestController
@RequestMapping(value = "/newsComment")
public class NewsCommentController {

	@Autowired
	NewsRepo newsRepo;

	@Autowired
	NewsService newsService;

	@Autowired
	NewsCommentRepo newsCommentRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	NCReplyRepo ncReplyRepo;

	@Autowired
	NewsCommentService newsCommentService;

	@Autowired
	AppUserDetailRepo appUserDetailRepo;

	@ApiOperation(value = "得到单个信息", notes = "GET")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Resp get_(@RequestParam("id") String id) {
		NewsComment newsComment = newsCommentRepo.findOne(id);
		if (newsComment != null) {
			// 设置咨询标题
			News news = newsRepo.findOne(newsComment.getNewsId());
			if (news != null) {
				newsComment.setTitle(news.getTitle());
			}
			AppUserDetail appUserDetail = appUserDetailRepo.findOne(newsComment.getUserId());
			if (appUserDetail != null) {
				newsComment.setCommentUserName(appUserDetail.getUserName());
			}
		}
		return new Resp(newsComment, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "得到信息", notes = "GET")
	@RequestMapping(value = "/getMore", method = RequestMethod.GET)
	public Resp getMore_(@ModelAttribute PageQuery query, @ModelAttribute NewsComment newsComment) {
		query.setSort("modifiedTime_desc");
		PageRequest pageable = new PageRequest(query.getPindex(), query.getPcount(), query.getSortObj());
		if (null == newsComment.getIsDeleted()) {
			newsComment.setIsDeleted(1);
		}
		int i = query.getPindex() * query.getPcount();
		Page<NewsComment> modulePage = newsCommentService.findAll(newsComment, pageable);
		List<NewsComment> list = modulePage.getContent();
		for (NewsComment newsComment2 : list) {
			i++;
			newsComment2.setSerialNumber(i);
		}
		return new Resp(modulePage, CodeDef.SUCCESS);
	}

	@ApiOperation(value = "审核回答信息", notes = "GET")
	@RequestMapping(value = "/answerIsAduit", method = RequestMethod.GET)
	public Resp enable_(@RequestParam String id, @RequestParam String isAduit) {
		NewsComment newsComment = newsCommentRepo.findOne(id);
		newsComment.setIsAduit(new Integer(isAduit));
		newsCommentRepo.save(newsComment);
		return new Resp(newsComment, CodeDef.SUCCESS);
	}

}
