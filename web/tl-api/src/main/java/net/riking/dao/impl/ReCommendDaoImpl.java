package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.ReCommendDao;
import net.riking.entity.model.Recommend;

@Repository("reCommendDao")
public class ReCommendDaoImpl implements ReCommendDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Set<Recommend> findALL() {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.report_id,r.code,t.industry_id from t_recommend t left join t_report r on t.report_id=r.id";
		PreparedStatement pstmt = null;
		Set<Recommend> list = new HashSet<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Recommend appUserRecommend = new Recommend(rs.getString(1), rs.getString(2), rs.getLong(3));
				list.add(appUserRecommend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
