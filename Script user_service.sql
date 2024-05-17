-- CREATE DATABASE user_service;

CREATE TABLE departments (
  id BIGSERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  code VARCHAR(255) NOT NULL,
  level INT,
  name VARCHAR(255) NOT NULL,
  parent_department_code VARCHAR(255),
  parent_department_id BIGINT,
  path VARCHAR(255),
  status VARCHAR(255),
  translate_name VARCHAR(255),
  type VARCHAR(255)
);

CREATE TABLE tenants (
  id BIGSERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  code VARCHAR(100) NOT NULL,
  name VARCHAR(100) NOT NULL,
  status VARCHAR(100) NOT NULL,
  translate_name VARCHAR(100) NOT NULL
);

CREATE TABLE roles (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  first_name VARCHAR(100),
  full_name VARCHAR(100),
  is_active SMALLINT,
  last_name VARCHAR(100),
  phone VARCHAR(100),
  department_id BIGINT,
  tenant_id BIGINT,
  FOREIGN KEY (department_id) REFERENCES departments (id),
  FOREIGN KEY (tenant_id) REFERENCES tenants (id)
);

CREATE TABLE user_logins (
  id BIGSERIAL PRIMARY KEY,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  login_type VARCHAR(100) NOT NULL,
  password VARCHAR(255) NOT NULL,
  user_name VARCHAR(100) NOT NULL,
  role_id BIGINT,
  user_id BIGINT,
  FOREIGN KEY (role_id) REFERENCES roles (id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE tokens (
  id BIGSERIAL PRIMARY KEY,
  expiration_date TIMESTAMP NOT NULL,
  expired BOOLEAN NOT NULL,
  is_mobile BOOLEAN DEFAULT FALSE,
  refresh_expiration_date TIMESTAMP NOT NULL,
  refresh_token VARCHAR(255) NOT NULL,
  revoked BOOLEAN NOT NULL,
  token TEXT NOT NULL,
  token_type VARCHAR(50) NOT NULL,
  user_login_id BIGINT NOT NULL,
  FOREIGN KEY (user_login_id) REFERENCES user_logins (id)
);
