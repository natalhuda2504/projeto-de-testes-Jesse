package org.isf.utils.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * @version 0.1 2005-11-06
 * @author bob
 * 
 * modified Mwithi - Added method for getting Connection object 
 * 				     (to manage Prepared Statement)
 * 
 */

/**
 * Class that executes a query using the connection DbSingleConn
 * The various methods that open a connection with the 
 * autocommit flag set to false have the responsibility
 * of doing the commit/rollback operation
 * 
 * @deprecated since OpenHospital 1.7.0 replaced by {@link DbQueryLogger}
 */
@Deprecated
public class DbQuery {
    /**
     * method that executes a query and returns a resultset
     * 
     * @param aQuery
     * @param autocommit
     * @return "ResultSet"
     * @throws SQLException
     * @throws IOException
     * 
     * @deprecated since OpenHospital 1.7.0 replaced by {@link DbQueryLogger}
     */
    @Deprecated
    public ResultSet getData(String aQuery,boolean autocommit) throws SQLException, IOException {
        Connection conn = DbSingleConn.getConnection();
        conn.setAutoCommit(autocommit);
        Statement stat = conn.createStatement();
        return stat.executeQuery(aQuery);
    }
    /**
     * method that executes an insert-update-delete query and returns true or false
     * depending on the success of the operation
     * @param aQuery
     * @param autocommit
     * @return Boolean True/False
     * @throws SQLException
     * @throws IOException
     * 
     * @deprecated since OpenHospital 1.7.0 replaced by {@link DbQueryLogger}
     */
    @Deprecated
    public boolean setData(String aQuery,boolean autocommit) throws SQLException , IOException {
        Connection conn = DbSingleConn.getConnection();
        conn.setAutoCommit(autocommit);
        Statement stat = conn.createStatement();
        if (stat.executeUpdate(aQuery)==0) return false;
        else
        	return true;
    }
    /**
     * method that executes an insert-update-delete query and returns A ResultSet
     * containing the autogenerated key (integer counter)
     * if no key has been generated the ResultSet will be empty
     * @param aQuery
     * @param autocommit
     * @return ResultSet
     * @throws SQLException
     * @throws IOException
     * 
     * @deprecated since OpenHospital 1.7.0 replaced by {@link DbQueryLogger}
     */
    @Deprecated
    public ResultSet setDataReturnGeneratedKey(String aQuery,boolean autocommit) throws SQLException , IOException {
        Connection conn = DbSingleConn.getConnection();
        conn.setAutoCommit(autocommit);
        Statement stat = conn.createStatement();
        stat.execute(aQuery,Statement.RETURN_GENERATED_KEYS);
        return stat.getGeneratedKeys();
    }
    
    /**
     * method that executes a query and returns true or false
     * depending on the existence of records or not in
     * the Recordset
     * @param aQuery
     * @return Boolean True/False
     * @throws SQLException
     * @throws IOException
     * 
     * @Deprecated since OpenHospital 1.7.0 replaced by {@link DbQueryLogger}
     */
    public boolean isData(String aQuery) throws SQLException , IOException {
        Connection conn = DbSingleConn.getConnection();
        Statement stat = conn.createStatement();
        ResultSet set = stat.executeQuery(aQuery);
        return set.first();
    }
    
    public Connection getConnection() throws SQLException, IOException {
    	return DbSingleConn.getConnection();
    }
    
    public void closeConnection()  throws SQLException, IOException{
        DbSingleConn.closeConnection();
    }
    public void releaseConnection() throws SQLException , IOException {
        DbSingleConn.releaseConnection();
    }
    public void commit() throws SQLException , IOException{
    	DbSingleConn.commitConnection();
    }
    public void rollback() throws SQLException , IOException{
    	DbSingleConn.rollbackConnection();
    }
}
