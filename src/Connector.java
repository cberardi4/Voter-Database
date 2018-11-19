import java.sql.Connection;
import java.sql.DriverManager;

public class Connector
{
    Connection con = null;

    public Connection Connector(String username, String password)
    {
        try {

            if (con == null) {
                Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Voters?serverTimezone=America/Los_Angeles", username, password);
            }
            return con;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
