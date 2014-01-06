CREATE TABLE IF NOT EXISTS `${PREFIX}version` (
  `schemaVer` varchar(16) NOT NULL,
  `codeVer` varchar(16) NOT NULL,
  PRIMARY KEY (`schemaVer`, `codeVer`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO `${PREFIX}version` (`schemaVer`, `codeVer`)
VALUES ('${SCHEMA_VER}', '${CODE_VER}');

CREATE TABLE IF NOT EXISTS `${PREFIX}eventTypes` (
  `id` int(11) NOT NULL,
  `class` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}eventTypes` (`id`, `class`) VALUES (1, 'PlayerJoinEvent')
ON DUPLICATE KEY UPDATE `class` = VALUES(`class`);
INSERT INTO `${PREFIX}eventTypes` (`id`, `class`) VALUES (2, 'PlayerQuitEvent')
ON DUPLICATE KEY UPDATE `class` = VALUES(`class`);

CREATE TABLE IF NOT EXISTS `${PREFIX}msgTypes` (
  `id` int(11) NOT NULL,
  `msgType` char(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}msgTypes` (`id`, `msgType`) VALUES (1, 'Broadcast')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);
INSERT INTO `${PREFIX}msgTypes` (`id`, `msgType`) VALUES (2, 'Individual')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);
INSERT INTO `${PREFIX}msgTypes` (`id`, `msgType`) VALUES (3, 'Channel')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);

CREATE TABLE IF NOT EXISTS `${PREFIX}triggers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32),
  `active` char(1) NOT NULL,
  `textId` int(11) NOT NULL,
  `priority` int(11),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}conditions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `triggerId` int(11) NOT NULL,
  `code` char(1),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}text` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
