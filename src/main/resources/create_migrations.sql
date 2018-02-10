CREATE TABLE migrations
(
    migration_id bigint NOT NULL,
    name character varying(255),
    created_at TIMESTAMP without time zone,
    PRIMARY KEY (migration_id)
);