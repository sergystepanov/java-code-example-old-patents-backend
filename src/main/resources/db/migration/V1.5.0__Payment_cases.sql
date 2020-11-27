--
-- PROPERTY CASES
--
-- Stores the information about some property payment work which needs to be done.
--
-- includes V1.3.0__Case_drafts.sql
--

--
-- Stores property cases.
--
CREATE TABLE IF NOT EXISTS PROPERTY_CASE
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255)                       NOT NULL,
    client_id  INTEGER                            NOT NULL,
    user_id    INTEGER                            NOT NULL,
    closed     BOOLEAN                            NOT NULL DEFAULT (FALSE),
    archived   BOOLEAN                            NOT NULL DEFAULT (FALSE),
    created_at TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    CONSTRAINT unique_case UNIQUE (name, client_id, user_id),

    FOREIGN KEY (client_id) REFERENCES CLIENT (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (user_id) REFERENCES USER (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION
);

--
-- Stores attached to a property case files.
--
CREATE TABLE IF NOT EXISTS PROPERTY_CASE_FILE
(
    id         INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    case_id    INTEGER                            NOT NULL,
    -- Types are simple file filtering or distinction method.
    type       VARCHAR(64)                        NOT NULL DEFAULT (''),
    -- File path will be relative to fs root of the app.
    path       VARCHAR(255)                       NOT NULL,
    filename   VARCHAR(255)                       NOT NULL,
    created_at TIMESTAMP                          NOT NULL DEFAULT (CURRENT_TIMESTAMP),

    FOREIGN KEY (case_id) REFERENCES PROPERTY_CASE (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

--
-- Stores property case records.
--
CREATE TABLE IF NOT EXISTS PROPERTY_CASE_RECORD
(
    id                             INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL,
    property_case_id               INTEGER                            NOT NULL,
    property_id                    INTEGER                            NOT NULL,
    checked                        BOOLEAN                            NOT NULL DEFAULT (FALSE),

    -- Payment stuff.
    pay_office                     INTEGER                            NOT NULL,
    -- Some offices have separate payments for each country.
    -- ISO 3166-1 alpha-2.
    pay_country_iso_code           VARCHAR(2)                         NOT NULL,
    -- A payment due date (former due_date).
    pay_deadline                   DATE                               NOT NULL,
    -- Payment period (one or multiple years).
    pay_annuity_start_year         TINYINT                            NOT NULL DEFAULT (0),
    pay_annuity_end_year           TINYINT                            NOT NULL DEFAULT (1),
    -- Payment amount of the client (as provided).
    pay_client_amount              DECIMAL(10, 2)                     NOT NULL DEFAULT (0.00),
    pay_client_fine                DECIMAL(10, 2)                     NOT NULL DEFAULT (0.00),
    -- It can be different for records in one case.
    pay_client_conversion_course   DECIMAL(10, 2)                     NOT NULL DEFAULT (1.00),
    -- Final payment amount with fee included calculated for registries in app's currency.
    pay_amount                     DECIMAL(10, 2)                     NOT NULL DEFAULT (0.00),
    pay_fine                       DECIMAL(10, 2)                     NOT NULL DEFAULT (0.00),
    -- A human-readable total payment breakdown.
    pay_amount_formula_description VARCHAR(255)                       NOT NULL DEFAULT (''),
    -- Our fee. Reserved for future.
    pay_profit_amount              DECIMAL(10, 2)                     NOT NULL DEFAULT (0.00),
    -- A date on which all the payment amounts will be fixated.
    -- If it's not specified than it's not fixed.
    pay_fixation_date              DATE,
    -- A list of included files along with the case.
    pay_covering_letter_file       INTEGER                                     DEFAULT (NULL),
    pay_export_file                INTEGER                                     DEFAULT (NULL),
    pay_registry_confirmation_file INTEGER                                     DEFAULT (NULL),
    -- Payment double check.
    -- Confirms that payment was made from the client to us.
    pay_status_client_sent         BOOLEAN                            NOT NULL DEFAULT (FALSE),
    -- Confirms that payment was made by us.
    pay_status_we_paid             BOOLEAN                            NOT NULL DEFAULT (FALSE),
    -- Confirms that payment visible in the official registries.
    pay_status_registry_confirmed  BOOLEAN                            NOT NULL DEFAULT (FALSE),

    CONSTRAINT unique_case_property UNIQUE (property_case_id, property_id),

    FOREIGN KEY (property_case_id) REFERENCES PROPERTY_CASE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (property_id) REFERENCES PROPERTY (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (pay_office) REFERENCES PATENT_OFFICE (id)
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    FOREIGN KEY (pay_covering_letter_file) REFERENCES PROPERTY_CASE_FILE (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (pay_export_file) REFERENCES PROPERTY_CASE_FILE (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    FOREIGN KEY (pay_registry_confirmation_file) REFERENCES PROPERTY_CASE_FILE (id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);
