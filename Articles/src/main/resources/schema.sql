-- Table: ArticlePost
CREATE TABLE Article_Post (
                         Id integer NOT NULL CONSTRAINT Article_pk PRIMARY KEY,
                         Date varchar(10) NOT NULL,
                         Timestamp varchar(255) NOT NULL,
                         Text varchar(255) NOT NULL,
                         Author_Id integer NOT NULL
);

-- End of file.
