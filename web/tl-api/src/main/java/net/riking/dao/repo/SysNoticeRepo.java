package net.riking.dao.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SysNotice;

@Repository
public interface SysNoticeRepo extends JpaRepository<SysNotice, String>, JpaSpecificationExecutor<SysNotice> {
	
	/**
	 * 根据userId,noticeId找出系统类型记录的id集合
	 * @param userId,noticeId
	 * @return
	 */
	@Query("select a.id from SysNotice a where a.noticeUserId = ?1 and a.dataType=0 ")
	List<String> findIdsBySysInfo(String userId);
	
	/**
	 * 根据userId,noticeId找出非系统类型记录的id集合
	 * @param userId,noticeId
	 * @return
	 */
	/*@Query("select a.id from SysNotice a where a.userId = ?1 and a.dataType<>0 and a.noticeId in ?2 ")
	List<String> findIdsByOtherSysNotice(String userId, String[] noticeId);*/
	
	/***
	 * 根据用户ID，批量删除
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午5:42:36
	 * @used TODO
	 * @param userId
	 * @param noticeIds
	 */
	@Transactional
	@Modifying
	@Query("delete from SysNotice where noticeUserId = ?1 and id in ?2 ")
	void batchDelete(String userId, String[] noticeIds);
	
}