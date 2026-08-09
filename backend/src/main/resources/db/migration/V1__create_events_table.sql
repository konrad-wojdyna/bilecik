

CREATE TABLE events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    venue VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    category VARCHAR(100),
    starts_at TIMESTAMPTZ NOT NULL,
    timezone VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    duration_minutes INT,
    min_age INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_events_capacity_positive CHECK (capacity > 0)
);