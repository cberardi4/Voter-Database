import java.util.Scanner;

// This class creates a new person object with all necessary attributes

public class Person
{
    String firstName, lastName, gender;
    int age, candidateID, partyID;

    String street, city, address, state;
    int  streetNumber, zip;

    String emailAddress, cellPhone, homePhone;

    Scanner keyboard = new Scanner(System.in);

    // creates a new record and gathers user input
    public String createPerson()
    {
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
        return " ";
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
}
