package io.github.monkeydatabase.jdbc;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PreparedStatement_WithoutSQLInjection {
    public static void main(String[] args) {
        ResourceBundle bundle=ResourceBundle.getBundle("jdbc");
        String driver=bundle.getString("driver");
        String url=bundle.getString("url");
        String user=bundle.getString("user");
        String pass=bundle.getString("pass");


        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        try {
            //1. 注册驱动
            Class.forName(driver);
            //2. 获取连接
            conn= DriverManager.getConnection(url,user,pass);
            //3. 获取预编译的数据库处理对象
            String sql = "select * from t_user where username =?";
            ps=conn.prepareStatement(sql);
            Map<String,String> map =getUserinfo();
            ps.setString(1,map.get("username"));
            //4. 执行sql
            rs=ps.executeQuery();
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
            if(ps!=null){
                try {
                    ps.close();
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
    当输入monkeydatabase' or 1=1 #时，因为在将输入加入之前就完成了编译，所以不会有SQL注入，从而返回空值
     */
    private static Map<String, String> getUserinfo() {
        Map<String,String> map=new HashMap<>();
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入用户名:");
        map.put("username",scanner.nextLine());
        return map;
    }
}
