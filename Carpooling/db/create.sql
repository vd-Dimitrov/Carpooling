CREATE DATABASE IF NOT EXISTS `carpooling`;
USE `carpooling`;

CREATE TABLE IF NOT EXISTS `users` (
    `user_id` INT(11) NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL ,
    `password` varchar(100) NOT NULL ,
    `first_name` varchar(50) NOT NULL ,
    `last_name` varchar(50) NOT NULL ,
    `email` varchar(100) NOT NULL  ,
    `phone_number` varchar(13) NOT NULL,
    `is_admin` boolean NOT NULL ,
    PRIMARY KEY (`user_id`),
    UNIQUE KEY (`user_id`),
    UNIQUE KEY (`email`)
);

CREATE TABLE IF NOT EXISTS `travels`(
    `travel_id` INT(11) NOT NULL AUTO_INCREMENT,
    `starting_point` varchar(50) NOT NULL,
    `ending_point` varchar(50) NOT NULL ,
    `driver` INT(11) NOT NULL ,
    `departure_time` DATETIME NOT NULL,
    `travel_status` enum('UPCOMING', 'ONGOING', 'COMPLETE'),
    `free_spots` int(11),
    PRIMARY KEY (`travel_id`),
    UNIQUE KEY (`travel_id`),
    KEY `travels_users_user_id_fk` (`driver`),
    CONSTRAINT `travels_users_user_id_fk` FOREIGN KEY (`driver`) REFERENCES `users` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `options`(
    `options_id` INT(11) NOT NULL AUTO_INCREMENT,
    `option` varchar(50) NOT NULL,
    PRIMARY KEY (`options_id`),
    UNIQUE KEY (`options_id`),
    UNIQUE KEY (`option`)
);

CREATE TABLE IF NOT EXISTS `travels_options`(
    `travels_options_id` INT(11) NOT NULL AUTO_INCREMENT,
    `travels_id` INT(11) NOT NULL ,
    `options_id` INT(11) NOT NULL ,
    PRIMARY KEY (`travels_options_id`),
    UNIQUE KEY (`travels_options_id`),
    CONSTRAINT `travels_options_travels_travel_id_fk` FOREIGN KEY (`travels_id`) REFERENCES `travels` (`travel_id`) ON DELETE  CASCADE ,
    CONSTRAINT `travels_options_options_option_id_fk` FOREIGN KEY (`options_id`) REFERENCES `options` (`options_id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `feedbacks`(
    `feedback_id` INT(11) NOT NULL AUTO_INCREMENT,
    `rating` FLOAT NOT NULL,
    `comment` VARCHAR(255),
    `author` INT(11) NOT NULL ,
    PRIMARY KEY (`feedback_id`),
    UNIQUE KEY (`feedback_id`),
    CONSTRAINT `user_id` FOREIGN KEY (`author`) REFERENCES `users` (`user_id`)
);

CREATE TABLE IF NOT EXISTS `feedbacks_travels`(
    `feedbacks_travels_id` INT(11) NOT NULL AUTO_INCREMENT,
    `feedbacks_id` INT(11) NOT NULL ,
    `travels_id` INT(11) NOT NULL ,
    PRIMARY KEY (`feedbacks_travels_id`),
    UNIQUE KEY (`feedbacks_travels_id`),
    CONSTRAINT `feedbacks_travels_feedbacks_feedback_id_fk` FOREIGN KEY (`feedbacks_id`) REFERENCES `feedbacks` (`feedback_id`),
    CONSTRAINT `feedbacks_travels_travels_travel_id_fk` FOREIGN KEY (`travels_id`) REFERENCES `travels` (`travel_id`)
);