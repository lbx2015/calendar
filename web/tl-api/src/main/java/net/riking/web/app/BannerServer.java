package net.riking.web.app;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.dao.repo.BannerRepo;
import net.riking.entity.AppResp;
import net.riking.entity.model.Banner;

@RestController
@RequestMapping(value = "/banner")
public class BannerServer {
	
	@Autowired
	BannerRepo bannerRepo;
	
	@ApiOperation(value = "得到Banner信息", notes = "GET")
	@RequestMapping(value = "/getBanners", method = RequestMethod.GET)	
	public AppResp getBanners() {
		PageRequest pageable = new PageRequest(0, 5, new Sort(Arrays.asList(new Order(Direction.valueOf("DESC"), "modifiedTime"))));
		Banner banner = new Banner();
		banner.setIsDeleted(1);//未删除
		banner.setEnabled("1");//启用
		banner.setIsAduit("1");//审核通过
		Example<Banner> example = Example.of(banner, ExampleMatcher.matchingAll());
		Page<Banner> page = bannerRepo.findAll(example, pageable);
		page.getContent().forEach(e->{
			e.setId(null);
			e.setCreatedBy(null);
			e.setCreatedTime(null);
			e.setModifiedBy(null);
			e.setModifiedTime(null);
			e.setEnabled(null);
			e.setCreatedTime(null);
			e.setIsAduit(null);
			e.setIsDeleted(null);
		});
		return new AppResp(page.getContent(), CodeDef.SUCCESS);
	}
	

}
