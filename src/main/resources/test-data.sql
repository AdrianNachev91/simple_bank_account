INSERT INTO account (id, first_name, last_name, email, iban, current_balance, created_on, last_updated_on) VALUES (1, 'Adrian', 'Nachev', 'adrian@test.com', 'NL63RABO3665292913', 453.43, '2024-02-17', '2024-02-17');
INSERT INTO account (id, first_name, last_name, email, iban, current_balance, created_on, last_updated_on) VALUES (2, 'Suzan', 'Hart', 'suzan@test.com', 'NL49RABO5350244469', 1232.12, '2024-02-16', '2024-02-17');
INSERT INTO account (id, first_name, last_name, email, iban, current_balance, created_on, last_updated_on) VALUES (3, 'Peter', 'Jhones', 'peter@test.com', 'NL72RABO8727958558', 43.21, '2024-02-15', '2024-02-17');

INSERT INTO payment_card (id, account_id, card_type, expiry_date, created_on, last_updated_on) VALUES (1, 1, 'DEBIT', '2026-02-17', '2024-02-17', '2024-02-17');
INSERT INTO payment_card (id, account_id, card_type, expiry_date, created_on, last_updated_on) VALUES (2, 2, 'CREDIT', '2027-02-17', '2024-02-16', '2024-02-17');
INSERT INTO payment_card (id, account_id, card_type, expiry_date, created_on, last_updated_on) VALUES (3, 3, 'DEBIT', '2028-02-17', '2024-02-15', '2024-02-17');
INSERT INTO payment_card (id, account_id, card_type, expiry_date, created_on, last_updated_on) VALUES (4, 1, 'CREDIT', '2026-02-17', '2024-02-17', '2024-02-17');
INSERT INTO payment_card (id, account_id, card_type, expiry_date, created_on, last_updated_on) VALUES (5, 1, 'DEBIT', '2023-02-17', '2024-02-17', '2024-02-17');