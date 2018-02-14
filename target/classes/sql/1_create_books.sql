CREATE SEQUENCE book_id_seq;

CREATE TABLE books
(
    book_id bigint NOT NULL default nextval('book_id_seq'),
    name character varying(255),
    author character varying(255),
    publish_date date,
    created_at TIMESTAMP without time zone,
    modified_at TIMESTAMP without time zone,
    PRIMARY KEY (book_id)
);

ALTER SEQUENCE book_id_seq OWNED BY books.book_id;