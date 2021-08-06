create table contract (
    contract_id         integer     not null auto_increment,
    symbol              varchar(10),
    long_name           varchar(2000),
    exchange            varchar(10),
    ibkr_contract_id    integer,

    previous_close      decimal,
    previous_close_date date,
    average_volume      bigint,
    low_52_week         decimal,
    high_52_week        decimal,

    last_updated        timestamp,

    primary key (contract_id)
);

CREATE INDEX idx_contract_symbol on contract (symbol);