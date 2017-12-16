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
		QuestionAnswer questionAnswer = new QuestionAnswer();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, questionId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				questionAnswer.setId(rs.getString("id"));
				questionAnswer.setContent(rs.getString("content"));
				questionAnswer.setCommentNum(rs.getInt("qaCommentNum"));
				questionAnswer.setAgreeNum(rs.getInt("qaAgreeNum"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return questionAnswer;

	}

	@Override
	public List<QAnswerResult> findCollectQAnswer(String userId, int start, int pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findCollectQAnswer(?,?,?)";
		PreparedStatement pstmt = null;
		List<QAnswerResult> qAnswerResults = new ArrayList<QAnswerResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, start);
			pstmt.setInt(3, pageCount);
			ResultSet rs = pstmt.executeQuery();
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
		}
		return qAnswerResults;

	}

}
