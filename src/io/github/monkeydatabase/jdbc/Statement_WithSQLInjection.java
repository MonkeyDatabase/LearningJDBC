package io.github.monkeydatabase.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Statement_WithSQLInjection {
    public static void main(String[] args) {
        ResourceBundle bundle=ResourceBundle.getBundle("jdbc");
        String driver=bundle.getString("driver");
        String url=bundle.getString("url");
        String user=bundle.getString("user");
        String pass=bundle.getString("pass");

        Map<String,String> map= getUserinfo();

        Connection conn=null;
        Statement stmt=null;
        ResultSet rs=null;

        try {
            //1. 注册驱动
            Class.forName(driver);
            //2. 获取连接
            conn=DriverManager.getConnection(url,user,pass);
            //3. 获取数据库处理对象
            stmt=conn.createStatement();
            //4. 执行sql语句
            String sql = "select * from t_user where username ='"+map.get("username") +"' ";
            rs = stmt.executeQuery(sql);
            //5. 处理查询结果集
            while(rs.next()){
                int id=rs.getInt("id");
                String username=rs.getString("username");
                String password=rs.getString("password");
                System.out.println("id: "+id+" username: "+username+" password: "+ password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //6. 释放资源
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

    /*
    当输入monkeydatabase时，正常返回内容
    当输入monkeydatabase' or 1=1 #时，会将表中所有的内容返回，这就是sql注入漏洞
     */
    private static Map<String, String> getUserinfo() {
        Map<String,String> map=new HashMap<>();
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入用户名:");
        map.put("username",scanner.nextLine());
        return map;
    }
}
