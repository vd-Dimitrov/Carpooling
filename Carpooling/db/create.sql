create database if not exists carpooling;
use carpooling;


create table if not exists users (
    user_id int auto_increment primary key,
    username varchar(50) not null unique ,
    password varchar(100) not null ,
    first_name varchar(50) not null ,
    last_name varchar(50) not null ,
    email varchar(100) not null unique ,
    phone_number varchar(13) not null unique ,
    user_rating double default 0,
    created_at timestamp default current_timestamp,
    is_admin boolean
);

create table if not exists travels(
    travel_id int auto_increment primary key ,
    title varchar(50) not null ,
    starting_point varchar(50) not null ,
    ending_point varchar(50) not null ,
    driver_id int,
    departure_time datetime not null,
    travel_status enum('Upcoming', 'Complete', 'Cancelled'),
    free_spots int,
    created_at timestamp default current_timestamp,
    constraint travels_users_user_id_fk foreign key (driver_id) references users (user_id)
);

create table if not exists travel_applications(
    application_id int auto_increment primary key,
    applicant_id int,
    travel_id int,
    application_status enum('Waiting', 'Accepted', 'Rejected'),
    constraint travel_applications_users_user_id_fk foreign key (applicant_id) references users (user_id) ,
    constraint travel_applications_travels_travel_id_fk foreign key (travel_id) references travels (travel_id)
);

create table if not exists options(
    options_id int auto_increment primary key ,
    `option` varchar(50) not null unique
);

create table if not exists travels_options(
    `travels_id` int not null ,
    `options_id` int not null ,
    constraint travels_options_travels_travel_id_fk foreign key (travels_id) references travels (travel_id) ,
    constraint travels_options_options_option_id_fk foreign key (options_id) references options (options_id)
);

create table if not exists feedbacks(
    feedback_id int auto_increment primary key ,
    rating double not null ,
    CHECK ( rating >= 1 and rating <= 5 ),
    comment varchar(255),
    author_id int ,
    receiver_id int ,
    constraint feedbacks_users_user_id_fk1 foreign key (author_id) references users (user_id),
    constraint feedbacks_users_user_id_fk2 foreign key (receiver_id) references users (user_id)
);

