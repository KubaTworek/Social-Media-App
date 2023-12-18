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

INSERT INTO `userX`(`username`, `password`, `role_id`) values('admin', 'admin', 2);

INSERT INTO `userX`(`username`, `password`, `role_id`) VALUES ('anna_k', 'password', 1);
INSERT INTO `userX`(`username`, `password`, `role_id`) VALUES ('tomek_nowak', 'password', 1);
INSERT INTO `userX`(`username`, `password`, `role_id`) VALUES ('kasia_w', 'password', 1);
INSERT INTO `userX`(`username`, `password`, `role_id`) VALUES ('j_kowalski', 'password', 1);
