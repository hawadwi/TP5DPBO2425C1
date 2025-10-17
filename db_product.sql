-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Oct 17, 2025 at 05:13 AM
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
-- Database: `db_product`
--

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` varchar(255) NOT NULL,
  `nama` varchar(255) NOT NULL,
  `harga` double NOT NULL,
  `kategori` varchar(255) NOT NULL,
  `ratingProduk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `nama`, `harga`, `kategori`, `ratingProduk`) VALUES
('PC001', 'Brightening Serum', 120000, 'Skincare', 5),
('PC002', 'Moisture Lotion', 112000, 'BodyCare', 4),
('PC003', 'Herbal Shampoo', 187000, 'Haircare', 5),
('PC004', 'Whitening Toothpaste', 15000, 'Oralcare', 3),
('PC005', 'Body Mist Vanilla', 85000, 'BodyCare', 4),
('PC006', 'Natur Hair Tonic Ginseng', 125000, 'Haircare', 4),
('PC007', 'Scarlett Jolly Body Lotion ', 75000, 'BodyCare', 5),
('PC008', 'The Body Shop White Musk Mist', 180000, 'BodyCare', 3),
('PC009', 'Sunscreen ', 41000, 'Skincare', 4),
('PC010', 'Sensodyne Gentle Whitening', 5000, 'Oralcare', 4),
('PC011', 'Lip Theraphy Advanced Healing ', 50000, 'Lipcare', 5),
('PC012', 'Face Wash Mugwort', 44000, 'Skincare', 2),
('PC013', 'White Secret Eye Cream', 93000, 'Eyecare', 5),
('PC014', 'Dove Deep Moisture Hand Cream', 170000, 'Handcare', 3),
('PC015', 'Oriflame Feet Up Cream', 57000, 'Footcare', 2),
('PC016', 'Aloe Vera Gel', 95000, 'Skincare', 4),
('PC017', 'Shea Butter Hand Cream', 78000, 'Handcare', 5),
('PC018', 'Argan Oil Shampoo', 110000, 'Haircare', 5),
('PC019', 'Rose Lip Balm', 42000, 'Lipcare', 2),
('PC020', 'Cracked Heel Balm', 43000, 'Footcare', 3),
('PC021', 'Lavender Bath Salt', 120000, 'BodyCare', 3);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
