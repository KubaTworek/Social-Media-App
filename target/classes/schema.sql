CREATE DATABASE IF NOT EXISTS `articlerest-db`;
USE `articlerest-db`;

CREATE TABLE IF NOT EXISTS `Author` (
                          `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          `first_name` varchar(20) NOT NULL,
                          `last_name` varchar(20) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `Magazine` (
                            `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                            `name` varchar(20) NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `Content` (
                           `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `title` varchar(50) NOT NULL,
                           `text` LONGTEXT NOT NULL
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `Article` (
                           `id` int NOT NULL AUTO_INCREMENT PRIMARY KEY,
                           `content_id` int NOT NULL,
                           `date` date NOT NULL,
                           `magazine_id` int NOT NULL,
                           `author_id` int NOT NULL,
                           `timestamp` BIGINT DEFAULT NULL,
                           FOREIGN KEY(`content_id`) REFERENCES `Content`(`id`),
                           FOREIGN KEY(`magazine_id`) REFERENCES `Magazine`(`id`),
                           FOREIGN KEY(`author_id`) REFERENCES `Author`(`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;