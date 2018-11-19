import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager
{
    Connector con = new Connector();
    Connection connection;

    // create a new person object
    Person p = new Person();


    public void createPerson(String username, String password) throws Exception {
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

        sql = p.createPerson();

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }


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

        sql = p.createVoterContactInfo(id);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

    }

    public String createVoterAddress(String username, String password, int id) throws Exception
    {
        /* ***********************
         ZipCodeInfo TABLE
         call person class and get address information for VoterAddress table

         Must return zip code to the main so that it can add it to the zip code database
        *************************** */

        PreparedStatement preparedStatement;
        String [] addressInfo;
        String sql, zip;


        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addressInfo = p.createVoterAddress(id);

        sql = addressInfo[1];
        zip = addressInfo[2];

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        return zip;

    }

    public String createZipCodeInfo(String username, String password, int id, String zip) throws Exception
    {
        /* ***********************
         ZipCodeInfo TABLE
         call person class and get zip and state information for ZipCodeInfo tablex
        *************************** */

        // first need to retrieve voter ID to make sure records are uniform for each id
        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // FIRST NEED TO CHECK THAT ZIP CODE ISN'T ALREADY IN THE TABLE AND THAT STATE IS VALID

        sql = p.createVoterZip(zip);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();

        return zip;

    }

        // print information from sql query
    public void displayResultSetPerson (ResultSet rs) throws SQLException
    {

        System.out.println("Selected Person: ");

        int id, age, candidateID, partyID;
        String firstName, lastName, gender;

        // iterate through SQL response to human-readable output
        while (rs.next())
        {
            id = rs.getInt("ID");
            firstName = rs.getString("firstName");
            lastName = rs.getString("lastName");
            age = rs.getInt("age");
            gender = rs.getString("gender");
            candidateID = rs.getInt("candidateID");
            partyID = rs.getInt("partyID");
            System.out.println(id + ", " + firstName + ", " + lastName + ", " + age + ", " + gender + ", " + candidateID
                    + ", " + partyID);

        }
    }
}