-- ════════════════════════════════════════════════════
--  UPCL Chatbot — MySQL Schema & Sample Data
--  Database: WSS_CHATBOT
-- ════════════════════════════════════════════════════

CREATE DATABASE IF NOT EXISTS WSS_CHATBOT
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE WSS_CHATBOT;

-- ── Customers ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS customers (
    account_number  VARCHAR(50)     NOT NULL PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    address         VARCHAR(255),
    phone           VARCHAR(15),
    email           VARCHAR(100),
    meter_number    VARCHAR(50),
    connection_type VARCHAR(50),
    sanctioned_load DOUBLE
);

-- ── Bills ──────────────────────────────────────────
CREATE TABLE IF NOT EXISTS bills (
    id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_number   VARCHAR(50)     NOT NULL,
    bill_number      VARCHAR(50)     UNIQUE,
    billing_month    VARCHAR(50),
    bill_date        DATE,
    due_date         DATE,
    units_consumed   DOUBLE,
    previous_reading DOUBLE,
    current_reading  DOUBLE,
    energy_charges   DOUBLE,
    fixed_charges    DOUBLE,
    other_charges    DOUBLE,
    taxes            DOUBLE,
    total_amount     DOUBLE,
    arrears          DOUBLE          DEFAULT 0,
    payment_status   ENUM('PAID','UNPAID','OVERDUE') DEFAULT 'UNPAID',
    payment_date     DATE,
    payment_mode     VARCHAR(50),
    FOREIGN KEY (account_number) REFERENCES customers(account_number)
);

-- ── Complaints ─────────────────────────────────────
CREATE TABLE IF NOT EXISTS complaints (
    id               BIGINT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    account_number   VARCHAR(50)     NOT NULL,
    complaint_number VARCHAR(50)     UNIQUE,
    complaint_type   VARCHAR(100),
    description      TEXT,
    status           ENUM('OPEN','IN_PROGRESS','RESOLVED','CLOSED') DEFAULT 'OPEN',
    created_at       DATETIME        DEFAULT CURRENT_TIMESTAMP,
    resolved_at      DATETIME,
    remarks          TEXT,
    FOREIGN KEY (account_number) REFERENCES customers(account_number)
);


-- ════════════════════════════════════════════════════
--  SAMPLE DATA
-- ════════════════════════════════════════════════════

-- Customers
INSERT INTO customers VALUES
('ACC12345', 'Ramesh Kumar Sharma',  'B-45, Rajpur Road, Dehradun', '9876543210', 'ramesh@email.com', 'MTR-9901', 'Domestic',   5.0),
('ACC67890', 'Priya Singh',          '12, Gandhi Nagar, Haridwar',  '9812345678', 'priya@email.com',  'MTR-7723', 'Domestic',   3.0),
('ACC11111', 'Mohan Lal Enterprises','Industrial Area, Roorkee',    '9456781234', 'mohan@email.com',  'MTR-4410', 'Commercial', 25.0),
('ACC22222', 'Sunita Devi',          'Village Raipur, Dehradun',    '9634512870', NULL,               'MTR-3301', 'Domestic',   2.0);


-- Bills for ACC12345
INSERT INTO bills (account_number, bill_number, billing_month, bill_date, due_date,
                   units_consumed, previous_reading, current_reading,
                   energy_charges, fixed_charges, other_charges, taxes,
                   total_amount, arrears, payment_status, payment_date, payment_mode)
VALUES
('ACC12345','BILL-2025-0201','February 2025','2025-02-05','2025-02-25',
 210, 8400, 8610, 1470, 150, 40, 82.8, 1742.8, 0,    'PAID',   '2025-02-20','Online'),

('ACC12345','BILL-2025-0301','March 2025','2025-03-05','2025-03-25',
 235, 8610, 8845, 1645, 150, 40, 90.75, 1925.75, 0,  'PAID',   '2025-03-22','PhonePe'),

('ACC12345','BILL-2025-0401','April 2025','2025-04-05','2025-04-25',
 198, 8845, 9043, 1386, 150, 40, 75.8,  1651.8,  0,  'PAID',   '2025-04-18','Online'),

('ACC12345','BILL-2025-0501','May 2025','2025-05-05','2025-05-25',
 275, 9043, 9318, 1925, 150, 40, 103.75, 2218.75, 0, 'PAID',   '2025-05-24','Google Pay'),

('ACC12345','BILL-2025-0601','June 2025','2025-06-05','2025-06-25',
 312, 9318, 9630, 2184, 150, 50, 119.2,  2503.2,  0, 'PAID',   '2025-06-20','BBPS'),

('ACC12345','BILL-2025-0701','July 2025','2025-07-05','2025-07-25',
 289, 9630, 9919, 2023, 150, 50, 111.15, 2334.15, 0, 'UNPAID', NULL, NULL);


-- Bills for ACC67890
INSERT INTO bills (account_number, bill_number, billing_month, bill_date, due_date,
                   units_consumed, previous_reading, current_reading,
                   energy_charges, fixed_charges, other_charges, taxes,
                   total_amount, arrears, payment_status, payment_date, payment_mode)
VALUES
('ACC67890','BILL-ACC2-0601','June 2025','2025-06-08','2025-06-28',
 185, 3200, 3385, 1295, 120, 30, 72.25, 1517.25, 0,    'PAID',   '2025-06-25','Cash'),

('ACC67890','BILL-ACC2-0701','July 2025','2025-07-08','2025-07-28',
 210, 3385, 3595, 1470, 120, 30, 81.0,  1701.0,  1517.25, 'OVERDUE', NULL, NULL);


-- Complaints for ACC12345
INSERT INTO complaints (account_number, complaint_number, complaint_type, description, status, created_at)
VALUES
('ACC12345','CMP-2025-001','High Bill','July bill seems very high compared to last month','OPEN',       '2025-07-07 10:30:00'),
('ACC12345','CMP-2025-002','Power Cut','Frequent power cuts in our area for past 2 days','IN_PROGRESS','2025-07-09 14:00:00'),
('ACC12345','CMP-2024-015','Meter Fault','Meter not showing correct reading',            'RESOLVED',   '2024-11-01 09:00:00');

-- Complaint for ACC67890
INSERT INTO complaints (account_number, complaint_number, complaint_type, description, status, created_at)
VALUES
('ACC67890','CMP-2025-003','Billing Error','Arrears charged incorrectly on July bill','OPEN','2025-07-10 11:00:00');
