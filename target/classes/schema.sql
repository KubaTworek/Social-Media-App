-- tables
-- Table: Author
CREATE TABLE Author (
                        Id integer NOT NULL CONSTRAINT Author_pk PRIMARY KEY,
                        FirstName integer NOT NULL,
                        LastName integer NOT NULL
);

-- Table: Content
CREATE TABLE Content (
                         Id integer NOT NULL CONSTRAINT Content_pk PRIMARY KEY,
                         Title varchar(30) NOT NULL,
                         Text text NOT NULL
);

-- Table: Magazine
CREATE TABLE Magazine (
                          Id integer NOT NULL CONSTRAINT Magazine_pk PRIMARY KEY,
                          Name varchar(40) NOT NULL
);

-- Table: Article
CREATE TABLE Article (
                         Id integer NOT NULL CONSTRAINT Article_pk PRIMARY KEY,
                         Date date NOT NULL,
                         Timestamp integer NOT NULL,
                         Author_Id integer NOT NULL,
                         Magazine_Id integer NOT NULL,
                         Content_Id integer NOT NULL,
                         CONSTRAINT Article_Author FOREIGN KEY (Author_Id)
                             REFERENCES Author (Id),
                         CONSTRAINT Article_Magazine FOREIGN KEY (Magazine_Id)
                             REFERENCES Magazine (Id),
                         CONSTRAINT Article_Content FOREIGN KEY (Content_Id)
                             REFERENCES Content (Id)
);

-- End of file.