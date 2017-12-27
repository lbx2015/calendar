package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Banner;

@Repository
public interface BannerRepo extends JpaRepository<Banner, String>, JpaSpecificationExecutor<Banner> {

}
