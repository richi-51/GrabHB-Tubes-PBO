-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 19, 2024 at 08:37 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `grab_hb_tubes`
--

-- --------------------------------------------------------

--
-- Table structure for table `laporan`
--

CREATE TABLE `laporan` (
  `ID_Laporan` int(11) NOT NULL,
  `ID_Order` int(11) NOT NULL,
  `isiKeluhan` varchar(255) DEFAULT NULL,
  `statusLaporan` enum('Waiting','On_Process','Done') DEFAULT NULL,
  `createdAt` date DEFAULT NULL,
  `finishAt` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `laporan`
--

INSERT INTO `laporan` (`ID_Laporan`, `ID_Order`, `isiKeluhan`, `statusLaporan`, `createdAt`, `finishAt`) VALUES
(1, 2, 'Driver tidak datang tepat waktu', 'Done', '2024-12-16', '2024-12-17');

-- --------------------------------------------------------

--
-- Table structure for table `managelaporan`
--

CREATE TABLE `managelaporan` (
  `ID_Laporan` int(11) NOT NULL,
  `ID_Admin` int(11) NOT NULL,
  `admin_note` varchar(255) DEFAULT NULL,
  `assign_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `managelaporan`
--

INSERT INTO `managelaporan` (`ID_Laporan`, `ID_Admin`, `admin_note`, `assign_date`) VALUES
(1, 3, 'Keluhan selesai diproses', '2024-12-17');

-- --------------------------------------------------------

--
-- Table structure for table `notlp`
--

CREATE TABLE `notlp` (
  `ID_Tlp` int(11) NOT NULL,
  `ID_User` int(11) DEFAULT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `notlp`
--

INSERT INTO `notlp` (`ID_Tlp`, `ID_User`, `phoneNumber`) VALUES
(1, 1, '081234567890'),
(2, 2, '081345678901'),
(3, 3, '081456789012'),
(4, 4, '081567890123'),
(5, 5, '081678901234'),
(7, 7, '082234543245'),
(9, 10, '082354234253'),
(10, 11, '081919191919'),
(11, 12, '084535675645'),
(12, 13, '087777777777'),
(13, 14, '08123432143'),
(14, 15, '081717243445'),
(15, 16, '081234567453');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `ID_Order` int(11) NOT NULL,
  `ID_Customer` int(11) NOT NULL,
  `ID_Driver` int(11) DEFAULT NULL,
  `ID_Voucher` int(11) DEFAULT NULL,
  `ID_WilayahPickUp` int(11) NOT NULL,
  `ID_WilayahDestination` int(11) NOT NULL,
  `serviceType` enum('GrabCar','GrabBike') DEFAULT NULL,
  `order_status` enum('Complete','On_Progress','Cancelled') DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `updatedOrder` datetime DEFAULT NULL,
  `paymentMethod` enum('Cash','OVO') DEFAULT NULL,
  `price` double DEFAULT NULL,
  `orderType` enum('Hemat','Reguler','XL') DEFAULT NULL,
  `rating` double DEFAULT NULL,
  `ulasan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`ID_Order`, `ID_Customer`, `ID_Driver`, `ID_Voucher`, `ID_WilayahPickUp`, `ID_WilayahDestination`, `serviceType`, `order_status`, `order_date`, `updatedOrder`, `paymentMethod`, `price`, `orderType`, `rating`, `ulasan`) VALUES
(1, 2, 1, 1, 1, 2, 'GrabCar', 'Complete', '2024-12-15', '2024-12-18 19:12:48', 'Cash', 50000, 'Reguler', 4.5, 'Good service'),
(2, 5, 4, 2, 3, 4, 'GrabBike', 'Cancelled', '2024-12-16', '2024-12-18 19:12:48', 'OVO', 30000, 'Hemat', NULL, 'N/A');

-- --------------------------------------------------------

--
-- Table structure for table `ovo`
--

CREATE TABLE `ovo` (
  `walletID` int(11) NOT NULL,
  `ID_Tlp` int(11) NOT NULL,
  `saldo` double DEFAULT NULL,
  `coins` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ovo`
--

INSERT INTO `ovo` (`walletID`, `ID_Tlp`, `saldo`, `coins`) VALUES
(1, 1, 100000, 50),
(2, 2, 50000, 25),
(3, 3, 200000, 100),
(4, 4, 150000, 75),
(5, 5, 30000, 15);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `ID_User` int(11) NOT NULL,
  `vehicle_ID` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `updateProfileAt` datetime DEFAULT NULL,
  `userType` enum('Driver','Customer','Admin') DEFAULT NULL,
  `statusAcc` enum('Unblock','Block') DEFAULT NULL,
  `createdAccAt` datetime DEFAULT NULL,
  `profilePhoto` varchar(255) DEFAULT NULL,
  `availabilityDriver` enum('Offline','Online','Occupied') DEFAULT NULL,
  `statusVerify` enum('Verified','Unverified','None','') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID_User`, `vehicle_ID`, `name`, `username`, `password`, `email`, `updateProfileAt`, `userType`, `statusAcc`, `createdAccAt`, `profilePhoto`, `availabilityDriver`, `statusVerify`) VALUES
(1, 1, 'John Doe', 'johndoe', 'password123', 'johndoe@example.com', '2024-12-18 19:12:47', 'Driver', 'Unblock', '2024-12-18 19:12:47', 'profile1.jpg', 'Online', 'Verified'),
(2, 2, 'Jane Smith', 'janesmith', 'password123', 'janesmith@example.com', '2024-12-18 19:12:47', 'Customer', 'Unblock', '2024-12-18 19:12:47', 'profile2.jpg', NULL, 'None'),
(3, NULL, 'Admin One', 'adminone', 'adminpass', 'adminone@example.com', '2024-12-18 19:12:47', 'Admin', 'Unblock', '2024-12-18 19:12:47', 'adminprofile.jpg', NULL, 'None'),
(4, 3, 'Alice Brown', 'aliceb', 'password123', 'aliceb@example.com', '2024-12-18 19:12:47', 'Driver', 'Unblock', '2024-12-18 19:12:47', 'profile3.jpg', 'Online', 'Verified'),
(5, 4, 'Bob White', 'bobw', 'password123', 'bobw@example.com', '2024-12-18 19:12:47', 'Customer', 'Block', '2024-12-18 19:12:47', 'profile4.jpg', NULL, 'None'),
(7, 7, 'Andi', 'idna', '1234567', 'andi@example.com', NULL, 'Driver', 'Unblock', '2024-12-18 21:11:40', NULL, 'Online', 'Unverified'),
(9, NULL, 'Alice', 'ecila', '12345', 'alice@contoh.com', NULL, 'Customer', 'Unblock', '2024-12-18 21:39:10', NULL, NULL, 'None'),
(10, 8, 'akwila', 'aliwka', '123', 'akwila@coba.com', NULL, 'Driver', 'Unblock', '2024-12-18 21:42:38', NULL, 'Online', 'Unverified'),
(11, NULL, 'AA', 'aa', '11111', 'aa@contoh.com', NULL, 'Customer', 'Unblock', '2024-12-18 21:46:48', NULL, NULL, 'None'),
(12, NULL, 'BB', 'bb', '123456789', 'BB@trial.com', NULL, 'Customer', 'Unblock', '2024-12-18 22:00:46', NULL, NULL, 'None'),
(13, NULL, 'CC', 'cc', '12345678', 'cc@trial.com', NULL, 'Customer', 'Unblock', '2024-12-18 22:02:29', NULL, NULL, 'None'),
(14, NULL, 'DD', 'dd', '1234567', 'dd@trial.com', NULL, 'Customer', 'Unblock', '2024-12-18 22:13:46', NULL, NULL, 'None'),
(15, 9, 'Adrian', 'eidorian', '123', 'adrian@trial.com', NULL, 'Driver', 'Unblock', '2024-12-19 08:51:21', NULL, 'Online', 'Unverified'),
(16, 10, 'Aloy', 'aloy1', '12345', 'aloy@contoh.com', NULL, 'Driver', 'Unblock', '2024-12-19 13:37:33', NULL, NULL, 'Unverified');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `vehicle_ID` int(11) NOT NULL,
  `plateNumber` varchar(20) DEFAULT NULL,
  `vehicleName` varchar(50) DEFAULT NULL,
  `vehicleType` enum('Car','Motorcycle') DEFAULT NULL,
  `jumlahSeat` int(3) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`vehicle_ID`, `plateNumber`, `vehicleName`, `vehicleType`, `jumlahSeat`) VALUES
