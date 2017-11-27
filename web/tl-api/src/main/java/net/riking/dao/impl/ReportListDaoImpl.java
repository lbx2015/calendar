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

import net.riking.dao.ReportListDao;
import net.riking.entity.model.ReportSubcribeRel;
import net.riking.entity.model.ReportFrequency;

@Repository("reportListDao")
public class ReportListDaoImpl implements ReportListDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.report_id,r.report_name FROM t_appuser_report_rel t LEFT JOIN t_report_list r ON t.report_id=r.Id WHERE t.appUser_id=?";
		PreparedStatement pstmt = null;
		List<ReportFrequency> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportFrequency reportFrequency = new ReportFrequency(rs.getString(1), rs.getString(2), "","","");
				list.add(reportFrequency);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
