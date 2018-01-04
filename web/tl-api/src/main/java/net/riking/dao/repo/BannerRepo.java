package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Banner;

@Repository
public interface BannerRepo extends JpaRepository<Banner, String>, JpaSpecificationExecutor<Banner> {
	
	@Query("select new net.riking.entity.model.Banner(bannerURL, relationURL) from Banner where isDeleted='1' and enabled='1' and enabled='1'")
	List<Banner> findByPage(Pageable pageable);

}
