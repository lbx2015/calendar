package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.AppUserDao;
import net.riking.entity.model.AppUserDetail;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.UserFollowCollect;
import net.riking.entity.resp.OtherUserResp;

@Repository("appUserDao")
public class AppUserDaoImpl implements AppUserDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	@Transactional
	public List<AppUserDetail> findPhoneDeviceByBirthDay(String brithDay) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT a.user_name userName, b.phone_device_id phoneDeviceId FROM t_app_user a ";
		sql += "left join t_appuser_detail b on a.id = b.id ";
		sql += "WHERE a.is_deleted = 1 and a.enabled = 1 and substring(b.birthday, 5, 4) = ? ";
		sql += "and b.phone_device_id <> '' ";
		PreparedStatement pstmt = null;
		List<AppUserDetail> list = new ArrayList<AppUserDetail>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, brithDay);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserDetail data = new AppUserDetail();
				data.setUserName(rs.getString("userName"));
				data.setPhoneDeviceId(rs.getString("phoneDeviceId"));
				list.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

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
	public List<AppUserResult> userFollowUser(String userId, Integer pageBegin, Integer pageCount) {
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
			pstmt.setInt(3, pageCount);
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
				if (rs.getInt("followStatus") == 0) {
					appUserResult.setIsFollow(0);// 未关注
				} else if (rs.getInt("followStatus") == 1) {
					appUserResult.setIsFollow(1);// 已关注
				} else if (rs.getInt("followStatus") == 2) {
					appUserResult.setIsFollow(2);// 互相关注
				}
				list.add(appUserResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<AppUserResult> findMyFans(String userId, Integer pageBegin, Integer pageCount) {
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
			pstmt.setInt(3, pageCount);
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
				if (rs.getInt("followStatus") == 0) {
					appUserResult.setIsFollow(0);// 未关注
				} else if (rs.getInt("followStatus") == 1) {
					appUserResult.setIsFollow(1);// 已关注
				} else if (rs.getInt("followStatus") == 2) {
					appUserResult.setIsFollow(2);// 互相关注
				}
				list.add(appUserResult);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public OtherUserResp getOtherMes(String toUserId, String userId) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT ";
		sql += "a.id,a.user_name,ap.sex,ap.descript,ap.experience,ap.photo_Url,";
		sql += "(select t.follow_status from t_user_follow_rel t where t.user_id = '" + userId
				+ "' and t.to_user_Id = a.id ) followStatus ";
		sql += "FROM t_app_user a ";
		sql += "LEFT JOIN t_appuser_detail ap on a.id=ap.id ";
		sql += "WHERE a.id = '" + toUserId + "' and a.enabled = 1 and a.is_deleted = 1";
		PreparedStatement pstmt = null;
		OtherUserResp otherUserResp = null;
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				otherUserResp = new OtherUserResp(rs.getString("id"), rs.getString("user_name"), rs.getInt("sex"),
						rs.getString("descript"), rs.getInt("experience"), rs.getString("photo_Url"),
						rs.getInt("followStatus"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return otherUserResp;

	}

	@Override
	public List<UserFollowCollect> findByFolColByUserId(String userId, String userName, Integer pindex,
			Integer pcount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findByFolColByUserId(?,?,?,?)";
		PreparedStatement pstmt = null;
		List<UserFollowCollect> list = new ArrayList<UserFollowCollect>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId)) {
				userId = "";
			}
			if (StringUtils.isBlank(userName)) {
				userName = "";
			}
			pstmt.setString(1, userId);
			pstmt.setString(2, userName);
			pstmt.setInt(3, pindex);
			pstmt.setInt(4, pcount);
			ResultSet rs = pstmt.executeQuery();
			int i = 1;
			while (rs.next()) {
				UserFollowCollect userFollowCollect = new UserFollowCollect();
				userFollowCollect.setSerialNum(i);
				userFollowCollect.setUserName(rs.getString("userName") + "/" + rs.getString("phone"));
				userFollowCollect.setUserId(rs.getString("userId"));
				if (rs.getString("toUserName") != null && rs.getString("toUserPhone") != null) {
					userFollowCollect.setToUserName(rs.getString("toUserName") + "/" + rs.getString("toUserPhone"));
				}
				userFollowCollect.setToUserId(rs.getString("toUserId"));
				userFollowCollect.setTitle(rs.getString("title"));
				userFollowCollect.setOptObject(rs.getInt("optObject"));
				userFollowCollect.setOptType(rs.getInt("optType"));
				userFollowCollect.setCreatedTime(rs.getTimestamp("createdTime"));
				list.add(userFollowCollect);
				i++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public Integer countByFolColByUserId(String userId, String userName) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call countFolColByUserId(?,?)";
		PreparedStatement pstmt = null;
		Integer count = null;
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId)) {
				userId = "";
			}
			if (StringUtils.isBlank(userName)) {
				userName = "";
			}
			pstmt.setString(1, userId);
			pstmt.setString(2, userName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;

	}

}
