CREATE DATABASE Logistic_Web_App_Service_Login;

USE Logistic_Web_App_Service_Login;

CREATE TABLE roles(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM('admin', 'user') NOT NULL UNIQUE 
);

CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100),
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME NOT NULL,
    updaetd_at DATETIME NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE user_logins(
	id INT PRIMARY KEY  AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    login_type ENUM('phone_number', 'email'),
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE tokens(
	id INT PRIMARY KEY AUTO_INCREMENT,
    token LONGTEXT NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    is_mobile TINYINT(1), 
    refresh_expiration_date DATETIME,
    refresh_token LONGTEXT,
    FOREIGN KEY(user_id) REFERENCES users(id)
);