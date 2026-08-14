

CREATE TABLE ticket_pools (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    note VARCHAR(100),
    price_grosze BIGINT NOT NULL,
    quantity INT NOT NULL,
    sold INT NOT NULL DEFAULT 0,
    event_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT fk_ticket_pools_event FOREIGN KEY (event_id) REFERENCES events(id)
                          ON DELETE RESTRICT,
    CONSTRAINT uq_ticket_pools_event_name UNIQUE (event_id, name),
    CONSTRAINT chk_ticket_pools_quantity_more_than_zero CHECK ( quantity > 0 ),
    CONSTRAINT chk_ticket_pools_sold_range CHECK (quantity >= sold AND sold >= 0),
    CONSTRAINT chk_ticket_pools_price_groszy_non_negative CHECK (price_grosze >= 0)
);