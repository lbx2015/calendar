package net.riking.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.dao.ReportAgenceFrencyDao;
import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.ReportFrequency;

@Repository("reportAgenceFrencyDao")
public class ReportAgenceFrencyDaoImpl implements ReportAgenceFrencyDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Set<String> findALLAgence() {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT substring_index(t.VALU, '-',  1) agenceName FROM t_base_modelpropdict t WHERE t.TABLENAME='T_REPORT' AND t.FIELD = 'MODLE_TYPE' GROUP BY agenceName";
		PreparedStatement pstmt = null;
		Set<String> list = new HashSet<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<BaseModelPropdict> findAgenceNameList(String value) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.ID,t.KE,SUBSTRING_INDEX(t.VALU,'-',-2) as VALU FROM t_base_modelpropdict t WHERE t.VALU LIKE '%"
				+ value + "%'";
		PreparedStatement pstmt = null;
		List<BaseModelPropdict> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			// pstmt.setString(1, value);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				BaseModelPropdict baseModelPropdict = new BaseModelPropdict(rs.getString(1), rs.getString(2),
						rs.getString(3));
				list.add(baseModelPropdict);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ReportFrequency> findReportByModuleType(String moduleType) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.id,t.code,t.title,r.is_complete,group_concat(c.frequency ORDER BY c.frequency ASC ) AS strFrequency FROM t_report t LEFT JOIN t_base_modelpropdict m ON t.module_type = m.ke LEFT JOIN t_report_submit_caliber c ON t.id = c.report_id LEFT JOIN t_report_subscribe_rel r ON r.report_id=t.id WHERE t.module_type=? GROUP BY t.id";
		PreparedStatement pstmt = null;
		List<ReportFrequency> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, moduleType);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportFrequency reportFrequency = new ReportFrequency(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5));
				list.add(reportFrequency);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<ReportFrequency> findReportListByName(String reportName) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.Id,t.report_name,t.report_title,group_concat(c.frequency ORDER BY c.frequency ASC) AS strFrequency FROM t_report t LEFT JOIN t_report_submit_caliber c ON t.Id = c.report_id WHERE (t.report_name LIKE CONCAT('%','"
				+ reportName + "','%') OR t.report_title LIKE CONCAT('%','" + reportName + "','%')) GROUP BY t.id";
		PreparedStatement pstmt = null;
		List<ReportFrequency> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			// pstmt.setString(1, reportList.getReportName());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportFrequency report = new ReportFrequency(rs.getString(1), rs.getString(2), rs.getString(3), "",
						rs.getString(4));
				list.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
