-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 06, 2024 at 06:59 AM
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
-- Database: `grab_hb`
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
(1, 1, 'Driver tidak ramah.', 'Done', '2024-12-01', '2024-12-02'),
(2, 2, 'Kendaraan terlalu kecil.', 'Waiting', '2024-12-02', NULL),
(3, 3, 'Driver tidak muncul.', 'On_Process', '2024-12-03', NULL),
(4, 4, 'Perjalanan terlalu lambat.', 'Done', '2024-12-04', '2024-12-05'),
(5, 5, 'Kesalahan lokasi jemput.', 'Waiting', '2024-12-05', NULL);

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
(1, 4, 'Masalah selesai dengan permintaan maaf.', '2024-12-02'),
(2, 4, 'Menunggu konfirmasi dari customer.', '2024-12-03'),
(3, 4, 'Driver sedang dihubungi.', '2024-12-04'),
(4, 4, 'Laporan selesai diproses.', '2024-12-05'),
(5, 4, 'Menunggu update dari customer.', '2024-12-06');

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `ID_Order` int(11) NOT NULL,
  `ID_Customer` int(11) NOT NULL,
  `ID_Driver` int(11) NOT NULL,
  `ID_Voucher` int(11) DEFAULT NULL,
  `ID_WilayahPickUp` int(11) NOT NULL,
  `ID_WilayahDestination` int(11) NOT NULL,
  `pickUpLoc` varchar(255) DEFAULT NULL,
  `serviceType` enum('GrabCar','GrabBike') DEFAULT NULL,
  `order_status` enum('Complete','On_Progress','Cancelled') DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `updatedOrder` datetime DEFAULT NULL,
  `paymentMethod` enum('Cash','OVO') DEFAULT NULL,
  `price` double DEFAULT NULL,
  `orderType` enum('Hemat','Reguler','XL') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order`
--

INSERT INTO `order` (`ID_Order`, `ID_Customer`, `ID_Driver`, `ID_Voucher`, `ID_WilayahPickUp`, `ID_WilayahDestination`, `pickUpLoc`, `serviceType`, `order_status`, `order_date`, `updatedOrder`, `paymentMethod`, `price`, `orderType`) VALUES
(1, 3, 1, 1, 1, 2, 'Jl. A No. 1', 'GrabCar', 'Complete', '2024-12-01', '2024-12-01 12:00:00', 'Cash', 50000, 'Reguler'),
(2, 5, 2, 2, 2, 3, 'Jl. B No. 2', 'GrabBike', 'On_Progress', '2024-12-02', '2024-12-02 14:00:00', 'OVO', 20000, 'Hemat'),
(3, 3, 1, 3, 3, 4, 'Jl. C No. 3', 'GrabCar', 'Cancelled', '2024-12-03', '2024-12-03 16:00:00', 'Cash', 70000, 'XL'),
(4, 5, 2, 4, 4, 5, 'Jl. D No. 4', 'GrabBike', 'Complete', '2024-12-04', '2024-12-04 18:00:00', 'OVO', 15000, 'Reguler'),
(5, 3, 1, NULL, 5, 1, 'Jl. E No. 5', 'GrabCar', 'On_Progress', '2024-12-05', '2024-12-05 20:00:00', 'Cash', 55000, 'Hemat');

-- --------------------------------------------------------

--
-- Table structure for table `ovo`
--

CREATE TABLE `ovo` (
  `walletID` int(11) NOT NULL,
  `ID_Driver_Customer` int(11) NOT NULL,
  `saldo` double DEFAULT NULL,
  `coins` double DEFAULT NULL,
  `userType` enum('Customer','Driver') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `ovo`
--

INSERT INTO `ovo` (`walletID`, `ID_Driver_Customer`, `saldo`, `coins`, `userType`) VALUES
(1, 3, 100000, 5000, 'Customer'),
(2, 5, 200000, 10000, 'Customer'),
(3, 1, 150000, 7500, 'Driver'),
(4, 2, 250000, 12500, 'Driver'),
(5, 3, 300000, 15000, 'Customer');

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `ID_Customer` int(11) NOT NULL,
  `ID_Order` int(11) NOT NULL,
  `rating` double DEFAULT NULL,
  `ulasan` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`ID_Customer`, `ID_Order`, `rating`, `ulasan`) VALUES
(3, 1, 4.5, 'Driver ramah dan tepat waktu.'),
(3, 3, 3, 'Sedikit terlambat.'),
(3, 5, 4, 'Overall bagus.'),
(5, 2, 4, 'Perjalanan nyaman.'),
(5, 4, 5, 'Pelayanan sangat memuaskan.');

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
  `phoneNumber` varchar(15) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `updateProfileAt` datetime DEFAULT NULL,
  `userType` enum('Admin','Customer','Driver') DEFAULT NULL,
  `statusAcc` enum('Block','Unblock','None') DEFAULT NULL,
  `createdAccAt` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`ID_User`, `vehicle_ID`, `name`, `username`, `password`, `phoneNumber`, `email`, `updateProfileAt`, `userType`, `statusAcc`, `createdAccAt`) VALUES
(1, 1, 'John Doe', 'johndoe', 'password123', '081234567890', 'john@example.com', '2024-12-01 08:00:00', 'Driver', 'Unblock', '2024-01-01 08:00:00'),
(2, 2, 'Jane Smith', 'janesmith', 'password123', '081298765432', 'jane@example.com', '2024-12-02 09:00:00', 'Driver', 'Unblock', '2024-01-02 09:00:00'),
(3, NULL, 'Michael Brown', 'mikebrown', 'password123', '081345678901', 'michael@example.com', '2024-12-03 10:00:00', 'Customer', 'Unblock', '2024-01-03 10:00:00'),
(4, NULL, 'Emily White', 'emilywhite', 'password123', '081456789012', 'emily@example.com', '2024-12-04 11:00:00', 'Admin', 'Unblock', '2024-01-04 11:00:00'),
(5, NULL, 'Sarah Green', 'sarahgreen', 'password123', '081567890123', 'sarah@example.com', '2024-12-05 12:00:00', 'Customer', 'Unblock', '2024-01-05 12:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `vehicle`
--

CREATE TABLE `vehicle` (
  `vehicle_ID` int(11) NOT NULL,
  `plateNumber` varchar(20) DEFAULT NULL,
  `vehicleName` varchar(50) DEFAULT NULL,
  `vehicleType` enum('Car','Motorcycle') DEFAULT NULL,
  `jumlahSeat` int(3) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vehicle`
