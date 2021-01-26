CREATE TABLE t_administrated_channels
(
    id   int8 GENERATED BY DEFAULT AS IDENTITY,
    name varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE t_cross_promotions
(
    id                        int8 GENERATED BY DEFAULT AS IDENTITY,
    end_date                  timestamp,
    start_date                timestamp,
    administrating_channel_id int8,
    outside_channel_id        int8,
    PRIMARY KEY (id)
);
CREATE TABLE t_cross_promotions_entered_users
(
    t_cross_promotions_id int8 NOT NULL,
    entered_users_id      int8 NOT NULL
);
CREATE TABLE t_outside_channels
(
    id   int8 GENERATED BY DEFAULT AS IDENTITY,
    name varchar(255),
    PRIMARY KEY (id)
);
CREATE TABLE t_users
(
    id                 int8 GENERATED BY DEFAULT AS IDENTITY,
    name               varchar(255),
    nickname           varchar(255),
    cross_promotion_id int8,
    PRIMARY KEY (id)
);
ALTER TABLE IF EXISTS t_cross_promotions_entered_users
    ADD CONSTRAINT uk_n87gj9gc3fe36kep80a86r64u UNIQUE (entered_users_id);
ALTER TABLE IF EXISTS t_cross_promotions
    ADD CONSTRAINT fknjsm26c4luiakkw18ary50wiw FOREIGN KEY (administrating_channel_id) REFERENCES t_administrated_channels;
ALTER TABLE IF EXISTS t_cross_promotions
    ADD CONSTRAINT fk5cjlio0xoamcqojsxa3qwx5w FOREIGN KEY (outside_channel_id) REFERENCES t_outside_channels;
ALTER TABLE IF EXISTS t_cross_promotions_entered_users
    ADD CONSTRAINT fkah3ktp479yor07qtcqcmajkgt FOREIGN KEY (entered_users_id) REFERENCES t_users;
ALTER TABLE IF EXISTS t_cross_promotions_entered_users
    ADD CONSTRAINT fk8by7p9gf3tpqdb3udm4ousq6y FOREIGN KEY (t_cross_promotions_id) REFERENCES t_cross_promotions;
ALTER TABLE IF EXISTS t_users
    ADD CONSTRAINT fkm45aauc51imr1pvsibn3naqwk FOREIGN KEY (cross_promotion_id) REFERENCES t_cross_promotions;