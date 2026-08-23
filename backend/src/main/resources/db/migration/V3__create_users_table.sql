
    CREATE TABLE users (
        id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
        email VARCHAR (255) NOT NULL,
        password_hash VARCHAR (255) NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
        updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

        CONSTRAINT uq_users_email UNIQUE (email),

        CONSTRAINT chk_users_email_not_empty CHECK (length(trim(email)) > 0),
        CONSTRAINT chk_users_email_lowercase CHECK (email = lower(email)),
        CONSTRAINT chk_users_email_format CHECK (email LIKE '%_@__%.__%'),

        CONSTRAINT chk_users_password_hash_not_empty CHECK (length(trim(password_hash)) > 0)
    );