--

INSERT INTO `vehicle` (`vehicle_ID`, `plateNumber`, `vehicleName`, `vehicleType`, `jumlahSeat`) VALUES
(1, 'B1234XYZ', 'Toyota Avanza', 'Car', 7),
(2, 'B5678XYZ', 'Honda Beat', 'Motorcycle', 1),
(3, 'B9123XYZ', 'Suzuki Ertiga', 'Car', 6),
(4, 'B4567XYZ', 'Yamaha Nmax', 'Motorcycle', 1),
(5, 'B8912XYZ', 'Toyota Kijang', 'Car', 8);

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
  `valid_from` datetime DEFAULT NULL,
  `valid_to` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `update_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `voucher`
--

INSERT INTO `voucher` (`ID_Voucher`, `ID_Customer`, `ID_Admin`, `kodeVoucher`, `serviceType`, `jumlahPotongan`, `valid_from`, `valid_to`, `created_at`, `update_at`) VALUES
(1, 3, 4, 'GRABCAR20', 'GrabCar', 20, '2024-01-01 08:00:00', '2024-12-31 23:59:59', '2024-01-01 08:00:00', '2024-12-01 08:00:00'),
(2, 5, 4, 'GRABBIKE15', 'GrabBike', 15, '2024-01-01 08:00:00', '2024-12-31 23:59:59', '2024-01-01 08:00:00', '2024-12-02 09:00:00'),
(3, 3, 4, 'GRABCAR25', 'GrabCar', 25, '2024-01-01 08:00:00', '2024-12-31 23:59:59', '2024-01-01 08:00:00', '2024-12-03 10:00:00'),
(4, 5, 4, 'GRABBIKE10', 'GrabBike', 10, '2024-01-01 08:00:00', '2024-12-31 23:59:59', '2024-01-01 08:00:00', '2024-12-04 11:00:00'),
(5, 3, 4, 'GRABCAR30', 'GrabCar', 30, '2024-01-01 08:00:00', '2024-12-31 23:59:59', '2024-01-01 08:00:00', '2024-12-05 12:00:00');

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
(1, 'Kelurahan A', 'Kecamatan A', 'Kota A', 'Jl. A No. 1', -6.2, 106.8),
(2, 'Kelurahan B', 'Kecamatan B', 'Kota B', 'Jl. B No. 2', -6.201, 106.801),
(3, 'Kelurahan C', 'Kecamatan C', 'Kota C', 'Jl. C No. 3', -6.202, 106.802),
(4, 'Kelurahan D', 'Kecamatan D', 'Kota D', 'Jl. D No. 4', -6.203, 106.803),
(5, 'Kelurahan E', 'Kecamatan E', 'Kota E', 'Jl. E No. 5', -6.204, 106.804);

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
  ADD KEY `ID_Driver_Customer` (`ID_Driver_Customer`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`ID_Customer`,`ID_Order`),
  ADD KEY `ID_Order` (`ID_Order`);

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
  ADD CONSTRAINT `ovo_ibfk_1` FOREIGN KEY (`ID_Driver_Customer`) REFERENCES `users` (`ID_User`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`ID_Customer`) REFERENCES `users` (`ID_User`),
  ADD CONSTRAINT `review_ibfk_2` FOREIGN KEY (`ID_Order`) REFERENCES `order` (`ID_Order`);

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
