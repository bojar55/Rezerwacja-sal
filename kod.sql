CREATE TABLE archives (
    id                      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_from               DATE,
    date_to                 DATE,
    user_id                 INTEGER NOT NULL,
    room_id                 INTEGER NOT NULL,
    type_of_reservation_id  INTEGER NOT NULL
);

CREATE TABLE reservation (
    id                      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_from               DATE,
    date_to                 DATE,
    user_id                 INTEGER NOT NULL,
    room_id                 INTEGER NOT NULL,
    type_of_reservation_id  INTEGER NOT NULL
);

CREATE TABLE room (
    id               INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(10),
    number_of_place  INTEGER
);

CREATE TABLE type_of_reservation (
    id    INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(15)
);

CREATE TABLE users (
    id          INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(15),
    last_name   VARCHAR(20),
    nick        VARCHAR(15),
    password    VARCHAR(100),
    email       VARCHAR(20),
    or_admin    CHAR(1)
);

ALTER TABLE archives
    ADD CONSTRAINT archives_room_fk FOREIGN KEY ( room_id )
        REFERENCES room ( id );

ALTER TABLE archives
    ADD CONSTRAINT archives_type__fk FOREIGN KEY ( type_of_reservation_id )
        REFERENCES type_of_reservation ( id );

ALTER TABLE archives
    ADD CONSTRAINT archives_user_fk FOREIGN KEY ( user_id )
        REFERENCES users ( id );

ALTER TABLE reservation
    ADD CONSTRAINT reservation_room_fk FOREIGN KEY ( room_id )
        REFERENCES room ( id );

ALTER TABLE reservation
    ADD CONSTRAINT reservation_type_fk FOREIGN KEY ( type_of_reservation_id )
        REFERENCES type_of_reservation ( id );

ALTER TABLE reservation
    ADD CONSTRAINT reservation_user_fk FOREIGN KEY ( user_id )
        REFERENCES users ( id );