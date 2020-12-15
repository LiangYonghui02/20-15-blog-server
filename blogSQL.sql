/*
SQLyog Ultimate v12.14 (64 bit)
MySQL - 5.7.32-log : Database - fafy-blog
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE
DATABASE /*!32312 IF NOT EXISTS*/`fafy-blog` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE
`fafy-blog`;

/*Table structure for table `article` */

DROP TABLE IF EXISTS `article`;

CREATE TABLE `article`
(
    `id`            bigint(64) NOT NULL,
    `title`         varchar(64)  DEFAULT NULL,
    `create_time`   datetime     DEFAULT NULL,
    `update_time`   datetime     DEFAULT NULL,
    `summary`       varchar(256) DEFAULT NULL,
    `poll_count`    int(11) DEFAULT NULL,
    `comment_count` int(11) DEFAULT NULL,
    `read_count`    int(11) DEFAULT NULL,
    `writer`        varchar(64)  DEFAULT NULL,
    `tag_id`        bigint(64) DEFAULT NULL,
    `class_id`      bigint(64) DEFAULT NULL,
    `is_del`        int(11) DEFAULT '0',
    `is_open`       int(11) DEFAULT '1',
    `user_id`       bigint(64) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQUE` (`user_id`),
    CONSTRAINT `FK_article_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `article` */

/*Table structure for table `article_detail` */

DROP TABLE IF EXISTS `article_detail`;

CREATE TABLE `article_detail`
(
    `id`         bigint(64) NOT NULL,
    `article_id` bigint(64) DEFAULT NULL,
    `content`    text,
    `text`       text,
    PRIMARY KEY (`id`),
    KEY          `FK_article_detail_article_id` (`article_id`),
    CONSTRAINT `FK_article_detail_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `article_detail` */

/*Table structure for table `blog` */

DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog`
(
    `id`       bigint(20) NOT NULL,
    `user_id`  bigint(20) DEFAULT NULL,
    `blog_url` varchar(128) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY        `FK_blog_user_id` (`user_id`),
    CONSTRAINT `FK_blog_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `blog` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment`
(
    `id`             bigint(20) NOT NULL,
    `time`           datetime     DEFAULT NULL,
    `content`        varchar(512) DEFAULT NULL,
    `user_id`        bigint(20) DEFAULT NULL,
    `article_id`     bigint(20) DEFAULT NULL,
    `father_comment` bigint(20) DEFAULT NULL,
    `poll_count`     int(11) DEFAULT NULL,
    `nickname`       varchar(64)  DEFAULT NULL,
    `to_nickname`    varchar(64)  DEFAULT NULL,
    `to_user_id`     bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY              `FK_comment_user_id` (`user_id`),
    KEY              `FK_comment_to_user_id` (`to_user_id`),
    KEY              `FK_comment_father_article_id` (`article_id`),
    KEY              `FK_comment_father_comment` (`father_comment`),
    CONSTRAINT `FK_comment_father_article_id` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_comment_father_comment` FOREIGN KEY (`father_comment`) REFERENCES `comment` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_comment_to_user_id` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `comment` */

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message`
(
    `id`              bigint(20) NOT NULL,
    `from_id`         bigint(20) DEFAULT NULL,
    `to_id`           bigint(20) DEFAULT NULL,
    `conversation_id` varchar(256) DEFAULT NULL,
    `content`         varchar(512) DEFAULT NULL,
    `status`          int(11) DEFAULT '0',
    `create_time`     datetime     DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY               `FK_message_to_id` (`to_id`),
    KEY               `FK_message_from_id` (`from_id`),
    CONSTRAINT `FK_message_from_id` FOREIGN KEY (`from_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `FK_message_to_id` FOREIGN KEY (`to_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `message` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user`
(
    `id`       bigint(20) NOT NULL,
    `nickname` varchar(64) DEFAULT NULL,
    `email`    varchar(64) DEFAULT NULL,
    `password` varchar(64) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user` */

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info`
(
    `id`         bigint(20) NOT NULL,
    `user_id`    bigint(20) DEFAULT NULL,
    `nickname`   varchar(32)  DEFAULT NULL,
    `avatar_url` varchar(128) DEFAULT NULL,
    `signature`  varchar(64)  DEFAULT NULL,
    `intro`      varchar(512) DEFAULT NULL,
    `tel`        varchar(32)  DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UN_user_id` (`user_id`),
    CONSTRAINT `FK_user_info_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `user_info` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
