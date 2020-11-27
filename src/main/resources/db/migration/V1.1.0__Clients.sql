--
-- CLIENTS
--

--
-- Stores data about all clients.
--
CREATE TABLE IF NOT EXISTS CLIENT
(
    id                 INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name               VARCHAR(255)                       NOT NULL,
    original_name      VARCHAR(255)                       NOT NULL DEFAULT (''),
    code               VARCHAR(64)                        NOT NULL DEFAULT (''),
    -- Used to determine if a client has the primary application as PCT
    pct_as_application BOOLEAN                            NOT NULL DEFAULT (0),
    created_at         TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    CONSTRAINT unique_client_name UNIQUE (name)
);

--
-- Stores specific clients' patent data template mappings for Excel-like input files.
--
CREATE TABLE IF NOT EXISTS CLIENT_IMPORT_TEMPLATE
(
    id              INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    client_id       INTEGER                            NOT NULL,
    field_map       VARCHAR(4096)                      NOT NULL, -- json
    patent_type_map VARCHAR(4096)                      NOT NULL, -- json

    FOREIGN KEY (client_id) REFERENCES CLIENT (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
