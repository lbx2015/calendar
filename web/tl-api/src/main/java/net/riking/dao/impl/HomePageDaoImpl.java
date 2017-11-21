package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;
import net.riking.dao.HomePageDao;
import net.riking.entity.model.TopicQuestionResult;

@Repository("homePageDao")
public class HomePageDaoImpl implements HomePageDao {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<TopicQuestionResult> findTopicQuestionByUserId(String userId, String page,String pageCount,String sTime) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "";
		if(StringUtils.isNotBlank(page) && "1".equals(page)){//第一页 则拉取最新的
			sql = "SELECT q.Id,t.topic_id,q.question_name,c.topic_name,a.answer_content,a.sum_comments,a.sum_praise,q.question_photo FROM t_topic_follow t LEFT JOIN t_question q ON t.topic_id = q.topic_id LEFT JOIN t_topic c ON c.id=t.topic_id LEFT JOIN t_question_answer a ON a.question_id=q.Id LEFT JOIN t_screen s ON s.question_id=q.Id WHERE t.user_id=? AND s.question_id IS NULL ORDER BY q.create_time ASC LIMIT ?,?";
		}else{
			sql = "SELECT q.Id,t.topic_id,q.question_name,c.topic_name,a.answer_content,a.sum_comments,a.sum_praise,q.question_photo FROM t_topic_follow t LEFT JOIN t_question q ON t.topic_id = q.topic_id LEFT JOIN t_topic c ON c.id=t.topic_id LEFT JOIN t_question_answer a ON a.question_id=q.Id LEFT JOIN t_screen s ON s.question_id=q.Id WHERE t.user_id=? AND s.question_id IS NULL AND q.create_time<='"+sTime+"' ORDER BY q.create_time ASC LIMIT ?,?";
		}
		PreparedStatement pstmt = null;
		List<TopicQuestionResult> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, Integer.parseInt(page));
			pstmt.setInt(3, Integer.parseInt(pageCount));//每页查询三条关注下的问题
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TopicQuestionResult topicQuestionResult = new TopicQuestionResult(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
				list.add(topicQuestionResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<TopicQuestionResult> findNotTopicQuestion(String userId, String page,String pageCount,String sTime) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "";
		if(StringUtils.isNotBlank(page) && "1".equals(page)){//第一页 则拉取最新的
			sql = "SELECT q.Id,t.topic_id,q.question_name,c.topic_name,a.answer_content,a.sum_comments,a.sum_praise,q.question_photo FROM t_topic_follow t LEFT JOIN t_question q ON t.topic_id = q.topic_id LEFT JOIN t_topic c ON c.id=t.topic_id LEFT JOIN t_question_answer a ON a.question_id=q.Id WHERE t.user_id  <>'' AND s.question_id IS NULL ORDER BY q.create_time ASC LIMIT ?,?";
		}else{
			sql = "SELECT q.Id,t.topic_id,q.question_name,c.topic_name,a.answer_content,a.sum_comments,a.sum_praise,q.question_photo FROM t_topic_follow t LEFT JOIN t_question q ON t.topic_id = q.topic_id LEFT JOIN t_topic c ON c.id=t.topic_id LEFT JOIN t_question_answer a ON a.question_id=q.Id WHERE q.create_time<='"+sTime+"' AND t.user_id  <>? AND s.question_id IS NULL  ORDER BY q.create_time ASC LIMIT ?,?";
		}
		PreparedStatement pstmt = null;
		List<TopicQuestionResult> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, Integer.parseInt(page));
			pstmt.setInt(3, Integer.parseInt(pageCount));//每页查询三条关注下的问题
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TopicQuestionResult topicQuestionResult = new TopicQuestionResult(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getString(8));
				list.add(topicQuestionResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
