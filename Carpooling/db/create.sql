create database if not exists carpooling;
use carpooling;

create table if not exists users (
    user_id int auto_increment primary key ,
    username varchar(50) not null unique ,
    password varchar(100) not null ,
    first_name varchar(50) not null ,
    last_name varchar(50) not null ,
    email varchar(100) not null unique ,
    phone_number varchar(13) not null unique ,
    is_admin boolean not null
);

create table if not exists travels(
    travel_id int auto_increment primary key ,
    starting_point varchar(50) not null ,
    ending_point varchar(50) not null ,
    driver int not null ,
    departure_time datetime  not null,
    travel_status enum('UPCOMING', 'ONGOING', 'COMPLETE'),
    free_spots int,
    constraint travels_users_user_id_fk
        foreign key (driver) references users (user_id) on delete set null
);

create table if not exists options(
    options_id int NOT NULL AUTO_INCREMENT,
    `option` varchar(50) NOT NULL,
    PRIMARY KEY (options_id),
    UNIQUE KEY (options_id),
    UNIQUE KEY (`option`)
);

create table if not exists travels_options(
    `travels_id` int not null ,
    `options_id` int not null ,
    foreign key (travels_id) references travels (travel_id) on delete cascade,
    foreign key (options_id) references options (options_id) on delete cascade
);

create table if not exists feedbacks(
    feedback_id int auto_increment primary key ,
    rating float not null ,
    comment varchar(255),
    author int not null ,
    constraint `user_id`
        foreign key (author) references users (user_id) on delete set null
);

create table if not exists feedbacks_travels(
    feedbacks_id int not null ,
    travels_id int not null ,
    foreign key (feedbacks_id) references feedbacks (feedback_id) on delete cascade ,
    foreign key (travels_id) references travels (travel_id) on delete cascade
);