(1, 'B1234XYZ', 'Toyota Avanza', 'Car', 5),
(2, 'B5678ABC', 'Honda Beat', 'Motorcycle', 2),
(3, 'B91011DEF', 'Suzuki Ertiga', 'Car', 7),
(4, 'B1213GHI', 'Yamaha NMAX', 'Motorcycle', 2),
(5, 'B1415JKL', 'Honda Jazz', 'Car', 4),
(7, 'D4323XM', 'Honda Beat', 'Motorcycle', 1),
(8, 'A1256AM', 'Toyota Alya', 'Car', 3),
(9, 'D2343ADY', 'Honda Beat Strret', 'Motorcycle', 1),
(10, 'D410Y', 'Honda Aja', 'Car', 3);

-- --------------------------------------------------------

--
-- Table structure for table `voucher`
--

CREATE TABLE `voucher` (
  `ID_Voucher` int(11) NOT NULL,
  `ID_Customer` int(11) NOT NULL,
  `ID_Admin` int(11) NOT NULL,
  `kodeVoucher` varchar(50) DEFAULT NULL,
  `serviceType` enum('GrabCar','GrabBike') DEFAULT NULL,
  `jumlahPotongan` double DEFAULT NULL,
  `valid_from` date DEFAULT NULL,
  `valid_to` date DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voucher`
--

INSERT INTO `voucher` (`ID_Voucher`, `ID_Customer`, `ID_Admin`, `kodeVoucher`, `serviceType`, `jumlahPotongan`, `valid_from`, `valid_to`, `created_at`, `update_at`) VALUES
(1, 2, 3, 'DISC10', 'GrabCar', 10000, '2024-12-01', '2024-12-31', '2024-12-18 19:12:48', '2024-12-18 19:12:48'),
(2, 5, 3, 'DISC20', 'GrabBike', 20000, '2024-12-01', '2024-12-31', '2024-12-18 19:12:48', '2024-12-18 19:12:48');

-- --------------------------------------------------------

--
-- Table structure for table `wilayah`
--

CREATE TABLE `wilayah` (
  `ID_Wilayah` int(11) NOT NULL,
  `kelurahan` varchar(100) DEFAULT NULL,
  `kecamatan` varchar(100) DEFAULT NULL,
  `kota` varchar(100) DEFAULT NULL,
  `alamat` varchar(255) DEFAULT NULL,
  `garisLintang` double DEFAULT NULL,
  `garisBujur` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `wilayah`
--

INSERT INTO `wilayah` (`ID_Wilayah`, `kelurahan`, `kecamatan`, `kota`, `alamat`, `garisLintang`, `garisBujur`) VALUES
(1, 'Kelurahan A', 'Kecamatan A', 'Kota A', 'Jalan A No.1', -6.2, 106.816666),
(2, 'Kelurahan B', 'Kecamatan B', 'Kota B', 'Jalan B No.2', -6.3, 106.716666),
(3, 'Kelurahan C', 'Kecamatan C', 'Kota C', 'Jalan C No.3', -6.4, 106.616666),
(4, 'Kelurahan D', 'Kecamatan D', 'Kota D', 'Jalan D No.4', -6.5, 106.516666),
(5, 'Kelurahan E', 'Kecamatan E', 'Kota E', 'Jalan E No.5', -6.6, 106.416666);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `laporan`
--
ALTER TABLE `laporan`
  ADD PRIMARY KEY (`ID_Laporan`),
  ADD KEY `ID_Order` (`ID_Order`);

--
-- Indexes for table `managelaporan`
--
ALTER TABLE `managelaporan`
  ADD PRIMARY KEY (`ID_Laporan`,`ID_Admin`),
  ADD KEY `ID_Admin` (`ID_Admin`);

--
-- Indexes for table `notlp`
--
ALTER TABLE `notlp`
  ADD PRIMARY KEY (`ID_Tlp`),
  ADD KEY `ID_User` (`ID_User`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`ID_Order`),
  ADD KEY `ID_Customer` (`ID_Customer`),
  ADD KEY `ID_Driver` (`ID_Driver`),
  ADD KEY `ID_Voucher` (`ID_Voucher`),
  ADD KEY `ID_WilayahPickUp` (`ID_WilayahPickUp`),
  ADD KEY `ID_WilayahDestination` (`ID_WilayahDestination`);

--
-- Indexes for table `ovo`
--
ALTER TABLE `ovo`
  ADD PRIMARY KEY (`walletID`),
  ADD KEY `ID_Tlp` (`ID_Tlp`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID_User`),
  ADD KEY `vehicle_ID` (`vehicle_ID`);

