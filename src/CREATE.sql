-- -----------------------------------------------------
-- Players Table
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ma_players` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `nick` VARCHAR(16) NOT NULL ,
  `password` VARCHAR(32)NOT NULL ,
  PRIMARY KEY (`id`) 
  )
ENGINE = InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;


-- -----------------------------------------------------
-- Inventory Table
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ma_inventoryxxx` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `player_id` INT NOT NULL ,
  `content` BLOB NULL ,
  `armor` BLOB NULL ,
  `server` CHAR(32) NULL ,
  PRIMARY KEY (`id` )
  )
ENGINE = InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;


-- -----------------------------------------------------
-- data_values Table
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ma_data_values` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `mc_name` char(32) DEFAULT NULL,
  `name` char(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) 
ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;


-- -----------------------------------------------------
-- auction point table
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ma_auction_points` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` char(32) DEFAULT NULL,
  `x` INT DEFAULT NOT NULL,
  `y` INT DEFAULT NOT NULL,
  `z` INT DEFAULT NOT NULL,
  PRIMARY KEY (`id`)
  ) 
ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1