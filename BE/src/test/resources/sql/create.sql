create table if not exists carbob_user
(
    created_date  datetime(6)  not null,
    modified_date datetime(6)  not null,
    user_id       bigint auto_increment
    primary key,
    email         varchar(255) not null,
    nickname      varchar(255) not null,
    password      varchar(255) not null,
    constraint UK_b4w904b6t6v3my998mdcjpxa2
    unique (nickname),
    constraint UK_ipmgnkh0tskghwsrlaelxfk4p
    unique (email)
);

create table if not exists carbob
(
    fee_per_hour  int          not null,
    carbob_id     bigint auto_increment
    primary key,
    created_date  datetime(6)  not null,
    host_id       bigint       not null,
    modified_date datetime(6)  not null,
    address       varchar(255) not null,
    charger_type  varchar(255) not null,
    description   varchar(255) not null,
    install_type  varchar(255) not null,
    location_type varchar(255) not null,
    nickname      varchar(255) not null,
    qr_image_url  varchar(255) null,
    speed_type    varchar(255) not null,
    point         point        not null,
    constraint FK5403oi9petaknpcvmyjl2cb9
    foreign key (host_id) references carbob_user (user_id)
);

create table if not exists carbob_image
(
    carbob_id       bigint       not null,
    carbob_image_id bigint auto_increment
    primary key,
    created_date    datetime(6)  not null,
    modified_date   datetime(6)  not null,
    image_url       varchar(255) not null,
    constraint UK_1j5x9yrhl5ktgkrmv9yguqpkd
    unique (carbob_id),
    constraint FKksbtlp70ct76fugybllx9fik0
    foreign key (carbob_id) references carbob (carbob_id)
);

create table if not exists fcm_token
(
    created_date  datetime(6)  not null,
    fcm_token_id  bigint auto_increment
    primary key,
    modified_date datetime(6)  not null,
    user_id       bigint       not null,
    fcm_token     varchar(255) not null,
    constraint UK_cy4bm0je79m0rcun90igvrj8s
    unique (fcm_token),
    constraint FKmq163lhgtbw43yedkghale8qn
    foreign key (user_id) references carbob_user (user_id)
);

create table if not exists notification
(
    created_date    datetime(6)  not null,
    modified_date   datetime(6)  not null,
    notification_id bigint auto_increment
    primary key,
    user_id         bigint       not null,
    message         varchar(200) not null,
    constraint FKmmjd1u4dpok21es383b7o23u2
    foreign key (user_id) references carbob_user (user_id)
);

create table if not exists payment
(
    amount        int          not null,
    created_date  datetime(6)  not null,
    modified_date datetime(6)  not null,
    payment_id    bigint auto_increment
    primary key,
    target_id     bigint       not null,
    user_id       bigint       not null,
    pay_type      varchar(255) not null,
    constraint FK6fxls35fqjp4y5jukl8tnreu
    foreign key (user_id) references carbob_user (user_id)
);

create table if not exists reservation
(
    total_fee      int          not null,
    carbob_id      bigint       not null,
    created_date   datetime(6)  not null,
    guest_id       bigint       not null,
    modified_date  datetime(6)  not null,
    reservation_id bigint auto_increment
    primary key,
    state_type     varchar(255) not null,
    constraint FKebrrvoviecdxhkybntcxt98ro
    foreign key (guest_id) references carbob_user (user_id),
    constraint FKfnp702pkhqgp9g5rtvwlc8qps
    foreign key (carbob_id) references carbob (carbob_id)
);

create table if not exists reservation_line
(
    line_idx         int         not null,
    reservation_id   bigint      not null,
    reservation_time datetime(6) not null,
    primary key (line_idx, reservation_id),
    constraint FKmsp8j8yweij0ed4jjv0k8qbua
    foreign key (reservation_id) references reservation (reservation_id)
);
