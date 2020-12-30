## 代码简介

1. Statement_WithSQLInjection.java 为使用**非预编译**的数据库操作对象进行操作
2. PreparedStatement_WithoutSQLInjection.java 为使用**预编译**的数据库操作对象进行操作
3. SimplifiedJDBC_WithDBUtil.java 为使用了util.DBUtil将JDBC的重复代码放入工具类中，对代码进行**精简**
4. NotTranscationInJdbc.java 为**未使用事务**进行两次数据库操作且在两次数据库操作之间出现异常的情况，此时查看数据库会发现第一次数据库操作的结果已经提交并出现在数据库中
5. TransactionInJdbc.java 为**使用事务**进行两次数据库操作且在两次数据库操作之间出现异常的情况，此时查看数据库会发现第一次数据库操作的结果并未生效，正常回滚

## 配置文件简介

1. jdbc.properties文件将被ResourceBundle资源绑定器进行读取和解析，用于将配置信息独立出来存放到一个文件中，这样不仅可以避免在代码中写死，还可以在修改配置文件后无需重新编译程序
2. driver为数据库驱动的类名，这样当从Mysql数据库更换到Oracle数据库只需要修改配置文件的这一行就可以影响所有注册驱动的代码
3. url为数据库的访问地址，一般为*protocol://ip:port/database_name*，不同数据库、同一数据库的不同版本该url的格式均可能不同
4. user为登录数据库系统的用户名
5. pass为登录数据库系统的密码

## 重点

1. JDBC中的序号都是从1开始的
2. 默认情况下JDBC是每执行一次SQL语句自动提交，然而实际使用中大多涉及到多次SQL操作，很有可能只执行了一部分SQL程序就崩溃了，这种情况下就需要把之前那部分的SQL操作给回滚，而不能提交
   * 一种特殊的场景就是银行转账，当刚把付款人的余额扣除的SQL操作执行完毕就程序就出现了异常，而没有来得及给收款人的余额增加，即第二天SQL没来得及完成，如果自动提交，就会造成钱凭空消失，这显然是不合理的。
   * 实现事务主要依靠JDBC中的三个函数Connection，setAutoCommit()、Connection.commit()、Connection.rollback();
   * Connection.setAutoCommit(false)语句位于获取连接之后在获取数据库操作对象之前
   * Connection.commit()位于try语句块的最后，此时所有SQL语句全部执行完毕
   * Connection.rollback()位于catch语句块，程序未执行到Connection.commit()出现异常时转入catch语句块，此时正好对之前的SQL进行回滚
3. Statement接口是非预编译的数据库操作对象，在实际中很少使用，应用场景主要为需要SQL注入的场景，如排序、选择多种筛选条件等
4. PreparedStatement接口是预编译的数据库操作对象，实际中基本使用的都是它，因为它安全性高。