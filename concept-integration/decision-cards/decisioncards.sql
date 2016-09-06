SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

CREATE DATABASE IF NOT EXISTS `concept`;
USE `concept`;

CREATE TABLE `badecisioncard` (
  `id` int(11) NOT NULL,
  `title` longtext NOT NULL,
  `keywords` longtext NOT NULL,
  `team` longtext NOT NULL,
  `decision` longtext NOT NULL,
  `ba_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `created_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `fmdecisioncard` (
  `id` int(11) NOT NULL,
  `title` longtext NOT NULL,
  `keywords` longtext NOT NULL,
  `team` longtext NOT NULL,
  `decision` longtext NOT NULL,
  `fm_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `created_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mbdecisioncard` (
  `id` int(11) NOT NULL,
  `title` longtext NOT NULL,
  `keywords` longtext NOT NULL,
  `team` longtext NOT NULL,
  `decision` longtext NOT NULL,
  `mb_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `created_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `mmdecisioncard` (
  `id` int(11) NOT NULL,
  `title` longtext NOT NULL,
  `keywords` longtext NOT NULL,
  `team` longtext NOT NULL,
  `decision` longtext NOT NULL,
  `mm_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `created_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sbdecisioncard` (
  `id` int(11) NOT NULL,
  `title` longtext NOT NULL,
  `keywords` longtext NOT NULL,
  `team` longtext NOT NULL,
  `decision` longtext NOT NULL,
  `sb_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `created_date` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `badecisioncard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ba_id` (`ba_id`),
  ADD KEY `uid` (`uid`);

ALTER TABLE `fmdecisioncard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_awsnn11obirpwqbb1me9weiol` (`fm_id`),
  ADD KEY `FK_f37lhkb6mhhwi2lg8xcquol11` (`uid`);

ALTER TABLE `mbdecisioncard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`),
  ADD KEY `mb_id` (`mb_id`);

ALTER TABLE `mmdecisioncard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`),
  ADD KEY `mm_id` (`mm_id`);

ALTER TABLE `sbdecisioncard`
  ADD PRIMARY KEY (`id`),
  ADD KEY `uid` (`uid`),
  ADD KEY `sb_id` (`sb_id`);


ALTER TABLE `badecisioncard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `fmdecisioncard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `mbdecisioncard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `mmdecisioncard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `sbdecisioncard`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE `badecisioncard`
  ADD CONSTRAINT `FK_3cieu8rpi1wsgajk1lns4cir5` FOREIGN KEY (`ba_id`) REFERENCES `briefanalysis` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_mt2edushianb656vhwr5ay8u1` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `fmdecisioncard`
  ADD CONSTRAINT `FK_awsnn11obirpwqbb1me9weiol` FOREIGN KEY (`fm_id`) REFERENCES `filemanagement` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_f37lhkb6mhhwi2lg8xcquol11` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `mbdecisioncard`
  ADD CONSTRAINT `FK_2t9ffeeq9xixnlqlyl4xqpl5w` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_lp6saqk44edrjl1th5ihkbrle` FOREIGN KEY (`mb_id`) REFERENCES `moodboard` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `mmdecisioncard`
  ADD CONSTRAINT `FK_o9d5d2b96kqxdlqyy7b36a6n2` FOREIGN KEY (`uid`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FK_opp4lvsvcj47e57mjsj7kaimp` FOREIGN KEY (`mm_id`) REFERENCES `mindmap` (`id`);

ALTER TABLE `sbdecisioncard`
  ADD CONSTRAINT `FK_318ol3j8h1jocpn9a7pugd2pn` FOREIGN KEY (`sb_id`) REFERENCES `storyboard` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_jq0etx2xuqolha07fmnero2d` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
