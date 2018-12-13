CREATE TABLE `Candidate` (
  `ID` int(11) NOT NULL,
  `candidateID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
)

CREATE TABLE `CandidateInfo` (
  `candidateID` int(11) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
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
  `partyID` int(11) NOT NULL,
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

INSERT INTO `Party` VALUES (1,'Republican'),(2,'Democrat'),(3,'Independent'),(4,'Libertarian'),(5,'Green Party');
INSERT INTO `CandidateNames` VALUES (1,"Erik","Linstead"),(2,"Rene","German"),(3,"Elizabeth","Stevens");
INSERT INTO `Person` VALUES (1001, "Erik", "Linstead", 40, "M", 1, 2),(1002,"Rene","German",30,"M", 2,1),(1003,"Elizabeth","Stevens",28,"F",3,3);
INSERT INTO `Candidate` VALUES (1001,1),(1002,2),(1003,3);
INSERT INTO `CandidateInfo` VALUES (1,"I believe that not all heroes wear capes, I am one of those.",0),(2,"Steak and lobster for all!",0),(3,"I was once DG president, I can do it again.",0);
INSERT INTO `VoterAddress` VALUES (1001,3246,"Chapman",92866),(1002,54679,"Glassel",92866),(1003,726,"Walnut",92867);
INSERT INTO `VoterContactInfo` VALUES (1001, "linstead@chapman.edu",7141234567,7143568976),(1002,"german@chapman.edu",7142349875,8581230098),(1003,"estevens@chapman.edu",7142387654,7148299332);

