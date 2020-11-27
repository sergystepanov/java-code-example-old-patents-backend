--
-- PATENT OFFICES
--

--
-- Stores data of various patent and trademark offices.
--
CREATE TABLE IF NOT EXISTS PATENT_OFFICE
(
    id   INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    code VARCHAR(64)                        NOT NULL CHECK (code <> ''),

    CONSTRAINT unique_patent_office_code UNIQUE (code)
);


--
-- PROPERTIES
--

--
-- Stores property types.
--
CREATE TABLE IF NOT EXISTS PROPERTY_TYPE
(
    id             INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    code           VARCHAR(64)                        NOT NULL CHECK (code <> ''),
    duration_years INTEGER                            NOT NULL DEFAULT (1),
    name           VARCHAR(255)                       NOT NULL DEFAULT (''),
    description    VARCHAR(255)                       NOT NULL DEFAULT (''),

    CONSTRAINT unique_property_type_code UNIQUE (code)
);
--
-- Stores various properties.
--
CREATE TABLE IF NOT EXISTS PROPERTY
(
    id                  INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    -- ex grant_no
    registration_number VARCHAR(64)                        NOT NULL DEFAULT (''),
    type                INTEGER                            NOT NULL,
    patent_office       INTEGER                            NOT NULL,
    nation              VARCHAR(4)                         NOT NULL,
    application_no      VARCHAR(64)                        NOT NULL,
    application_no_ex   VARCHAR(32)                        NOT NULL DEFAULT (''),
    application_date    DATE                                        DEFAULT (''),
    grant_date          DATE                               NOT NULL,
    description         VARCHAR(255)                       NOT NULL DEFAULT (''),
    -- CHECK (status IN ('no_data', 'active', 'can_be_terminated', 'terminated_can_be_recovered', 'terminated', 'inactive', 'archive'))
    status              VARCHAR(64)                        NOT NULL DEFAULT ('no_data'),
    open_license        BOOLEAN                            NOT NULL DEFAULT (0),

    FOREIGN KEY (type) REFERENCES PROPERTY_TYPE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (patent_office) REFERENCES PATENT_OFFICE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);
--
-- Stores property owner.
--
CREATE TABLE IF NOT EXISTS PROPERTY_OWNER
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255)                       NOT NULL,
    entity_status VARCHAR(64)                        NOT NULL DEFAULT (''),

    CONSTRAINT unique_property_owner_name UNIQUE (name)
);
--
-- Stores all owners of a property.
--
CREATE TABLE IF NOT EXISTS PROPERTY_OWNERS
(
    property_id       INTEGER NOT NULL,
    property_owner_id INTEGER NOT NULL,

    PRIMARY KEY (property_id, property_owner_id)
);

--
-- Stores fees.
--
CREATE TABLE IF NOT EXISTS PATENT_OFFICE_FEE
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    property_type INTEGER                            NOT NULL,
    patent_office INTEGER                            NOT NULL,
    year          INTEGER                            NOT NULL,
    since         DATE                               NOT NULL DEFAULT ('1922-12-30'),
    cost          REAL                               NOT NULL DEFAULT (0.00),
    country       VARCHAR(4)                         NOT NULL DEFAULT 'RU',
    currency      VARCHAR(8)                         NOT NULL DEFAULT 'RUB',
    part          VARCHAR(255)                       NOT NULL DEFAULT '',

    CONSTRAINT unique_fee_record UNIQUE (property_type, patent_office, year, country, since),

    FOREIGN KEY (property_type) REFERENCES PROPERTY_TYPE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (patent_office) REFERENCES PATENT_OFFICE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);