--
-- Indexes for table `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`vehicle_ID`);

--
-- Indexes for table `voucher`
--
ALTER TABLE `voucher`
  ADD PRIMARY KEY (`ID_Voucher`),
  ADD KEY `ID_Customer` (`ID_Customer`),
  ADD KEY `ID_Admin` (`ID_Admin`);

--
-- Indexes for table `wilayah`
--
ALTER TABLE `wilayah`
  ADD PRIMARY KEY (`ID_Wilayah`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `laporan`
--
ALTER TABLE `laporan`
  MODIFY `ID_Laporan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `notlp`
--
ALTER TABLE `notlp`
  MODIFY `ID_Tlp` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `order`
--
ALTER TABLE `order`
  MODIFY `ID_Order` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `ovo`
--
ALTER TABLE `ovo`
  MODIFY `walletID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `ID_User` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `vehicle`
--
ALTER TABLE `vehicle`
  MODIFY `vehicle_ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `voucher`
--
ALTER TABLE `voucher`
  MODIFY `ID_Voucher` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `wilayah`
--
ALTER TABLE `wilayah`
  MODIFY `ID_Wilayah` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `laporan`
--
ALTER TABLE `laporan`
  ADD CONSTRAINT `laporan_ibfk_1` FOREIGN KEY (`ID_Order`) REFERENCES `order` (`ID_Order`);

