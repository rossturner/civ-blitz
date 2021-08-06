create table contract (
    contract_id         SERIAL PRIMARY KEY,
    symbol              varchar(10),
    long_name           varchar(2000),
    exchange            varchar(10),
    ibkr_contract_id    integer,

    previous_close      decimal,
    previous_close_date date,
    average_volume      bigint,
    low_52_week         decimal,
    high_52_week        decimal,

    last_updated        timestamp

);

CREATE INDEX idx_contract_symbol on contract (symbol);