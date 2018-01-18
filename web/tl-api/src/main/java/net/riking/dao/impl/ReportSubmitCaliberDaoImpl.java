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

import net.riking.dao.ReportSubmitCaliberDao;
import net.riking.entity.model.QueryReport;
import net.riking.util.DBUtil;

@Repository("reportSubmitCaliberDao")
public class ReportSubmitCaliberDaoImpl implements ReportSubmitCaliberDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Set<QueryReport> findAllByFreeDatefromReportId(String userId, Integer week, Integer ten, Integer month,
			Integer season, Integer halfYear, Integer Year, Integer isWorkDay) {

		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT c.Id, c.report_name,c.report_code,c.module_type,b.caliber_type,b.frequency from t_appuser_report_rel a LEFT JOIN t_report_submit_caliber b ON a.report_id = b.report_id LEFT JOIN t_report_list c ON a.report_id = c.Id WHERE a.appUser_id=? AND ((b.frequency='1') or (b.frequency='2' and b.delay_dates>=?) or (b.frequency='3' and  b.delay_dates>=?) or (b.frequency='4' and b.delay_dates>=?) or(b.frequency='5' and b.delay_dates>=?) or (b.frequency='6' and b.delay_dates>=?) or (b.frequency='7' and b.delay_dates>=?)) and b.is_work_day=? and b.enabled = 1 and c.delete_state = '1' order by b.frequency";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Set<QueryReport> list = new HashSet<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, week);
			pstmt.setInt(3, ten);
			pstmt.setInt(4, month);
			pstmt.setInt(5, season);
			pstmt.setInt(6, halfYear);
			pstmt.setInt(7, Year);
			pstmt.setInt(8, isWorkDay);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6));
				list.add(queryReport);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return list;
	}

	@Override
	public Set<QueryReport> findAllfromReportId() {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT c.Id, c.report_name,c.report_code,c.module_type,b.caliber_type,b.frequency from  t_report_submit_caliber b  LEFT JOIN t_report_list c ON b.report_id = c.Id WHERE b.enabled = 1 AND c.delete_state = '1' ORDER BY b.frequency";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Set<QueryReport> list = new HashSet<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6));
				list.add(queryReport);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, rs);
		}
		return list;
	}

	@Override
	public int updateDelayDateAfter(String type, String remarks) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = null;
		if ("May".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates+1  WHERE remarks = ?";
		} else if ("October".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates+3  WHERE remarks = ?";
		}
		int rs = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, remarks);
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, null);
		}
		return rs;
	}

	@Override
	public int updateDelayDateBefer(String type, String remarks, String frequency) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = null;
		if ("May".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates-1  WHERE remarks = ? AND frequency =?";
		} else if ("October".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates-3  WHERE remarks = ? AND frequency =?";
		}
		int rs = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, remarks);
			pstmt.setString(2, frequency);
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			DBUtil.closeResource(connection, pstmt, null);
		}
		return rs;
	}

}
