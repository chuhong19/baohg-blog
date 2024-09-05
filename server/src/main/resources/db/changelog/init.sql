--liquibase formatted sql

--changeset giabaost1910:create-table-roles
CREATE TABLE "roles" (
    "id" bigserial PRIMARY KEY,
    "role" varchar(255) NOT NULL,
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6)
);

--changeset giabaost1910:create-table-permissions
CREATE TABLE "permissions" (
    "id" bigserial PRIMARY KEY,
    "permission" varchar(255) NOT NULL,
    "enabled" boolean DEFAULT true,
    "note" varchar(255),
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6)
);

--changeset giabaost1910:create-table-relations-roles-permissions
CREATE TABLE "permissions_roles" (
    "role_id" bigint,
    "permission_id" bigint,
    PRIMARY KEY ("role_id", "permission_id"),
    FOREIGN KEY ("role_id") REFERENCES "roles" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("permission_id") REFERENCES "permissions" ("id") ON DELETE CASCADE
);

INSERT INTO permissions(id, permission, note) VALUES (1, 'LOGIN', 'User Login');
INSERT INTO permissions(id, permission, note) VALUES (2, 'VIEW_PROFILE', 'View user profile');
INSERT INTO permissions(id, permission, note) VALUES (3, 'ADMIN_USER_DATA', 'Manage user data');

INSERT INTO permissions(id, permission, note) VALUES (4, 'MANAGE_CONTENT', 'Content moderation');

INSERT INTO roles(id, role) VALUES (1, 'USER');
INSERT INTO roles(id, role) VALUES (2, 'ADMINISTRATOR');
INSERT INTO roles(id, role) VALUES (3, 'MOD');

INSERT INTO permissions_roles(permission_id, role_id) VALUES (1, 1);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (2, 1);

INSERT INTO permissions_roles(permission_id, role_id) VALUES (1, 2);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (2, 2);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (3, 2);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (4, 2);

INSERT INTO permissions_roles(permission_id, role_id) VALUES (1, 3);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (2, 3);
INSERT INTO permissions_roles(permission_id, role_id) VALUES (4, 3);

--changeset giabaost1910:create-table-users
CREATE TABLE "users" (
    "id" bigserial PRIMARY KEY,
    "username" varchar(255) UNIQUE,
    "email" varchar(255) UNIQUE,
    "password" varchar(255),
    "firstname" varchar(255),
    "lastname" varchar(255),
    "gender" varchar(255) CHECK (gender::text = ANY (ARRAY['MALE'::character varying, 'FEMALE'::character varying, 'OTHER'::character varying, 'UNKNOWN'::character varying]::text[])),
    "roles" varchar(255)[],
    "account_non_expired" bool NOT NULL,
    "account_non_locked" bool NOT NULL,
    "credentials_non_expired" bool NOT NULL,
    "enabled" bool NOT NULL,
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6),
    "reset_token" varchar(255)
);

--changeset giabaost1910:create-table-users_roles
CREATE TABLE "users_roles" (
    "user_id" bigint,
    "role_id" bigint,
    PRIMARY KEY ("user_id", "role_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("role_id") REFERENCES "roles" ("id") ON DELETE CASCADE
);

--changeset giabaost1910:create-table-token
CREATE TABLE "token" (
    "id" bigserial PRIMARY KEY,
    "user_id" int8,
    "expired" bool,
    "revoked" bool,
    "token_type" varchar(255) CHECK (token_type::text = 'BEARER'::text),
    "token" text UNIQUE,
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6)
);


--changeset giabaost1910:create-table-users_special_permissions
CREATE TABLE "users_special_permissions" (
    "user_id" bigint,
    "special_permission_id" bigint,
    PRIMARY KEY ("user_id", "special_permission_id"),
    FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE,
    FOREIGN KEY ("special_permission_id") REFERENCES "permissions" ("id") ON DELETE CASCADE
);

--changeset giabaost1910:create-table-posts
CREATE TABLE "posts" (
    "id" bigserial PRIMARY KEY,
    "title" varchar(255) NOT NULL,
    "content" varchar(255) NOT NULL,
    "author_id" bigint,
    "created_at" timestamptz(6),
    "updated_at" timestamptz(6),
    "is_banned" bool
);

--changeset giabaost1910:create-table-users_follows
CREATE TABLE "user_follow" (
    "id" bigserial PRIMARY KEY,
    "user_id" bigint,
    "follow_id" bigint
);

--changeset giabaost1910:create-table-posts_report
CREATE TABLE "posts_report" (
    "id" bigserial PRIMARY KEY,
    "post_id" bigint,
    "author_id" bigint,
    "report_user_id" bigint
)

--changeset giabaost1910:add-like-dislike-count-to-post
ALTER TABLE "posts"
    ADD COLUMN "like_count" bigint,
    ADD COLUMN "dislike_count" bigint;

--changeset giabaost1910:create-table-posts_comment
CREATE TABLE "posts_comment" (
    "id" bigserial PRIMARY KEY,
    "post_id" bigint,
    "user_id" bigint,
    "content" varchar(255)
)

--changeset giabaost1910:create-table-meeting
CREATE TABLE "meetings" (
    "id" bigserial PRIMARY KEY,
    "meeting_name" varchar(255),
    "meeting_description" varchar(255),
    "host_id" bigint,
    "host_name" varchar(255),
    "max_participant" bigint,
    "count_participant" bigint,
    "time_start" timestamptz(6),
    "rating" double precision,
    "status" varchar(255) CHECK (status::text = ANY (ARRAY['SCHEDULED'::character varying, 'ONGOING'::character varying, 'COMPLETED'::character varying]::text[]))
)

--changeset giabaost1910:create-table-meeting_participant
CREATE TABLE "meeting_participant" (
    "id" bigserial PRIMARY KEY,
    "meeting_id" bigint,
    "host_id" bigint,
    "participant_id" bigint
)

--changeset giabaost1910:create-table-meeting_rating
CREATE TABLE "meeting_rating" (
    "id" bigserial PRIMARY KEY,
    "user_rating_id" bigint,
    "rate" bigint,
    "comment" varchar(255)
)

--changeset giabaost1910:add-meeting_id-to-meeting_rating
ALTER TABLE "meeting_rating"
    ADD COLUMN "meeting_id" bigint;

--changeset giabaost1910:add-rating_count-to-table-meeting
ALTER TABLE "meetings"
    ADD COLUMN "meeting_count" bigint;

--changeset giabaost1910:add-duration-to-table-meeting
ALTER TABLE "meetings"
    ADD COLUMN "duration" bigint;

