DROP TABLE IF EXISTS `LIKES`;
DROP TABLE IF EXISTS ARTICLES;

CREATE TABLE IF NOT EXISTS ARTICLES
(
    ARTICLE_ID integer AUTO_INCREMENT,
    CREATE_AT  timestamp     NOT NULL,
    CONTENT    varchar(4000) NOT NULL,
    AUTHOR_ID  integer       NOT NULL,
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

ALTER TABLE `LIKES`
    ADD CONSTRAINT FK_LIKES__ARTICLE_ID_TO_ARTICLES FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLES (ARTICLE_ID);

CREATE INDEX ARTICLES__AUTHOR_ID ON ARTICLES (AUTHOR_ID);