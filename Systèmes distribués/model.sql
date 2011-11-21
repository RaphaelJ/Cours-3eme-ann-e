SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `sysdist` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;

USE `sysdist`;

CREATE  TABLE IF NOT EXISTS `sysdist`.`Aeroport` (
  `idAeroport` INT(11) NOT NULL ,
  `nom` VARCHAR(45) NOT NULL ,
  `adresse` VARCHAR(45) NOT NULL ,
  `ville` VARCHAR(45) NOT NULL ,
  `codePostal` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`idAeroport`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `sysdist`.`Pilote` (
  `idPilote` INT(11) NOT NULL ,
  `nom` VARCHAR(45) NOT NULL ,
  `prenom` VARCHAR(45) NOT NULL ,
  `dateNaissance` DATE NULL DEFAULT NULL ,
  PRIMARY KEY (`idPilote`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `sysdist`.`Avion` (
  `idAvion` INT(11) NOT NULL ,
  `type` VARCHAR(45) NOT NULL ,
  `nbPlaces` INT(11) NOT NULL ,
  PRIMARY KEY (`idAvion`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;

CREATE  TABLE IF NOT EXISTS `sysdist`.`Vol` (
  `idVol` INT(11) NOT NULL ,
  `heureDepart` DATETIME NOT NULL ,
  `heureArrivee` DATETIME NOT NULL ,
  `aeroportDepart` INT(11) NOT NULL ,
  `aeroportArrivee` INT(11) NOT NULL ,
  `pilote` INT(11) NOT NULL ,
  `avion` INT(11) NOT NULL ,
  PRIMARY KEY (`idVol`) ,
  INDEX `fk_Vol_aeroport_depart` (`aeroportDepart` ASC) ,
  INDEX `fk_Vol_aeroport_arrivee` (`aeroportArrivee` ASC) ,
  INDEX `fk_Vol_avion` (`avion` ASC) ,
  INDEX `fk_Vol_pilote` (`pilote` ASC) ,
  CONSTRAINT `fk_Vol_aeroport_depart`
    FOREIGN KEY (`aeroportDepart` )
    REFERENCES `sysdist`.`Aeroport` (`idAeroport` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Vol_aeroport_arrivee`
    FOREIGN KEY (`aeroportArrivee` )
    REFERENCES `sysdist`.`Aeroport` (`idAeroport` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Vol_avion`
    FOREIGN KEY (`avion` )
    REFERENCES `sysdist`.`Avion` (`idAvion` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Vol_pilote`
    FOREIGN KEY (`pilote` )
    REFERENCES `sysdist`.`Pilote` (`idPilote` )
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1
COLLATE = latin1_swedish_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

