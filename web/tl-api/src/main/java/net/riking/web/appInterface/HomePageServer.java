package net.riking.web.appInterface;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.entity.model.TopicFollow;
import net.riking.entity.model.TopicQuestionResult;
import net.riking.service.HomePageService;
import net.riking.service.repo.ScreenRepo;
import net.riking.service.repo.TopicFollowRepo;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月18日 上午11:40:40
 * @used 首页接口
 */
@RestController
@RequestMapping(value = "/homePageServer")
public class HomePageServer {
	
	@Autowired
	ScreenRepo screenRepo;
	
	@Autowired
	TopicFollowRepo topicFollowRepo;
	
	@Autowired
	HomePageService homePageService;
	
	@ApiOperation(value = "显示首页数据", notes = "POST")
	@RequestMapping(value = "/findHomePageData", method = RequestMethod.POST)
	public AppResp findHomePageData(@RequestBody AppUser appUser){
		//判断用户id是否为空
		if(StringUtils.isNotBlank(appUser.getId())){//为空 则是未登录 则首页数据显示的是热点数据
			
		}else{//查询用户屏蔽的话题id，查询用户是否关注了话题、问题、人脉
			 
		}
		return new AppResp( CodeDef.SUCCESS);
	}

	//查询用户关注话题下的问题显示  和 关注的话题下的问题数量不足 凑数
	public List<TopicQuestionResult> findUserScreenById(AppUser appUser){
		String userId = appUser.getId();
		String page = appUser.getPage();//页数
		String sTime = appUser.getsTime();
		//用户在屏蔽话题的时候，先查询用户是否关注了这个话题，如果关注了这个话题则先在用户关注表中删除这条数据再去屏蔽表新增数据；
		//这里关注和屏蔽的关系 则先查询出用户关注的话题，关注的话题中选取几个热点问题；非关注的话题取几个热点问题（这边需要跟屏蔽表进行关联，屏蔽的话题下的问题不显示）
		
		//用户关注的话题下的问题(屏蔽的问题不显示)
		List<TopicQuestionResult> resultList = homePageService.findTopicQuestionByUserId(userId, page,"3",sTime);//写死 三条数据
		if(resultList.size() < 3 ){//如果关注的话题下能查询出大于3个的话题 则传给移动端；如果查询的问题数不足3个 则从热点里面去取
			//这里还要计算 比如：每一页显示5条数据，但是只能从上面查询出的中拿出3条关注话题下的问题，剩下的2条应该是未关注的话题下的问题和未屏蔽的话题下的问题
			
			//查询用户屏蔽的问题
			//Set<String> screenList = screenRepo.findByUserId(userId);
			 
			//用户关注的话题id 集合
			//Set<String> followList = topicFollowRepo.findByUserId(userId);
			
			//screenList.contains(followList);//当关注的话题下的问题不够数的时候  凑数；查询的条件是：不能查出用户屏蔽的话题 和 用户关注过的话题    因为用户关注过的之前都已经查询出来了
			//String strList = String.join(",", screenList);
			
			List<TopicQuestionResult> list= homePageService.findNotTopicQuestion(userId, page,"3",sTime);//查询屏蔽之外的话题下的问题（根据时间查询，点赞条件忽略）
			resultList.contains(list);//关注的话题下的问题和
		}
		
		return resultList;
	}
	
	//查询关注的好友赞的回答
	public void findUserFollowQuestion(AppUser appUser){
		String userId = appUser.getId();
		String page = appUser.getPage();//页数
		String sTime = appUser.getsTime();
		
//		SELECT
//		p.id,
//		p.user_id,
//	  q.question_name,
//	  a.answer_content,
//	  a.sum_comments,
//		 u.`name`,
//	  a.sum_praise
//	FROM
//		t_answer_praise p
//	LEFT JOIN t_appuser_follow f ON f.to_user_id = p.user_id
//	LEFT JOIN t_app_user u ON p.user_id = u.Id
//	LEFT JOIN t_question q ON q.Id = p.question_id
//	LEFT JOIN t_question_answer a ON q.id=a.question_id
//	WHERE
//		f.user_id = '' ORDER BY p.create_time ASC
		
		//查询关注的好友的点赞的回答 
		
		//1.查询关注的好友 
		
		//2.回答点赞表查询关注的好友点赞的回答，根据点赞时间取最新的（后面如果有别的好友点赞了回答，则从第二页开始根据移动端那边传过来的时间进行条件查询获取第几页的数据）
		
	}
	
	//查询用户关注的好友 他 关注的问题
	public void findUserFollowTopicQuestion(){
		//
		
	}
	
}
