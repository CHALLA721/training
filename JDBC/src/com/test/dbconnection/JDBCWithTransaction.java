package com.test.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBCWithTransaction {

	private static final String SQL_INSERT = "INSERT INTO EMP (PersonID, LastName, FirstName, Address, City)"
			+ "VALUES(?, ?, ?, ?, ?);";

	public static void main(String[] args) {
		Connection con = null;
		try {
			// Driver changes based on the database used Oracle/MySQL
			Class.forName("com.mysql.cj.jdbc.Driver");

			// here sample is database name, root is username and password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample", "root", "@database4ME");

			// Enable transaction
			con.setAutoCommit(false);

			// ************ Using Prepared Statement *************//
			PreparedStatement prepStmt = con.prepareStatement(SQL_INSERT);
			prepStmt.setInt(1, 1001);
			prepStmt.setString(2, "Joseph");
			prepStmt.setString(3, "John");
			prepStmt.setString(4, "USA");
			prepStmt.setString(5, "New York");

			prepStmt.execute();
			
//			errorMethod();
			
			PreparedStatement prepStmt2 = con.prepareStatement(SQL_INSERT);
			prepStmt2.setInt(1, 1002);
			prepStmt2.setString(2, "Mike");
			prepStmt2.setString(3, "Sherrard");
			prepStmt2.setString(4, "USA");
			prepStmt2.setString(5, "Iowa");

			prepStmt2.execute();
			
			// commit the changes
			con.commit();
			System.out.println("Row inserted");
			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				//roll back the changes
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} finally {
			// at last close the connection
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void errorMethod() throws Exception {
		throw new Exception("Some error occurred");
	}

}
