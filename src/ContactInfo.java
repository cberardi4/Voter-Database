import java.util.Scanner;

public class ContactInfo
{
    String emailAddress, cellPhone, homePhone;

    Scanner keyboard = new Scanner(System.in);

    // creates new record in contactInfo table for the same person and sends query to DM manager to execute
    public String createVoterContactInfo(int id)
    {
        System.out.println("--------------------");
        System.out.println("Contact Information:");
        System.out.println("--------------------");
        System.out.println();

        homePhone = createPhoneNumber("home");
        cellPhone = createPhoneNumber("cell");
        emailAddress = createEmailAddress();

        return "INSERT INTO VoterContactInfo (ID, email, homePhone, cellPhone) " +
                "VALUES (" + id + ", '" + emailAddress + "', '" + homePhone + "', '" + cellPhone + "');";
    }

    public String deleteContactInfo(int id)
    {
        return "DELETE FROM VoterContactInfo WHERE ID = " + id + ";";
    }


    public String[] updateContactInfo(int id)
    {
        System.out.println("Print new update information: ");

        homePhone = createPhoneNumber("home");
        cellPhone = createPhoneNumber("cell");
        emailAddress = createEmailAddress();

        // have to return an array of sql statements so that it can update every field in VoterContactInfo
        String []sql = {"UPDATE VoterContactInfo SET email = '" + emailAddress + "' WHERE ID = " + id + ";",
                "UPDATE VoterContactInfo SET homePhone = '" + homePhone + "' WHERE ID = " + id + ";",
                "UPDATE VoterContactInfo SET cellPhone = '" + cellPhone + "' WHERE ID = " + id + ";"};
        return sql;
    }



    /*
    ***********************************************
    INTERNAL FUNCTIONS
    GET USER INPUT TO CREATE NEW DATABASE RECORDS
    ***********************************************
    */


    public String createPhoneNumber(String type)
    {

        String number;

        while(true)
        {
            // ask user for phone number (either cell or home, type will tell you
            System.out.println("Enter your " + type + " phone number. Must have 10 digits.");
            number = keyboard.nextLine();

            // not a valid phone number, didn't type correct amount of digits
            if (number.length() > 10 || number.length() < 10)
            {
                System.out.println("Not a valid phone number. Must have 10 digits.");
                System.out.println("Try again.");
                continue;
            }
            // valid number, leave while loop
            else break;
        }
        return number;
    }

    // gets user's email address and validates that they actually entered an email address
    public String createEmailAddress()
    {
        String email;

        while(true)
        {
            System.out.println("Enter your email address.");
            email = keyboard.nextLine();

            // not a valid email address because doesn't have a domain name
            if (!email.contains("@"))
            {
                System.out.println("Not a valid email address. Must have a domain name.");
                System.out.println("Try again.");
                continue;
            }
            // make sure that length isn't over 45 characters (what's allocated in MySQL Workbench
            if (email.length() > 45)
            {
                System.out.println("Must be under 45 characters. Try again.");
                continue;
            }
            // if a valid email, leave while loop
            else break;
        }
        return email;
    }

}
