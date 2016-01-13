package edu.kit.ipd.crowdcontrol.workerservice.database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * initializes and holds the connection to the database and eventually the database itself.
 * @author LeanderK
 * @version 1.0
 */
public class DatabaseManager {
    @SuppressWarnings("FieldCanBeLocal")
    private final Connection connection;
    private final DSLContext context;


    /**
     * creates new DatabaseManager
     * @param userName the username for the database
     * @param password the password for the database
     * @param url the url to the database
     * @param sqlDialect the dialect to use
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    public DatabaseManager(String userName, String password, String url, SQLDialect sqlDialect) throws SQLException {
        connection = DriverManager.getConnection(url, userName, password);
        context = DSL.using(connection, sqlDialect);
        initDatabase();
    }

    /**
     * initializes the database if not already initialized.
     */
    private void initDatabase() {
        String sql = "-- MySQL Workbench Forward Engineering\n" +
                "\n" +
                "SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;\n" +
                "SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;\n" +
                "SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Schema mydb\n" +
                "-- -----------------------------------------------------\n" +
                "-- -----------------------------------------------------\n" +
                "-- Schema crowdcontrol\n" +
                "-- -----------------------------------------------------\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Schema crowdcontrol\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE SCHEMA IF NOT EXISTS `crowdcontrol` DEFAULT CHARACTER SET utf8mb4 ;\n" +
                "USE `crowdcontrol` ;\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Templates`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Templates` (\n" +
                "  `idTemplates` INT NOT NULL,\n" +
                "  `template` LONGTEXT NOT NULL,\n" +
                "  PRIMARY KEY (`idTemplates`))\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Experiment`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Experiment` (\n" +
                "  `idexperiment` INT(11) NOT NULL,\n" +
                "  `rating_options` JSON NULL DEFAULT NULL,\n" +
                "  `titel` VARCHAR(255) NULL DEFAULT NULL,\n" +
                "  `answer_description` TEXT NULL DEFAULT NULL,\n" +
                "  `rating_description` TEXT NULL DEFAULT NULL,\n" +
                "  `ratings_per_answer` INT(11) NULL DEFAULT NULL,\n" +
                "  `algorithm_task_chooser` VARCHAR(255) NULL DEFAULT NULL,\n" +
                "  `algorithm_quality_answer` VARCHAR(255) NULL DEFAULT NULL,\n" +
                "  `algorithm_quality_rating` VARCHAR(255) NULL DEFAULT NULL,\n" +
                "  `base_payment` INT(11) NULL DEFAULT NULL,\n" +
                "  `bonus_answer` INT(11) NULL DEFAULT NULL,\n" +
                "  `bonus_rating` INT(11) NULL DEFAULT NULL,\n" +
                "  `template_data` MEDIUMTEXT NULL DEFAULT NULL,\n" +
                "  `template` INT NULL,\n" +
                "  PRIMARY KEY (`idexperiment`),\n" +
                "  INDEX `usedTemplate_idx` (`template` ASC),\n" +
                "  CONSTRAINT `usedTemplate`\n" +
                "    FOREIGN KEY (`template`)\n" +
                "    REFERENCES `crowdcontrol`.`Templates` (`idTemplates`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Platforms`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Platforms` (\n" +
                "  `idPlatforms` INT NOT NULL,\n" +
                "  `native_qualifications` BIT(1) NULL,\n" +
                "  `native_payment` BIT(1) NULL,\n" +
                "  PRIMARY KEY (`idPlatforms`))\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Task`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Task` (\n" +
                "  `idTask` INT(11) NOT NULL,\n" +
                "  `experiment` INT(11) NOT NULL,\n" +
                "  `running` BIT(1) NOT NULL,\n" +
                "  `platform_data` MEDIUMTEXT NULL DEFAULT NULL,\n" +
                "  `crowd_platform` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idTask`),\n" +
                "  INDEX `idexperiment_idx` (`experiment` ASC),\n" +
                "  INDEX `runningOnPlattform_idx` (`crowd_platform` ASC),\n" +
                "  CONSTRAINT `idexperimenthit`\n" +
                "    FOREIGN KEY (`experiment`)\n" +
                "    REFERENCES `crowdcontrol`.`Experiment` (`idexperiment`)\n" +
                "    ON DELETE RESTRICT\n" +
                "    ON UPDATE RESTRICT,\n" +
                "  CONSTRAINT `runningOnPlattform`\n" +
                "    FOREIGN KEY (`crowd_platform`)\n" +
                "    REFERENCES `crowdcontrol`.`Platforms` (`idPlatforms`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Worker`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Worker` (\n" +
                "  `idWorker` INT(11) NOT NULL,\n" +
                "  `identification` VARCHAR(255) NULL DEFAULT NULL,\n" +
                "  `platform` INT NOT NULL,\n" +
                "  `email` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`idWorker`),\n" +
                "  INDEX `workerOrigin_idx` (`platform` ASC),\n" +
                "  CONSTRAINT `workerOrigin`\n" +
                "    FOREIGN KEY (`platform`)\n" +
                "    REFERENCES `crowdcontrol`.`Platforms` (`idPlatforms`)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Answers`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Answers` (\n" +
                "  `idAnswers` INT(11) NOT NULL,\n" +
                "  `task` INT(11) NOT NULL,\n" +
                "  `answer` MEDIUMTEXT NOT NULL,\n" +
                "  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  `worker_id` INT(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`idAnswers`),\n" +
                "  INDEX `workerAnswered_idx` (`worker_id` ASC),\n" +
                "  INDEX `idHITanswers_idx` (`task` ASC),\n" +
                "  CONSTRAINT `idHITanswers`\n" +
                "    FOREIGN KEY (`task`)\n" +
                "    REFERENCES `crowdcontrol`.`Task` (`idTask`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `workerAnswered`\n" +
                "    FOREIGN KEY (`worker_id`)\n" +
                "    REFERENCES `crowdcontrol`.`Worker` (`idWorker`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Constraints`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Constraints` (\n" +
                "  `idConstraints` INT(11) NOT NULL,\n" +
                "  `constraint` VARCHAR(45) NULL DEFAULT NULL,\n" +
                "  `experiment` INT(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`idConstraints`),\n" +
                "  INDEX `constrainedExperiment` (`experiment` ASC),\n" +
                "  CONSTRAINT `constrainedExperiment`\n" +
                "    FOREIGN KEY (`experiment`)\n" +
                "    REFERENCES `crowdcontrol`.`Experiment` (`idexperiment`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Payment`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Payment` (\n" +
                "  `idPayment` INT(11) NOT NULL,\n" +
                "  `worker_id` INT(11) NOT NULL,\n" +
                "  `experiment_id` INT(11) NOT NULL,\n" +
                "  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  `amount` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idPayment`),\n" +
                "  INDEX `payedWorker_idx` (`worker_id` ASC),\n" +
                "  INDEX `payedExperiment_idx` (`experiment_id` ASC),\n" +
                "  CONSTRAINT `payedExperiment`\n" +
                "    FOREIGN KEY (`experiment_id`)\n" +
                "    REFERENCES `crowdcontrol`.`Experiment` (`idexperiment`)\n" +
                "    ON DELETE RESTRICT\n" +
                "    ON UPDATE RESTRICT,\n" +
                "  CONSTRAINT `payedWorker`\n" +
                "    FOREIGN KEY (`worker_id`)\n" +
                "    REFERENCES `crowdcontrol`.`Worker` (`idWorker`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Population`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Population` (\n" +
                "  `idPopulation` INT(11) NOT NULL,\n" +
                "  `property` VARCHAR(255) NOT NULL,\n" +
                "  `answers` VARCHAR(255) NOT NULL,\n" +
                "  `description` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`idPopulation`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Ratings`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Ratings` (\n" +
                "  `idRatings` INT(11) NOT NULL,\n" +
                "  `task` INT(11) NOT NULL,\n" +
                "  `answer_r` INT(11) NOT NULL,\n" +
                "  `timestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\n" +
                "  `rating` INT(11) NULL DEFAULT NULL,\n" +
                "  `worker_id` INT(11) NULL,\n" +
                "  PRIMARY KEY (`idRatings`),\n" +
                "  INDEX `idAnswers_idx` (`answer_r` ASC),\n" +
                "  INDEX `workerRated_idx` (`worker_id` ASC),\n" +
                "  INDEX `idHITrating_idx` (`task` ASC),\n" +
                "  CONSTRAINT `idAnswersratins`\n" +
                "    FOREIGN KEY (`answer_r`)\n" +
                "    REFERENCES `crowdcontrol`.`Answers` (`idAnswers`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `idHITrating`\n" +
                "    FOREIGN KEY (`task`)\n" +
                "    REFERENCES `crowdcontrol`.`Task` (`idTask`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `workerRated`\n" +
                "    FOREIGN KEY (`worker_id`)\n" +
                "    REFERENCES `crowdcontrol`.`Worker` (`idWorker`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`Tags`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`Tags` (\n" +
                "  `idTags` INT(11) NOT NULL,\n" +
                "  `tag` VARCHAR(255) NOT NULL,\n" +
                "  `experiment` INT(11) NOT NULL,\n" +
                "  PRIMARY KEY (`idTags`),\n" +
                "  INDEX `idexperiment_idx` (`experiment` ASC),\n" +
                "  CONSTRAINT `idexperimenttags`\n" +
                "    FOREIGN KEY (`experiment`)\n" +
                "    REFERENCES `crowdcontrol`.`Experiment` (`idexperiment`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`PopulationAnswersOptions`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`PopulationAnswersOptions` (\n" +
                "  `idPopulationAnswers` INT NOT NULL,\n" +
                "  `population` INT NULL,\n" +
                "  `answer` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`idPopulationAnswers`),\n" +
                "  INDEX `populationAnswer_idx` (`population` ASC),\n" +
                "  CONSTRAINT `populationAnswer`\n" +
                "    FOREIGN KEY (`population`)\n" +
                "    REFERENCES `crowdcontrol`.`Population` (`idPopulation`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`ExperimentsPopulation`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`ExperimentsPopulation` (\n" +
                "  `idExperimentsPopulation` INT NOT NULL,\n" +
                "  `population_user` INT NOT NULL,\n" +
                "  `referenced_population` INT NOT NULL,\n" +
                "  `referenced_platform` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idExperimentsPopulation`),\n" +
                "  INDEX `populationUser_idx` (`population_user` ASC),\n" +
                "  INDEX `referencedPopulation_idx` (`referenced_population` ASC),\n" +
                "  INDEX `referencedPlatform_idx` (`referenced_platform` ASC),\n" +
                "  CONSTRAINT `populationUser`\n" +
                "    FOREIGN KEY (`population_user`)\n" +
                "    REFERENCES `crowdcontrol`.`Experiment` (`idexperiment`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `referencedPopulation`\n" +
                "    FOREIGN KEY (`referenced_population`)\n" +
                "    REFERENCES `crowdcontrol`.`Population` (`idPopulation`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `referencedPlatform`\n" +
                "    FOREIGN KEY (`referenced_platform`)\n" +
                "    REFERENCES `crowdcontrol`.`Platforms` (`idPlatforms`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`PopulationResults`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`PopulationResults` (\n" +
                "  `idPopulationResults` INT NOT NULL,\n" +
                "  `worker` INT NOT NULL,\n" +
                "  `answer` INT NOT NULL,\n" +
                "  PRIMARY KEY (`idPopulationResults`),\n" +
                "  INDEX `referencedWorker_idx` (`worker` ASC),\n" +
                "  INDEX `referencedAnswer_idx` (`answer` ASC),\n" +
                "  CONSTRAINT `referencedWorker`\n" +
                "    FOREIGN KEY (`worker`)\n" +
                "    REFERENCES `crowdcontrol`.`Worker` (`idWorker`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE,\n" +
                "  CONSTRAINT `referencedAnswer`\n" +
                "    FOREIGN KEY (`answer`)\n" +
                "    REFERENCES `crowdcontrol`.`PopulationAnswersOptions` (`idPopulationAnswers`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "-- -----------------------------------------------------\n" +
                "-- Table `crowdcontrol`.`TemplateVariables`\n" +
                "-- -----------------------------------------------------\n" +
                "CREATE TABLE IF NOT EXISTS `crowdcontrol`.`TemplateVariables` (\n" +
                "  `idTemplateVariables` INT NOT NULL,\n" +
                "  `template` INT NOT NULL,\n" +
                "  `name` VARCHAR(255) NOT NULL,\n" +
                "  `description` VARCHAR(255) NOT NULL,\n" +
                "  `type` VARCHAR(255) NOT NULL,\n" +
                "  PRIMARY KEY (`idTemplateVariables`),\n" +
                "  INDEX `referencedTemplate_idx` (`template` ASC),\n" +
                "  CONSTRAINT `referencedTemplate`\n" +
                "    FOREIGN KEY (`template`)\n" +
                "    REFERENCES `crowdcontrol`.`Templates` (`idTemplates`)\n" +
                "    ON DELETE CASCADE\n" +
                "    ON UPDATE CASCADE)\n" +
                "ENGINE = InnoDB;\n" +
                "\n" +
                "\n" +
                "SET SQL_MODE=@OLD_SQL_MODE;\n" +
                "SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;\n" +
                "SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;\n";
        context.fetch(sql);
    }

    /**
     * returns the Context used to communicate with the database
     * @return an instance of DSLContext
     */
    public DSLContext getContext() {
        return context;
    }
}

