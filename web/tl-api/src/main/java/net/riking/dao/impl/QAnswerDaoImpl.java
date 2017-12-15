package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.QAnswerDao;
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

}
