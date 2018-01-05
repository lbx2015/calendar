package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.SysNoticeDao;
import net.riking.entity.model.SysNoticeResult;

@Repository("sysNoticeDao")
public class SysNoticeDaoImpl implements SysNoticeDao {
	Logger logger = LogManager.getLogger(getClass());

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<SysNoticeResult> findSysNoticeResult(String userId) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT a.id noticeId, a.title, a.content, a.data_type dataType, ";
		sql += "CASE WHEN EXISTS ( SELECT t.user_id FROM t_sys_notice_read t WHERE t.notice_id = a.id AND t.user_id = ? ) THEN 1 END AS isRead, ";
		sql += "a.created_time createdTime FROM t_sys_notice a WHERE a.data_type=0 and ";
		sql += "EXISTS ( SELECT t.user_id FROM t_sys_notice_read t WHERE t.notice_id = a.id and t.is_deleted = 1 AND t.user_id = ? ) ";
		sql += "order by a.created_time desc ";
		PreparedStatement pstmt = null;
		List<SysNoticeResult> list = new ArrayList<SysNoticeResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setString(2, userId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SysNoticeResult data = new SysNoticeResult();
				data.setNoticeId(rs.getString("noticeId"));
				data.setTitle(rs.getString("title"));
				data.setContent(rs.getString("content"));
				data.setDataType(rs.getInt("dataType"));
				data.setIsRead(rs.getInt("isRead"));
				data.setCreatedTime(rs.getDate("createdTime"));
				list.add(data);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (pstmt != null || !pstmt.isClosed()) {
					pstmt.close();
					pstmt = null;
				}

				if (connection != null || !connection.isClosed()) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			}

		}
		return list;

	}

	@Override
	public List<SysNoticeResult> findUserNoticeResult(String userId, Date reqTimeStamp) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT a.id noticeId, a.title, a.content, a.data_type dataType, ";
		sql += "(SELECT t.user_name FROM t_appuser_detail t WHERE t.id = a.from_user_id ) fromUserName, ";
		sql += "(SELECT t.photo_url FROM t_appuser_detail t WHERE t.id = a.from_user_id ) userPhotoUrl, a.obj_id objId, ";
		sql += "CASE WHEN EXISTS (SELECT t.user_id FROM t_sys_notice_read t WHERE t.notice_id = a.id AND t.user_id = a.notice_user_id) THEN 1 ELSE 0 END AS isRead, ";
		sql += "a.created_time createdTime FROM t_sys_notice a ";
		sql += "WHERE a.data_type<>0 and a.notice_user_id = ? ";
		if (reqTimeStamp != null) {
			Timestamp timestamp = new Timestamp(reqTimeStamp.getTime());
			sql += "and '" + timestamp + "' > a.created_time ";
		}
		sql += "order by a.created_time desc limit 0, 30";
		PreparedStatement pstmt = null;
		List<SysNoticeResult> list = new ArrayList<SysNoticeResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				SysNoticeResult data = new SysNoticeResult();
				data.setNoticeId(rs.getString("noticeId"));
				data.setTitle(rs.getString("title"));
				data.setContent(rs.getString("content"));
				data.setDataType(rs.getInt("dataType"));
				data.setFromUserName(rs.getString("fromUserName"));
				data.setUserPhotoUrl(rs.getString("userPhotoUrl"));
				data.setObjId(rs.getString("objId"));
				data.setIsRead(rs.getInt("isRead"));
				data.setCreatedTime(rs.getDate("createdTime"));
				data.setReqTimeStamp(rs.getTimestamp("createdTime"));
				list.add(data);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			try {
				if (pstmt != null || !pstmt.isClosed()) {
					pstmt.close();
					pstmt = null;
				}

				if (connection != null || !connection.isClosed()) {
					connection.close();
					connection = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(e);
			}

		}
		return list;

	}

}
