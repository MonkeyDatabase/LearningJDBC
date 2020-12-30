package io.github.monkeydatabase.jdbc.util;

import java.sql.*;
import java.util.ResourceBundle;

public class DBUtil {
    /*
    工具类一般不需要实例化，其方法一般为静态方法，故可将其构造方法私有化
     */
    private DBUtil(){

    }

    private static String driver=null;
    private static String url=null;
    private static String user=null;
    private static String pass=null;

    //静态代码块在加载时就会自动运行，将驱动注册
    static {
        ResourceBundle bundle=ResourceBundle.getBundle("jdbc");
        driver=bundle.getString("driver");
        url=bundle.getString("url");
        user=bundle.getString("user");
        pass=bundle.getString("pass");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,user,pass);
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(stmt!=null){
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
