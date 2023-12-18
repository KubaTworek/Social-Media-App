DROP TABLE IF EXISTS `Author_Post`;
CREATE TABLE IF NOT EXISTS Author_Post (
                             Id integer NOT NULL CONSTRAINT Author_pk PRIMARY KEY,
                             firstname varchar(40) NOT NULL,
                             lastname varchar(40) NOT NULL,
                             username varchar(40) NOT NULL
);

INSERT INTO `Author_Post`(`Id`, `firstname`, `lastname`, `username`) VALUES (1000, 'Anna', 'Kowalska', 'anna_k');
INSERT INTO `Author_Post`(`Id`, `firstname`, `lastname`, `username`) VALUES (1001, 'Tomasz', 'Nowak', 'tomek_nowak');
INSERT INTO `Author_Post`(`Id`, `firstname`, `lastname`, `username`) VALUES (1002, 'Katarzyna', 'Wiśniewska', 'kasia_w');
INSERT INTO `Author_Post`(`Id`, `firstname`, `lastname`, `username`) VALUES (1003, 'Jan', 'Kowalski', 'j_kowalski');
