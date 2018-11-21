import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
    This class calls the Person, Candidate, or Address classes to get user input and generate a SQL script.
    Once it receives that information, it turns the SQL string into Prepared Statements and executes them
    in the MySQL Workbench.
*/



public class DatabaseManager
{
    Connector con = new Connector();
    Connection connection;

    // create a new person object
    Person p = new Person();
    Address a = new Address();
    ContactInfo i = new ContactInfo();
    Person c = new Candidate();

    /*
    **********************
    CREATE NEW RECORDS
    **********************
     */

    // creates a new record in Person table
    public void createPerson(String username, String password, int id) throws Exception {
        /* *****************
         Person TABLE
        // call person class and get all general user information from input for Person table
         ******************* */

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = p.createPerson(id);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }


    // create a new record in VoterContactInfo
    public void createVoterContactInfo(String username, String password, int id) throws Exception
    {
        /* ***********************
         VoterContactInfo TABLE
         call person class and get contact information for VoterContactInfo table
        *************************** */

        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = i.createVoterContactInfo(id);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

    }

    // create a new record in VoterAddress table
    public void createVoterAddress(String username, String password, int id) throws Exception
    {
        /* ***********************
         ZipCodeInfo TABLE
         call person class and get address information for VoterAddress table

         Must create record in zip code table  with zip and state info
        *************************** */

        PreparedStatement preparedStatement;
        String [] addressInfo;
        String sql, zip, state;


        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressInfo = a.createVoterAddress(id);

        sql = addressInfo[0];
        zip = addressInfo[1];
        state = addressInfo[2];

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

       createZipCodeInfo(username, password, zip, state);
    }

    // create a new record in the ZipCodeInfo table
    public void createZipCodeInfo(String username, String password, String zip, String state) throws Exception
    {
        /* ***********************
         ZipCodeInfo TABLE
         call person class and get zip and state information for ZipCodeInfo table
        *************************** */


        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // FIRST NEED TO CHECK THAT ZIP CODE ISN'T ALREADY IN THE TABLE AND THAT STATE IS VALID

        sql = a.createVoterZip(zip, state);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();


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

    public void updateVoterAddress(String username, String password, int id) throws Exception
    {

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
        for (int k = 0; k< 4; ++k) {
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
    public void selectAllVoters(String username, String password) throws Exception
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

        sql = p.printVoterInformation();

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        displayResultSetPerson(rs);
    }

    // returns the number of voters in a certain party (given by user input)
    public void numberRegisteredVotersInParty(String username, String password) throws Exception

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

        sql = p.numberRegisteredVotersInParty();

        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
    }



    public String printNameFromID(String username, String password, int id) throws Exception
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

        sql = p.printPersonFromID(id);
        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();

        String f="", l="" ,name;

        // get first and last name from Result Set
        while (rs.next())
        {
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
    public void displayResultSetPerson (ResultSet rs) throws SQLException
    {

        System.out.println("Selected Person: ");

        int id, age, partyID;
        String firstName, lastName, gender;

        // iterate through SQL response to human-readable output
        while (rs.next())
        {
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            age = rs.getInt("age");
            gender = rs.getString("gender");
            partyID = rs.getInt("partyID");
            System.out.println(firstName + ", " + lastName + ", " + age + ", " + gender + ", " + partyID);
        }
    }
}