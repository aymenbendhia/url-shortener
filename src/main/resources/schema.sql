--DROP TABLE IF EXISTS URL_BRIDGE;
--DROP TABLE IF EXISTS DOMAIN_PART;
--DROP TABLE IF EXISTS HANDLED_PART;
--DROP SEQUENCE IF EXISTS handled_part_seq;

CREATE SEQUENCE IF NOT EXISTS handled_part_seq;

CREATE TABLE IF NOT EXISTS DOMAIN_PART (
                    id int AUTO_INCREMENT PRIMARY KEY,
                    fixed_url VARCHAR(100) NOT NULL,
                    UNIQUE KEY FIXED_URL_UNIQUE (fixed_url)
);

CREATE TABLE IF NOT EXISTS HANDLED_PART (
                      id bigint default handled_part_seq.nextval PRIMARY KEY,
                      original_handled_part VARCHAR(100) NOT NULL,
                      UNIQUE KEY HANDLED_PART_UNIQUE (original_handled_part)
);

CREATE TABLE IF NOT EXISTS URL_BRIDGE (
                      domain_part_id int NOT NULL,
                      handled_part_id bigint NOT NULL,
                      foreign key (domain_part_id) references DOMAIN_PART(id),
                      foreign key (handled_part_id) references HANDLED_PART(id),
                      UNIQUE(handled_part_id, domain_part_id)

);
