import java.util.Scanner;

// This class creates a new person object with all necessary attributes

public class Person
{
    String firstName, lastName, gender;
    int age, candidateID, partyID;

    String street, city, state, zip;
    int  streetNumber;

    String emailAddress, cellPhone, homePhone;

    Scanner keyboard = new Scanner(System.in);

    // creates a new record and gathers user input
    public String createPerson()
    {
        System.out.println("--------------------");
        System.out.println("Personal Information:");
        System.out.println("--------------------");
        System.out.println();

        firstName = createFirstName();
        lastName = createLastName();
        age = createAge();
        gender = createGender();
        // voter hasn't voted yet, so sets candidate ID they voted for to 0 and change once they do vote
        candidateID = 0;
        partyID = createPartyID();

        String sql = "INSERT INTO Person (firstName, lastName, age, gender, candidateID, partyID)" +
                        "VALUES ('" +  firstName + "', '" + lastName + "', " +  age + ", '" + gender +
                        "', " + candidateID + ", " + partyID + ");";
        return sql;
    }

    public String createVoterContactInfo(int id)
    {
        System.out.println("--------------------");
        System.out.println("Contact Information:");
        System.out.println("--------------------");
        System.out.println();

        homePhone = createPhoneNumber("home");
        cellPhone = createPhoneNumber("cell");
        emailAddress = createEmailAddress();

        return "INSERT INTO VoterContactInfo (ID, email, homePhone, cellPhone)" +
                "VALUES (" + id + ", '" + emailAddress + "', '" + homePhone + "', '" + cellPhone + "');";
    }

    public String[] createVoterAddress(int id)
    {
        System.out.println("--------------------");
        System.out.println("Address Information:");
        System.out.println("--------------------");
        System.out.println();

        streetNumber = createStreetNumber();
        street = createStreet();
        city = createCity();
        zip = createZip();

        // need to return the zip information on its own so that it can be added to the zip database in the
        // main without having to ask the user twice for this information
        String [] addressInfo =  {"INSERT INTO VoterAddress (ID, streetNumner, street, city, zip)" +
                "VALUES (" + id + ", " + streetNumber + ", '" + street + "', '" +
                city + ", '" + zip + "');", zip};
        return addressInfo;
    }

    public String createVoterZip(String zipC)
    {
        state = createState();

        return "INSERT INTO ZipCodeInfo (zip, state)" + "VALUES ('" + zipC + "', '" + state + "');";
    }

    /*
    ***********************************************
    INTERNAL FUNCTIONS
    GET USER INPUT TO CREATE NEW DATABASE RECORDS
    ***********************************************
    */

    // gets user input to create first name and validates input
    public String createFirstName()
    {
        String fName;
        while(true)
        {
            System.out.println("Enter first name: ");
            fName = keyboard.nextLine();
            if (fName.length() <= 20)
            {
                break;
            }
            else
            {
                System.out.println("First name over character limit--must be 20 or less");
                System.out.println("Enter shorter name.");
            }
        }
        return fName;
    }

    // gets user input to create last name and validates input
    public String createLastName()
    {
        String lName;
        while(true)
        {
            System.out.println("Enter last name: ");
            lName = keyboard.nextLine();
            if (lName.length() <= 20)
            {
                break;
            }
            else
            {
                System.out.print("Last name over character limit--must be 20 or less.");
                System.out.println("Enter shorter name.");
            }
        }
        return lName;
    }

    // gets users age and validates input
    public int createAge()
    {
        int age;
        while(true)
        {
            System.out.println("Enter age ");
            age = keyboard.nextInt();
            keyboard.nextLine();

            // voters must be over the age of 18 and ages over 110 are unrealistic
            if (age >= 18 || age < 110)
            {
                break;
            }
            // voter too young to register
            else if (age < 17)
            {
                System.out.println("Not a valid age. Must be 18 or over.");
                System.out.println("Cannot register to vote. Exiting now.");
                System.exit(1);
                break;
            }
            // voter didn't enter a valid age
            else
            {
                System.out.println("Not a valid age. Age range is 18-110");
                System.out.println("Try again.");
            }
        }
        return age;
    }

    // gets users gender and validates input
    public String createGender()
    {
        String gender;
        while(true)
        {
            System.out.println("Enter gender (M or F): ");
            gender = keyboard.nextLine().toUpperCase();
            if (gender.equals("F") || gender.equals("M"))
            {
                break;
            }
            else
            {
                System.out.print("Not a valid option. Must be F or M");
            }
        }
        return gender;
    }

    // gets ID of candidate user wants to vote for and validates input
    public int createPartyID()
    {
        int partyID;
        while(true)
        {

            System.out.println("Enter ID of your political party. Options:");
            System.out.println("1: Republican");
            System.out.println("2: Democrat");
            System.out.println("3: Independent");
            System.out.println("4: Libertarian");
            System.out.println("5: Green Party");
            partyID = keyboard.nextInt();

            // chose a valid party option
            if (partyID >= 1|| partyID <= 5)
            {
                break;
            }
            else
            {
                System.out.println("Entered an invalid party option. Must be between 1 and 5");
                System.out.println("Try again.");
            }
        }
        return partyID;
    }

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
                c+= keyboard.next();
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
        int numWords;

        while(true)
        {
            // if state has multiple words, have to make sure to get user input after the space
            System.out.println("How many words in state (1 or 2)? ");
            numWords = Integer.parseInt(keyboard.nextLine());

            System.out.println();
            System.out.println("State (Abbreviated state code, i.e. 'CA'): ");

            // normal keyboard input
            if (numWords == 1)
            {
                s = keyboard.nextLine();
            }

            // have to get word after space
            else if (numWords == 2)
            {
                s = keyboard.next();
                keyboard.skip(" ");
                s+= keyboard.next();
            }
            // not a valid response
            else
            {
                System.out.println("Not a valid option. Try again.");
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
