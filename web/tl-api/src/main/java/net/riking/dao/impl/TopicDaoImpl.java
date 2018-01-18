package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.TopicDao;
import net.riking.entity.model.TopicResult;
import net.riking.util.DBUtil;

@Repository("topicDao")
public class TopicDaoImpl implements TopicDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<TopicResult> findTopicOfInterest(String userId, String topicIds, int begin, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findTopicOfInterest(?,?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TopicResult> list = new ArrayList<TopicResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setString(2, topicIds);
			pstmt.setInt(3, begin);
			pstmt.setInt(4, end);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TopicResult topicResult = new TopicResult();
				topicResult.setId(rs.getString("id"));
				topicResult.setTopicUrl(rs.getString("topicUrl"));
				topicResult.setTitle(rs.getString("title"));
				list.add(topicResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return list;

	}

	@Override
	public List<TopicResult> userFollowTopic(String userId, int begin, int pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call userFollowTopic(?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TopicResult> list = new ArrayList<TopicResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, begin);
			pstmt.setInt(3, pageCount);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TopicResult topicResult = new TopicResult();
				topicResult.setId("topicId");
				topicResult.setTopicUrl(rs.getString("topicUrl"));
				topicResult.setTitle(rs.getString("title"));
				topicResult.setFollowNum(rs.getInt("followNum"));
				topicResult.setIsFollow(1);// 已关注
				list.add(topicResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return list;

	}
}
