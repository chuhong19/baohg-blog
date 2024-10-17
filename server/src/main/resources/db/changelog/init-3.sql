--changeset giabaost1910:create-table-leaderboard
CREATE TABLE "leaderboard" (
    "id" bigserial PRIMARY KEY,
    "score_name" varchar(50),
    "score" int8,
    "user_create_id" bigint
);
