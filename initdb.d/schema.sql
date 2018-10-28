SET CHARSET UTF8;
CREATE DATABASE IF NOT EXISTS test DEFAULT CHARACTER SET utf8;

USE test;

-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`user` (
  `system_id` INT AUTO_INCREMENT NOT NULL,
  `id` VARCHAR(30) NOT NULL UNIQUE,
  `password` VARCHAR(100) NULL,
  PRIMARY KEY (`system_id`));


-- -----------------------------------------------------
-- Table `Loggedin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`loggedin` (
  `token` VARCHAR(36) NOT NULL,
  `expired_at` DATETIME NOT NULL,
  `user_system_id` INT NOT NULL,
  PRIMARY KEY (`token`),
  INDEX `fk_loggedin_user1_idx` (`user_system_id` ASC),
  CONSTRAINT `fk_loggedin_user1`
    FOREIGN KEY (`user_system_id`)
    REFERENCES `user` (`system_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Article`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`article` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `title` VARCHAR(250) NOT NULL,
  `content` TEXT NOT NULL,
  `added_at` DATETIME NOT NULL,
  `modified_at` DATETIME NOT NULL,
  `user_system_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_article_user1_idx` (`user_system_id` ASC),
  CONSTRAINT `fk_article_user1`
    FOREIGN KEY (`user_system_id`)
    REFERENCES `user` (`system_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`tag` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `name` VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `Article_has_Tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`article_has_tag` (
  `article_id` INT NOT NULL,
  `tag_id` INT NOT NULL,
  --  PRIMARY KEY (`Article_id`, `Tag_id`),
  INDEX `fk_tag_has_article_article1_idx` (`article_id` ASC),
  INDEX `fk_tag_has_article_tag1_idx` (`tag_id` ASC),
  CONSTRAINT `fk_tag_has_article_tag1`
    FOREIGN KEY (`tag_id`)
    REFERENCES `tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_has_article_article1`
    FOREIGN KEY (`article_id`)
    REFERENCES `article` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `Uploaded`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `test`.`uploaded` (
  `id` INT AUTO_INCREMENT NOT NULL,
  `file_name` VARCHAR(200) NOT NULL,
  `file_uri` VARCHAR(500) NOT NULL UNIQUE,
  `user_system_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_uploaded_user1_idx` (`user_system_id` ASC),
  CONSTRAINT `fk_uploaded_user1`
    FOREIGN KEY (`user_system_id`)
    REFERENCES `user` (`system_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);