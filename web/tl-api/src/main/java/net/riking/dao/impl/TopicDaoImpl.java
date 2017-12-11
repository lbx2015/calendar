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

@Repository("topicDao")
public class TopicDaoImpl implements TopicDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<TopicResult> findTopicOfInterest(String userId, int begin, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findTopicOfInterest(?,?,?)";
		PreparedStatement pstmt = null;
		List<TopicResult> list = new ArrayList<TopicResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, begin);
			pstmt.setInt(3, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TopicResult topicResult = new TopicResult();
				topicResult.setId("id");
				topicResult.setTopicUrl(rs.getString("topicUrl"));
				topicResult.setTitle(rs.getString("title"));
				list.add(topicResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}
}
