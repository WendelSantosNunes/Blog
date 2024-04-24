create table if not exists users (
    id bigint not null auto_increment,
    nome varchar(100) not null,
    email varchar(100) not null unique,
    phone varchar(11) not null,
    password varchar(50) not null,
    is_enabled boolean not null,
    role enum('ADMIN','STANDARD') not null,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    primary key(id)
);
