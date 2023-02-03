-- tables
-- Table: AuthorPost


-- Table: Content
CREATE TABLE Content (
                         Id integer NOT NULL CONSTRAINT Content_pk PRIMARY KEY,
                         Title varchar(30) NOT NULL,
                         Text varchar(255) NOT NULL
);

-- Table: MagazinePost


-- Table: ArticlePost
CREATE TABLE Article_Post (
                         Id integer NOT NULL CONSTRAINT Article_pk PRIMARY KEY,
                         Date date NOT NULL,
                         Timestamp timestamp NOT NULL,
                         Author_Id integer NOT NULL,
                         Magazine_Id integer NOT NULL,
                         Content_Id integer NOT NULL,
                         CONSTRAINT Article_Author FOREIGN KEY (Author_Id)
                             REFERENCES Author_Post (Id),
                         CONSTRAINT Article_Magazine FOREIGN KEY (Magazine_Id)
                             REFERENCES Magazine_Post (Id),
                         CONSTRAINT Article_Content FOREIGN KEY (Content_Id)
                             REFERENCES Content (Id)
);

-- End of file.
