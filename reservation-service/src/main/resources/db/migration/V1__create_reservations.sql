CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE reservations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id VARCHAR(20) NOT NULL,
    machine_id INT NOT NULL,
    machine_type VARCHAR(6) NOT NULL,

    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,

    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    
    CONSTRAINT valid_reservation_time
        CHECK (end_time > start_time),

    CONSTRAINT no_overlapping_reservations
        EXCLUDE USING GIST (
            machine_id WITH =,
            tstzrange(start_time, end_time, '[)') WITH &&
        )
);

CREATE INDEX idx_reservations_user_id ON reservations (user_id);
CREATE INDEX idx_reservations_machine_start ON reservations (machine_id, start_time);