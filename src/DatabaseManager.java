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
    //ZipCodeInfo z = new ZipCodeInfo();

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

                // get primary key from sql query to use as PK in other tables
                rs = preparedStatementPerson.getGeneratedKeys();
                if (rs != null && rs.next())
                    id = rs.getInt(1);

                log.append("Create person record with ID = "+id+". '\n'");
                System.out.println("Created person with ID = "+id+".");
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating person record failed. '\n'");
                System.out.println("Creating person record failed.");

            }


            // log action


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
                log.append("Create VoterContactInfo record with ID = "+id+". '\n'");
                System.out.println("Create contact info successful with ID = "+id+".");
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating VoterContactInfo with ID = "+id+" record failed. '\n'");
                System.out.println("Create contact info failed");
            }


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
                log.append("Create VoterAddress with ID = "+id+" record. '\n'");
                System.out.println("Create address successful.");
            } catch (SQLException s) {
                connection.rollback();
                log.append("Creating VoterAddress with ID = "+id+" record failed. '\n'");
                System.out.println("Create address failed");
            }


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
                    sqlZ = a.createZipCodeInfo(zip, state);
                    preparedStatementZip = connection.prepareStatement(sqlZ);
                    preparedStatementZip.executeUpdate();
                    connection.commit();
                    log.append("Create ZipCode with zip = "+zip+" +record. '\n'");
                } catch (SQLException e) {
                    connection.rollback();
                    log.append("Create ZipCodeInfo with zip = "+zip+" record failed. '\n'");
                }

            }


            // end of transaction
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




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


    public void updateCandidateInfo(String username, String password, int candidateID) throws Exception
    {
        String sql;
        PreparedStatement preparedStatement;

        //connect to database
        try{
            connection = con.Connector(username, password);
        } catch (Exception e){
            e.printStackTrace();
        }

        sql = c.updateDescription(candidateID);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        log.append("Updated description in CandidateInfo record with candidateID =  "+candidateID+". '\n'");
        log.close();

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


    public void vote(String username, String password, int id, int candidateID) throws Exception
    {
        PreparedStatement preparedStatement;
        ResultSet rs;
        int numVotesBeforeNewVote;
        String sql, name="", firstName, lastName;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = c.getCandidateNameFromID(candidateID);
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        // need to get name of the candidate they voted for for log/print statement
        while(rs.next())
        {
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            name = firstName + " " + lastName;
        }


        // update Person record to change who they voted for
        sql = p.updateVote(id, candidateID);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        // need to add vote to their name in CandidateInfo Table

        // first must get how many votes they have
        sql = c.getNumberVotesForCandidate(candidateID);
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
        rs.next();
        numVotesBeforeNewVote = rs.getInt("numberVotes");

        // now must increment number of votes by one
        sql = c.addVote(candidateID, numVotesBeforeNewVote);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        System.out.println("Successfully voted for " + name);

        // log update
        log.append("Voter with id =  "+id+" has voted for "+name+". '\n'");
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

        // have to decrement amount of votes for candidate they voted for now that they're no longer in database
        try {
            // get the candidateID of who they voted for
            sql = p.getCandidateID(id);
            preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            rs.first();
            int candID = rs.getInt(1);
            int numVotesBeforeDeletingVote;

            // first must get how many votes they have
            sql = c.getNumberVotesForCandidate(candID);
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();
            rs.next();
            numVotesBeforeDeletingVote = rs.getInt(1);

            // now must increment number of votes by one
            sql = c.removeVote(candID, numVotesBeforeDeletingVote);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();

            log.append("Removed vote for candidate with ID = " + candID);
        }
        catch (SQLException s) {
            System.out.println("Could not change count of votes");
            log.append("Could not change count of votes");
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
        sql = c.deleteCandNames(candidateID);

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
        sql3 = c.deleteCandidate(candidateID);

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

    public void selectAllCandidates(String username, String password) throws Exception
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

        sql = c.printCandidateInformation();


        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        displayResultSetCandidate(rs);

    }

    public void numberRegisteredVotersInParty(String username, String password) throws Exception {

        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // contains SQL statement at index 0 and the partyID at index 1
        // needed so that you can print name of party
        String[] sql = p.numberRegisteredVotersInParty();

        // execute query to get number of registered voters in selected party
        preparedStatement = connection.prepareStatement(sql[0]);
        rs = preparedStatement.executeQuery();
        int numInParty = 0;

        // get count of registered voters from party from resultset
        if (rs.next())
            numInParty = rs.getInt("count");

        // execute query to find the name of the party they selected for print statement
        String sqlPartyName = p.getPartyName(Integer.parseInt(sql[1]));
        preparedStatement = connection.prepareStatement(sqlPartyName);
        rs = preparedStatement.executeQuery();
        rs.next();
        String pName = rs.getString(1);


        System.out.println("There are " + numInParty + " registered voters in the " + pName+".");
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

        sql = c.getCandidateName(candidateID);
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

    public String printNumberVotesCandidate(String username, String password) throws SQLException
    {
        String sql1, sql2, sql3, result;
        PreparedStatement preparedStatement1, preparedStatement2, preparedStatement3;
        ResultSet rs1, rs2, rs3;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql1 = c.getNumberVotesForCandidate(1);
        sql2 = c.getNumberVotesForCandidate(2);
        sql3 = c.getNumberVotesForCandidate(3);

        preparedStatement1 = connection.prepareStatement(sql1);
        preparedStatement2 = connection.prepareStatement(sql2);
        preparedStatement3 = connection.prepareStatement(sql3);
        rs1 = preparedStatement1.executeQuery();
        rs2 = preparedStatement2.executeQuery();
        rs3 = preparedStatement3.executeQuery();


        int candidates[] = new int[3];
        int numVotes;
        int index = 0;

        candidates[0] = rs1.getInt("numberVotes");
        candidates[1] = rs2.getInt("numberVotes");
        candidates[2] = rs3.getInt("numberVotes");

//        while(rs.next())
//        {
//            numVotes = rs.getInt("numberVotes");
//
//            candidates[index] = numVotes;
//            index++;
//        }

        return result = "Erik Linstead " + String.valueOf(candidates[0]) + "Rene German " + String.valueOf(candidates[1]) + "Elizabeth Stevens " + String.valueOf(candidates[2]);
    }

    public String[] printAllCandidates(String username, String password) throws SQLException {
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = c.selectAllCandidateNames();
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        String candidates[] = new String[3];
        String firstName, lastName, fullName;
        int index = 0;

        while(rs.next())
        {
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            fullName = firstName + " " + lastName;
            candidates[index] = fullName;
            index++;
        }
        return candidates;
    }


    public void contactInfoReport(String username, String password) throws SQLException, IOException {

        String sql = "Select p.ID, p.firstName, p.lastName, i.email, i.homePhone, i.cellPhone, a.streetNumber, a.street, a.zip, z.state" +
                " From Person p, VoterContactInfo i, VoterAddress a, ZipCodeInfo z" +
                " Where p.ID = i.ID AND p.ID = a.ID AND a.zip = z.zip";
        String firstName="", lastName="", email="", homePhone="", cellPhone="", street="", state="";
        int streetNumber=0, zip=0, id=0;

        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
        FileWriter writer = new FileWriter("ContactInfo.csv");

        while(rs.next())
        {
            id = rs.getInt("ID");
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            email = rs.getString("email");
            homePhone = rs.getString("homePhone");
            cellPhone = rs.getString("cellPhone");
            streetNumber = rs.getInt("streetNumber");
            street = rs.getString("street");
            zip = rs.getInt("zip");
            state = rs.getString("state");
            writer.append(id + ", " + firstName + ", " + lastName + ", " + email + ", " + homePhone + ", " +
                    cellPhone + ", " + streetNumber + ", " + street + ", " + zip + ", " + state + '\n');
        }
        writer.close();
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

    public void displayResultSetCandidate(ResultSet rs) throws SQLException
    {
        System.out.println("Selected Candidate: ");
        String firstName, lastName;

        while (rs.next())
        {
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            System.out.println(firstName + ", " + lastName);
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

            sqlCheck = a.checkIfZipAlreadyInDB(zip);
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