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
	public List<AppUserResult> findUserMightKnow(String userId, int begin, int end) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findUserMightKnow(?,?,?)";
		PreparedStatement pstmt = null;
		List<AppUserResult> list = new ArrayList<AppUserResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, begin);
			pstmt.setInt(3, end);
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

}
