import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    This class calls the Person, Candidate, or Address classes to get user input and generate a SQL script.
    Once it receives that information, it turns the SQL string into Prepared Statements and executes them
    in the MySQL Workbench.
*/



public class DatabaseManager {
    Connector con = new Connector();
    Connection connection;

    // create a new person object
    Person p = new Person();
    Address a = new Address();
    ContactInfo i = new ContactInfo();
    Candidate c = new Candidate();
    ZipCodeInfo z = new ZipCodeInfo();


    FileWriter log;

    // creating log variable
    {
        try {
            log = new FileWriter("log.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
    **********************
    CREATE NEW RECORDS
    **********************
     */

    // creates a new record in Person table
    public void createPerson(String username, String password) throws Exception {
        /* *****************
         Person TABLE
        // call person class and get all general user information from input for Person table
         ******************* */

        String sqlP, sqlC, sqlA, sqlZ, state;
        int zip, id=0;
        boolean addZip;
        String[] addressInfo;
        PreparedStatement preparedStatementPerson, preparedStatementContact, preparedStatementAddress, preparedStatementZip;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);

            // Transaction
            connection.setAutoCommit(false);

            // *************
            // PERSON
            // *************

            // get SQL statement for creating a new record in Person table
            sqlP = p.createPerson();

            // convert string into SQL statement and insert into database
            preparedStatementPerson = connection.prepareStatement(sqlP, PreparedStatement.RETURN_GENERATED_KEYS);

            // want to rollback unless all insert statements are executed in this function
            try {
                // execute insert statement
                preparedStatementPerson.executeUpdate();
                connection.commit();
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating person record failed. '\n'");

            }

            // get primary key from sql query to use as PK in other tables
            rs = preparedStatementPerson.getGeneratedKeys();
            if (rs != null && rs.next())
                id = rs.getInt(1);

            // log action
            log.append("Create person record with ID = "+id+". '\n'");

            // *************
            // CONTACT INFO
            // *************

            // Transaction
            connection.setAutoCommit(false);

            // get SQL statement for creating a new record in Contact Info table
            sqlC = i.createVoterContactInfo(id);

            // convert string into SQL statement and insert into database
            preparedStatementContact = connection.prepareStatement(sqlC);

            // want to rollback unless all insert statements are executed in this function
            try {
                // execute insert statement
                preparedStatementContact.executeUpdate();
                connection.commit();
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating VoterContactInfo with ID = "+id+" record failed. '\n'");
            }

            log.append("Create VoterContactInfo record with ID = "+id+". '\n'");

            // *************
            // ADDRESS
            // *************

            // Transaction
            connection.setAutoCommit(false);

            // get SQL query for creating a new record in Address Table
            // also return zip and state for ZipCodeInfo Table
            addressInfo = a.createVoterAddress(id);

            // extract zip and state from contact
            sqlA = addressInfo[0];
            zip = Integer.parseInt(addressInfo[1]);
            state = addressInfo[2];

            // convert string into SQL statement and insert into database
            preparedStatementAddress = connection.prepareStatement(sqlA);

            // want to rollback unless all insert statements are executed in this function
            try {
                // execute insert statement
                preparedStatementAddress.executeUpdate();
                connection.commit();
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating VoterAddress with ID = "+id+" record failed. '\n'");
            }


            log.append("Create VoterAddress with ID = "+id+" record. '\n'");

            // *************
            // ZIP CODE
            // *************

            // Transaction
            connection.setAutoCommit(false);


            // check if zipcode is already in table and doesn't need to be added again
            addZip = checkZipCode(zip);

            // only want to add zip to table if the record doesn't already exist
            if (addZip) {
                // check if you need to add zip to database
                try {
                    sqlZ = z.createZipCodeInfo(zip, state);
                    preparedStatementZip = connection.prepareStatement(sqlZ);
                    preparedStatementZip.executeUpdate();
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                    log.append("Create ZipCodeInfo with zip = "+zip+" record failed. '\n'");
                }

                log.append("Create ZipCode with zip = "+zip+" +record. '\n'");
            }


            // end of transaction
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void createCandidate(String username, String password) throws Exception
//    {
//        /* *****************
//         Candidate TABLE
//         ******************* */
//
//        String sqlC, sqlCI, sqlCN;
//        int id=0, candidateID=0;
//        PreparedStatement preparedStatementCandidate, preparedStatementCandidateInfo, preparedStatementCandidateNames;
//        ResultSet rs;
//
//        // connect to database
//        try {
//            connection = con.Connector(username, password);
//
//            // Transaction
//            connection.setAutoCommit(false);
//
//            // *************
//            // Candidate
//            // *************
//
//            // get SQL statement for creating a new record in Candidate table
//            sqlC = c.createCandidate(id);
//
//            // convert string into SQL statement and insert into database
//            preparedStatementCandidate = connection.prepareStatement(sqlC, PreparedStatement.RETURN_GENERATED_KEYS);
//
//            // want to rollback unless all insert statements are executed in this function
//            try {
//                // execute insert statement
//                preparedStatementCandidate.executeUpdate();
//                connection.commit();
//            } catch (SQLException s) {
//                connection.rollback();
//            }
//
//            // get primary key from sql query to use as PK in other tables
//            rs = preparedStatementCandidate.getGeneratedKeys();
//            if (rs != null && rs.next())
//                id = rs.getInt(1);
//
//            // *************
//            // CANDIDATE INFO
//            // *************
//
//            // Transaction
//            connection.setAutoCommit(false);
//
//            // get SQL statement for creating a new record in Candidate Info table
//            sqlCI = c.populateCandidateInfo(candidateID);
//
//            // convert string into SQL statement and insert into database
//            preparedStatementCandidateInfo = connection.prepareStatement(sqlCI);
//
//            // want to rollback unless all insert statements are executed in this function
//            try {
//                // execute insert statement
//                preparedStatementCandidateInfo.executeUpdate();
//                connection.commit();
//            } catch (SQLException s) {
//                connection.rollback();
//            }
//
//            // *************
//            // Candidate Names
//            // *************
//
//            // Transaction
//            connection.setAutoCommit(false);
//
//            sqlCN = c.populateCandidateNames(candidateID);
//
//            // convert string into SQL statement and insert into database
//            preparedStatementCandidateNames = connection.prepareStatement(sqlCN);
//
//            // want to rollback unless all insert statements are executed in this function
//            try {
//                // execute insert statement
//                preparedStatementCandidateNames.executeUpdate();
//                connection.commit();
//            } catch (SQLException s) {
//                connection.rollback();
//            }
//            // end of transaction
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /*
    *********************
    UPDATE FUNCTIONS
    *********************
     */

    // updates a user's first or last name in the Person table
    public void updateName(String username, String password, int id) throws Exception {

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete record from Person table
        sql = p.updateName(id);

        // Transaction
        connection.setAutoCommit(false);

        try{
            // execute query
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Changing name of ID = "+id+" record failed. '\n'");
        }


        // log update
        log.append("Updated name in Person record with id =  "+id+". '\n'");
    }

    public void updateVoterAddress(String username, String password, int id) throws Exception {

        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete record from VoterAddress table
        String[] sql = a.updateAddress(id);


        // Transaction
        connection.setAutoCommit(false);

        try {

            // 3 indexes in sql array, have to execute each update statement to update all
            // of address fields
            for (int k = 0; k < 3; ++k) {
                preparedStatement = connection.prepareStatement(sql[k]);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Changing address in VoterAddress record with id = "+id+" record failed. '\n'");
        }

        // log update
        log.append("Updating address in VoterAddress record with id =  "+id+". '\n'");

    }


    public void updateVoterContactInfo(String username, String password, int id) throws Exception {

        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterContactInfo table
        String[] sql = i.updateContactInfo(id);

        try {
            // 3 indexes in sql array, have to execute each update statement to update all
            // of address fields

            preparedStatement = connection.prepareStatement(sql[0]);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Updating home phone in VoterContactInfo record with id = "+id+" record failed. '\n'");
        }

        // Transaction
        connection.setAutoCommit(false);
        try {
            // 3 indexes in sql array, have to execute each update statement to update all
            // of address fields

            preparedStatement = connection.prepareStatement(sql[1]);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Updating cell phone in VoterContactInfo record with id = "+id+" record failed. '\n'");
        }
        // Transaction
        connection.setAutoCommit(false);
        try {
            // 3 indexes in sql array, have to execute each update statement to update all
            // of address fields

            preparedStatement = connection.prepareStatement(sql[2]);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Updating email in VoterContactInfo record with id = "+id+" record failed. '\n'");
        }


        // log update
        log.append("Updated contact info in VoterContactInfo record with id =  "+id+". '\n'");

    }

    public void updateAge(String username, String password, int id) throws Exception
    {

        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterContactInfo table
        String sql = p.updateAge(id);
        System.out.println("SQL: " + sql);

        try {

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Updating age in Person record with id = "+id+" record failed. '\n'");
        }

        // log update
        log.append("Updated age in Person record with id =  "+id+". '\n'");

    }

    public void updateParty(String username, String password, int id) throws Exception
    {

        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterContactInfo table
        String sql = p.updatePoliticalParty(id);

        try {

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
            connection.commit();
        }
        catch (SQLException e) {
            connection.rollback();
            log.append("Updating political party in Person record with id = "+id+" record failed. '\n'");
        }

        // log update
        log.append("Updated political party in Person record with id =  "+id+". '\n'");

    }



    /*
    *******************
    DELETE FUNCTIONS
    *******************
     */

    // deletes a voter, including their record in Person, VoterAddress, and VoterContactInfo
    public void deletePerson(String username, String password, int id) throws Exception {

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // *************
        // PERSON
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from Person table
        sql = p.deletePerson(id);

        preparedStatement = connection.prepareStatement(sql);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute delete statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting Person with ID = "+id+" record failed. '\n'");
        }

        log.append("Delete person with ID = "+id+" record. '\n'");

        // *************
        // Address
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterAddress table
        sql = a.deleteAddress(id);

        preparedStatement = connection.prepareStatement(sql);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute delete statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting VoterAddress with ID = "+id+" record failed. '\n'");
        }

        log.append("Delete VoterAddress with ID = "+id+" record. '\n'");

        // *************
        // CONTACT INFO
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterContactInfo table
        sql = i.deleteContactInfo(id);

        preparedStatement = connection.prepareStatement(sql);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute insert statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting ContactInfo with ID = "+id+" record failed. '\n'");
        }

        log.append("Delete ContactInfo with ID = "+id+" record. '\n'");

    }

    // deletes a candidate, including their record in Candidate, CandidateInfo, and CandidateNames
    public void deleteCandidate(String username, String password, int candidateID) throws Exception {

        String sql, sql2, sql3;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // *************
        // Candidate
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from Candidate table
        sql = c.deleteCandidate(candidateID);

        preparedStatement = connection.prepareStatement(sql);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute delete statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting candidate with candidateID = "+candidateID+" record failed. '\n'");
            //log.close();
        }

        log.append("Delete candidate with candidateID = "+candidateID+" record. '\n'");
        //log.close();

        // *************
        // CandidateInfo
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from VoterAddress table
        sql2 = c.deleteCandidateInfo(candidateID);

        preparedStatement = connection.prepareStatement(sql2);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute delete statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting CandidateInfo with candidateID = "+candidateID+" record failed. '\n'");
            //log.close();
        }

