package net.riking.util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created by bing.xun on 2017/6/20.
 */
public class NamedParameterStatement {
    private final PreparedStatement statement;

    private final Map indexMap;

    public NamedParameterStatement(Connection connection, String query)
            throws SQLException {
        indexMap = new HashMap();
        String parsedQuery = parse(query, indexMap);
        statement = connection.prepareStatement(parsedQuery);
    }

    static final String parse(String query, Map paramMap) {

        int length = query.length();
        StringBuffer parsedQuery = new StringBuffer(length);
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        int index = 1;

        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            if (inSingleQuote) {
                if (c == '\'') {
                    inSingleQuote = false;
                }
            } else if (inDoubleQuote) {
                if (c == '"') {
                    inDoubleQuote = false;
                }
            } else {
                if (c == '\'') {
                    inSingleQuote = true;
                } else if (c == '"') {
                    inDoubleQuote = true;
                } else if (c == ':' && i + 1 < length
                        && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
                    int j = i + 2;
                    while (j < length
                            && Character.isJavaIdentifierPart(query.charAt(j))) {
                        j++;
                    }
                    String name = query.substring(i + 1, j);
                    c = '?';
                    i += name.length();

                    List indexList = (List) paramMap.get(name);
                    if (indexList == null) {
                        indexList = new LinkedList();
                        paramMap.put(name, indexList);
                    }
                    indexList.add(new Integer(index));

                    index++;
                }
            }
            parsedQuery.append(c);
        }

        for (Iterator itr = paramMap.entrySet().iterator(); itr.hasNext();) {
            Map.Entry entry = (Map.Entry) itr.next();
            List list = (List) entry.getValue();
            int[] indexes = new int[list.size()];
            int i = 0;
            for (Iterator itr2 = list.iterator(); itr2.hasNext();) {
                Integer x = (Integer) itr2.next();
                indexes[i++] = x.intValue();
            }
            entry.setValue(indexes);
        }

        return parsedQuery.toString();
    }

    private int[] getIndexes(String name) {
        int[] indexes = (int[]) indexMap.get(name);
        return indexes;
    }

    public void setObject(String name, Object value) throws SQLException {
        int[] indexes = getIndexes(name);
        if(null == indexes){
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            if(null!=value && value.getClass().getName().equals("java.math.BigDecimal")){
                statement.setFloat(indexes[i],((BigDecimal) value).floatValue());
                continue;
            }
            if(null!=value && value.getClass().getName().equals("java.util.Date")){
                statement.setDate(indexes[i],new java.sql.Date(((Date)value).getTime()));
                continue;
            }
            statement.setObject(indexes[i], value);
        }
    }

    public void setString(String name, String value) throws SQLException {
        int[] indexes = getIndexes(name);
        if(null == indexes){
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            statement.setString(indexes[i], value);
        }
    }

    public void setInt(String name, int value) throws SQLException {
        int[] indexes = getIndexes(name);
        if(null == indexes){
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            statement.setInt(indexes[i], value);
        }
    }

    public void setLong(String name, long value) throws SQLException {
        int[] indexes = getIndexes(name);
        if(null == indexes){
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            statement.setLong(indexes[i], value);
        }
    }

    public void setTimestamp(String name, Timestamp value) throws SQLException {
        int[] indexes = getIndexes(name);
        if(null == indexes){
            return;
        }
        for (int i = 0; i < indexes.length; i++) {
            statement.setTimestamp(indexes[i], value);
        }
    }

    public PreparedStatement getStatement() {
        return statement;
    }

    public boolean execute() throws SQLException {
        return statement.execute();
    }

    public ResultSet executeQuery() throws SQLException {
        return statement.executeQuery();
    }

    public int executeUpdate() throws SQLException {
        return statement.executeUpdate();
    }

    public void close() throws SQLException {
        statement.close();
    }

    public void addBatch() throws SQLException {
        statement.addBatch();
    }

    public int[] executeBatch() throws SQLException {
        return statement.executeBatch();
    }
}