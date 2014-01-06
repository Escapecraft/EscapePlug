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

CREATE TABLE IF NOT EXISTS `${PREFIX}dataTypes` (
  `id` int(11) NOT NULL,
  `tableName` varchar(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}dataTypes` (`id`, `tableName`) VALUES (1, 'messages')
ON DUPLICATE KEY UPDATE `messages` = VALUES(`messages`);
INSERT INTO `${PREFIX}dataTypes` (`id`, `tableName`) VALUES (2, 'commands')
ON DUPLICATE KEY UPDATE `commands` = VALUES(`commands`);

CREATE TABLE IF NOT EXISTS `${PREFIX}msgTypeLookup` (
  `id` int(11) NOT NULL,
  `msgType` char(16) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `${PREFIX}msgTypeLookup` (`id`, `msgType`) VALUES (1, 'Broadcast')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);
INSERT INTO `${PREFIX}msgTypeLookup` (`id`, `msgType`) VALUES (2, 'Individual')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);
INSERT INTO `${PREFIX}msgTypeLookup` (`id`, `msgType`) VALUES (3, 'Channel')
ON DUPLICATE KEY UPDATE `msgType` = VALUES(`msgType`);

CREATE TABLE IF NOT EXISTS `${PREFIX}trigger` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `active` char(1) NOT NULL,
  `eventType` int(11) NOT NULL,
  `dataType` int(11) NOT NULL,
  `dataId` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`dataTable`, `dataId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgType` int(11) NOT NULL,
  `tag` varchar(16),
  `text` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
