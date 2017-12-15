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

import net.riking.dao.AppUserDao;
import net.riking.entity.model.AppUserResult;

@Repository("appUserDao")
public class AppUserDaoImpl implements AppUserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<AppUserResult> findUserMightKnow(String userId, String userIds, int begin, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findUserMightKnow(?,?,?,?)";
		PreparedStatement pstmt = null;
		List<AppUserResult> list = new ArrayList<AppUserResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setString(2, userIds);
			pstmt.setInt(3, begin);
			pstmt.setInt(4, end);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserResult appUserResult = new AppUserResult();
				appUserResult.setId(rs.getString("userId"));
				appUserResult.setUserName(rs.getString("userName"));
				appUserResult.setPhotoUrl(rs.getString("photoUrl"));
				appUserResult.setExperience(rs.getInt("experience"));
				list.add(appUserResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageEnd) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call userFollowUser(?,?,?)";
		PreparedStatement pstmt = null;
		List<AppUserResult> list = new ArrayList<AppUserResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, pageBegin);
			pstmt.setInt(3, pageEnd);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserResult appUserResult = new AppUserResult();
				appUserResult.setId(rs.getString("userId"));
				appUserResult.setUserName(rs.getString("userName"));
				appUserResult.setExperience(rs.getInt("experience"));
				appUserResult.setPhotoUrl(rs.getString("photoUrl"));
				if (rs.getString("descript") == null) {
					appUserResult.setDescript("");
				} else {
					appUserResult.setDescript(rs.getString("descript"));
				}
				appUserResult.setAnswerNum(rs.getInt("qanswerNum"));
				appUserResult.setAgreeNum(rs.getInt("qaAgreeNum"));
				list.add(appUserResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageEnd) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findMyFans(?,?,?)";
		PreparedStatement pstmt = null;
		List<AppUserResult> list = new ArrayList<AppUserResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, pageBegin);
			pstmt.setInt(3, pageEnd);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserResult appUserResult = new AppUserResult();
				appUserResult.setId(rs.getString("userId"));
				appUserResult.setUserName(rs.getString("userName"));
				appUserResult.setExperience(rs.getInt("experience"));
				appUserResult.setPhotoUrl(rs.getString("photoUrl"));
				if (rs.getString("descript") == null) {
					appUserResult.setDescript("");
				} else {
					appUserResult.setDescript(rs.getString("descript"));
				}
				appUserResult.setAnswerNum(rs.getInt("qanswerNum"));
				appUserResult.setAgreeNum(rs.getInt("qaAgreeNum"));
				list.add(appUserResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

}
