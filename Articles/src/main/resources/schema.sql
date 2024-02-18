DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS ARTICLES;

CREATE TABLE IF NOT EXISTS ARTICLES
(
    ARTICLE_ID        integer AUTO_INCREMENT,
    CREATE_AT         timestamp     NOT NULL,
    CONTENT           varchar(4000) NOT NULL,
    AUTHOR_ID         integer       NOT NULL,
    MOTHER_ARTICLE_ID integer,
    PRIMARY KEY (ARTICLE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    LIKE_ID    integer AUTO_INCREMENT,
    CREATE_AT  timestamp NOT NULL,
    AUTHOR_ID  integer   NOT NULL,
    ARTICLE_ID integer   NOT NULL,
    PRIMARY KEY (LIKE_ID)
);

ALTER TABLE LIKES
    ADD CONSTRAINT FK_LIKES__ARTICLE_ID_TO_ARTICLES FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLES (ARTICLE_ID);

ALTER TABLE ARTICLES
    ADD CONSTRAINT FK_ARTICLES__MOTHER_ARTICLE_ID_TO_ARTICLES FOREIGN KEY (MOTHER_ARTICLE_ID) REFERENCES ARTICLES (ARTICLE_ID);

CREATE INDEX ARTICLES__AUTHOR_ID ON ARTICLES (AUTHOR_ID);

INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1000, '2023-12-17 08:30:00.000',
        'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin in neque at quam euismod consectetur. Sed ultrices justo sit amet purus facilisis, ac accumsan erat cursus. Sed interdum ex et ultrices scelerisque. Phasellus nec justo vitae nulla tincidunt cursus. Nulla facilisi. Sed venenatis, quam a cursus accumsan, ligula felis gravida neque, ut tincidunt augue augue vel orci. Morbi eu imperdiet felis. Donec scelerisque elit nec libero egestas, a elementum mi pellentesque.',
        1000, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1001, '2023-12-17 10:15:45.123',
        'Vivamus lobortis vestibulum ex, vel mattis libero cursus eget. Nulla eget dictum elit. Integer ut imperdiet elit. Fusce sodales, libero a efficitur hendrerit, tortor ex sollicitudin risus, a eleifend libero lectus a tellus. Fusce interdum massa vel tristique dignissim. Fusce nec orci sed purus volutpat lacinia eget vel libero. Phasellus vestibulum efficitur massa, vel gravida tortor fringilla eu.',
        1001, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1002, '2023-12-17 12:00:30.987',
        'Proin scelerisque velit ut odio bibendum, sit amet ullamcorper mi pharetra. Aenean vel magna sed quam consectetur tristique. Nullam tempus facilisis risus, eu malesuada sapien interdum vel. In hac habitasse platea dictumst. Phasellus vel congue orci, in efficitur erat. Aliquam ac lacus eu lacus sodales vehicula vel vel lacus. Suspendisse at nunc non turpis ultrices fermentum.',
        1002, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1003, '2023-12-17 13:45:15.000',
        'Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Quisque nec dui nec leo tristique tincidunt. Sed sagittis, ex vel tincidunt tempor, nunc mi aliquam metus, vel vehicula orci odio a tellus. Sed interdum malesuada elit, a scelerisque urna egestas vitae. Fusce vitae dui eu odio laoreet eleifend. Morbi nec feugiat elit, sit amet pellentesque nulla. Sed sed felis non justo sodales efficitur et ac nisi.',
        1003, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1004, '2023-12-17 15:30:00.789',
        'Sed fringilla mauris sit amet nibh malesuada, sit amet accumsan nulla auctor. Etiam ut lectus ut nisi semper cursus. Sed sollicitudin dolor vel turpis consequat, at vestibulum eros accumsan. Morbi vel eros et elit iaculis hendrerit nec quis nunc. Curabitur eget metus nec tellus bibendum condimentum. Integer consectetur nec turpis vel sagittis. Integer vitae aliquam augue.',
        1000, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1005, '2023-12-17 17:15:45.456',
        'Fusce nec tellus sed augue semper porta. Maecenas varius volutpat ligula, in consequat felis. Proin eget erat ac arcu scelerisque cursus. Maecenas tempus lacus a metus aliquet, id feugiat metus auctor. Nullam quis feugiat odio. Nunc vel tortor justo. Ut vel sollicitudin risus. Vivamus vehicula purus non orci volutpat, a cursus quam dictum.',
        1001, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1006, '2023-12-17 19:00:30.000',
        'Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Curabitur vel lacus ut odio tempus tincidunt. Duis varius accumsan sem. Aenean cursus feugiat quam ut cursus. Etiam volutpat enim ut turpis cursus, ac venenatis mi efficitur. Nulla facilisi. Integer at finibus turpis, id ultrices velit. Nulla facilisi. Suspendisse potenti.',
        1002, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1007, '2023-12-17 20:45:15.321',
        'Sed dignissim lacinia nunc. Proin vel urna hendrerit, varius ex in, volutpat metus. Nam in est ut lectus viverra tincidunt. Nullam id tortor orci. Nullam id mi euismod, rhoncus arcu et, scelerisque libero. Nullam malesuada accumsan quam, nec gravida felis commodo in. Sed vestibulum urna eu metus feugiat, eu varius neque tincidunt. Morbi hendrerit mi sed dui venenatis, id fringilla nunc venenatis.',
        1003, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1008, '2023-12-17 22:30:00.000',
        'Aenean quam. Phasellus id odio eget erat varius interdum vel in justo. Nunc in metus nec quam viverra interdum. Duis tincidunt neque at justo consectetur, nec hendrerit libero rhoncus. In hac habitasse platea dictumst. Sed et congue justo. Pellentesque congue odio ac lectus fermentum, non bibendum libero ultricies. Vestibulum eu sem justo.',
        1000, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1009, '2023-12-17 00:15:45.987',
        'Sed convallis tristique sem. Proin ut ligula vel nunc egestas porttitor. Sed nec justo in elit aliquet vestibulum at nec tellus. Suspendisse potenti. Nulla facilisi. Integer tincidunt metus vel nunc bibendum facilisis. Phasellus at hendrerit nunc, ut aliquam velit. Morbi vel fermentum odio, a cursus felis. Mauris eu tortor turpis.',
        1001, NULL);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1010, '2023-12-18 02:00:30.654',
        'Curabitur a felis in nunc fringilla tristique. Sed ultricies, ex ut tempus feugiat, augue felis consectetur mauris, in facilisis turpis purus sit amet nunc. Sed at sem vel erat vestibulum imperdiet in vel quam. Integer consequat massa eget libero dictum, eu imperdiet neque tempus. Sed vel sem in est facilisis blandit a nec ipsum.',
        1002, 1001);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1011, '2023-12-18 03:45:15.000',
        'Phasellus gravida semper nisi. Vivamus et massa arcu. Etiam consectetur justo vel ligula aliquam, ut tincidunt ligula fringilla. Sed id velit risus. Duis at tristique elit. Etiam consectetur, ex id hendrerit aliquet, augue urna suscipit libero, at malesuada purus risus vel elit. Fusce id ex vel quam ultrices cursus. Maecenas nec venenatis dui.',
        1003, 1001);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1012, '2023-12-18 05:30:00.123',
        'Integer ut neque. Vivamus nisi metus, molestie vel, gravida in, condimentum sit amet, nunc. Sed rhoncus dapibus sapien a posuere. Integer in arcu justo. Ut volutpat sem vel mi euismod hendrerit. Suspendisse potenti. Maecenas non nulla eget urna ultrices feugiat vel a est. Quisque nec sem sed ligula malesuada tristique.',
        1000, 1001);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1013, '2023-12-18 07:15:45.789',
        'Praesent ac massa at ligula laoreet iaculis. Nulla neque dolor, sagittis eget, iaculis quis, molestie non, velit. Sed tristique quam id nisi tincidunt, a sollicitudin libero volutpat. Duis eget feugiat nisl, vel sollicitudin justo. Nullam eu lacus nec dui fermentum sollicitudin. Sed varius ligula at metus dictum bibendum. Ut bibendum cursus ligula a fermentum.',
        1001, 1002);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1014, '2023-12-18 09:00:30.000',
        'Morbi purus libero, faucibus adipiscing, commodo quis, gravida id, est. Duis nec augue eu libero fermentum vulputate. Etiam tincidunt dolor nec augue eleifend, vel sagittis mi efficitur. Sed cursus tortor nec ligula fringilla blandit. Vestibulum quis augue at ex dapibus vulputate. Nullam hendrerit tellus id arcu blandit, at dictum ipsum cursus. Sed id massa a ligula hendrerit cursus eget eu ex.',
        1002, 1002);
