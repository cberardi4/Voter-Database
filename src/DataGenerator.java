import org.fluttercode.datafactory.impl.DataFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class DataGenerator
{
    DataFactory df = new DataFactory();


    String firstName, lastName;

    String street, city, address, state;
    int  streetNumber, zip;

    String emailAddress, cellPhone, homePhone;

    String [] states = {"AL", "MT", "AK", "NE", "AZ", "NV", "AR", "NH", "CA", "NJ", "CO", "NM", "CT", "NY", "DE",
                        "NC", "FL", "ND", "GA", "OH", "HI", "OK", "ID", "OR", "IL", "PA", "IN", "RI", "IA", "SC",
                        "KS", "SD", "KY", "TN", "LA", "TX", "ME", "UT", "MD", "VT", "MA", "VA", "MI", "WA", "MN",
                        "WV", "MS", "WI", "MO", "WY"};
    int index;

    char [] politicalParties = {'R', 'D', 'I', 'L', 'G'};
    char politicalParty;

    int genderOption = 1, age, candidateID;
    char gender;

    void createData(int numberRecords)  throws IOException
    {
        FileWriter write = new FileWriter("data.csv");

        for (int i = 0; i < numberRecords; i++) {

            // Create names
            firstName = df.getFirstName();
            lastName = df.getLastName();

            // Create addresses
            address = df.getAddress();
            Scanner scanner = new Scanner(address);

            // split address into street number and street name and also convert string to int
            streetNumber = Integer.parseInt(scanner.next());
            // convert string to int
            scanner.skip(" ");
            street = scanner.next();
            city = df.getCity();

            // randomly generate an index to choose the state from the states array
            index = df.getNumberBetween(0,49);
            state = states[index];
            zip = Integer.parseInt(df.getNumberText(5));
            System.out.println("address: "+ streetNumber + " " + street + " " + city + " " + state + ", " + zip);

            // contact info
            emailAddress = df.getEmailAddress();
            // generate cell and home phone numbers
            cellPhone = df.getNumberText(10);
            homePhone = df.getNumberText(10);

            // political party
            index = df.getNumberBetween(0,4);
            politicalParty = politicalParties[index];

            // gender
            // want an equal ratio of men to women, so switch off every time it loops
            if (genderOption == 1)
            {
                gender = 'F';
                // next loop will change the gender to man
                genderOption = 0;
            }
            else
            {
                gender = 'M';
                genderOption = 1;
            }

            // age
            age = df.getNumberBetween(18,100);

            // voted for?
            candidateID = df.getNumberBetween(1,4);

            // write record to file
            write.append(firstName + ", " + lastName + ", " + age + ", " + gender + ", " + candidateID + ", " +
                         politicalParty + ", " + emailAddress + ", " + homePhone + ", " + cellPhone + ", " +
                         streetNumber + ", " + street + ", " + zip);
            write.append('\n');
        }
        write.close();
    }



}
