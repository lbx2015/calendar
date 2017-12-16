package net.riking.dao.impl;

import java.awt.print.Pageable;
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

import net.riking.core.entity.PageQuery;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.ReportCompletedRel;
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
		sql += "b.code reportCode, b.title reportName, SUBSTR(a.completed_date, 1, 8) dateStr, ";
		sql += "(select CONCAT(t.VALU, IFNULL(b.report_batch,'')) from t_base_modelpropdict t ";
		sql += "where t.TABLENAME='T_REPORT' and t.FIELD='FREQUENTLY' and t.KE=c.frequency) frequencyType ";
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
			pstmt.setInt(2, (pageQuery.getPindex()-1) * pageQuery.getPcount());
			pstmt.setInt(3, pageQuery.getPcount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCompletedRelResult rel = new ReportCompletedRelResult();
				rel.setReportCode(rs.getString("reportCode"));
				rel.setReportName(rs.getString("reportName"));
				rel.setFrequencyType(rs.getString("frequencyType"));
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
		sql += "(select CONCAT(t.VALU, IFNULL(b.report_batch,'')) from t_base_modelpropdict t where t.TABLENAME='T_REPORT' and t.FIELD='FREQUENTLY' and t.KE=c.frequency) frequencyType ";
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
			pstmt.setInt(2, (pageQuery.getPindex()-1) * pageQuery.getPcount());
			pstmt.setInt(3, pageQuery.getPcount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportCompletedRelResult rel = new ReportCompletedRelResult();
				rel.setReportCode(rs.getString("reportCode"));
				rel.setReportName(rs.getString("reportName"));
				rel.setFrequencyType(rs.getString("frequencyType"));
				rel.setDateStr(rs.getString("dateStr"));
				list.add(rel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}


}
