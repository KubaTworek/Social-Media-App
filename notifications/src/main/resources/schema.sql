CREATE TABLE Notification (
                              Id integer NOT NULL CONSTRAINT Notification_pk PRIMARY KEY,
                              Article_Id integer NOT NULL,
                              Author_Id integer NOT NULL,
                              Timestamp varchar(255) NOT NULL,
                              Type varchar(255) NOT NULL
);
