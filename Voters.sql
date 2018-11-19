CREATE TABLE `Candidate` (
  `ID` int(11) NOT NULL,
  `candidateID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `CandidateInfo` (
  `candidateID` int(11) NOT NULL,
  `description` varchar(40) DEFAULT NULL,
  `numberVotes` int(11) DEFAULT NULL,
  PRIMARY KEY (`candidateID`)
)

CREATE TABLE `CandidateNames` (
  `candidateID` int(11) NOT NULL,
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`candidateID`)
)

CREATE TABLE `Party` (
  `partyID` int(11) NOT NULL AUTO_INCREMENT,
  `partyName` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`partyID`)
)

CREATE TABLE `Person` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `firstName` varchar(20) DEFAULT NULL,
  `lastName` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `gender` varchar(1) DEFAULT NULL,
  `candidateID` int(11) DEFAULT NULL,
  `partyID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `VoterAddress` (
  `ID` int(11) NOT NULL,
  `streetNumber` int(11) DEFAULT NULL,
  `street` varchar(30) DEFAULT NULL,
  `zip` int(11) DEFAULT NULL,
  `city` varchar(30) DEFAULT NULL,
  `state` varchar(2) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `VoterContactInfo` (
  `ID` int(11) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `homePhone` varchar(10) DEFAULT NULL,
  `cellPhone` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `Voters`.`ZipCodeInfo` (
  `zipID` INT NOT NULL AUTO_INCREMENT,
  `zip` VARCHAR(5) NULL,
  `state` VARCHAR(2) NULL,
  PRIMARY KEY (`idZipCodeInfo`))
