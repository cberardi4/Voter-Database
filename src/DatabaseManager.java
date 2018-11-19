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
        String sql;
        PreparedStatement preparedStatement;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // PERSON TABLE
        // call person class and get all general user information from input for Person table
        sql = p.createPerson();

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
    }

    // VOTERCONTACTINFO TABLE
    // call person class and get contact information for VoterContactInfo table
    public void createVoterContactInfo(String username, String password, int id) throws Exception
    {
        // first need to retrieve voter ID to make sure records are uniform for each id
        String sql;
        PreparedStatement preparedStatement;
        ResultSet rs;

        // connect to database
        try {
            connection = con.Connector(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sql = "SELECT ID FROM Person WHERE ID = " + id;

        preparedStatement = connection.prepareStatement(sql);
        rs = preparedStatement.executeQuery();
        id = rs.getInt("ID");

        sql = p.createVoterContactInfo(id);

        // convert string into SQL statement and insert into database
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.executeUpdate();
        */
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