CREATE TABLE IF NOT EXISTS `${PREFIX}version` (
  `schemaVer` char(16) NOT NULL,
  `codeVer` char(16) NOT NULL,
  PRIMARY KEY (`schemaVer`, `codeVer`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT IGNORE INTO `${PREFIX}version` (`schemaVer`, `codeVer`)
VALUES ('${SCHEMA_VER}', '${CODE_VER}');

CREATE TABLE IF NOT EXISTS `${PREFIX}messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(300) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `${PREFIX}commands` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data` varchar(1000) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
