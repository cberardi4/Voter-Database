/*
import org.apache.commons.csv.*;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReadData
{
    Connector con = new Connector();
    Connection connection;

    PreparedStatement pS;
    ResultSet rset;

    void readCSV(String username, String password) throws IOException, SQLException
    {
        String sql;
        try {
            connection = con.Connector(username, password);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        FileReader in = new FileReader("data.csv");
        Iterable<CSVRecord> people  = CSVFormat.EXCEL.parse(in);

        for (CSVRecord p: people)
        {

        }
    }
}
*/