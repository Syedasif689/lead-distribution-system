CREATE TABLE leads (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(32) NOT NULL,
    city VARCHAR(120) NOT NULL,
    service_type VARCHAR(32) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_leads_phone_service UNIQUE (phone, service_type)
);

CREATE TABLE providers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(120) NOT NULL UNIQUE,
    monthly_quota INTEGER NOT NULL CHECK (monthly_quota >= 0),
    used_quota INTEGER NOT NULL CHECK (used_quota >= 0),
    CONSTRAINT ck_provider_quota CHECK (used_quota <= monthly_quota)
);

CREATE TABLE lead_assignments (
    id BIGSERIAL PRIMARY KEY,
    lead_id BIGINT NOT NULL REFERENCES leads(id) ON DELETE CASCADE,
    provider_id BIGINT NOT NULL REFERENCES providers(id) ON DELETE RESTRICT,
    assigned_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_assignment_lead_provider UNIQUE (lead_id, provider_id)
);

CREATE INDEX idx_assignments_provider_id ON lead_assignments(provider_id);
CREATE INDEX idx_assignments_lead_id ON lead_assignments(lead_id);

CREATE TABLE allocation_state (
    service_type VARCHAR(32) PRIMARY KEY,
    last_provider_index INTEGER NOT NULL
);

CREATE TABLE webhook_events (
    event_id VARCHAR(120) PRIMARY KEY,
    processed_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
