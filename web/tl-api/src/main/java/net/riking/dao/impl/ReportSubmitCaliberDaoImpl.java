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
@Repository("reportSubmitCaliberDao")
public class ReportSubmitCaliberDaoImpl implements ReportSubmitCaliberDao {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public Set<QueryReport> findAllByFreeDatefromReportId(String userId,Integer week, Integer ten, Integer month, Integer season,
			Integer halfYear, Integer Year,Integer isWorkDay) {
		
		 SessionImplementor session =entityManager.unwrap(SessionImplementor.class);
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
	        pstmt.setInt(6,halfYear);
	        pstmt.setInt(7, Year);
	        pstmt.setInt(8, isWorkDay);
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	QueryReport queryReport = new QueryReport(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
	        	list.add(queryReport);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return list;
	}
}
