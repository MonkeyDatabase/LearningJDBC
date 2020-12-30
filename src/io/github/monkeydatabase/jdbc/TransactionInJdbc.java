package io.github.monkeydatabase.jdbc;

import io.github.monkeydatabase.jdbc.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TransactionInJdbc {
    public static void main(String[] args) {
        Connection conn=null;
        PreparedStatement ps=null;
        ResultSet rs=null;

        try {
            //1&2. 注册驱动&获取连接
            conn= DBUtil.getConnection();
            //关闭JDBC的自动提交
            conn.setAutoCommit(false);
            //3. 获取预编译的数据库处理对象
            String sql = "update t_user set password=? where username =?";
            ps=conn.prepareStatement(sql);
            Map<String,String> map =getUserinfo();
            ps.setString(2,map.get("username"));
            ps.setString(1,map.get("password"));
            //4. 执行sql
            int count=ps.executeUpdate();
            //5. 处理查询结果集（由于是修改，所以没有查询结果集）

            //此处两句会引发空指针异常
            String str=null;
            str.toString();

            ps=conn.prepareStatement(sql);
            map =getUserinfo();
            ps.setString(2,map.get("username"));
            ps.setString(1,map.get("password"));
            //4. 执行sql
            count += ps.executeUpdate();
            //5. 处理查询结果集（由于是修改，所以没有查询结果集）



            //运行到此处，说明所有的语句都执行完毕，提交事务
            conn.commit();
        } catch (SQLException throwables) {
            try {
                conn.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
        System.out.println("请输入新密码:");
        map.put("password",scanner.nextLine());
        return map;
    }
}
