CREATE TABLE parkingBoy (
    id  BIGINT   NOT NULL  AUTO_INCREMENT  PRIMARY KEY,
    username    VARCHAR(50)     NOT NULL UNIQUE ,
    nickname VARCHAR (20) UNIQUE NOT NULL ,
    password VARCHAR (20) NOT NULL ,
    email   VARCHAR(50)     NOT NULL ,
    phone    VARCHAR(20)     NOT NULL,
    status  INT     NOT NULL,
);
CREATE TABLE parking_lot (
    id  BIGINT  NOT NULL  AUTO_INCREMENT  PRIMARY KEY,
    name    VARCHAR(20)     NOT NULL,
    size    INT     NOT NULL,
    status  VARCHAR(20)     NOT NULL,
    parking_boy_id  BIGINT,
    FOREIGN KEY (parking_boy_id) REFERENCES parkingBoy(id)
);
CREATE TABLE car_order (
    id  BIGINT  NOT NULL    AUTO_INCREMENT  PRIMARY KEY,
    type    VARCHAR(20)     NOT NULL,
    parking_boy_id  BIGINT ,
    plate_number    VARCHAR(20)     NOT NULL,
    status  VARCHAR(20)     NOT NULL,
    receipt_id  VARCHAR(20)     NOT NULL,
    park_date TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (parking_boy_id) REFERENCES parkingBoy(id)
);