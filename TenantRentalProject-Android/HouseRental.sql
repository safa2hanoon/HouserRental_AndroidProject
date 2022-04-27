-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Jan 09, 2022 at 06:01 PM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `HouseRental`
--

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE `cities` (
  `city_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `country_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `cities`
--

INSERT INTO `cities` (`city_id`, `name`, `country_id`) VALUES
(1, 'Ramallah', 1),
(2, 'Jerusalim', 1),
(3, 'Amman', 2),
(4, 'Aqaba', 2),
(5, 'Mekkah', 3),
(6, 'Jeddah', 3),
(7, 'Beirut', 4),
(8, 'Sayda', 4),
(9, 'Cairo', 5),
(10, 'Jeezah', 5),
(11, 'Damascus', 6),
(12, 'Halab', 6);

-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

CREATE TABLE `countries` (
  `country_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `zip_code` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `countries`
--

INSERT INTO `countries` (`country_id`, `name`, `zip_code`) VALUES
(1, 'Palestine', '00970'),
(2, 'Jordan', '00962'),
(3, 'Saudi Arabia', '00966'),
(4, 'Lebanon ', '00961'),
(5, 'Egypt', '0020'),
(6, 'Syria', '00963');

-- --------------------------------------------------------

--
-- Table structure for table `nationalities`
--

CREATE TABLE `nationalities` (
  `nationality_id` int(11) NOT NULL,
  `nationality` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `nationalities`
--

INSERT INTO `nationalities` (`nationality_id`, `nationality`) VALUES
(5, 'Egyptian'),
(2, 'Jordanian'),
(4, 'lebaneese'),
(1, 'Palestinian'),
(3, 'Saudi'),
(6, 'Syrian');

-- --------------------------------------------------------

--
-- Table structure for table `properties`
--

CREATE TABLE `properties` (
  `property_id` int(11) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `availability_date` datetime(6) DEFAULT NULL,
  `bedrooms_count` int(11) DEFAULT NULL,
  `construction_year` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `has_balcony` bit(1) DEFAULT NULL,
  `has_garden` bit(1) DEFAULT NULL,
  `rental_price` int(11) DEFAULT NULL,
  `rented` bit(1) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `surface_area` int(11) DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `agency_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `properties`
--

INSERT INTO `properties` (`property_id`, `created_at`, `availability_date`, `bedrooms_count`, `construction_year`, `description`, `has_balcony`, `has_garden`, `rental_price`, `rented`, `status`, `surface_area`, `city_id`, `agency_id`) VALUES
(2, '2022-01-08 23:57:13.000000', '2022-01-08 23:57:13.000000', 2, 2019, 'first level apartment with garden', b'1', b'0', 150, b'1', 'unfurnished', 100, 1, 'agency@mail.com'),
(3, '2022-01-08 23:57:13.000000', '2022-01-08 23:57:13.000000', 2, 2019, 'Description about property, Description about property, Description about property, Description about property, Description about property, Description about property,', b'0', b'1', 250, b'0', 'unfurnished', 150, 2, 'agency2@mail.com'),
(4, '2022-01-08 23:57:13.000000', '2022-01-08 23:57:13.000000', 3, 2019, 'Description about property, Description about property, Description about property, Description about property, Description about property, Description about property,', b'1', b'1', 450, b'0', 'furnished', 250, 1, 'agency2@mail.com'),
(5, '2022-01-08 23:57:13.000000', '2022-01-08 23:57:13.000000', 4, 2019, 'Description about property, Description about property, Description about property, Description about property, Description about property, Description about property,', b'0', b'0', 550, b'0', 'furnished', 350, 2, 'agency2@mail.com'),
(6, '2022-01-08 23:57:13.000000', '2022-01-08 23:57:13.000000', 1, 2019, 'Description about property, Description about property, Description about property, Description about property, Description about property, Description about property,', b'1', b'0', 150, b'0', 'unfurnished', 150, 1, 'agency2@mail.com');

-- --------------------------------------------------------

--
-- Table structure for table `propertyimgee`
--

CREATE TABLE `propertyimgee` (
  `image_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `property_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `propertyimgee`
--

INSERT INTO `propertyimgee` (`image_id`, `file_name`, `property_id`) VALUES
(3, 'p2.jpg', 2),
(4, 'p3.jpg', 3),
(5, 'p4.jpg', 4),
(6, 'p5.jpg', 5),
(7, 'p6.jpg', 6);

-- --------------------------------------------------------

--
-- Table structure for table `rentalhistory`
--

CREATE TABLE `rentalhistory` (
  `request_id` int(11) NOT NULL,
  `property_id` int(11) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rentalhistory`
--

INSERT INTO `rentalhistory` (`request_id`, `property_id`, `email_address`) VALUES
(1, 2, 'ahmad@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `rentalrequest`
--

CREATE TABLE `rentalrequest` (
  `request_id` int(11) NOT NULL,
  `result_id` int(11) NOT NULL,
  `property_id` int(11) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rentalrequest`
--

INSERT INTO `rentalrequest` (`request_id`, `result_id`, `property_id`, `email_address`) VALUES
(1, 1, 2, 'ahmad@gmail.com');

-- --------------------------------------------------------

--
-- Table structure for table `rentingagencies`
--

CREATE TABLE `rentingagencies` (
  `email_address` varchar(255) NOT NULL,
  `hashed_password` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `city_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `rentingagencies`
--

INSERT INTO `rentingagencies` (`email_address`, `hashed_password`, `name`, `phone_number`, `city_id`, `country_id`) VALUES
('agency2@mail.com', '$31$16$aLT28_puUKe8fw77x6LjyzBaKP4pocb-QLwUXlKNYA0', 'agency 2', '00970654891324', 1, 1),
('agency@mail.com', '$31$16$q1X9DdtE7pfXqmEOlw9zZLH-6dhCBACdADh-oVEkpE0', 'agency1', '00970123456789', 2, 1);

-- --------------------------------------------------------

--
-- Table structure for table `tenants`
--

CREATE TABLE `tenants` (
  `email_address` varchar(255) NOT NULL,
  `family_size` int(11) NOT NULL,
  `first_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `hashed_password` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `monthly_salary` int(11) NOT NULL,
  `occupation` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `nationality_id` int(11) DEFAULT NULL,
  `city_id` int(11) DEFAULT NULL,
  `country_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `tenants`
--

INSERT INTO `tenants` (`email_address`, `family_size`, `first_name`, `gender`, `hashed_password`, `last_name`, `monthly_salary`, `occupation`, `phone_number`, `nationality_id`, `city_id`, `country_id`) VALUES
('ahmad@gmail.com', 3, 'ahmad', 'male', '$31$16$7xGHWOgRtk05yWt-ZGSEjYT3iHyvQk91P7bRoHqG8DM', 'Ali', 1000, 'Engineer', '00970123456789', 1, 2, 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cities`
--
ALTER TABLE `cities`
  ADD PRIMARY KEY (`city_id`),
  ADD UNIQUE KEY `UK_l61tawv0e2a93es77jkyvi7qa` (`name`),
  ADD KEY `FK6gatmv9dwedve82icy8wrkdmk` (`country_id`);

--
-- Indexes for table `countries`
--
ALTER TABLE `countries`
  ADD PRIMARY KEY (`country_id`),
  ADD UNIQUE KEY `UK_1pyiwrqimi3hnl3vtgsypj5r` (`name`),
  ADD UNIQUE KEY `UK_4rjgqu7lf96yj2igiewi22yuu` (`zip_code`);

--
-- Indexes for table `nationalities`
--
ALTER TABLE `nationalities`
  ADD PRIMARY KEY (`nationality_id`),
  ADD UNIQUE KEY `UK_12dw9wtufmiacyb4mpq9iuqtu` (`nationality`);

--
-- Indexes for table `properties`
--
ALTER TABLE `properties`
  ADD PRIMARY KEY (`property_id`),
  ADD KEY `FKk566k4d814s2e8gne83deonc0` (`city_id`),
  ADD KEY `FK3bjsa0vtysbdammcntkvvixwd` (`agency_id`);

--
-- Indexes for table `propertyimgee`
--
ALTER TABLE `propertyimgee`
  ADD PRIMARY KEY (`image_id`),
  ADD KEY `FK6j66dtj7gbrqvq14duw8cqpmh` (`property_id`);

--
-- Indexes for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  ADD PRIMARY KEY (`request_id`),
  ADD KEY `FK1f9a1upqyde6q512pllfs72d2` (`property_id`),
  ADD KEY `FKkxio0ou7e0jgtsqsgyjfnyp9f` (`email_address`);

--
-- Indexes for table `rentalrequest`
--
ALTER TABLE `rentalrequest`
  ADD PRIMARY KEY (`request_id`),
  ADD KEY `FKcfiud78i4rc813bv5g9dov1kx` (`property_id`),
  ADD KEY `FKqxj6maowjiqa809rwye95s1eq` (`email_address`);

--
-- Indexes for table `rentingagencies`
--
ALTER TABLE `rentingagencies`
  ADD PRIMARY KEY (`email_address`),
  ADD KEY `FKk8y5rj7a8uysv1cvmynicil6q` (`city_id`),
  ADD KEY `FK55ykcpni9ud6hlrhef9otrgor` (`country_id`);

--
-- Indexes for table `tenants`
--
ALTER TABLE `tenants`
  ADD PRIMARY KEY (`email_address`),
  ADD KEY `FKheyqprxle8765r4wsq5faj9gf` (`nationality_id`),
  ADD KEY `FKdp7kucyelgo662l4de6yd669f` (`city_id`),
  ADD KEY `FKnthaoddi0x2wj0n2fig0puhut` (`country_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cities`
--
ALTER TABLE `cities`
  MODIFY `city_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `countries`
--
ALTER TABLE `countries`
  MODIFY `country_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `nationalities`
--
ALTER TABLE `nationalities`
  MODIFY `nationality_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `properties`
--
ALTER TABLE `properties`
  MODIFY `property_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `propertyimgee`
--
ALTER TABLE `propertyimgee`
  MODIFY `image_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `rentalrequest`
--
ALTER TABLE `rentalrequest`
  MODIFY `request_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cities`
--
ALTER TABLE `cities`
  ADD CONSTRAINT `FK6gatmv9dwedve82icy8wrkdmk` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`);

--
-- Constraints for table `properties`
--
ALTER TABLE `properties`
  ADD CONSTRAINT `FK3bjsa0vtysbdammcntkvvixwd` FOREIGN KEY (`agency_id`) REFERENCES `rentingagencies` (`email_address`),
  ADD CONSTRAINT `FKk566k4d814s2e8gne83deonc0` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`);

--
-- Constraints for table `propertyimgee`
--
ALTER TABLE `propertyimgee`
  ADD CONSTRAINT `FK6j66dtj7gbrqvq14duw8cqpmh` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`);

--
-- Constraints for table `rentalhistory`
--
ALTER TABLE `rentalhistory`
  ADD CONSTRAINT `FK1f9a1upqyde6q512pllfs72d2` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`),
  ADD CONSTRAINT `FKkxio0ou7e0jgtsqsgyjfnyp9f` FOREIGN KEY (`email_address`) REFERENCES `tenants` (`email_address`);

--
-- Constraints for table `rentalrequest`
--
ALTER TABLE `rentalrequest`
  ADD CONSTRAINT `FKcfiud78i4rc813bv5g9dov1kx` FOREIGN KEY (`property_id`) REFERENCES `properties` (`property_id`),
  ADD CONSTRAINT `FKqxj6maowjiqa809rwye95s1eq` FOREIGN KEY (`email_address`) REFERENCES `tenants` (`email_address`);

--
-- Constraints for table `rentingagencies`
--
ALTER TABLE `rentingagencies`
  ADD CONSTRAINT `FK55ykcpni9ud6hlrhef9otrgor` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`),
  ADD CONSTRAINT `FKk8y5rj7a8uysv1cvmynicil6q` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`);

--
-- Constraints for table `tenants`
--
ALTER TABLE `tenants`
  ADD CONSTRAINT `FKdp7kucyelgo662l4de6yd669f` FOREIGN KEY (`city_id`) REFERENCES `cities` (`city_id`),
  ADD CONSTRAINT `FKheyqprxle8765r4wsq5faj9gf` FOREIGN KEY (`nationality_id`) REFERENCES `nationalities` (`nationality_id`),
  ADD CONSTRAINT `FKnthaoddi0x2wj0n2fig0puhut` FOREIGN KEY (`country_id`) REFERENCES `countries` (`country_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
