-- Table: ArticlePost
CREATE TABLE Article_Post (
                         Id integer NOT NULL CONSTRAINT Article_pk PRIMARY KEY,
                         Date varchar(10) NOT NULL,
                         Timestamp varchar(255) NOT NULL,
                         Text varchar(255) NOT NULL,
                         Author_Id integer NOT NULL
);

CREATE TABLE Like_Article (
                              Id integer NOT NULL CONSTRAINT Like_pk PRIMARY KEY,
                              Timestamp varchar(255) NOT NULL,
                              Author_Id integer NOT NULL,
                              Article_Id integer NOT NULL,
                              FOREIGN KEY (Article_Id) REFERENCES Article_Post(Id)
);

-- End of file.
