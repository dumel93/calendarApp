
CREATE TABLE IF NOT EXISTS companies
(
    company_id                 VARCHAR(36),
    company_name               varchar(100)
);
INSERT into companies values(UUID(), DATABASE());

CREATE TABLE IF NOT EXISTS users
(
    userId                 VARCHAR(36),
    position                 varchar(100),
    user_name                 varchar(100),
    password                 VARCHAR(100),
    default_time_zone            varchar(100),
    email                       varchar(100),
    company_id                 VARCHAR(36)
);
INSERT into users values(UUID() , 'admin', 'admin1', '$2a$12$xT/dMquWZDYTXzg3BX4dfe7rUabgALGAl8bn6nNRBR1cQ421J3qTO', 'Asia/Karachi', 'admin@gmail.com', (select company_id from companies where company_name=DATABASE()));

CREATE INDEX userId_ix
ON users (userId);

CREATE TABLE IF NOT EXISTS locations
(
    location_id          VARCHAR(36),
    name                 varchar(100),
    address              varchar(100),
    user_id               VARCHAR(36),
    PRIMARY KEY (location_id),
    FOREIGN KEY (user_id) REFERENCES users(userId)
);

CREATE TABLE IF NOT EXISTS meetings
(
    meeting_id                 VARCHAR(36),
    name                 varchar(100),
    agenda                 varchar(100),
    start                 TIMESTAMP,
    end                 TIMESTAMP,
    location_id                 VARCHAR(36),
    owner_id              VARCHAR(36),
    PRIMARY KEY (meeting_id),
    FOREIGN KEY (owner_id) REFERENCES users(userId)
);


CREATE TABLE IF NOT EXISTS users_meeting (
  user_id varchar(36) NOT NULL,
  meeting_id varchar(36) NOT NULL,
  PRIMARY KEY (user_id, meeting_id),
    CONSTRAINT users_meeting_ibfk_1
   FOREIGN KEY (meeting_id) REFERENCES meetings(meeting_id),
     CONSTRAINT users_meeting_ibfk_2
   FOREIGN KEY (user_id) REFERENCES users(userId)
);


