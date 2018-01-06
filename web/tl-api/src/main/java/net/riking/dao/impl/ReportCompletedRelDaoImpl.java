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

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import net.riking.core.entity.PageQuery;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;

@Repository("reportCompletedRelDaoImpl")
public class ReportCompletedRelDaoImpl implements ReportCompletedRelDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<ReportCompletedRelResult> findExpireReportByPage(String userId, PageQuery pageQuery) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT ";
		sql += "b.code reportCode, b.title reportName, SUBSTR(a.submit_end_time, 1, 8) dateStr, ";
		sql += "(select t.VALU from t_base_modelpropdict t ";
		sql += "where t.TABLENAME='T_REPORT' and t.FIELD='FREQUENTLY' and t.KE=c.frequency) frequency, b.report_batch reportBatch ";
		sql += "FROM t_report_completed_rel a INNER join t_report b on a.report_id=b.id ";
		sql += "LEFT JOIN t_report_submit_caliber c on a.report_id=c.report_id ";
		sql += "WHERE a.user_id = ? AND a.is_completed = 0 and b.is_aduit =1 and b.is_deleted=1 ";
		sql += "AND SUBSTR(a.submit_end_time, 1, 8) < REPLACE(CURRENT_DATE(), '-', '') ";
		sql += "order by submit_end_time desc ";
		sql += "LIMIT ? , ?";
		PreparedStatement pstmt = null;
		List<ReportCompletedRelResult> list = new ArrayList<ReportCompletedRelResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, (pageQuery.getPindex() - 1) * pageQuery.getPcount());
			pstmt.setInt(3, pageQuery.getPcount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCompletedRelResult rel = new ReportCompletedRelResult();
				rel.setReportCode(rs.getString("reportCode"));
				rel.setReportName(rs.getString("reportName"));
				rel.setFrequency(rs.getString("frequency"));
				rel.setReportBatch(rs.getString("reportBatch"));
				rel.setDateStr(rs.getString("dateStr"));
				list.add(rel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<ReportCompletedRelResult> findHisCompletedReportByPage(String userId, PageQuery pageQuery) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT ";
		sql += "b.code reportCode, b.title reportName, SUBSTR(a.completed_date, 1, 8) dateStr, ";
		sql += "(select t.VALU from t_base_modelpropdict t where t.TABLENAME='T_REPORT' and t.FIELD='FREQUENTLY' and t.KE=c.frequency) frequency, b.report_batch reportBatch ";
		sql += "FROM t_report_completed_rel a INNER join t_report b on a.report_id=b.id ";
		sql += "LEFT JOIN t_report_submit_caliber c on a.report_id=c.report_id ";
		sql += "WHERE a.user_id = ? AND a.is_completed = 1 and b.is_aduit =1 and b.is_deleted=1 ";
		sql += "AND SUBSTR(a.completed_date, 1, 8) < REPLACE(CURRENT_DATE(), '-', '') ";
		sql += "order by completed_date desc ";
		sql += "LIMIT ? , ?";
		PreparedStatement pstmt = null;
		List<ReportCompletedRelResult> list = new ArrayList<ReportCompletedRelResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, userId);
			pstmt.setInt(2, (pageQuery.getPindex() - 1) * pageQuery.getPcount());
			pstmt.setInt(3, pageQuery.getPcount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCompletedRelResult rel = new ReportCompletedRelResult();
				rel.setReportCode(rs.getString("reportCode"));
				rel.setReportName(rs.getString("reportName"));
				rel.setFrequency(rs.getString("frequency"));
				rel.setReportBatch(rs.getString("reportBatch"));
				rel.setDateStr(rs.getString("dateStr"));
				list.add(rel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public List<CurrentReportTaskResp> findCurrentTasks(String userId, String currentDate) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select ";
		sql += "a.report_id reportId, b.code reportCode, b.title reportName, ";
		// sql += "(select CONCAT(t.VALU, IFNULL(b.report_batch,'')) from t_base_modelpropdict t
		// where t.TABLENAME='T_REPORT' and t.FIELD='FREQUENTLY' and t.KE=c.frequency)
		// frequencyType, ";
		sql += "c.frequency, b.report_batch reportBatch, ";
		sql += "a.submit_start_time submitStartTime, a.submit_end_time submitEndTime, ";
		sql += "a.is_completed isCompleted, d.remind_id remindId, d.content remindContent ";
		sql += "from t_report_completed_rel a INNER join t_report b on a.report_id=b.id ";
		sql += "LEFT JOIN t_report_submit_caliber c on a.report_id=c.report_id ";
		sql += "LEFT JOIN t_remind d on d.report_id=a.report_id and d.user_id=a.user_id ";
		sql += "where a.user_id= ? and ? BETWEEN SUBSTRING(a.submit_start_time, 1,8) and SUBSTRING(a.submit_end_time, 1, 8) ";
		sql += "order by a.is_completed, a.submit_end_time, c.frequency ";
		PreparedStatement pstmt = null;
		List<CurrentReportTaskResp> list = new ArrayList<CurrentReportTaskResp>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, currentDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CurrentReportTaskResp data = new CurrentReportTaskResp();
				data.setReportId(rs.getString("reportId"));
				data.setReportCode(rs.getString("reportCode"));
				data.setReportName(rs.getString("reportName"));
				data.setFrequency(rs.getInt("frequency"));
				data.setReportBatch(rs.getString("reportBatch") == null ? "" : rs.getString("reportBatch"));
				data.setSubmitStartTime(rs.getString("submitStartTime"));
				data.setSubmitEndTime(rs.getString("submitEndTime"));
				data.setIsCompleted(rs.getString("isCompleted"));
				data.setRemindId(rs.getString("remindId"));
				data.setRemindContent(rs.getString("remindContent"));
				list.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	@Transactional
	public List<CurrentReportTaskResp> findUsersByCurrentDayTasks(String currentDate) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "select ";
		sql += "a.report_id reportId, a.submit_start_time submitStartTime, a.submit_end_time submitEndTime, "; 
		sql += "a.user_id userId, c.phone_device_id phoneDeviceId from t_report_completed_rel a ";
		sql += "inner join t_app_user b on a.user_id = b.id inner join t_appuser_detail c on c.id = b.id "; 
		sql += "where b.is_deleted=1 and b.enabled=1 and c.phone_device_id <> '' and ? BETWEEN SUBSTRING(a.submit_start_time, 1, 8) ";
		sql += "and SUBSTRING(a.submit_end_time, 1, 8) and a.is_completed=0 group by a.user_id ";
		PreparedStatement pstmt = null;
		List<CurrentReportTaskResp> list = new ArrayList<CurrentReportTaskResp>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, currentDate);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CurrentReportTaskResp data = new CurrentReportTaskResp();
				data.setReportId(rs.getString("reportId"));
				data.setSubmitStartTime(rs.getString("submitStartTime"));
				data.setSubmitEndTime(rs.getString("submitEndTime"));
				data.setIsCompleted(rs.getString("userId"));
				data.setRemindId(rs.getString("phoneDeviceId"));
				list.add(data);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

}
