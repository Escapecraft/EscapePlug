CREATE TABLE IF NOT EXISTS `${PREFIX}version` (
  `schemaVer` char(16) NOT NULL,
  `codeVer` char(16) NOT NULL,
  PRIMARY KEY (`schemaVer`, `codeVer`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO `${PREFIX}version` (`schemaVer`, `codeVer`)
VALUES ('${SCHEMA_VER}', '${CODE_VER}');

CREATE TABLE IF NOT EXISTS `${PREFIX}statusLookup` (
  `code` char(1) NOT NULL,
  `name` char(16) NOT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('P', 'Pending')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('R', 'Repeat')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('F', 'Failed')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('C', 'Cancelled')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('D', 'Done')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);
INSERT INTO `${PREFIX}statusLookup` (`code`, `name`) VALUES ('I', 'Inactive')
ON DUPLICATE KEY UPDATE `name` = VALUES(`name`);

CREATE TABLE IF NOT EXISTS `${PREFIX}eventLookup` (
  `id` int(11) NOT NULL,
  `class` char(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}eventLookup` (`id`, `class`) VALUES (1, 'PlayerJoinEvent')
ON DUPLICATE KEY UPDATE `class` = VALUES(`class`);

CREATE TABLE IF NOT EXISTS `${PREFIX}msgTypeLookup` (
  `id` int(11) NOT NULL,
  `msgType` char(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}msgTypeLookup` (`id`, `msgType`) VALUES (1, 'Motd')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);

CREATE TABLE IF NOT EXISTS `${PREFIX}trigger` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dataTable` char(32) NOT NULL,
  `dataId` int(11) NOT NULL,
  `status` char(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`dataTable`, `dataId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgType` int(11) NOT NULL,
  `text` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(1000) NOT NULL,
  `event` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
