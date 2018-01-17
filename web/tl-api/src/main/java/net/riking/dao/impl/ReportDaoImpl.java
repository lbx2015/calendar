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
	public List<ReportResult> getAllReportByParams(String param, String userId, Integer upperLimit) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		// String sql = "call getAllReport(?)";
		String sql = "select ";
		sql += "a.report_type reportType, ";
		sql += "(select t.VALU from t_base_modelpropdict t WHERE t.TABLENAME = 'T_REPORT' AND t.FIELD = 'REPORT_TYPE' and t.KE=a.report_type) reportTypeName, ";
		sql += "a.report_kind reportKind, ";
		sql += "(select t.VALU from t_base_modelpropdict t WHERE t.TABLENAME = 'T_REPORT' AND t.FIELD = 'REPORT_KIND' and t.KE=a.report_kind) reportKindName, ";
		sql += "a.module_type moduleType, ";
		sql += "(select t.VALU from t_base_modelpropdict t WHERE t.TABLENAME = 'T_REPORT' AND t.FIELD = 'MODULE_TYPE' and t.KE=a.module_type) moduleTypeName, ";
		sql += "(select tsr.report_id from t_report_subscribe_rel tsr where tsr.user_id = ?";
		sql += " and  tsr.report_id= a.id) isSubscribe, ";
		sql += "a.id reportId, a.`code`, a.title, b.frequency, a.report_batch reportBatch ";
		sql += "from t_report a left join t_report_submit_caliber b on b.report_id=a.id ";
		sql += "where a.is_deleted=1 and a.is_aduit=1 ";
		if (StringUtils.isNotBlank(param)) {
			sql += "and (a.`code` like '%" + param + "%' or a.title like '%" + param + "%') ";
		}
		sql += "order by a.report_type, a.module_type, a.`code` ";
		if(upperLimit != null){
			sql +=(" limit 0," + upperLimit);
		}
		PreparedStatement pstmt = null;
		List<ReportResult> list = new ArrayList<ReportResult>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ReportResult report = new ReportResult();
				// report.setAgenceCode(rs.getString("agenceCode"));
				report.setReportType(rs.getString("reportType"));
				// report.setReportMode(rs.getString("reportMode"));
				report.setReportTypeName(rs.getString("reportTypeName"));
				report.setReportKind(rs.getString("reportKind"));
				report.setReportKindName(rs.getString("reportKindName"));
				report.setModuleType(rs.getString("moduleType"));
				report.setModuleTypeName(rs.getString("moduleTypeName"));
				report.setReportId(rs.getString("reportId"));
				report.setCode(rs.getString("code"));
				report.setTitle(rs.getString("title"));
				if (StringUtils.isNotBlank(rs.getString("isSubscribe"))) {
					report.setIsSubscribe(1);// 已订阅
				} else {
					report.setIsSubscribe(0);// 未订阅
				}
				report.setFrequency(rs.getInt("frequency"));
				report.setReportBatch(rs.getString("reportBatch"));
				list.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}
	
}
