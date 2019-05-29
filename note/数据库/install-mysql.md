# windows 64位安装mysql

1. 下载
   下载地址：https://dev.mysql.com/downloads/mysql/

2. 解压
   如：将`mysql-5.6.43-winx64.zip`解压到`D:\Program Files\javaTools\mysql-5.6.43-winx64`目录，当然这个目录可以自己随便指定

3. 配置环境变量
   将`D:\Program Files\javaTools\mysql-5.6.43-winx64\bin`添加到环境变量path下

4. 生成data文件
   以管理员身份运行cmd

   进入D:\Program Files\javaTools\mysql-5.6.43-winx64\bin 下

   执行命令：`mysqld --initialize-insecure --user=mysql`  在D:\Program Files\javaTools\mysql-5.6.43-winx64目录下生成data目录

5. 启动服务
   执行命令：`net start mysql`  启动mysql服务，若提示：服务名无效...，参考如下

6. 解决服务启动失败
   执行`mysqld -install`即可

7. 登录
   执行命令`mysql -u root -p`。因为之前没设置密码，所以密码为空，不用输入密码，直接回车即可

8. 设置root用户密码

   ```bash
   mysql> update mysql.user set password=password("root") where user="root";   
   Query OK, 1 row affected, 1 warning (0.00 sec)
   Rows matched: 1  Changed: 1  Warnings: 1
   
   mysql> flush privileges;  #作用：相当于保存，执行此命令后，设置才生效，若不执行，还是之前的密码不变
   Query OK, 0 rows affected (0.01 sec) 
   ```