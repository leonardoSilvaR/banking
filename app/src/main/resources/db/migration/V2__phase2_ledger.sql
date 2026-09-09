CREATE TABLE ledger_event (
    event_id        BIGSERIAL PRIMARY KEY,
    account_id      UUID NOT NULL,
    sequence_number BIGINT NOT NULL,   -- por conta, não global — usado pelo snapshot
    event_type      VARCHAR(30) NOT NULL, -- DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    amount_cents    BIGINT NOT NULL,
    metadata        JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (account_id, sequence_number)
);

CREATE TABLE account_snapshot (
    account_id           UUID PRIMARY KEY,
    balance_cents        BIGINT NOT NULL,
    last_sequence_number  BIGINT NOT NULL, -- contrato que evita ambiguidade de qual evento já foi incluído
    updated_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Saldo atual = snapshot + eventos com sequence_number > last_sequence_number.
-- Nunca lido isoladamente; sempre snapshot + delta.
