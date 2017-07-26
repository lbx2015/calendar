package net.riking.util;

import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by bing.xun on 2017/7/12.
 */
@Repository
@Transactional
public class JdbcBatch {

    @PersistenceContext
    protected EntityManager em;

    public void batchInsert(String sql,List<Map<String,Object>> list) throws ClassNotFoundException, SQLException {
        long start = System.currentTimeMillis();

        SessionImplementor session =em.unwrap(SessionImplementor.class);
        Connection connection = session.connection();
        connection.setAutoCommit(false);

        NamedParameterStatement p = new NamedParameterStatement(connection, sql);

        for(Map<String,Object> map : list){
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                p.setObject(entry.getKey(), entry.getValue());
            }
            p.addBatch();
        }
        int[] result = p.executeBatch();
        System.out.println("批量插入条数"+result.length);
        connection.commit();
        p.close();
        //connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:"+(end - start)+"ms"); //批量插入需要时间:24675
    }
}
