# Voter-Database

### Current Functionality:
- can register a new voter

## Classes
Written in order in which the classes get called

### Main
- get login info for the database
- opening menus to register voters, register candidates, and print information
- initializes Database Manager object

### Database Manager
- interacts with the MySQL workbench 
- gets the sql String from the People class and turns them into prepared statements, result sets and prints results
- calls the Connection class, which just connects to the database

### People:
- creates a new person object by getting user input (and cleans it) and crafts special sql statements tailored to that person

### Address:
- creates a new address for a person object by getting user input, cleaning it, and crafting SQL statements for the VoterAddress table

### ContactInfo:
- creates contact info for a person object by getting user input, cleaning it, and crafting SQL statements for the VoterContactInfo table

** NEED TO CREATE RECORD FOR PEOPLE, ADDRESS, AND CONTACTINFO TABLES EVERY TIME REGISTERING A VOTER

### DataGenerator
- creates csv file of data

### ReadData
- read in csv file? idk could get rid of it if theres an easier way

#### How classes get called:
- Main > Database Manager > Connection
- Main > Database Manager > Person


*** currently working on the voter registration option right now, adding voter addresses and have to also add voter contact info

## To Create a new table:
- main:
  - use the DatabaseManager manager object to the create function in the DB Manager class
   - DB Manager class calls the Person p, Address a, or ContactInfo i objects which create the SQL statements
   - SQL statements are then executed in the DB manager class
   
