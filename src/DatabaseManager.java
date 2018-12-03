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
    Person c = new Candidate();
    ZipCodeInfo z = new ZipCodeInfo();

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

        String sqlP, sqlC, sqlA, sqlZ, state, sqlCheck;
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
            }

            // get primary key from sql query to use as PK in other tables
            rs = preparedStatementPerson.getGeneratedKeys();
            if (rs != null && rs.next())
                id = rs.getInt(1);

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
            } catch (SQLException s) {
                connection.rollback();
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
                    sqlZ = z.createZipCodeInfo(zip, state);
                    preparedStatementZip = connection.prepareStatement(sqlZ);
                    preparedStatementZip.executeUpdate();
                    connection.commit();
                } catch (SQLException e) {
                    connection.rollback();
                }
            }


            // end of transaction
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

        /*
    public void createCandidate(String username, String password, int id) throws Exception
    {
        /*
        **************************

        ***************************
        */
/*

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = c.createCandidate(id);

    }
    */

    /*
    *********************
    UPDATE FUNCTIONS
    *********************
     */

    // updates a user's first or last name in the Person table
    public void updatePerson(String username, String password, int id) throws Exception {

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

        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

    }

    public void updateVoterAddress(String username, String password, int id) throws Exception {

        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete record from Person table
        String[] sql = a.updateAddress(id);

        // 4 indexes in sql array, have to execute each update statement to update all
        // of address fields
        for (int k = 0; k < 4; ++k) {
            preparedStatement = connection.prepareStatement(sql[k]);
            preparedStatement.executeUpdate();
        }

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

        // delete record from Person table
        sql = p.deletePerson(id);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    public void deleteContactInfo(String username, String password, int id) throws Exception {

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete record from VoterContactInfo table
        sql = i.deleteContactInfo(id);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

    }

    public void deleteAddress(String username, String password, int id) throws Exception {

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // delete record from VoterAddress table
        sql = a.deleteAddress(id);
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
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
}