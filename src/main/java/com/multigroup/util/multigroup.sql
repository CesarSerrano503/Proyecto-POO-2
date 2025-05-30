-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 30-05-2025 a las 05:57:40
-- Versión del servidor: 9.1.0
-- Versión de PHP: 8.3.14

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `multigroup`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignaciones`
--

DROP TABLE IF EXISTS `asignaciones`;
CREATE TABLE IF NOT EXISTS `asignaciones` (
  `id_asignacion` int NOT NULL AUTO_INCREMENT,
  `id_cotizacion` int NOT NULL,
  `id_empleado` int NOT NULL,
  `area` varchar(100) NOT NULL,
  `costo_hora` double NOT NULL,
  `fecha_inicio` datetime NOT NULL,
  `fecha_fin` datetime NOT NULL,
  `horas_estimadas` decimal(10,2) NOT NULL,
  `titulo_actividad` varchar(255) NOT NULL,
  `tareas` text NOT NULL,
  `costo_base` double NOT NULL,
  `total` double NOT NULL,
  `incremento_pct` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_asignacion`),
  KEY `idx_asig_cot` (`id_cotizacion`),
  KEY `idx_asig_emp` (`id_empleado`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `asignaciones`
--

INSERT INTO `asignaciones` (`id_asignacion`, `id_cotizacion`, `id_empleado`, `area`, `costo_hora`, `fecha_inicio`, `fecha_fin`, `horas_estimadas`, `titulo_actividad`, `tareas`, `costo_base`, `total`, `incremento_pct`) VALUES
(1, 7, 7, 'Vigilancia', 100, '2025-05-30 21:56:00', '2025-05-31 21:56:00', 12.00, 'Vigilantes', 'vigilar', 1200, 1200, 0),
(3, 4, 6, 'Vigilancia', 100, '2025-05-31 22:12:00', '2025-06-02 22:12:00', 30.00, 'Vigilantes', 'vigilar', 3000, 3000, 0),
(6, 6, 7, 'Vigilancia', 10, '2025-05-30 23:24:00', '2025-05-31 23:24:00', 10.00, 'Vigilantes', 'Vigilantes', 100, 100, 0),
(7, 7, 14, 'Vigilancia333333', 100, '2025-05-31 01:28:00', '2025-06-01 01:28:00', 10.00, 'Vigilantes', 'Vigilantes', 1000, 1000, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

DROP TABLE IF EXISTS `clientes`;
CREATE TABLE IF NOT EXISTS `clientes` (
  `id_cliente` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `documento` varchar(50) NOT NULL,
  `tipo_persona` enum('Natural','Jurídica') NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `correo` varchar(255) NOT NULL,
  `direccion` text,
  `estado` enum('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
  `creado_por` varchar(100) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fecha_inactivacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  UNIQUE KEY `documento` (`documento`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nombre`, `documento`, `tipo_persona`, `telefono`, `correo`, `direccion`, `estado`, `creado_por`, `fecha_creacion`, `fecha_actualizacion`, `fecha_inactivacion`) VALUES
(3, 'Cesar Antonio Serrano Gutierrez', '414141', 'Natural', '76160065', '17cesarserrano17@gmail.com', 'mi casa', 'Inactivo', 'Cesar', '2025-05-28 19:37:48', '2025-05-28 19:38:46', '2025-05-28 19:38:46'),
(4, 'Cesar Antonio Serrano Gutierrez', 'wazaza', 'Natural', '76160065', '17cesarserrano17@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 20:02:45', '2025-05-28 20:02:45', NULL),
(5, '131313', '3113131', 'Natural', '3131313', '131313@gmail.com', '13131', 'Activo', '3131', '2025-05-28 21:15:46', '2025-05-28 21:15:46', NULL),
(6, 'Cesar Antonio Serrano Gutierrez', '21211212', 'Natural', '76160065', '17cesarserrano17@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 21:50:33', '2025-05-28 21:50:33', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cotizaciones`
--

DROP TABLE IF EXISTS `cotizaciones`;
CREATE TABLE IF NOT EXISTS `cotizaciones` (
  `id_cotizacion` int NOT NULL AUTO_INCREMENT,
  `id_cliente` int NOT NULL,
  `estado` enum('En proceso','Finalizada') NOT NULL DEFAULT 'En proceso',
  `total_horas` decimal(10,2) NOT NULL DEFAULT '0.00',
  `fecha_inicio` datetime DEFAULT NULL,
  `fecha_fin` datetime DEFAULT NULL,
  `costo_asignaciones` decimal(10,2) NOT NULL DEFAULT '0.00',
  `costos_adicionales` decimal(10,2) NOT NULL DEFAULT '0.00',
  `total` decimal(10,2) NOT NULL DEFAULT '0.00',
  `creado_por` varchar(100) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fecha_finalizacion` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id_cotizacion`),
  KEY `idx_cot_cliente` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `cotizaciones`
--

INSERT INTO `cotizaciones` (`id_cotizacion`, `id_cliente`, `estado`, `total_horas`, `fecha_inicio`, `fecha_fin`, `costo_asignaciones`, `costos_adicionales`, `total`, `creado_por`, `fecha_creacion`, `fecha_actualizacion`, `fecha_finalizacion`) VALUES
(4, 3, 'Finalizada', 300.00, '2025-05-30 07:37:00', '2025-05-31 07:38:00', 300.00, 300.00, 300.00, 'Cesar', '2025-05-29 01:38:13', '2025-05-29 02:13:14', '2025-05-29 08:13:14'),
(6, 3, 'Finalizada', 3000.00, '2025-05-30 07:56:00', '2025-05-31 07:56:00', 100.00, 100.00, 1000.00, 'Julio', '2025-05-29 01:57:11', '2025-05-29 02:14:28', '2025-05-29 08:04:38'),
(7, 4, 'En proceso', 100.00, '2025-05-30 08:16:00', '2025-05-31 08:16:00', 200.00, 200.00, 2000.00, 'Julian', '2025-05-29 02:16:43', '2025-05-29 02:16:43', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `empleados`
--

DROP TABLE IF EXISTS `empleados`;
CREATE TABLE IF NOT EXISTS `empleados` (
  `id_empleado` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) NOT NULL,
  `documento` varchar(50) NOT NULL,
  `tipo_persona` varchar(20) NOT NULL,
  `tipo_contratacion` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `correo` varchar(255) DEFAULT NULL,
  `direccion` text,
  `estado` enum('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
  `creado_por` varchar(100) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `fecha_inactivacion` datetime DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  UNIQUE KEY `documento` (`documento`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `empleados`
--

INSERT INTO `empleados` (`id_empleado`, `nombre`, `documento`, `tipo_persona`, `tipo_contratacion`, `telefono`, `correo`, `direccion`, `estado`, `creado_por`, `fecha_creacion`, `fecha_actualizacion`, `fecha_inactivacion`) VALUES
(6, 'Pollo', 'wa', 'JurÃ­dica', 'Permanente', '7616-0065', 'wazaaa@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 00:12:54', '2025-05-28 18:58:06', NULL),
(7, 'Cesar Serrano', '414141-33', 'Natural', 'Por Horas', '7616-0065', 'wazaaaz@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 00:18:44', '2025-05-28 00:18:44', NULL),
(9, 'Juan Felipe', 'Juan Felipe', 'Jurídica', 'Por Horas', '70605235', 'cs0659705@gmail.com', 'Colonia el pepeto', 'Inactivo', 'Cesar', '2025-05-28 18:50:55', '2025-05-28 19:00:09', NULL),
(10, 'Juan', 'wazazaa', 'Jurídica', 'Permanente', '76160065', 'css@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 18:51:32', '2025-05-28 18:51:32', NULL),
(11, '131313', '131313131', 'Natural', 'Por Horas', '313131', '31131@gmail.com', '31313131', 'Activo', '313131', '2025-05-28 21:22:21', '2025-05-28 21:22:21', NULL),
(14, 'Cesar Antonio ', 'Cesar Antonio ', 'Natural', 'Permanente', '76160065', '17cesarserrano17@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 21:26:14', '2025-05-28 21:26:14', NULL),
(15, 'Cesar Antonio Serrano Gutierrez', 'wazaza', 'Natural', 'Permanente', '', '', '', 'Activo', 'cesar', '2025-05-28 21:28:06', '2025-05-28 21:28:06', NULL),
(17, 'Cesar', '414141', 'Natural', 'Permanente', '76160065', '17cesarserrano17@gmail.com', 'mi casa', 'Activo', 'Cesar', '2025-05-28 21:38:25', '2025-05-28 21:38:25', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subtareas`
--

DROP TABLE IF EXISTS `subtareas`;
CREATE TABLE IF NOT EXISTS `subtareas` (
  `id_subtarea` int NOT NULL AUTO_INCREMENT,
  `id_asignacion` int NOT NULL,
  `titulo_subtarea` varchar(255) NOT NULL,
  `descripcion_subtarea` text NOT NULL,
  PRIMARY KEY (`id_subtarea`),
  KEY `fk_subtareas_asignacion` (`id_asignacion`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `subtareas`
--

INSERT INTO `subtareas` (`id_subtarea`, `id_asignacion`, `titulo_subtarea`, `descripcion_subtarea`) VALUES
(2, 1, 'HOLA', 'HOLASas'),
(3, 1, 'adada', 'dadad'),
(4, 1, 'adadaadadaadadaadada', 'adadaadadaadadaadadaadada'),
(6, 7, 'zero', 'zero');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `rol` enum('admin','usuario') NOT NULL DEFAULT 'usuario',
  `estado` enum('Activo','Inactivo') NOT NULL DEFAULT 'Activo',
  `creado_por` varchar(100) DEFAULT NULL,
  `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_actualizacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `username`, `password`, `rol`, `estado`, `creado_por`, `fecha_creacion`, `fecha_actualizacion`) VALUES
(5, 'admin', '12345', 'admin', 'Activo', 'sistema', '2025-05-29 22:38:50', '2025-05-29 23:49:29'),
(6, 'colaborador', '123456', 'usuario', 'Inactivo', 'sistema', '2025-05-29 22:38:50', '2025-05-29 23:49:55');

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `asignaciones`
--
ALTER TABLE `asignaciones`
  ADD CONSTRAINT `asignaciones_ibfk_1` FOREIGN KEY (`id_cotizacion`) REFERENCES `cotizaciones` (`id_cotizacion`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `asignaciones_ibfk_2` FOREIGN KEY (`id_empleado`) REFERENCES `empleados` (`id_empleado`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `cotizaciones`
--
ALTER TABLE `cotizaciones`
  ADD CONSTRAINT `cotizaciones_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE RESTRICT ON UPDATE CASCADE;

--
-- Filtros para la tabla `subtareas`
--
ALTER TABLE `subtareas`
  ADD CONSTRAINT `fk_subtareas_asignacion` FOREIGN KEY (`id_asignacion`) REFERENCES `asignaciones` (`id_asignacion`) ON DELETE CASCADE,
  ADD CONSTRAINT `subtareas_ibfk_1` FOREIGN KEY (`id_asignacion`) REFERENCES `asignaciones` (`id_asignacion`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
