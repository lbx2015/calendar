package net.riking.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.model.Banner;

public interface BannerService {

	/********************* WEB ***************/

	Page<Banner> findAllToPage(Banner banner, PageRequest pageable);

	public void moveFile(Banner banner);

	/******************** WEB END ***********/
}