        log.append("Delete CandidateID with candidateID = "+candidateID+" record. '\n'");
        //log.close();

        // *************
        // CandidateNames
        // *************

        // Transaction
        connection.setAutoCommit(false);

        // delete record from CandidateNames table
        sql3 = c.deleteCandNames(candidateID);

        preparedStatement = connection.prepareStatement(sql3);

        // want to rollback unless all delete statements are executed in this function
        try {
            // execute insert statement
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException s) {
            connection.rollback();
            log.append("Deleting CandidateNames with candidateID = "+candidateID+" record failed. '\n'");
            //log.close();
        }

        log.append("Delete CandidateNames with candidateID = "+candidateID+" record. '\n'");
        log.close();

    }




    /*
    ***********************
    QUERY FUNCTIONS
    ***********************
     */

    // returns name, age, gender, and party of all voters
    public void selectAllVoters(String username, String password) throws Exception {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = p.printVoterInformation();

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        displayResultSetPerson(rs);
    }

    // returns the number of voters in a certain party (given by user input)
    public void numberRegisteredVotersInParty(String username, String password) throws Exception {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = p.numberRegisteredVotersInParty();

        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
    }


    public String printNameFromID(String username, String password, int id) throws Exception {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = p.printPersonFromID(id);
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        String f = "", l = "", name;

        // get first and last name from Result Set
        while (rs.next()) {
            f = rs.getString("firstName");
            l = rs.getString("lastName");
        }

        name = f + " " + l;
        System.out.println(name);

        return name;
    }

    public String printCandidateNameFromCID(String username, String password, int candidateID) throws Exception
    {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = c.printCandidateFromCID(candidateID);
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        String f = "", l = "", name;

        // get first and last name from Result Set
        while (rs.next()) {
            f = rs.getString("firstName");
            l = rs.getString("lastName");
        }

        name = f + " " + l;
        //System.out.println(name);

        return name;

    }




    /*
    ****************************
    INTERNAL FUNCTIONS
    ****************************
    */

    // print information from sql query
    public void displayResultSetPerson(ResultSet rs) throws SQLException {

        System.out.println("Selected Person: ");

        int id, age, partyID;
        String firstName, lastName, gender;

        // iterate through SQL response to human-readable output
        while (rs.next()) {
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            age = rs.getInt("age");
            gender = rs.getString("gender");
            partyID = rs.getInt("partyID");
            System.out.println(firstName + ", " + lastName + ", " + age + ", " + gender + ", " + partyID);
        }
    }

    // query ZipCodeInfo Table to see if that zip is already in it
    // if it is, then you don't need to add it
    // returns true if a new zip code record needs to be created
    public boolean checkZipCode(int zip) throws SQLException {
        String sqlCheck;
        int zipQuery;
        PreparedStatement preparedStatementZip;
        ResultSet rs;

        try {

            sqlCheck = z.checkIfZipAlreadyInDB(zip);
            preparedStatementZip = connection.prepareStatement(sqlCheck);
            rs = preparedStatementZip.executeQuery();

            while (rs.next()) {
                zipQuery = rs.getInt("zip");
                if (zip == zipQuery)
                    return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }


    public void closeFile() throws IOException
    {
        log.close();
    }
}