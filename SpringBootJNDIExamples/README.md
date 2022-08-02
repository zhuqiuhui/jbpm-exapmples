### 1. mysql install
- install address:https://dev.mysql.com/downloads/file/?id=511553
- username and password example:
> - username：root
> - password:123456
- mysql statement init
```myql
create database jbpm;
DROP TABLE IF EXISTS `kpi_role`;

CREATE TABLE `kpi_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(255) NOT NULL,
  `status` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_role` */

insert  into `kpi_role`(`id`,`rolename`,`status`) values 
(1,'Administrators',1),
(2,'PM',1),
(3,'SVP',1),
(4,'EVP',1),
(5,'Admin',1);

/*Table structure for table `kpi_user` */

DROP TABLE IF EXISTS `kpi_user`;

CREATE TABLE `kpi_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `age` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_user` */

insert  into `kpi_user`(`id`,`username`,`password`,`age`,`status`) values 
(1,'Nero','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',23,1),
(2,'JOJO','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',17,1),
(3,'Test','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',99,1),
(4,'Administrator','$2a$10$6NQ7ZWOGHT.W7JhlIa6hQOpABmGlBW7Essr5m78Pu.S3Gav7rha/K',100,1);

/*Table structure for table `kpi_user_role` */

DROP TABLE IF EXISTS `kpi_user_role`;

CREATE TABLE `kpi_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `uid` (`uid`),
  KEY `rid` (`rid`),
  CONSTRAINT `kpi_user_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `kpi_role` (`id`),
  CONSTRAINT `kpi_user_role_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `kpi_user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `kpi_user_role` */

insert  into `kpi_user_role`(`id`,`uid`,`rid`) values 
(1,1,1),
(2,2,3),
(3,3,4),
(4,1,2),
(5,1,3),
(6,2,4),
(7,2,1),
(8,1,5),
(9,2,5),
(10,3,5),
(11,4,1);
 - ```

### 2. start mysql server：
### 3. run spring boot application, see Application.java
use user.properties login 
### 4. visit follow url:
- case 0: validate jpa query：http://localhost:8080/spring/kpi/getUserByUsername/Nero
- case 1: http://localhost:8080/spring/kpi/error
- case 2：http://localhost:8080/spring/kpi/index
  // Test 用户先登录，且startProcess，后Nero再登录，进行 getTask
- case 3：http://localhost:8080/spring/login?username=Test&password=123456
- case 4：http://localhost:8080/spring/kpi/startProcess?userId=Test
- case 5: http://localhost:8080/spring/login?username=Nero&password=123456
- case 6：http://localhost:8080/spring/kpi/getTask

note：in process UserTask_1 is executed by PM role, UserTask_2 is executed by AM role,
