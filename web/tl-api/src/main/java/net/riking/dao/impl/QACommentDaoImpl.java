package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.QACommentDao;
import net.riking.entity.model.QACommentResult;
import net.riking.util.DBUtil;

@Repository("qaCommentDao")
public class QACommentDaoImpl implements QACommentDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<QACommentResult> findByUserId(String userId, Integer pageBegin, Integer pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select q.id,q.created_time createdTime,q.modified_time modifiedTime,q.user_id userId,q.question_answer_id qaId,q.content, ";
		sql += "(select qa.question_id from t_question_answer qa where qa.id = q.question_answer_id) questionId,";
		sql += "(select tq.title from t_question_answer qa left join t_topic_question tq on tq.id = qa.question_id where qa.id = q.question_answer_id) title, ";
		sql += "(select qa.content from t_question_answer qa where qa.id = q.question_answer_id) qaContent, ";
		sql += "(select a.user_name from t_app_user a where q.user_id = a.id and a.is_deleted=1) userName, ";
		sql += "(select app.photo_url from t_appuser_detail app where q.user_id = app.id) photoUrl, ";
		sql += "(select app.experience from t_appuser_detail app where q.user_id = app.id) experience ";
		sql += "from t_qa_comment q ";
		sql += "where q.user_id = ? and q.is_aduit <> 2 and q.is_deleted=1 ORDER BY q.created_time desc limit ?,?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<QACommentResult> list = new ArrayList<QACommentResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, pageBegin);
			pstmt.setInt(3, pageCount);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QACommentResult qaCommentResult = new QACommentResult();
				qaCommentResult.setId(rs.getString("id"));
				qaCommentResult.setCreatedTime(rs.getTimestamp("createdTime"));
				qaCommentResult.setModifiedTime(rs.getTimestamp("modifiedTime"));
				qaCommentResult.setUserId(rs.getString("userId"));
				qaCommentResult.setQuestionAnswerId(rs.getString("qaId"));
				qaCommentResult.setContent(rs.getString("content"));
				qaCommentResult.setTqId(rs.getString("questionId"));
				qaCommentResult.setTqTitle(rs.getString("title"));
				qaCommentResult.setQaContent(rs.getString("qaContent"));
				qaCommentResult.setUserName(rs.getString("userName"));
				qaCommentResult.setPhotoUrl(rs.getString("photoUrl"));
				qaCommentResult.setExperience(rs.getInt("experience"));

				list.add(qaCommentResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return list;
	}

}
