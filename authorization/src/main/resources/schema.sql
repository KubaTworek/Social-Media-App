DROP TABLE IF EXISTS `USERS`;

CREATE TABLE IF NOT EXISTS `USERS` (
    `USERNAME` VARCHAR(50),
    `PASSWORD` VARCHAR(255) NOT NULL,
    `ROLE` VARCHAR(10),
    PRIMARY KEY (USERNAME)
);

INSERT INTO `USERS`(`USERNAME`, `PASSWORD`, `ROLE`) VALUES ('admin', 'admin', 'ROLE_ADMIN');
INSERT INTO `USERS`(`USERNAME`, `PASSWORD`, `ROLE`) VALUES ('anna_k', 'password', 'ROLE_USER');
INSERT INTO `USERS`(`USERNAME`, `PASSWORD`, `ROLE`) VALUES ('tomek_nowak', 'password', 'ROLE_USER');
INSERT INTO `USERS`(`USERNAME`, `PASSWORD`, `ROLE`) VALUES ('kasia_w', 'password', 'ROLE_USER');
INSERT INTO `USERS`(`USERNAME`, `PASSWORD`, `ROLE`) VALUES ('j_kowalski', 'password', 'ROLE_USER');