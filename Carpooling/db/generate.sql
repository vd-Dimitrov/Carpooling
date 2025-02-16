INSERT INTO carpooling.users (username, password, first_name, last_name, email, phone_number, is_admin) VALUES
('jsmith', 'P@ssw0rd123', 'John', 'Smith', 'jsmith@example.com', '0888123456', 0),
('emily_w', 'aB!2cD3eF', 'Emily', 'Watson', 'emily.w@example.com', '0899765432', 0),
('tommyG', 'Tommy$Pass', 'Tom', 'Green', 'tommyg@example.com', '0877123456', 0),
('sarah.k', 'S@r@h2024', 'Sarah', 'Keller', 'sarah.k@example.com', '0898123456', 0),
('maxT14', 'Maxy!987', 'Max', 'Turner', 'maxT14@example.com', '0887654321', 0),
('lily_m', 'L1lY_R0cks', 'Lily', 'Moore', 'lily.m@example.com', '0876543210', 0),
('charles78', 'Charles#78', 'Charles', 'Wilson', 'charles78@example.com', '0898543210', 0),
('zoe1999', 'Zoe1999!Pass', 'Zoe', 'Harris', 'zoe1999@example.com', '0879123456', 0),
('robert88', 'R0b@rtP88', 'Robert', 'Adams', 'robert88@example.com', '0897345678', 0),
('mia_star', 'Mia!2TheMoon', 'Mia', 'Star', 'mia.star@example.com', '0889234567', 0),
('loganB', 'L0gan_Boss', 'Logan', 'Bennett', 'loganb@example.com', '0876345678', 0),
('sophie22', 'Sophie@22!', 'Sophie', 'Carter', 'sophie22@example.com', '0899123456', 0),
('danX', 'Xp@55w0rd!', 'Daniel', 'Xavier', 'danx@example.com', '0887987654', 0),
('lucas_g', 'G#Lucas2023', 'Lucas', 'Graham', 'lucas.g@example.com', '0898564321', 0),
('ella.rose', 'R0seElla_99', 'Ella', 'Rose', 'ella.rose@example.com', '0887543210', 0),
('ben2020', 'B3n_Tiger', 'Ben', 'Ford', 'ben2020@example.com', '0879456123', 0),
('julia_k', 'Jk@R0ck$', 'Julia', 'King', 'julia.k@example.com', '0887567890', 0),
('kevin_m', 'Kev_M$$ter', 'Kevin', 'Morgan', 'kevin.m@example.com', '0899786543', 0),
('emmaL', 'Emma_L0ve', 'Emma', 'Lewis', 'emmal@example.com', '0876345897', 0),
('nick_42', 'N!ck42Win', 'Nick', 'Anderson', 'nick42@example.com', '0889123456', 0);

INSERT INTO carpooling.travels (title, starting_point, ending_point, driver_id, departure_time, travel_status, free_spots) VALUES
                                                                                                                  ('Mountain Escape', 'Sofia', 'Bansko', 5, '2025-03-15 08:00:00', 1, 2),
                                                                                                                  ('Seaside Adventure', 'Varna', 'Burgas', 12, '2025-04-10 09:30:00', 3,  3),
                                                                                                                  ('Balkan Road Trip', 'Plovdiv', 'Veliko Tarnovo', 7, '2025-05-05 07:45:00', 1,  1),
                                                                                                                  ('Danube River Journey', 'Ruse', 'Vidin', 14, '2025-06-01 14:20:00', 4,   4),
                                                                                                                  ('Countryside Getaway', 'Stara Zagora', 'Kazanlak', 3, '2025-07-22 06:10:00', 1,  0),
                                                                                                                  ('Sunset in the Hills', 'Gabrovo', 'Tryavna', 9, '2025-08-18 17:50:00', 4,   2),
                                                                                                                  ('Thracian Trek', 'Haskovo', 'Kardzhali', 11, '2025-09-12 10:15:00', 3,   1),
                                                                                                                  ('Sofia City Tour', 'Sofia', 'Sofia', 4, '2025-10-03 13:40:00',3,   0),
                                                                                                                  ('Black Sea Retreat', 'Burgas', 'Varna', 16, '2025-11-27 15:00:00',3,   3),
                                                                                                                  ('Winter Wonderland', 'Bansko', 'Pamporovo', 8, '2025-12-20 05:30:00',  1, 4),
                                                                                                                  ('Forest Hiking', 'Smolyan', 'Devin', 13, '2026-01-05 07:00:00', 3,   1),
                                                                                                                  ('Hidden Waterfalls', 'Kyustendil', 'Rila', 6, '2026-02-14 12:25:00',3,  2),
                                                                                                                  ('Castle Exploration', 'Veliko Tarnovo', 'Lovech', 15, '2026-03-09 11:50:00',  1, 3),
                                                                                                                  ('Wine Tasting Trip', 'Melnik', 'Sandanski', 2, '2026-04-22 16:35:00', 1,  0),
                                                                                                                  ('Cultural Heritage', 'Dobrich', 'Shumen', 18, '2026-05-17 18:10:00', 1,  4);