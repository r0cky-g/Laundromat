CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TYPE machine_type AS ENUM ('WASHER', 'DRYER');
CREATE TYPE reservation_status AS ENUM ('PENDING_PAYMENT', 'CONFIRMED', 'CANCELLED', 'FINISHED');

CREATE TABLE reservations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id VARCHAR(20) NOT NULL,
    machine_id INT NOT NULL,
    machine_type machine_type NOT NULL,
    cost NUMERIC(10, 2) NOT NULL,
    status reservation_status NOT NULL,

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
        WHERE (status IN ('PENDING_PAYMENT', 'CONFIRMED'))
);
	
CREATE INDEX idx_reservations_user_start
    ON reservations (user_id, start_time DESC);

CREATE INDEX idx_reservations_machine_start
    ON reservations (machine_id, start_time DESC);

CREATE INDEX idx_reservations_status_start
    ON reservations (status, start_time DESC);