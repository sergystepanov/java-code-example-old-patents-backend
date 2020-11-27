CREATE TABLE PROXY_SERVER
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    alias       VARCHAR(45)                        NOT NULL DEFAULT (''),
    platform    VARCHAR(45)                        NOT NULL DEFAULT (''),
    ip          VARCHAR(45)                        NOT NULL,
    traffic     INTEGER                            NOT NULL DEFAULT (0),
    status      VARCHAR(45)                        NOT NULL DEFAULT (''),
    requests    INTEGER                            NOT NULL DEFAULT (0),
    enabled     BOOLEAN                            NOT NULL DEFAULT (1),
    created_at  TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    updated_at  TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),
    accessed_at TIMESTAMP                          NOT NULL DEFAULT ('1970-01-01 00:00:00')
);

CREATE TABLE PROXY_SERVER_REQUEST
(
    id        INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    server_id INTEGER                            NOT NULL,
    date_     DATE                               NOT NULL DEFAULT (CURRENT_DATE),
    count     INTEGER                            NOT NULL,
    access    TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    FOREIGN KEY (server_id) REFERENCES PROXY_SERVER (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE VIEW PROXY_SERVER_REQUEST_MONTH_CHART
AS
SELECT FORMATDATETIME(access, 'Y-MM') AS period, SUM(count) AS requests
FROM PROXY_SERVER_REQUEST
GROUP BY FORMATDATETIME(access, 'Y-MM');
