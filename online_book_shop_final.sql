-- -------------------------------------------------------------
-- TablePlus 3.12.6(366)
--
-- https://tableplus.com/
--
-- Database: online_book_shop
-- Generation Time: 2021-05-09 13:20:43.1340
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address1` varchar(45) NOT NULL,
  `address2` varchar(45) DEFAULT NULL,
  `address3` varchar(45) DEFAULT NULL,
  `city` varchar(45) NOT NULL,
  `county` varchar(45) NOT NULL,
  `postcode` varchar(45) NOT NULL,
  `country` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

CREATE TABLE `book` (
  `ISBN` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(45) NOT NULL,
  `language` varchar(45) NOT NULL,
  `stock` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`ISBN`),
  UNIQUE KEY `ISBN_UNIQUE` (`ISBN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fname` varchar(45) NOT NULL,
  `sname` varchar(45) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

CREATE TABLE `customer_address` (
  `customer_id` int(11) DEFAULT NULL,
  `address_id` int(11) DEFAULT NULL,
  KEY `id_idx` (`address_id`),
  KEY `id_idx1` (`customer_id`),
  CONSTRAINT `address_id` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `customer_id` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `delivery_company` (
  `reg_no` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `reg_date` datetime NOT NULL,
  `contract_due` datetime NOT NULL,
  PRIMARY KEY (`reg_no`),
  UNIQUE KEY `reg_num_UNIQUE` (`reg_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order` (
  `create_time` timestamp NOT NULL DEFAULT current_timestamp(),
  `update_time` timestamp NULL DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cust_id` int(11) NOT NULL,
  `type` varchar(45) NOT NULL,
  `addr_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `id_idx` (`cust_id`),
  KEY `id_idx1` (`addr_id`),
  CONSTRAINT `addr_id` FOREIGN KEY (`addr_id`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `cust_id` FOREIGN KEY (`cust_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

CREATE TABLE `order_book` (
  `order_id` int(11) NOT NULL,
  `ISBN` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `discount` decimal(10,2) DEFAULT NULL,
  `sale_price` decimal(10,2) DEFAULT NULL,
  KEY `id_idx` (`order_id`),
  KEY `ISBN_idx` (`ISBN`),
  CONSTRAINT `ISBN` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `order_delivery` (
  `ord_id` int(11) NOT NULL,
  `delivery_reg` int(11) NOT NULL,
  `due_date` datetime DEFAULT NULL,
  `delivery_staff_no` int(11) DEFAULT NULL,
  KEY `id_idx` (`ord_id`),
  KEY `company_reg_idx` (`delivery_reg`),
  CONSTRAINT `delivery_reg` FOREIGN KEY (`delivery_reg`) REFERENCES `delivery_company` (`reg_no`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `ord_id` FOREIGN KEY (`ord_id`) REFERENCES `order` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `address` (`id`, `address1`, `address2`, `address3`, `city`, `county`, `postcode`, `country`) VALUES
(1, '1 East Street', NULL, NULL, 'Tynemouth', 'Tyne & Wear', 'NE30 4EB', 'United Kingdom'),
(2, 'Potters Cottage', 'Islington', NULL, 'London', 'London', 'SW1 1SB', 'United Kingdom'),
(3, 'Beach Hut 15', 'Markov Complex', 'Isle Town', 'St Kitts', 'St.Kitts', '10011', 'Bart & St.Kitts'),
(4, '12 New Road', NULL, NULL, 'Liverpool', 'Humberside', 'LV3 4SG', 'United Kingdom'),
(5, 'Bamburgh Castle', 'The Coast', '', 'Bamburgh', 'Northumberland', ' NE68 1AS', 'United Kingdom');

INSERT INTO `book` (`ISBN`, `title`, `author`, `publisher`, `language`, `stock`, `price`) VALUES
(543697, 'On The Road', 'Jack Kerouac', 'HarperCollins', 'English', 18, 10.99),
(2564532, 'Ready Player One', 'Ernest Cline', 'HarperCollins', 'English', 22, 8.99),
(5436596, 'Fiesta', 'Ernest Hemingway', 'HarperCollins', 'English', 4, 10.99),
(9783354, 'Matilda', 'Roald Dahl', 'Penguin', 'English', 12, 6.99),
(54367594, 'Dune', 'Frank Herbert', 'MacMillan', 'English', 8, 8.99),
(87534923, 'Hitchhikers Guide To The Galaxy', 'Douglas Adams', 'Penguin', 'English', 24, 6.99),
(101134567, 'The Blue Death', 'Richard Cosgrove', 'Penguin', 'English', 13, 6.99),
(239537323, 'The Count Of Monte Cristo', 'Alexandre Dumas', 'Wordsworth', 'French', 16, 12.99),
(749318678, 'Of Mice And Men', 'John Steinbeck', 'Penguin', 'English', 10, 7.99),
(848814290, 'One Hundred Years Of Solitude', 'Gabriel Garcia Marquez', 'HarperCollins', 'Spanish', 12, 10.99),
(1131941047, 'East of Eden', 'John Steinbeck', 'Penguin', 'English', 5, 8.99),
(1472200349, 'Ocean At The End Of The Lane', 'Neil Gaiman', 'Penguin', 'English', 8, 7.99),
(1501260421, 'the road', 'Cormac McCarthy', 'MacMillan', 'English', 15, 8.99),
(1856862216, 'Jurassic Park', 'Michael Crichton', 'MacMillan', 'English', 25, 6.99);

INSERT INTO `customer` (`id`, `fname`, `sname`, `phone`, `email`) VALUES
(1, 'Jack', 'Healy', '01912963432', 'jack@jack.jack'),
(2, 'Joe', 'Strummer', '01214325673', 'joe@clash.com'),
(3, 'George', 'Harrison', '07654397683', 'george@abbeyroad.com'),
(5, 'Nancy', 'Nowinter', '07654392847', 'nancy@fofof.com'),
(6, 'Bruce', 'Wayne', '0800888666', 'bruce@wayne.com'),
(7, 'Jenny', 'Johnson', '07897654564', 'jenjo@yahoo.com'),
(8, 'John', 'Henry', '07659382753', 'johnhenry@gmail.com'),
(12, 'Wendy', 'Rice', '07659384576', 'wendobingo@gmail.com'),
(13, 'Rachel', 'McCready', '07695837586', 'rach@gmail.com'),
(14, 'Jane', 'Roberts', '07968465963', 'jane@rob.com');

INSERT INTO `customer_address` (`customer_id`, `address_id`) VALUES
(1, 1),
(2, 2),
(3, 3),
(3, 4);

INSERT INTO `delivery_company` (`reg_no`, `name`, `reg_date`, `contract_due`) VALUES
(12345, 'Parsons', '2017-01-01 00:00:00', '2025-12-01 00:00:00'),
(49765, 'Parcelforce', '2019-06-05 00:00:00', '2030-08-12 00:00:00'),
(54321, 'Speedys', '2020-05-05 00:00:00', '2023-07-12 00:00:00');

INSERT INTO `order` (`create_time`, `update_time`, `id`, `cust_id`, `type`, `addr_id`) VALUES
('2021-04-18 18:29:55', NULL, 1, 1, 'books x2', 1);

INSERT INTO `order_book` (`order_id`, `ISBN`, `quantity`, `discount`, `sale_price`) VALUES
(1, 1501260421, 1, NULL, 8.99),
(1, 1131941047, 1, NULL, 8.99);

INSERT INTO `order_delivery` (`ord_id`, `delivery_reg`, `due_date`, `delivery_staff_no`) VALUES
(1, 54321, '2021-05-21 00:00:00', 3);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;