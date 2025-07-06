-- db/changelog/v1.0/seed_data.sql
INSERT INTO applications (id, number, title, description, status, created_at, updated_at)
VALUES
    ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'APP-001', 'Тестовая заявка', 'Описание тестовой заявки', 'NEW', NOW(), NOW());