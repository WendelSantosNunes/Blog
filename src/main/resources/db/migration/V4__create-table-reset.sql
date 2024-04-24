create table password_reset_token_users (
    id bigint not null auto_increment,
    token VARCHAR(255),
    users_id BIGINT NOT NULL,
    expiry_date TIMESTAMP,
    FOREIGN KEY (users_id) REFERENCES users(id),

    primary key(id)
);
