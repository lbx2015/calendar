package net.riking.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.riking.entity.model.Days;

public class DateTest {
	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://172.16.32.14:3306/rk_tl_dev";
		String user = "sa";
		String password = "1qaz@WSX";
		try {
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			Statement statement = conn.createStatement();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			java.util.Date start = sdf.parse("20170101");
			java.util.Date end = sdf.parse("20171231");
			List<Date> lists = dateSplit(start, end);
			if (!lists.isEmpty()) {
				for (Date date : lists) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'7','"
								+ sdf.format(date) + "',0)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'1','"
								+ sdf.format(date) + "',1)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'2','"
								+ sdf.format(date) + "',1)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'3','"
								+ sdf.format(date) + "',1)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'4','"
								+ sdf.format(date) + "',1)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'5','"
								+ sdf.format(date) + "',1)";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						String insertSql = "INSERT INTO t_days (weekday,dates,is_work) VALUES(" + "'6','"
								+ sdf.format(date) + "',0)";
						statement.executeUpdate(insertSql);
					}

				}
			}

			List<Days> holidays = new ArrayList<Days>();
			// holidays.add(new Days(0, "20170101"));
			// holidays.add(new Days(0, "20170102"));
			//
			// holidays.add(new Days(0, "20170127"));
			// holidays.add(new Days(0, "20170128"));
			// holidays.add(new Days(0, "20170129"));
			// holidays.add(new Days(0, "20170130"));
			// holidays.add(new Days(0, "20170131"));
			// holidays.add(new Days(0, "20170201"));
			// holidays.add(new Days(0, "20170202"));
			//
			// holidays.add(new Days(0, "20170402"));
			// holidays.add(new Days(0, "20170403"));
			// holidays.add(new Days(0, "20170404"));
			//
			// holidays.add(new Days(0, "20170501"));
			//
			// holidays.add(new Days(0, "20171001"));
			// holidays.add(new Days(0, "20171002"));
			// holidays.add(new Days(0, "20171003"));
			//
			// holidays.add(new Days(0, "20171004"));
			// holidays.add(new Days(0, "20171005"));
			// holidays.add(new Days(0, "20171006"));
			//
			// holidays.add(new Days(0, "20171007"));
			// holidays.add(new Days(0, "20171008"));
			// for (Days day : holidays) {
			// String updateSql = "UPDATE t_days set is_work='"
			// + day.getIsWork() + "' where dates = '"
			// + day.getDate() + "'";
			// statement.executeUpdate(updateSql);
			//
			// }
			//
			// List<Days> workDays = new ArrayList<Days>();
			// workDays.add(new Days(1,"20170122"));
			// workDays.add(new Days(1, "20170401"));
			// workDays.add(new Days(1, "20170204"));
			// workDays.add(new Days(1, "20170527"));
			// workDays.add(new Days(1, "20170930"));
			//
			// for (Days day : workDays) {
			// String updateSql = "UPDATE t_days set is_work='"
			// + day.getIsWork() + "' where dates = '"
			// + day.getDate() + "'";
			// statement.executeUpdate(updateSql);
			// }

			conn.close();
		} catch (ClassNotFoundException e) {
			System.out.println("Sorry,can't find the Driver!");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static List<Date> dateSplit(java.util.Date start, Date end) throws Exception {
		if (!start.before(end))
			throw new Exception("");
		Long spi = end.getTime() - start.getTime();
		Long step = spi / (24 * 60 * 60 * 1000);

		List<Date> dateList = new ArrayList<Date>();
		dateList.add(end);
		for (int i = 1; i <= step; i++) {
			dateList.add(new Date(dateList.get(i - 1).getTime() - (24 * 60 * 60 * 1000)));
		}
		return dateList;
	}
}
