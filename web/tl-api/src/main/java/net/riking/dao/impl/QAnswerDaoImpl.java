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

import net.riking.dao.QAnswerDao;
import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;
import net.riking.util.DBUtil;

@Repository("QAnswerDao")
public class QAnswerDaoImpl implements QAnswerDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public QuestionAnswer getAContentByOne(String questionId) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call getAContentByOne(?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		QuestionAnswer questionAnswer = new QuestionAnswer();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, questionId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				questionAnswer.setId(rs.getString("id"));
				questionAnswer.setContent(rs.getString("content"));
				questionAnswer.setCommentNum(rs.getInt("qaCommentNum"));
				questionAnswer.setAgreeNum(rs.getInt("qaAgreeNum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return questionAnswer;

	}

	@Override
	public List<QAnswerResult> findCollectQAnswer(String userId, int start, int pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findCollectQAnswer(?,?,?)";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QAnswerResult> qAnswerResults = new ArrayList<QAnswerResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, start);
			pstmt.setInt(3, pageCount);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QAnswerResult qAnswerResult = new QAnswerResult();
				qAnswerResult.setTqId(rs.getString("tqId"));
				qAnswerResult.setTitle(rs.getString("tqTitle"));
				qAnswerResult.setCreatedTime(rs.getTimestamp("createdTime"));
				qAnswerResult.setQaId(rs.getString("qaId"));
				qAnswerResult.setUserId(rs.getString("userId"));
				qAnswerResult.setCoverUrl(rs.getString("coverUrl"));
				qAnswerResult.setUserName(rs.getString("userName"));
				qAnswerResult.setPhotoUrl(rs.getString("photoUrl"));
				qAnswerResult.setExperience(rs.getInt("experience"));
				if (StringUtils.isBlank(rs.getString("isAgree"))) {
					qAnswerResult.setIsAgree(0);// 未点赞
				} else {
					qAnswerResult.setIsAgree(1);// 已点赞
				}
				qAnswerResults.add(qAnswerResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return qAnswerResults;

	}
	
	@Override
	public List<QAnswerResult> findQAByTopicIdAndUserId(String topicId,
			String userId, int start, int pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select qa.question_id tqId, t.title tqTitle, "//问题信息
				+ "qa.id qaId, qa.created_time createdTime, qa.cover_url coverUrl, qa.content content"//回答信息
				+" from t_question_answer qa LEFT JOIN t_topic_question t on qa.question_Id = t.id "
				+"where  t.topic_id =? and qa.user_id = ?"
				+"limit ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QAnswerResult> qAnswerResults = new ArrayList<QAnswerResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, topicId);
			pstmt.setString(2, userId);
			pstmt.setInt(3, start);
			pstmt.setInt(4, pageCount);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QAnswerResult qAnswerResult = new QAnswerResult();
				qAnswerResult.setTqId(rs.getString("tqId"));
				qAnswerResult.setTitle(rs.getString("tqTitle"));
				qAnswerResult.setQaId(rs.getString("qaId"));
				qAnswerResult.setCreatedTime(rs.getTimestamp("createdTime"));
				qAnswerResult.setCoverUrl(rs.getString("coverUrl"));
				qAnswerResult.setContent(rs.getString("content"));
				qAnswerResults.add(qAnswerResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return qAnswerResults;
	}

	@Override
	public Long findQACountByTopicIdAndUserId(String topicId, String userId) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select COUNT(qa.id) num"//回答信息
				+" from t_question_answer qa LEFT JOIN t_topic_question t on qa.question_Id = t.id "
				+"where  t.topic_id =? and qa.user_id = ?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, topicId);
			pstmt.setString(2, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				return rs.getLong("num");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return 0L;
	}

	@Override
	public List<QAnswerResult> findQAResultByKeyWord(String keyWord) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select tqId, tqTitle, a1.qaId, createdTime, coverUrl, content, qaAgreeNum, qaCommentNum from "
				
				+" (select qa.question_id tqId, t.title tqTitle,"
				+" qa.id qaId, qa.created_time createdTime, qa.cover_url coverUrl, qa.content content"
				+" from t_question_answer qa "
				+" LEFT JOIN t_topic_question t on qa.question_Id = t.id and t.title LIKE ?"
				+" where qa.created_time in("
						+" select max(qa2.created_time) maxTime"
						+" from t_question_answer qa2"
						+" LEFT JOIN t_topic_question t2 on qa2.question_Id = t2.id and t2.title LIKE ?" 
						+" GROUP BY qa2.question_id  )) as a1"
						
				+" LEFT JOIN"
				+" (select qar.qa_id qaId, count(qar.qa_id) qaAgreeNum  from t_qa_rel qar where qar.data_type =1 GROUP BY qar.qa_id) "
				+" as b1 ON a1.qaId = b1.qaId"
				
				+" LEFT JOIN"
				+" (select qac.question_answer_id qaId, count(qac.question_answer_id) qaCommentNum from t_qa_comment qac GROUP BY qac.question_answer_id) "
				+" as c1 ON a1.qaId = c1.qaId";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QAnswerResult> qAnswerResults = new ArrayList<QAnswerResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, "%"+keyWord+"%");
			pstmt.setString(2, "%"+keyWord+"%");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QAnswerResult qAnswerResult = new QAnswerResult();
				qAnswerResult.setTqId(rs.getString("tqId"));
				qAnswerResult.setTitle(rs.getString("tqTitle"));
				qAnswerResult.setQaId(rs.getString("qaId"));
				qAnswerResult.setCreatedTime(rs.getTimestamp("createdTime"));
				qAnswerResult.setCoverUrl(rs.getString("coverUrl"));
				qAnswerResult.setContent(rs.getString("content"));
				qAnswerResult.setQaAgreeNum(rs.getInt("qaAgreeNum"));
				qAnswerResult.setQaCommentNum(rs.getInt("qaCommentNum"));
				qAnswerResults.add(qAnswerResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return qAnswerResults;
	}

}
