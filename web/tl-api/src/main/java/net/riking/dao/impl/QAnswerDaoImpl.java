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

@Repository("QAnswerDao")
public class QAnswerDaoImpl implements QAnswerDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public String getAContentByOne(String questionId) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call getAContentByOne(?)";
		PreparedStatement pstmt = null;
		String qaContent = "";
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, questionId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				qaContent = rs.getString("content");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return qaContent;

	}

}
