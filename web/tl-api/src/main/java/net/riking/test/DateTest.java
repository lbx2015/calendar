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
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// 要插入的数据库，表
		String url = "jdbc:mysql://172.16.32.14:3306/rk_tl_dev";
		String user = "sa";
		String password = "1qaz@WSX";
		try {
			// 加载驱动程序
			Class.forName(driver);
			// 连续MySQL 数据库
			Connection conn = DriverManager.getConnection(url, user, password);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			Statement statement = conn.createStatement();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			java.util.Date start = sdf.parse("20170101");// 开始时间
			java.util.Date end = sdf.parse("20171231");// 结束时间
			List<Date> lists = dateSplit(start, end);

			// -------------------插入周末时间---------------
			if (!lists.isEmpty()) {
				for (Date date : lists) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'7','" + sdf.format(date) + "','0"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'1','" + sdf.format(date) + "','1"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'2','" + sdf.format(date) + "','1"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'3','" + sdf.format(date) + "','1"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'4','" + sdf.format(date) + "','1"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周末");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'5','" + sdf.format(date) + "','1"
								+ "')";
						statement.executeUpdate(insertSql);
					}
					if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
						System.out.println("插入日期:" + sdf.format(date) + ",周");
						String insertSql = "INSERT INTO t_days (weekday,dates,remark) VALUES("
								+ "'6','" + sdf.format(date) + "','0"
								+ "')";
						statement.executeUpdate(insertSql);
					}

				}
			}

			// ---------------插入节假日时间------------------
			List<Days> holidays = new ArrayList<Days>();
			holidays.add(new Days("0", "20170101"));
			holidays.add(new Days("0", "20170102"));

			holidays.add(new Days("0", "20170127"));
			holidays.add(new Days("0", "20170128"));
			holidays.add(new Days("0", "20170129"));
			holidays.add(new Days("0", "20170130"));
			holidays.add(new Days("0", "20170131"));
			holidays.add(new Days("0", "20170201"));
			holidays.add(new Days("0", "20170202"));

			holidays.add(new Days("0", "20170402"));
			holidays.add(new Days("0", "20170403"));
			holidays.add(new Days("0", "20170404"));

			holidays.add(new Days("0", "20170501"));

			holidays.add(new Days("0", "20171001"));
			holidays.add(new Days("0", "20171002"));
			holidays.add(new Days("0", "20171003"));

			holidays.add(new Days("0", "20171004"));
			holidays.add(new Days("0", "20171005"));
			holidays.add(new Days("0", "20171006"));

			holidays.add(new Days("0", "20171007"));
			holidays.add(new Days("0", "20171008"));
			for (Days day : holidays) {
				// // 跟周末冲突的，不重复插入
				// String sql = "select count(1) as numbers from fn_all_holiday
				// where date ='"
				// + sdf.format(day.getDate()) + "'";
				// // 结果集
				// ResultSet rs = statement.executeQuery(sql);
				// boolean hasRecord = false;
				// while (rs.next()) {
				// if (!"0".equals(rs.getString("numbers"))) {
				// hasRecord = true;
				// }
				// }
				// if (!hasRecord) {
				// System.out.println("插入日期：" + sdf.format(day.getDate()) + ","
				// + day.getTitle());
				// String insertSql = "INSERT INTO fn_all_holiday (title,date)
				// VALUES("
				// + day.getId() + "'," + "'" + day.getTitle() + "','"
				// + sdf.format(day.getDate()) + "')";
				// statement.executeUpdate(insertSql);
				// }

				String updateSql = "UPDATE t_days set remark='"
						+ day.getRemark() + "' where dates = '"
						+ day.getDate() + "'";
				statement.executeUpdate(updateSql);

			}

			// -------------- 剔除补班时间(周末需要补班的)---------------------
			List<Days> workDays = new ArrayList<Days>();
			workDays.add(new Days("1","20170122"));
			workDays.add(new Days("1", "20170401"));
			workDays.add(new Days("1", "20170204"));
			workDays.add(new Days("1", "20170527"));
			workDays.add(new Days("1", "20170930"));

			for (Days day : workDays) {
				// System.out.println("剔除日期：" + sdf.format(day.getDate()) + ","
				// + day.getTitle());
				// String delSql = "delete from fn_all_holiday where date ='"
				// + sdf.format(day.getDate()) + "'";
				// statement.executeUpdate(delSql);
				String updateSql = "UPDATE t_days set remark='"
						+ day.getRemark() + "' where dates = '"
						+ day.getDate() + "'";
				statement.executeUpdate(updateSql);
			}

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

	private static List<Date> dateSplit(java.util.Date start, Date end)
			throws Exception {
		if (!start.before(end))
			throw new Exception("开始时间应该在结束时间之后");
		Long spi = end.getTime() - start.getTime();
		Long step = spi / (24 * 60 * 60 * 1000);// 相隔天数

		List<Date> dateList = new ArrayList<Date>();
		dateList.add(end);
		for (int i = 1; i <= step; i++) {
			dateList.add(new Date(
					dateList.get(i - 1).getTime() - (24 * 60 * 60 * 1000)));// 比上一天减一
		}
		return dateList;
	}
}
