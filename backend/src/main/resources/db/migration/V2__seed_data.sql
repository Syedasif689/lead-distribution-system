INSERT INTO providers (id, name, monthly_quota, used_quota) VALUES
    (1, 'Provider 1', 10, 0),
    (2, 'Provider 2', 10, 0),
    (3, 'Provider 3', 10, 0),
    (4, 'Provider 4', 10, 0),
    (5, 'Provider 5', 10, 0),
    (6, 'Provider 6', 10, 0),
    (7, 'Provider 7', 10, 0),
    (8, 'Provider 8', 10, 0)
ON CONFLICT (id) DO NOTHING;

SELECT setval('providers_id_seq', (SELECT MAX(id) FROM providers));

INSERT INTO allocation_state (service_type, last_provider_index) VALUES
    ('SERVICE_1', -1),
    ('SERVICE_2', -1),
    ('SERVICE_3', -1)
ON CONFLICT (service_type) DO NOTHING;
