# Asset Manager Backend

Spring Boot backend for the Tomato laptop asset lending workflow.

## Quick Setup

### Requirements

- Java 17
- Maven
- MySQL

I used Laragon MySQL locally because it is quick and easy to set up.

### 1. Create the database

Run this SQL first:

```sql
CREATE DATABASE IF NOT EXISTS asset_mgmt;
USE asset_mgmt;

CREATE TABLE employees (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  employee_id VARCHAR(50) NOT NULL UNIQUE,
  full_name VARCHAR(255) NOT NULL,
  department VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  employee_id BIGINT NOT NULL,
  password_hash VARCHAR(255) NOT NULL,

  role ENUM('EMPLOYEE','ASSET_ADMIN','SYSTEM_ADMIN') NOT NULL DEFAULT 'EMPLOYEE',
  is_active BOOLEAN NOT NULL DEFAULT TRUE,

  last_password_changed_at DATETIME NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  UNIQUE KEY uk_users_employee_id (employee_id),

  CONSTRAINT fk_users_employee
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;

CREATE TABLE assets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  asset_tag VARCHAR(255) NOT NULL UNIQUE,
  serial_number BIGINT NOT NULL UNIQUE,

  model VARCHAR(255) NOT NULL,
  cpu_amt VARCHAR(255) NOT NULL,
  ram_amt INT NOT NULL,
  storage_amt INT NOT NULL,

  unit_status VARCHAR(255) NOT NULL,

  image_url VARCHAR(500) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE assets_request (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  employee_id BIGINT NOT NULL,

  request_type VARCHAR(50) NOT NULL,
  min_cpu_amt VARCHAR(255) NULL,
  min_ram_amt INT NULL,
  min_storage_amt INT NULL,
  reason VARCHAR(255) NULL,

  unit_status VARCHAR(50) NOT NULL,

  reviewed_by BIGINT NULL,
  reviewed_at DATETIME NULL,
  review_comment VARCHAR(255) NULL,

  request_asset_id BIGINT NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_assets_request_employee
    FOREIGN KEY (employee_id) REFERENCES employees(id),

  CONSTRAINT fk_assets_request_reviewer
    FOREIGN KEY (reviewed_by) REFERENCES employees(id),

  CONSTRAINT fk_assets_request_asset
    FOREIGN KEY (request_asset_id) REFERENCES assets(id)
);

CREATE TABLE assets_assignment (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  asset_id BIGINT NOT NULL,
  employee_id BIGINT NOT NULL,
  approved_by BIGINT NULL,

  assigned_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  returned_at DATETIME NULL,

  note VARCHAR(255) NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  CONSTRAINT fk_assets_assignment_asset
    FOREIGN KEY (asset_id) REFERENCES assets(id),

  CONSTRAINT fk_assets_assignment_employee
    FOREIGN KEY (employee_id) REFERENCES employees(id),

  CONSTRAINT fk_assets_assignment_approved_by
    FOREIGN KEY (approved_by) REFERENCES employees(id)
) ENGINE=InnoDB;

USE asset_mgmt;

CREATE TABLE IF NOT EXISTS password_reset_tokens (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,

  employee_id BIGINT NOT NULL,

  token_hash VARCHAR(255) NOT NULL,
  expires_at DATETIME NOT NULL,
  used_at DATETIME NULL,

  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

  UNIQUE KEY uk_password_reset_token_hash (token_hash),
  KEY idx_password_reset_employee (employee_id),
  KEY idx_password_reset_expires (expires_at),

  CONSTRAINT fk_password_reset_employee
    FOREIGN KEY (employee_id) REFERENCES employees(id)
) ENGINE=InnoDB;
```

### 2. Check database config

This project currently uses:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/asset_mgmt
spring.datasource.username=root
spring.datasource.password=
```

You can change that in:

```text
src/main/resources/application.properties
```

### 3. Run locally

```bash
mvn spring-boot:run
```

Backend runs at:

```text
http://localhost:8080
```

## API Endpoints

### Auth

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `POST` | `/api/auth/login` | Public | Login and get JWT token |
| `POST` | `/api/auth/change-password` | Authenticated | Change password for current logged-in user |
| `POST` | `/api/auth/forgot-password` | Public | Send password reset email |
| `POST` | `/api/auth/reset-password` | Public | Reset password from token |

### Current User

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `GET` | `/api/me/laptops` | `EMPLOYEE` | Get current assigned laptops for logged-in employee |

### Assets

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `GET` | `/api/assets` | Authenticated | Optional query params: `model`, `unitStatus`, `minRam`, `minStorage` |
| `GET` | `/api/assets/{id}` | Authenticated | Get single asset detail |
| `POST` | `/api/assets` | Authenticated | Create asset |
| `PUT` | `/api/assets/{id}` | Authenticated | Update asset |
| `DELETE` | `/api/assets/{id}` | Authenticated | Delete asset |

### Assignments

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `POST` | `/api/assignments/assign` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Assign asset to employee / approved request |
| `POST` | `/api/assignments/return` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Return assigned asset |
| `GET` | `/api/assignments/current` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Global current active assignments |
| `GET` | `/api/assignments/employee/{employeeId}/current` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Current assignments for one employee |
| `GET` | `/api/assignments/asset/{assetId}/history` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Full lending history for one asset |

### Requests

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `GET` | `/api/requests` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Optional query param: `status` |
| `POST` | `/api/requests/review` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Approve or reject request |
| `POST` | `/api/requests/fulfill` | `ASSET_ADMIN`, `SYSTEM_ADMIN` | Legacy/manual fulfill endpoint still exists |

### Employee Requests

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `POST` | `/api/employee-requests` | `EMPLOYEE` | Create own request |
| `GET` | `/api/employee-requests/employee/{employeeId}` | `EMPLOYEE` | List own requests |

### Employees

| Method | Endpoint | Access | Notes |
|---|---|---|---|
| `GET` | `/api/employees` | `SYSTEM_ADMIN`, `ASSET_ADMIN` | List employees |
| `GET` | `/api/employees/{id}` | `SYSTEM_ADMIN`, `ASSET_ADMIN` | Get one employee |
| `POST` | `/api/employees` | `SYSTEM_ADMIN` | Create employee |
| `PUT` | `/api/employees/{id}` | `SYSTEM_ADMIN` | Update employee |
| `DELETE` | `/api/employees/{id}` | `SYSTEM_ADMIN` | Delete employee |

## Important Disclaimer

The password reset / change password email flow is not fully usable out of the box here.

Reason:

- it needs a real SMTP / email service
- in my case that would be something linked to `@jptomato.com`
- I did not set that up in this repo

So the rest of the backend works for local development, but email-based password reset is not fully configured unless you plug in a real mail service in `application.properties`.
