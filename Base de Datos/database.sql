CREATE DATABASE IF NOT EXISTS employees;
use employees;
CREATE TABLE IF NOT EXISTS employees(
  id_employee INT PRIMARY KEY AUTO_INCREMENT,
  name_employee VARCHAR(30) NOT NULL,
  paternalsurname_employee VARCHAR(30) NOT NULL,
  maternalsurname_employee VARCHAR(30) NOT NULL,
  direction VARCHAR(100) NOT NULL,
  telephone VARCHAR(11) NOT NULL,
  isActive ENUM('Y','N') NOT NULL
);
CREATE TABLE IF NOT EXISTS loginEmployee(
  id_employee INT NOT NULL,
  user_employee VARCHAR(30) NOT NULL,
  password_employee VARCHAR(30) NOT NULL,
  type_User ENUM('A','E'),
  FOREIGN KEY (id_employee) REFERENCES employees(id_employee)
);
CREATE TABLE IF NOT EXISTS schedule(
  id_employee INT NOT NULL,
  sunday ENUM('Y','N') NOT NULL,
  monday ENUM('Y','N') NOT NULL,
  tuesday ENUM('Y','N') NOT NULL,
  wednesday ENUM('Y','N') NOT NULL,
  thursday ENUM('Y','N') NOT NULL,
  friday ENUM('Y','N') NOT NULL,
  saturday ENUM('Y','N') NOT NULL,
  entry_time TIME NOT NULL,
  departure_time TIME NOT NULL,
  FOREIGN KEY (id_employee) REFERENCES employees(id_employee)
);