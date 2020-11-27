--
-- Stores draft data for a case with properties.
--
CREATE TABLE IF NOT EXISTS PROPERTY_CASE_FILE_DRAFT
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)                       NOT NULL DEFAULT (''),
    checksum   VARCHAR(64)                        NOT NULL,
    version    VARCHAR(2)                         NOT NULL,
    text       VARCHAR                            NULL     DEFAULT (''),
    created_at TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP)
);