--
-- Constraints for table `managelaporan`
--
ALTER TABLE `managelaporan`
  ADD CONSTRAINT `managelaporan_ibfk_1` FOREIGN KEY (`ID_Laporan`) REFERENCES `laporan` (`ID_Laporan`),
  ADD CONSTRAINT `managelaporan_ibfk_2` FOREIGN KEY (`ID_Admin`) REFERENCES `users` (`ID_User`);

--
-- Constraints for table `notlp`
--
ALTER TABLE `notlp`
  ADD CONSTRAINT `notlp_ibfk_1` FOREIGN KEY (`ID_User`) REFERENCES `users` (`ID_User`);

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `order_ibfk_1` FOREIGN KEY (`ID_Customer`) REFERENCES `users` (`ID_User`),
  ADD CONSTRAINT `order_ibfk_2` FOREIGN KEY (`ID_Driver`) REFERENCES `users` (`ID_User`),
  ADD CONSTRAINT `order_ibfk_3` FOREIGN KEY (`ID_Voucher`) REFERENCES `voucher` (`ID_Voucher`),
  ADD CONSTRAINT `order_ibfk_4` FOREIGN KEY (`ID_WilayahPickUp`) REFERENCES `wilayah` (`ID_Wilayah`),
  ADD CONSTRAINT `order_ibfk_5` FOREIGN KEY (`ID_WilayahDestination`) REFERENCES `wilayah` (`ID_Wilayah`);

--
-- Constraints for table `ovo`
--
ALTER TABLE `ovo`
  ADD CONSTRAINT `ovo_ibfk_1` FOREIGN KEY (`ID_Tlp`) REFERENCES `notlp` (`ID_Tlp`);

--
-- Constraints for table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `users_ibfk_1` FOREIGN KEY (`vehicle_ID`) REFERENCES `vehicle` (`vehicle_ID`);

--
-- Constraints for table `voucher`
--
ALTER TABLE `voucher`
  ADD CONSTRAINT `voucher_ibfk_1` FOREIGN KEY (`ID_Customer`) REFERENCES `users` (`ID_User`),
  ADD CONSTRAINT `voucher_ibfk_2` FOREIGN KEY (`ID_Admin`) REFERENCES `users` (`ID_User`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
