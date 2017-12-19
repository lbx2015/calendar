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

import net.riking.dao.NewsDao;
import net.riking.entity.model.News;

@Repository("newsDao")
public class NewsDaoImpl implements NewsDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<News> findCollectNews(String userId, int begin, int pageCount) {
		SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
		Connection connection = session.connection();
		String sql = "call findCollectNews(?,?,?)";
		PreparedStatement pstmt = null;
		List<News> list = new ArrayList<News>();
		try {
			pstmt = (PreparedStatement) connection.prepareCall(sql);
			if (StringUtils.isBlank(userId))
				userId = "";
			pstmt.setString(1, userId);
			pstmt.setInt(2, begin);
			pstmt.setInt(3, pageCount);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				News news = new News();
				news.setId(rs.getString("id"));
				news.setCreatedTime(rs.getTimestamp("createdTime"));
				news.setModifiedTime(rs.getTimestamp("modifiedTime"));
				news.setTitle(rs.getString("title"));
				news.setSeat(rs.getString("seat"));
				news.setCoverUrls(rs.getString("coverUrls"));
				news.setContent(rs.getString("content"));
				news.setIssued(rs.getString("issued"));
				news.setUserName(rs.getString("userName"));
				news.setPhotoUrl(rs.getString("photoUrl"));
				news.setExperience(rs.getInt("experience"));
				list.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;

	}

}
