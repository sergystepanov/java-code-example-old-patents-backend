--
-- USERS & ROLES
--

--
-- Stores data of users.
--
CREATE TABLE IF NOT EXISTS USER
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name       VARCHAR(128)                       NOT NULL,
    username   VARCHAR(64)                        NOT NULL,
    email      VARCHAR(256)                       NOT NULL,
    password   VARCHAR(128)                       NOT NULL,
    created_at TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    CONSTRAINT unique_user_username UNIQUE (username),
    CONSTRAINT unique_user_email UNIQUE (email)
);

--
-- Stores data of roles.
--
CREATE TABLE IF NOT EXISTS ROLE
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name VARCHAR(64)                        NOT NULL,

    CONSTRAINT unique_role_name UNIQUE (name)
);

--
-- Stores data of user roles.
--
CREATE TABLE IF NOT EXISTS USER_ROLE
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,

    PRIMARY KEY (user_id, role_id),

    FOREIGN KEY (user_id) REFERENCES USER (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (role_id) REFERENCES ROLE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

CREATE INDEX IDX_USER_ROLE ON USER_ROLE (role_id);

--
-- DEFAULT ROLES
--

INSERT INTO ROLE (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');
