CREATE DATABASE IF NOT EXISTS `articlerest-db`;
USE `articlerest-db`;

CREATE TABLE `Author` (
                          `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `first_name` varchar(20) DEFAULT NULL,
                          `last_name` varchar(20) DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Magazine` (
                            `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `name` varchar(20) DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Content` (
                           `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `title` varchar(50) DEFAULT NULL,
                           `text` LONGTEXT DEFAULT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE `Article` (
                           `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `content_id` int DEFAULT NULL,
                           `date` date DEFAULT NULL,
                           `magazine_id` int DEFAULT NULL,
                           `author_id` int DEFAULT NULL,
                           `timestamp` BIGINT DEFAULT NULL,
                           FOREIGN KEY(`content_id`) REFERENCES `Content`(`id`),
                           FOREIGN KEY(`magazine_id`) REFERENCES `Magazine`(`id`),
                           FOREIGN KEY(`author_id`) REFERENCES `Author`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


INSERT INTO `Author` VALUES
                         (1,'Adam','Smith'),
                         (2,'John','Cook'),
                         (3,'Jerry','Kowalsky');

INSERT INTO `Magazine` VALUES
                           (1,'Times'),
                           (2,'Pudelek'),
                           (3,'WP');