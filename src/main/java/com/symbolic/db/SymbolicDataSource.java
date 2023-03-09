package com.symbolic.db;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

public class SymbolicDataSource implements DataSource {
	private DataSource dataSource;
	private Connection realConn;
	private Connection symConn;
	
	public SymbolicDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.realConn = null;
		this.symConn = null;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		if (this.dataSource != null) {
			this.realConn = this.dataSource.getConnection();
		}
		this.symConn = new SymbolicConnection(this.realConn);
		return this.symConn;
	}

	@Override
	public Connection getConnection(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Connection getRealConnection() throws SQLException {
		return this.realConn;
	}
	
	public Connection getSymbolicConnection() throws SQLException {
		return this.symConn;
	}

}
