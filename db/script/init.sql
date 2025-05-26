-- 数据库已经由MYSQL_DATABASE环境变量创建，这里只需要使用它
USE db_metadata;

-- 设置正确的字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 允许root用户从任何主机连接（root用户已存在，只需修改权限）
ALTER USER 'root'@'localhost' IDENTIFIED BY 'root123';
CREATE USER IF NOT EXISTS 'root'@'%' IDENTIFIED BY 'root123';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;

-- 确保dbmeta用户也有正确的权限（用户可能已经由环境变量创建）
-- 如果用户不存在则创建，如果存在则修改密码
CREATE USER IF NOT EXISTS 'dbmeta'@'%' IDENTIFIED BY 'dbmeta123';
ALTER USER 'dbmeta'@'%' IDENTIFIED BY 'dbmeta123';
GRANT ALL PRIVILEGES ON db_metadata.* TO 'dbmeta'@'%';
GRANT SELECT ON mysql.* TO 'dbmeta'@'%';

-- 如果本地用户存在，也给予权限
CREATE USER IF NOT EXISTS 'dbmeta'@'localhost' IDENTIFIED BY 'dbmeta123';
ALTER USER 'dbmeta'@'localhost' IDENTIFIED BY 'dbmeta123';
GRANT ALL PRIVILEGES ON db_metadata.* TO 'dbmeta'@'localhost';
GRANT SELECT ON mysql.* TO 'dbmeta'@'localhost';

-- 刷新权限
FLUSH PRIVILEGES; 