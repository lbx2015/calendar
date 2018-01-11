package net.riking.dao.repo;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SysNoticeRead;

@Repository
public interface SysNoticeReadRepo extends JpaRepository<SysNoticeRead, String>, JpaSpecificationExecutor<SysNoticeRead> {
	
	
	/**
	 * 根据userId,noticeId找出唯一记录
	 * @param userId,noticeId
	 * @return
	 */
	@Query("from SysNoticeRead where userId = ?1 and noticeId = ?2 ")
	SysNoticeRead findByOne(String userId, String noticeId);
	
	/***
	 * 批量删除
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午5:42:36
	 * @used TODO
	 * @param userId
	 * @param noticeIds
	 */
	@Transactional
	@Modifying
	@Query("delete from SysNoticeRead where userId = ?1 and noticeId in ?2 ")
	void batchDelete(String userId, Collection<String> noticeIds);
	
}