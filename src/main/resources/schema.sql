CREATE TYPE IF NOT EXISTS booking_status AS ENUM
(
'WAITING',
'APPROVED',
'REJECTED',
'CANCELED'
);

CREATE TABLE IF NOT EXISTS users
(
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(128)    NOT NULL,
    email           VARCHAR(128)    UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description     VARCHAR(128)    NOT NULL,
    requester_id    BIGINT          REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS items
(
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            VARCHAR(128)    NOT NULL,
    description     VARCHAR(128)    NOT NULL,
    available       BOOLEAN         DEFAULT TRUE NOT NULL,
    owner_id        BIGINT          REFERENCES users(id),
    request_id      BIGINT          REFERENCES requests(id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date      TIMESTAMP       NOT NULL,
    end_date        TIMESTAMP       NOT NULL,
    item_id         BIGINT          REFERENCES items(id),
    booker_id       BIGINT          REFERENCES users(id),
    status          booking_status  NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id              BIGINT          GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text            VARCHAR(128)    NOT NULL,
    item_id         BIGINT          REFERENCES items(id),
    author_id       BIGINT          REFERENCES users(id),
    created         TIMESTAMP       NOT NULL
);