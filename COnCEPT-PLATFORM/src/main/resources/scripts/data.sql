-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jun 15, 2015 at 01:11 PM
-- Server version: 5.5.40-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `concept`
--

-- --------------------------------------------------------



INSERT INTO `Locale` (`id`, `description`) VALUES
('EN', 'English');

-- --------------------------------------------------------



INSERT INTO `User` (`id`, `created_date`, `email`, `first_name`, `last_name`, `password`, `phone`, `status`, `username`, `locale`) VALUES
(2, NULL, 'admin@admin.eu', 'Admin', 'Admin', '35c1ec574813604b050730d15bb572242f38fc04', '', NULL, 'admin', 'EN');