INSERT INTO ARTICLES(ARTICLE_ID, CREATE_AT, CONTENT, AUTHOR_ID, MOTHER_ARTICLE_ID)
VALUES (1015, '2023-12-18 10:45:15.456',
        'Sed lectus. Praesent elementum hendrerit tortor. Sed semper lorem at felis hendrerit, et fringilla nulla fringilla. In vel ex vitae quam venenatis maximus. Quisque eu purus sed leo scelerisque malesuada. Aenean pharetra, risus vel dictum venenatis, lectus quam tincidunt libero, sit amet hendrerit augue turpis sit amet justo. Duis eget ligula non leo feugiat hendrerit vel a urna. Vivamus sit amet accumsan turpis, nec ullamcorper quam.',
        1003, 1003);


INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1000, '2023-12-17 08:32:00.000', 1000, 1000);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1001, '2023-12-17 08:35:00.000', 1001, 1000);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1002, '2023-12-17 10:17:45.123', 1000, 1001);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1003, '2023-12-17 10:19:45.123', 1001, 1001);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1004, '2023-12-17 10:20:45.123', 1002, 1001);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1005, '2023-12-17 12:03:30.987', 1000, 1002);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1006, '2023-12-17 13:48:15.000', 1000, 1003);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1007, '2023-12-17 13:52:15.000', 1001, 1003);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1008, '2023-12-17 13:55:15.000', 1002, 1003);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1009, '2023-12-17 13:58:15.000', 1003, 1003);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1010, '2023-12-17 15:32:00.789', 1000, 1004);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1011, '2023-12-17 15:35:00.789', 1001, 1004);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1012, '2023-12-17 17:17:45.456', 1000, 1005);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1013, '2023-12-17 17:18:45.456', 1001, 1005);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1014, '2023-12-17 19:03:30.000', 1000, 1006);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1015, '2023-12-17 19:04:30.000', 1001, 1006);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1016, '2023-12-17 19:07:30.000', 1002, 1006);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1017, '2023-12-17 20:48:15.321', 1000, 1007);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1018, '2023-12-17 22:32:00.000', 1000, 1008);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1019, '2023-12-17 22:35:00.000', 1001, 1008);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1020, '2023-12-17 22:32:00.000', 1002, 1008);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1021, '2023-12-17 22:36:00.000', 1003, 1008);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1022, '2023-12-17 00:18:45.987', 1000, 1009);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1023, '2023-12-17 00:19:45.987', 1001, 1009);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1024, '2023-12-18 02:01:30.654', 1000, 1010);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1025, '2023-12-18 02:05:30.654', 1001, 1010);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1026, '2023-12-18 03:47:15.000', 1000, 1011);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1027, '2023-12-18 03:48:15.000', 1001, 1011);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1028, '2023-12-18 03:49:15.000', 1002, 1011);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1029, '2023-12-18 05:32:00.123', 1000, 1012);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1030, '2023-12-18 07:16:45.789', 1000, 1013);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1031, '2023-12-18 07:18:45.789', 1001, 1013);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1032, '2023-12-18 07:19:45.789', 1002, 1013);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1033, '2023-12-18 07:21:45.789', 1003, 1013);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1034, '2023-12-18 09:02:30.000', 1000, 1014);
INSERT INTO LIKES(LIKE_ID, CREATE_AT, AUTHOR_ID, ARTICLE_ID)
VALUES (1035, '2023-12-18 09:05:30.000', 1001, 1014);