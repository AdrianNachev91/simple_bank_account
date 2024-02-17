INSERT INTO account (id, first_name, last_name, email, created_on, last_updated_on) VALUES (1, 'Adrian', 'Nachev', 'adrian@example.com', '2024-02-17', '2024-02-17');
INSERT INTO account (id, first_name, last_name, email, created_on, last_updated_on) VALUES (2, 'Suzan', 'Hart', 'suzan@example.com', '2024-02-16', '2024-02-17');
INSERT INTO account (id, first_name, last_name, email, created_on, last_updated_on) VALUES (3, 'Peter', 'Jhones', 'peter@example.com', '2024-02-15', '2024-02-17');

INSERT INTO payment_card (account_id, card_type, current_balance, created_on, last_updated_on) VALUES (1, 'DEBIT', 453.43, '2024-02-17', '2024-02-17');
INSERT INTO payment_card (account_id, card_type, current_balance, created_on, last_updated_on) VALUES (2, 'CREDIT', 1232.12, '2024-02-16', '2024-02-17');
INSERT INTO payment_card (account_id, card_type, current_balance, created_on, last_updated_on) VALUES (3, 'DEBIT', 43.21, '2024-02-15', '2024-02-17');