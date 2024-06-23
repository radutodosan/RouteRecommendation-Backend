CREATE TABLE `users` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `username` varchar(255),
  `full_name` varchar(255),
  `password` varchar(255),
  `email` varchar(255),
  `picture_url` varchar(255),
  `points` int,
  `saved_address` varchar(255)
);

CREATE TABLE `saved_addresses` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `home` varchar(255),
  `work` varchar(255),
  `school` varchar(255),
  `other` varchar(255)
);

CREATE TABLE `routes` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `start` varchar(255),
  `end` varchar(255),
  `transport` ENUM ('electric_vehicle', 'public_transport', 'bike', 'on_foot'),
  `distance` double,
  `time` double,
  `emissions_saved` int,
  `cal_burned` int,
  `status` ENUM ('pending', 'completed'),
  `date` date
);

CREATE TABLE `friendship` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user01_id` int,
  `user02_id` int,
  `status` ENUM ('pending', 'completed')
);

CREATE TABLE `uvt_rewards` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `placement` int,
  `reward` varchar(255),
  `status` ENUM ('pending', 'completed'),
  `date` date
);

ALTER TABLE `saved_addresses` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `friendship` ADD FOREIGN KEY (`user01_id`) REFERENCES `users` (`id`);

ALTER TABLE `friendship` ADD FOREIGN KEY (`user02_id`) REFERENCES `users` (`id`);

ALTER TABLE `routes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `uvt_rewards` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
