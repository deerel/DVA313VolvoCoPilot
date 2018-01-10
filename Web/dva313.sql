-- phpMyAdmin SQL Dump
-- version 4.6.6deb4
-- https://www.phpmyadmin.net/
--
-- Värd: localhost:3306
-- Tid vid skapande: 02 jan 2018 kl 20:16
-- Serverversion: 10.1.23-MariaDB-9+deb9u1
-- PHP-version: 7.0.19-1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Databas: `dva313`
--

-- --------------------------------------------------------

--
-- Tabellstruktur `AssocUandC`
--

CREATE TABLE `AssocUandC` (
  `id` int(11) NOT NULL,
  `worker_id` varchar(50) DEFAULT NULL,
  `copilot_id` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Tabellstruktur `ConstructionSite`
--

CREATE TABLE `ConstructionSite` (
  `id` int(11) NOT NULL,
  `site_id` varchar(100) NOT NULL,
  `site_name` varchar(70) NOT NULL,
  `p1_longitude` double NOT NULL,
  `p1_latitude` double NOT NULL,
  `p2_longitude` double NOT NULL,
  `p2_latitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumpning av Data i tabell `ConstructionSite`
--

INSERT INTO `ConstructionSite` (`id`, `site_id`, `site_name`, `p1_longitude`, `p1_latitude`, `p2_longitude`, `p2_latitude`) VALUES
(1, 'CS001', 'MDH Dig site', 16.538994, 59.619332, 16.543307, 59.616923),
(2, 'CS002', 'Kristiansborgsbadet', 16.541698, 59.621339, 16.545732, 59.620721),
(3, 'CS003', 'RL Home', 16.567501, 59.632444, 16.568477, 59.631674),
(4, 'CS004', 'Spain site', -3.775272, 40.668342, -3.774371, 40.667191),
(5, 'CS005', 'simulator', 12.000736, 57.000408, 11.998969, 56.999482);

-- --------------------------------------------------------

--
-- Tabellstruktur `Copilot`
--

CREATE TABLE `Copilot` (
  `id` int(11) NOT NULL,
  `copilot_id` varchar(50) NOT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumpning av Data i tabell `Copilot`
--

INSERT INTO `Copilot` (`id`, `copilot_id`, `longitude`, `latitude`, `last_updated`) VALUES
(3, 'w001', 11.999655619717183, 56.999616337632716, '2018-01-02 16:16:31'),
(1, 'w005', 12.000419953717374, 57.00008351136844, '2018-01-02 12:22:55'),
(5, 'w007', 11.999642416476386, 56.99961244552511, '2018-01-02 16:05:12'),
(2, 'w008', 12.000406538029626, 57.00006561961197, '2018-01-02 12:30:09'),
(4, 'w009', 11.999890948905778, 56.99973152601115, '2018-01-02 13:48:10');

-- --------------------------------------------------------

--
-- Tabellstruktur `gpstest`
--

CREATE TABLE `gpstest` (
  `id` int(11) NOT NULL,
  `worker_id` varchar(20) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumpning av Data i tabell `gpstest`
--

INSERT INTO `gpstest` (`id`, `worker_id`, `latitude`, `longitude`, `time`) VALUES
(47, 'w001', 59.63313777137504, 16.567870652599595, '2017-12-14 13:04:30'),
(48, 'w001', 59.63250774853057, 16.568063928955844, '2017-12-14 13:04:58'),
(49, 'w001', 59.63248667831903, 16.568083789876074, '2017-12-14 13:05:08'),
(50, 'w001', 59.63191065684007, 16.568434579839046, '2017-12-14 13:06:23'),
(51, 'w001', 59.632242530210625, 16.568314605473795, '2017-12-14 13:06:24'),
(52, 'w001', 59.63295408391935, 16.568708545624478, '2017-12-14 13:06:25'),
(53, 'w001', 59.632890471961275, 16.568661417066664, '2017-12-14 13:06:26'),
(54, 'w001', 59.63277203777391, 16.568601702236865, '2017-12-14 13:06:27'),
(55, 'w001', 59.632700283611854, 16.56855300386871, '2017-12-14 13:06:28'),
(56, 'w001', 59.63263064438154, 16.56845302171393, '2017-12-14 13:06:29'),
(57, 'w001', 59.63256756281256, 16.568398508365544, '2017-12-14 13:06:30'),
(58, 'w001', 59.63254138031368, 16.568400915001668, '2017-12-14 13:06:31'),
(59, 'w001', 59.63247213903497, 16.56834421394224, '2017-12-14 13:06:32'),
(60, 'w001', 59.6325021469957, 16.56824235878021, '2017-12-14 13:06:33'),
(61, 'w001', 59.632522397574434, 16.56818358482308, '2017-12-14 13:06:35'),
(62, 'w001', 59.632458698558, 16.568232014380705, '2017-12-14 13:06:36'),
(63, 'w001', 59.632522397574434, 16.56818358482308, '2017-12-14 13:06:36'),
(64, 'w001', 59.63249547738152, 16.56824622576113, '2017-12-14 13:06:38'),
(65, 'w001', 59.63249547738152, 16.56824622576113, '2017-12-14 13:06:38'),
(66, 'w001', 59.63211299318346, 16.567992085563677, '2017-12-14 13:07:03'),
(67, 'w001', 59.63213283328238, 16.56798119202404, '2017-12-14 13:07:04'),
(68, 'w001', 59.63218020836074, 16.568073962229366, '2017-12-14 13:07:05'),
(69, 'w001', 59.63203937199965, 16.56802347306962, '2017-12-14 13:07:06'),
(70, 'w001', 59.63178264665498, 16.56810592684773, '2017-12-14 13:07:07'),
(71, 'w001', 59.63180131648784, 16.567912767488096, '2017-12-14 13:07:17'),
(72, 'w001', 59.632082148727385, 16.567574858102844, '2017-12-14 13:07:18'),
(73, 'w001', 59.632082148727385, 16.567574858102844, '2017-12-14 13:07:20'),
(74, 'w001', 59.632082148727385, 16.567574858102844, '2017-12-14 13:07:21'),
(75, 'w001', 59.63227585043673, 16.567806610809743, '2017-12-14 13:07:36'),
(76, 'w001', 59.632281271630646, 16.567810717870344, '2017-12-14 13:07:37'),
(77, 'w001', 59.63228393448191, 16.567813950513923, '2017-12-14 13:07:38'),
(78, 'w001', 59.63228399484304, 16.567823647870828, '2017-12-14 13:07:39'),
(79, 'w001', 59.63228540510133, 16.567834199067526, '2017-12-14 13:07:41'),
(80, 'w001', 59.63228537481043, 16.56783441489816, '2017-12-14 13:07:42'),
(81, 'w001', 59.6322858981147, 16.567834445972103, '2017-12-14 13:07:43'),
(82, 'w001', 59.63227698719134, 16.56784761958257, '2017-12-14 13:07:44'),
(83, 'w001', 59.632274904184776, 16.56783327280624, '2017-12-14 13:07:45'),
(84, 'w001', 59.632276883177454, 16.567817548450446, '2017-12-14 13:07:47'),
(85, 'w001', 59.63229262751432, 16.567899700903038, '2017-12-14 13:08:04'),
(86, 'w001', 59.632316149200676, 16.567931389442432, '2017-12-14 13:08:05'),
(87, 'w001', 59.632286346275, 16.56796478972358, '2017-12-14 13:08:07'),
(88, 'w001', 59.6323120955928, 16.568063829091248, '2017-12-14 13:08:08'),
(89, 'w001', 59.632303688185516, 16.568143533812442, '2017-12-14 13:08:09'),
(90, 'w001', 59.632287051190985, 16.568157819697525, '2017-12-14 13:08:10');

-- --------------------------------------------------------

--
-- Tabellstruktur `Worker`
--

CREATE TABLE `Worker` (
  `id` int(11) NOT NULL,
  `worker_id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(70) DEFAULT NULL,
  `role` enum('WORKER','MANAGER','ADMINISTRATOR') DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumpning av Data i tabell `Worker`
--

INSERT INTO `Worker` (`id`, `worker_id`, `name`, `password`, `role`, `longitude`, `latitude`, `last_updated`) VALUES
(1, 'w001', 'Rickard', 'pass', 'WORKER', 16.5043, 14.144298333333332, '2018-01-01 23:41:06'),
(2, 'w002', 'David', 'pass', 'WORKER', 16.5045472, 59.3585309, '2017-12-30 18:54:52'),
(4, 'w003', 'Johanna', 'pass', 'WORKER', 16.5405671, 59.6191607, '2017-12-20 12:30:39'),
(5, 'w004', 'Sixten', 'pass', 'WORKER', 0, 0, '2017-12-18 10:28:41'),
(6, 'w005', 'Sophia', 'pass', 'WORKER', 16.540556666666664, 59.61866499999999, '2018-01-02 13:07:00'),
(7, 'w006', 'Jose', 'pass', 'WORKER', 0, 0, '2017-12-28 19:38:04'),
(8, 'w007', 'Joe', 'pass', 'WORKER', NULL, NULL, '2017-12-17 12:20:22'),
(9, 'w008', 'Julia', 'pass', 'MANAGER', 15.859104078710896, 59.39443885207522, '2017-12-24 14:44:04'),
(10, 'w009', 'Alvaro', 'pass', 'WORKER', 0, 0, '2017-12-21 01:12:59');

--
-- Index för dumpade tabeller
--

--
-- Index för tabell `AssocUandC`
--
ALTER TABLE `AssocUandC`
  ADD UNIQUE KEY `id` (`id`),
  ADD KEY `worker_id` (`worker_id`),
  ADD KEY `copilot_id` (`copilot_id`);

--
-- Index för tabell `ConstructionSite`
--
ALTER TABLE `ConstructionSite`
  ADD PRIMARY KEY (`site_id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Index för tabell `Copilot`
--
ALTER TABLE `Copilot`
  ADD PRIMARY KEY (`copilot_id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Index för tabell `gpstest`
--
ALTER TABLE `gpstest`
  ADD PRIMARY KEY (`id`);

--
-- Index för tabell `Worker`
--
ALTER TABLE `Worker`
  ADD PRIMARY KEY (`worker_id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- AUTO_INCREMENT för dumpade tabeller
--

--
-- AUTO_INCREMENT för tabell `AssocUandC`
--
ALTER TABLE `AssocUandC`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT för tabell `ConstructionSite`
--
ALTER TABLE `ConstructionSite`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT för tabell `Copilot`
--
ALTER TABLE `Copilot`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT för tabell `gpstest`
--
ALTER TABLE `gpstest`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;
--
-- AUTO_INCREMENT för tabell `Worker`
--
ALTER TABLE `Worker`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
--
-- Restriktioner för dumpade tabeller
--

--
-- Restriktioner för tabell `AssocUandC`
--
ALTER TABLE `AssocUandC`
  ADD CONSTRAINT `AssocUandC_ibfk_1` FOREIGN KEY (`worker_id`) REFERENCES `Worker` (`worker_id`),
  ADD CONSTRAINT `AssocUandC_ibfk_2` FOREIGN KEY (`copilot_id`) REFERENCES `Copilot` (`copilot_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
