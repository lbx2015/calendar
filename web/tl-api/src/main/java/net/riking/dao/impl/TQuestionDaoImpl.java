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
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.TQuestionDao;
import net.riking.entity.model.QAExcellentResp;
import net.riking.entity.model.TQuestionResult;

@Repository("tQuestionDao")
public class TQuestionDaoImpl implements TQuestionDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<TQuestionResult> findTopicHomeUp(String userId, Date reqTimeStamp, int start, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findTopicHomeUp(?,?,?,?)";
		PreparedStatement pstmt = null;
		List<TQuestionResult> list = new ArrayList<TQuestionResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			Timestamp timestamp = new Timestamp(reqTimeStamp.getTime());
			pstmt.setTimestamp(2, timestamp);
			pstmt.setInt(3, start);
			pstmt.setInt(4, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TQuestionResult tQuestionResult = new TQuestionResult();
				tQuestionResult.setTqId(rs.getString("tqId"));
				tQuestionResult.setTqTitle(rs.getString("tqTitle"));
				tQuestionResult.setCreatedTime(rs.getTimestamp("createdTime"));
				tQuestionResult.setTopicTitle(rs.getString("topicTitle"));
				tQuestionResult.setQaContent(rs.getString("qAContent"));
				tQuestionResult.setTfollowNum(rs.getInt("tFollowNum"));
				tQuestionResult.setUserId(rs.getString("userId"));
				tQuestionResult.setUserName(rs.getString("userName"));
				tQuestionResult.setFromImgUrl(rs.getString("fromImgUrl"));
				tQuestionResult.setExperience(rs.getInt("experience"));
				tQuestionResult.setQaCommentNum(rs.getInt("qaCommentNum"));
				tQuestionResult.setQaAgreeNum(rs.getInt("qaAgreeNum"));
				tQuestionResult.setQfollowNum(rs.getInt("qFollowNum"));
				tQuestionResult.setQanswerNum(rs.getInt("qAnswerNum"));
				tQuestionResult.setPushType(rs.getInt("pushType"));
				list.add(tQuestionResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<TQuestionResult> findTopicHomeDown(String userId, Date reqTimeStamp, int start, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findTopicHomeDown(?,?,?,?)";
		PreparedStatement pstmt = null;
		List<TQuestionResult> list = new ArrayList<TQuestionResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			Timestamp timestamp = new Timestamp(reqTimeStamp.getTime());
			pstmt.setTimestamp(2, timestamp);
			pstmt.setInt(3, start);
			pstmt.setInt(4, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TQuestionResult tQuestionResult = new TQuestionResult();
				tQuestionResult.setTqId(rs.getString("tqId"));
				tQuestionResult.setTqTitle(rs.getString("tqTitle"));
				tQuestionResult.setCreatedTime(rs.getTimestamp("createdTime"));
				tQuestionResult.setTopicTitle(rs.getString("topicTitle"));
				tQuestionResult.setQaContent(rs.getString("qAContent"));
				tQuestionResult.setTfollowNum(rs.getInt("tFollowNum"));
				tQuestionResult.setUserId(rs.getString("userId"));
				tQuestionResult.setUserName(rs.getString("userName"));
				tQuestionResult.setFromImgUrl(rs.getString("fromImgUrl"));
				tQuestionResult.setExperience(rs.getInt("experience"));
				tQuestionResult.setQaCommentNum(rs.getInt("qaCommentNum"));
				tQuestionResult.setQaAgreeNum(rs.getInt("qaAgreeNum"));
				tQuestionResult.setQfollowNum(rs.getInt("qFollowNum"));
				tQuestionResult.setQanswerNum(rs.getInt("qAnswerNum"));
				tQuestionResult.setPushType(rs.getInt("pushType"));
				list.add(tQuestionResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<TQuestionResult> findEssenceByTid(String topicId, int start, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findEssenceByTid(?,?,?)";
		PreparedStatement pstmt = null;
		List<TQuestionResult> list = new ArrayList<TQuestionResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(topicId))
				topicId = "";
			pstmt.setString(1, topicId);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				TQuestionResult tQuestionResult = new TQuestionResult();
				tQuestionResult.setTqId(rs.getString("tqId"));
				tQuestionResult.setTqTitle(rs.getString("tqTitle"));
				tQuestionResult.setCreatedTime(rs.getTimestamp("createdTime"));
				tQuestionResult.setQaContent(rs.getString("qAContent"));
				tQuestionResult.setQaId(rs.getString("qaId"));
				tQuestionResult.setUserName(rs.getString("userName"));
				tQuestionResult.setFromImgUrl(rs.getString("fromImgUrl"));
				tQuestionResult.setExperience(rs.getInt("experience"));
				list.add(tQuestionResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<QAExcellentResp> findExcellentResp(String topicId, int start, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findExcellentResp(?,?,?)";
		PreparedStatement pstmt = null;
		List<QAExcellentResp> list = new ArrayList<QAExcellentResp>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(topicId))
				topicId = "";
			pstmt.setString(1, topicId);
			pstmt.setInt(2, start);
			pstmt.setInt(3, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QAExcellentResp qaExcellentResp = new QAExcellentResp();
				qaExcellentResp.setUserId(rs.getString("userId"));
				qaExcellentResp.setUserName(rs.getString("userName"));
				qaExcellentResp.setPhotoUrl(rs.getString("photoUrl"));
				qaExcellentResp.setQanswerNum(rs.getInt("qanswerNum"));
				qaExcellentResp.setQaAgreeNum(rs.getString("qaAgreeNum"));
				qaExcellentResp.setExperience(rs.getInt("experience"));
				list.add(qaExcellentResp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}
}
