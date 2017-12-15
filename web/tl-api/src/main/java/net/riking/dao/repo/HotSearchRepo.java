package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.HotSearch;

@Repository
public interface HotSearchRepo extends JpaRepository<HotSearch, String>, JpaSpecificationExecutor<HotSearch> {
	/**
	 * 热门搜索
	 * @param pageable
	 * @return
	 */
	@Query("from HotSearch order by searchCount desc")
	List<HotSearch> findHotSearch(Pageable pageable);
}
