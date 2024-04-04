CREATE DATABASE Logistic_Web_App_Service_Login;

USE Logistic_Web_App_Service_Login;

CREATE TABLE roles(
	id INT PRIMARY KEY AUTO_INCREMENT,
    name ENUM('admin', 'user') NOT NULL UNIQUE 
);

CREATE TABLE tenants(
    id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    translate_name VARCHAR(100) NOT NULL,
    status VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users(
	id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100),
    last_name VARCHAR(100),
    first_name VARCHAR(100),
    is_active TINYINT(1) DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    tenant_id INT NOT NULL,
    FOREIGN KEY (tenant_id) REFERENCES tenants(id)
);

CREATE TABLE user_logins(
	id INT PRIMARY KEY  AUTO_INCREMENT,
    user_name VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    login_type ENUM('phone_number', 'email'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
    role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE tokens(
	id INT PRIMARY KEY AUTO_INCREMENT,
    token LONGTEXT NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    is_mobile TINYINT(1), 
    refresh_expiration_date DATETIME,
    refresh_token LONGTEXT,
    user_login_id INT,
    FOREIGN KEY(user_login_id) REFERENCES user_logins(id)
);