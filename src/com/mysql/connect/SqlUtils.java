package com.mysql.connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.ResultSetMetaData;

public class SqlUtils {
	private final String USERNAME = "root";
	private final String PSWD = "root";
	private final String URL = "jdbc:mysql://localhost:3306/shuju";
	private final String DRIVER ="com.mysql.jdbc.Driver";
	//定义数据库的连接
	private Connection connection;
	//  定义sql的执行对像
	private PreparedStatement pstmt;
	//定义查询返回的数据集
	private ResultSet  resultSet;
			
	
	public SqlUtils() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("jdbc驱动加载成功");
		}
	}
	
	public Connection getConnection() {
		try {
			connection = DriverManager.getConnection(URL, USERNAME, PSWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}

	public boolean updateByPreparedStatement(String sql, List<Object> params)
			throws SQLException {

		boolean flag = false;

		int result = -1;

		pstmt = connection.prepareStatement(sql);

		int index = 1;
		if (params != null && !params.isEmpty()) {
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		result = pstmt.executeUpdate();
		flag = result > 0 ? true : false;
		return flag;

	}
		
	
	public Map<String, Object>  findSimpleResult(String sql,List<Object> params) throws SQLException{
		Map<String, Object> map =new HashMap<String, Object>();
		pstmt = connection.prepareStatement(sql);
		int  index = 1;
		if(params != null  && !params.isEmpty()){
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, params.get(i));
			}
		}
		
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = (ResultSetMetaData) pstmt.getMetaData();
		
		int col_len = metaData.getColumnCount();//计算数据的列数
		while (resultSet.next()) {
		for(int i =0;i<col_len;i++){
			String col_name = metaData.getColumnName(i+1);
			Object col_value = resultSet.getObject(col_name);
			if(col_value ==  null){
				col_value = "";
			}
			map.put(col_name, col_value);
			}
			
		}
		
		return map;
	}
	
	public List<Map<String, Object>> findMoreResult(String sql,
			List<Object> params) throws SQLException {
		List<Map<String ,Object>> list = new ArrayList<Map<String,Object>>();
		pstmt = connection.prepareStatement(sql);
		int index =1;
		if(params != null && ! params.isEmpty()){
			for (int i = 0; i < params.size(); i++) {
				pstmt.setObject(index++, i);
			}
		}
		
		resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = (ResultSetMetaData) resultSet.getMetaData();
		int col_len = metaData.getColumnCount();
		while(resultSet.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 0; i < col_len; i++) {
				String col_name = metaData.getColumnName(i+1);
				Object col_value = resultSet.getObject(col_name);
				map.put(col_name, col_value);
			}
			list.add(map);
			}
		return  list;
	}
	
	public  void  releseCon(){
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			SqlUtils  sqlUtils = new SqlUtils();
			sqlUtils.getConnection();
/*		   String sql = "insert into userinfo (username,pswd) values(?,?)";
			List<Object> params = new ArrayList<Object>();
			params.add("hfy");
			params.add("hfy233");
			try {
				boolean result = sqlUtils.updateByPreparedStatement(sql,params);
				System.out.println(result);		
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		/*	String sql = "select * from userinfo ";
			 try {
				List<Map<String, Object>> list  = sqlUtils.findMoreResult(sql, null);
				System.out.println(list);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				sqlUtils.releseCon();
			}*/
			String sql = "select *   from userinfo  where id = ? ";
			List<Object> params = new  ArrayList<Object>();
			params.add(5);
			Map<String, Object> map;
			try {
				map = sqlUtils.findSimpleResult(sql, params);
				System.out.println(map);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
	}
}
