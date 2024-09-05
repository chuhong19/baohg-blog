--changeset giabaost1910:add-refreshtoken-table
CREATE TABLE "refresh_token" (
    "id" bigserial PRIMARY KEY,
    "user_id" int8,
    "revoked" bool,
    "token" text UNIQUE,
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6),
    "expiry_date" timestamptz(6) NOT NULL
);
