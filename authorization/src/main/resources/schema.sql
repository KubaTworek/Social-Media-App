CREATE TABLE IF NOT EXISTS `authorities` (
                                             `id` int NOT NULL UNIQUE PRIMARY KEY,
                                             `authority` VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS `userX` (
                                       `username` VARCHAR(50) NOT NULL UNIQUE PRIMARY KEY,
                                       `password` VARCHAR(255) NOT NULL,
                                       `role_id` int,
                                       FOREIGN KEY(`role_id`) REFERENCES `authorities`(`id`)
);

INSERT INTO `authorities`(`id`, `authority`) values(1, 'ROLE_USER');
INSERT INTO `authorities`(`id`, `authority`) values(2, 'ROLE_ADMIN');