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

import net.riking.dao.ReportSubmitCaliberDao;
import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.QueryReport;

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
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6));
				list.add(queryReport);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public Set<QueryReport> findAllfromReportId() {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT c.Id, c.report_name,c.report_code,c.module_type,b.caliber_type,b.frequency from  t_report_submit_caliber b  LEFT JOIN t_report_list c ON b.report_id = c.Id WHERE b.enabled = 1 AND c.delete_state = '1' ORDER BY b.frequency";
		PreparedStatement pstmt = null;
		Set<QueryReport> list = new HashSet<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6));
				list.add(queryReport);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int updateDelayDateAfter(String type,String remarks) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = null ;
		if ("May".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates+1  WHERE remarks = ?";
		}else if ("October".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates+3  WHERE remarks = ?";
		}
		int rs = 0 ;
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, remarks);
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public int updateDelayDateBefer(String type,String remarks,String frequency) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = null ;
		if ("May".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates-1  WHERE remarks = ? AND frequency =?";
		}else if ("October".equalsIgnoreCase(type)) {
			sql = "update t_report_submit_caliber a SET delay_dates = delay_dates-3  WHERE remarks = ? AND frequency =?";
		}
		int rs = 0 ;
		PreparedStatement pstmt = null;
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, remarks);
			pstmt.setString(2, frequency);
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	public List<QueryReport> findAllReport() {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT a.id, a.report_name, substring_index(b.VALU, '-', 1) toOrgan,case when substring_index(b.VALU, '-', 1)='PBOC' then '中国人民银行监管报表' when substring_index(b.VALU, '-', 1)='CBRC' then '银监会监管报表报表' end as rptType,substring_index(b.VALU, '-', -2) rptPackage, group_concat(c.frequency ORDER BY c.frequency ASC) AS strFrequency from t_report_list a left join t_base_modelpropdict b on b.tablename='T_REPORT_LIST' and b.FIELD='MODLE_TYPE' LEFT JOIN t_report_submit_caliber c ON a.id=c.report_id where a.module_type=b.KE and a.delete_state=1 GROUP BY a.Id ";
		PreparedStatement pstmt = null;
		List<QueryReport> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5),rs.getString(6),"");
				list.add(queryReport);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<AppUserReportCompleteRel> findCompleteReportByIdAndTime(String userId, String time) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.id,t.app_user_id,t.report_id,t.complete_date,t.is_complete,l.report_name, group_concat(c.frequency ORDER BY c.frequency ASC ) AS strFrequency FROM t_app_user_report_complete_rel t LEFT JOIN t_report_list l ON l.id = t.report_id LEFT JOIN t_report_submit_caliber c ON t.report_id = c.report_id WHERE t.app_user_id= ? AND t.complete_date=? GROUP BY c.report_id";
		PreparedStatement pstmt = null;
		List<AppUserReportCompleteRel> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, userId);
			pstmt.setString(2, time);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserReportCompleteRel appUserReportCompleteRel = new AppUserReportCompleteRel(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getInt(5), rs.getString(6),rs.getString(7));
				list.add(appUserReportCompleteRel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public List<AppUserReportCompleteRel> findAllUserReport(AppUserReportCompleteRel appUserReportCompleteRel) {
		// TODO Auto-generated method stub
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "SELECT t.id,t.app_user_id,t.report_id,t.complete_date,t.is_complete,l.report_name, group_concat(c.frequency ORDER BY c.frequency ASC ) AS strFrequency FROM t_app_user_report_complete_rel t LEFT JOIN t_report_list l ON l.id = t.report_id LEFT JOIN t_report_submit_caliber c ON t.report_id = c.report_id WHERE t.app_user_id= ? AND t.is_complete=? GROUP BY c.report_id LIMIT ?,?";
		PreparedStatement pstmt = null;
		List<AppUserReportCompleteRel> list = new ArrayList<>();
		try {
			pstmt = (PreparedStatement) connection.prepareStatement(sql);
			pstmt.setString(1, appUserReportCompleteRel.getAppUserId());
			pstmt.setInt(2, appUserReportCompleteRel.getIsComplete());
			pstmt.setInt(3, appUserReportCompleteRel.getPindex());
			pstmt.setInt(4, appUserReportCompleteRel.getPcount());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				AppUserReportCompleteRel aurcl = new AppUserReportCompleteRel(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getInt(5), rs.getString(6),rs.getString(7));
				list.add(aurcl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
