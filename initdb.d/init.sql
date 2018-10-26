-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`User` (
  `id` VARCHAR(30) NOT NULL,
  `password` VARCHAR(100) NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `Loggedin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`Loggedin` (
  `token` VARCHAR(36) NOT NULL,
  `expired_at` DATETIME NOT NULL,
  `User_id` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`token`),
  INDEX `fk_Loggedin_User1_idx` (`User_id` ASC),
  CONSTRAINT `fk_Loggedin_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`Article` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `title` VARCHAR(250) NOT NULL,
  `content` TEXT NOT NULL,
  `added_at` DATETIME NOT NULL,
  `modified_at` DATETIME NOT NULL,
  `User_id` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Article_User1_idx` (`User_id` ASC),
  CONSTRAINT `fk_Article_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`Tag` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `Article_has_Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`Article_has_Tag` (
  `Article_id` INT NOT NULL,
  `Tag_id` INT NOT NULL,
  --  PRIMARY KEY (`Article_id`, `Tag_id`),
  INDEX `fk_Tag_has_Article_Article1_idx` (`Article_id` ASC),
  INDEX `fk_Tag_has_Article_Tag1_idx` (`Tag_id` ASC),
  CONSTRAINT `fk_Tag_has_Article_Tag1`
    FOREIGN KEY (`Tag_id`)
    REFERENCES `Tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Tag_has_Article_Article1`
    FOREIGN KEY (`Article_id`)
    REFERENCES `Article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Uploaded`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`Uploaded` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `file_name` VARCHAR(200) NOT NULL,
  `file_uri` VARCHAR(500) NOT NULL UNIQUE,
  `User_id` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Uploaded_User1_idx` (`User_id` ASC),
  CONSTRAINT `fk_Uploaded_User1`
    FOREIGN KEY (`User_id`)
    REFERENCES `User` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);