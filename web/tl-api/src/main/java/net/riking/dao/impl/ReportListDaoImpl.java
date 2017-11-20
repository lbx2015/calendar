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
import net.riking.entity.model.AppUserReportRel;

@Repository("reportListDao")
public class ReportListDaoImpl implements ReportListDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<AppUserReportRel> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT  t.appUser_id,t.report_id,t.is_complete,r.report_name FROM t_appuser_report_rel t LEFT JOIN t_report_list r ON t.report_id=r.Id WHERE t.appUser_id=?";
		PreparedStatement pstmt = null;
		List<AppUserReportRel> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserReportRel appUserReportRel = new AppUserReportRel(rs.getString(1), rs.getString(2), rs.getString(3),rs.getString(4));
				list.add(appUserReportRel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
