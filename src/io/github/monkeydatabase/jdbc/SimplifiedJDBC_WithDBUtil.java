package io.github.monkeydatabase.jdbc;

import io.github.monkeydatabase.jdbc.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimplifiedJDBC_WithDBUtil {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        try {
            //1&2. 注册驱动&获取连接
            conn=DBUtil.getConnection();
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
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //6. 释放资源
            DBUtil.close(conn,ps,rs);
        }
    }

    private static Map<String, String> getUserinfo() {
        Map<String,String> map=new HashMap<>();
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入用户名:");
        map.put("username",scanner.nextLine());
        return map;
    }
}
