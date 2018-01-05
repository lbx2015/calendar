package net.riking.web.controller;

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
import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.NCReplyRepo;
import net.riking.dao.repo.NewsCommentRepo;
import net.riking.dao.repo.NewsRepo;
import net.riking.entity.model.AppUser;
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
			AppUser appUser = appUserRepo.findOne(newsComment.getUserId());
			if (appUser != null) {
				newsComment.setCommentUserName(appUser.getUserName());
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
		// Example<NewsComment> example = Example.of(newsComment, ExampleMatcher.matchingAll());
		// Page<NewsComment> page = newsCommentRepo.findAll(example, pageable);
		// List<NewsComment> list = page.getContent();
		// for (NewsComment newsComment2 : list) {
		// // news1.setNid(news1.getId());
		// i++;
		// newsComment2.setSerialNumber(new Integer(i));
		// // 设置咨询标题
		// News news = newsRepo.findOne(newsComment2.getNewsId());
		// if (news != null) {
		// newsComment2.setTitle(news.getTitle());
		// }
		// AppUser appUser = appUserRepo.findOne(newsComment2.getUserId());
		// if (appUser != null) {
		// newsComment2.setCommentUserName(appUser.getUserName());
		// }
		// // 设置回复审核数
		// String isAduitNum = getIsAduitNum(newsComment2.getId());
		// newsComment2.setIsAduitNum(isAduitNum);
		// }
		// Page<NewsComment> modulePage = new PageImpl<NewsComment>(list, pageable,
		// page.getTotalElements());
		Page<NewsComment> modulePage = newsCommentService.findAll(newsComment, pageable);
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

	/**
	 * 获取回复评论审核数
	 * @return
	 */
	private String getIsAduitNum(String commentId) {
		// 获取未审核
		Integer num0 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(0));
		Integer num1 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(1));
		Integer num2 = ncReplyRepo.commentCountByNewsIdAndIsAduit(commentId, new Integer(2));
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(num0 + " / ");
		stringBuilder.append(num2 + " / ");
		stringBuilder.append(num1);
		return stringBuilder.toString();
	}

}
