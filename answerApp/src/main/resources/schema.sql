DROP TABLE IF EXISTS employee;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS member;

CREATE TABLE employee (
 employeeId INTEGER PRIMARY KEY ,
 employeeName VARCHAR(30) NOT NULL ,
 section VARCHAR(30) NOT NULL ,
 phone VARCHAR(30) NOT NULL
);

CREATE TABLE category (
 categoryId VARCHAR(2) PRIMARY KEY ,
 categoryName VARCHAR(40) NOT NULL ,
 picture VARCHAR(30) NOT NULL
);

CREATE TABLE product (
 productId VARCHAR(12) PRIMARY KEY ,
 categoryId VARCHAR(2) NOT NULL ,
 productName VARCHAR(40) NOT NULL ,
 price DECIMAL(7) NOT NULL ,
 picture VARCHAR(30) NOT NULL ,
 point INTEGER NOT NULL ,
 FOREIGN KEY(categoryId) REFERENCES category(categoryId)
);

CREATE TABLE stock (
 productId  VARCHAR(12) PRIMARY KEY ,
 quantity  INTEGER   NOT NULL ,
 FOREIGN KEY(productId) REFERENCES product(productId)
);

CREATE TABLE member (
 memberId  VARCHAR(255) PRIMARY KEY ,
 password  CHAR(8)   NOT NULL ,
 memberName  VARCHAR(40)  NOT NULL ,
 gender   CHAR(1)   NOT NULL ,
 address   VARCHAR(80)  NOT NULL ,
 phone   VARCHAR(15)  NOT NULL ,
 memberPoint  INTEGER   NOT NULL
);
