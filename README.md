# Voter-Database

### Current Functionality:
- register a new voter
- registered voter can vote
- update voter information
- withdrawal a candidate
- update candidate information
- print voter and candidate information
- prints reports to a CSV file
- logs transactions with database

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
- update user information
- delete users
- get user information

### Address:
- creates a new address for a person object by getting user input, cleaning it, and crafting SQL statements for the VoterAddress table
- update VoterAddress information
- delete VoterAddress record
- returns VoterAddress information

### ContactInfo:
- creates contact info for a person object by getting user input, cleaning it, and crafting SQL statements for the VoterContactInfo table
- update VoterContactInfo record
- delete VoterContactInfo record
- returns information on VoterContact information

### Candidate:
- creates Candidate entry
- update Candidate information
- withdrawal (delete) a candidate from the race
- return various SQL queries on candidate information

### DataGenerator
- creates csv file of data using DataFactory class

#### How Database get called:
- Main > Database Manager > Connection
- Main > Database Manager > Person


   
