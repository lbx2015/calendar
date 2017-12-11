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

import net.riking.dao.ReportDao;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportResult;

@Repository("reportDao")
public class ReportDaoImpl implements ReportDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.report_id,r.report_name FROM t_appuser_report_rel t LEFT JOIN t_report_list r ON t.report_id=r.Id WHERE t.appUser_id=?";
		PreparedStatement pstmt = null;
		List<ReportFrequency> list = new ArrayList<ReportFrequency>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportFrequency reportFrequency = new ReportFrequency(rs.getString(1), rs.getString(2), "", "", "");
				list.add(reportFrequency);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ReportResult> getAllReportByParams(String param) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call getAllReport(?)";
		PreparedStatement pstmt = null;
		List<ReportResult> list = new ArrayList<ReportResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(param))
				param = "";
			pstmt.setString(1, param);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportResult report = new ReportResult();
				report.setAgenceCode(rs.getString("agenceCode"));
				report.setReportType(rs.getString("reportType"));
				report.setReportMode(rs.getString("reportMode"));
				report.setReportId(rs.getString("reportId"));
				report.setCode(rs.getString("code"));
				report.setTitle(rs.getString("title"));
				report.setIsSubscribe(rs.getString("isSubcribe"));
				list.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

}
