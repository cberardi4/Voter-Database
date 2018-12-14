import java.util.Scanner;

 /*
    This class gets user information on their address.
    It sanitizes the input and puts it into an SQL statement to send to DB Manager
 */

public class Address
{
    Scanner keyboard = new Scanner(System.in);

    String street, city, state, zip;
    int  streetNumber;

    String [] states = {"AL", "MT", "AK", "NE", "AZ", "NV", "AR", "NH", "CA", "NJ", "CO", "NM", "CT", "NY", "DE",
            "NC", "FL", "ND", "GA", "OH", "HI", "OK", "ID", "OR", "IL", "PA", "IN", "RI", "IA", "SC",
            "KS", "SD", "KY", "TN", "LA", "TX", "ME", "UT", "MD", "VT", "MA", "VA", "MI", "WA", "MN",
            "WV", "MS", "WI", "MO", "WY"};


    public String[] createVoterAddress(int id)
    {
        System.out.println("--------------------");
        System.out.println("Address Information:");
        System.out.println("--------------------");
        System.out.println();

        streetNumber = createStreetNumber();
        street = createStreet();
        //city = createCity();
        zip = createZip();
        state = createState();

        // need to return the zip information on its own so that it can be added to the zip database in the
        // main without having to ask the user twice for this information
        String [] addressInfo =  {"INSERT INTO VoterAddress (ID, streetNumber, street, zip)" +
                " VALUES (" + id + ", " + streetNumber + ", '" + street + "', '" +
                zip + "');", zip, state};
        return addressInfo;
    }


    public String deleteAddress(int id)
    {
        return "DELETE FROM VoterAddress WHERE ID = " + id + ";";
    }

    public String[] updateAddress(int id)
    {
        System.out.println("Enter new address.");

        // get new address
        streetNumber = createStreetNumber();
        street = createStreet();
        //city = createCity();
        zip = createZip();
        state = createState();

        // have to return an array of sql statements so that it can update every field in VoterAddress
        String []sql = {"UPDATE VoterAddress SET streetNumber = " + streetNumber + " WHERE ID = " + id + ";",
                "UPDATE VoterAddress SET street = '" + street + "' WHERE ID = " + id + ";",
                "UPDATE VoterAddress SET zip = '" + zip + "' WHERE ID = " + id + ";"};
        return sql;
    }

    public String createZipCodeInfo(int zip, String state)
    {
        return "INSERT INTO ZipCodeInfo(zip, state) VALUES(" + zip + ", '" + state + "');";
    }

    public String checkIfZipAlreadyInDB(int zip)
    {
        return "SELECT zip FROM ZipCodeInfo;";
    }


    /*
    ***********************************************
    INTERNAL FUNCTIONS
    GET USER INPUT TO CREATE NEW DATABASE RECORDS
    ***********************************************
    */

    // gets users street number
    public int createStreetNumber()
    {
        System.out.println("Street number: ");

        // gets number as String input and converts it to an int
        int num = Integer.parseInt(keyboard.nextLine());

        return num;
    }

    public String createStreet()
    {
        String street, suffix, streetName;

        while(true) {
            System.out.println("Street name (without suffix, i.e. 'Ave'): ");
            street = keyboard.nextLine();
            System.out.println("Suffix: ");
            suffix = keyboard.nextLine();

            streetName = street + " " + suffix;

            // must be 30 characters
            if (streetName.length() > 30) {
                System.out.println("Street too long. Must be under 30 characters.");
                System.out.println("Try again.");
            } else break;
        }
        return streetName;
    }

    /*
    public String createCity()
    {
        String c;
        int numWords;
        while (true)
        {
            // if city has multiple words, have to make sure to get user input after the space
            System.out.println("How many words in city (1 or 2)? ");
            numWords = Integer.parseInt(keyboard.nextLine());

            System.out.println();
            System.out.println("City: ");

            // normal keyboard input
            if (numWords == 1)
            {
                c = keyboard.nextLine();
            }

            // have to get word after space
            else if (numWords == 2)
            {
                c = keyboard.next();
                keyboard.skip(" ");
                c+= " ";
                c+= keyboard.next();
                keyboard.nextLine();
            }
            // not a valid response
            else
            {
                System.out.println("Not a valid option. Try again.");
                continue;
            }

            // city name is too many characters
            if (c.length() > 30)
            {
                System.out.println("City name must be 30 characters. Try again.");
                continue;
            }
            else break;
        }
        return c;
    }
*/

    public String createZip()
    {
        String zipC;

        while (true)
        {
            System.out.println("Zip code: ");
            zipC = keyboard.nextLine();

            // not a valid zip code because not 5 digits
            if (zipC.length() > 5 || zipC.length() < 5)
            {
                System.out.println("Not a valid zip code. Must be 5 digits.");
                System.out.println("Try again.");
                continue;
            }
            else break;
        }
        return zipC;
    }

    public String createState()
    {
        String s;
        int index = 0;
        boolean isValid = false;

        while(true)
        {

            System.out.println("State (Abbreviated state code, i.e. 'CA'): ");
            s = keyboard.nextLine().toUpperCase();

            // state codes are always 2 letters
            if (s.length() > 2)
            {
                System.out.println("Not a valid option. Try again.");
                continue;
            }

            // make sure state entered is a real option from states array
            while (index < 50)
            {
                // state inputed is a real state, exit while loop
                if (s.equals(states[index]))
                {
                    isValid = true;
                    break;
                }
                ++index;
            }

            // isValid is false when the inputted state doesn't exist. have to go back to beginning of while loop
            if (isValid == false)
            {
                System.out.println("Not a real state. Try again.");
                continue;
            }
            // state name is not valid
            if (s.length() > 2)
            {
                System.out.println("State code must be two characters. Try again.");
                continue;
            }
            else break;
        }
        return s;
    }



}
