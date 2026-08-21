CREATE TABLE account (
    account_id      UUID PRIMARY KEY,
    owner_name      VARCHAR(255) NOT NULL,
    balance_cents   BIGINT NOT NULL DEFAULT 0,  -- sempre em centavos, nunca float
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE transfer (
    transfer_id     UUID PRIMARY KEY,
    from_account_id UUID NOT NULL REFERENCES account(account_id),
    to_account_id   UUID NOT NULL REFERENCES account(account_id),
    amount_cents    BIGINT NOT NULL CHECK (amount_cents > 0),
    status          VARCHAR(20) NOT NULL, -- PENDING, COMPLETED, REJECTED
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);
