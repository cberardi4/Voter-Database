import java.util.Scanner;

/*
   This class gets user information on their personal information.
   It sanitizes the input and puts it into an SQL statement to send to DB Manager
*/
public class Person
{
    String firstName, lastName, gender;
    int age, candidateID, partyID;

    Scanner keyboard = new Scanner(System.in);

    // creates a new Person record and gathers user input, then sends SQL query to DB manager to execute
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
                " VALUES ('" + firstName + "', '" + lastName + "', " +  age + ", '" + gender +
                "', " + candidateID + ", " + partyID + ");";
        return sql;
    }



    public String printVoterInformation()
    {
        return "SELECT firstName, lastName, age, gender, partyID FROM Person;";
    }

    // returns the number of registered voters in inputted party
    public String[] numberRegisteredVotersInParty() {

        System.out.println("Which party would you like to select? ");
        int choice = createPartyID();

        // need to also return partyID so that you can query name for print statement
        String[] sql = {"SELECT COUNT(*) AS count FROM Person WHERE partyID = " + choice + ";", Integer.toString(choice)};
        return sql;
    }

    public String printPersonFromID(int id)
    {
        return "SELECT firstName, lastName FROM Person WHERE ID = " + id;
    }


    public String deletePerson(int id)
    {
        return "DELETE FROM Person WHERE ID = " + id + ";";
    }


    public String updateName(int id)
    {
        System.out.println("Update first (1) or last (2) name: ");
        int choice = Integer.parseInt(keyboard.nextLine());
        String sql="";

        // variable to exit while loop
        boolean cont = true;

        while(cont) {
            switch (choice) {
                // change first name
                case 1:
                    System.out.println("New first name: ");
                    String fName = keyboard.nextLine();
                    sql = "UPDATE Person SET firstName = '" + fName + "' WHERE ID = " + id + ";";

                    // exit while loop
                    cont = false;
                    break;

                // change last name
                case 2:
                    System.out.println("New last name: ");
                    String lName = keyboard.nextLine();
                    sql = "UPDATE Person SET lastName = '" + lName + "' WHERE ID = " + id + ";";

                    // exit while loop
                    cont = false;
                    break;

                // didn't enter a valid option
                default:
                    System.out.println("Not a valid option.");
            }
        }

        return sql;

    }

    public String updateAge(int id)
    {
        System.out.println("Enter new age: ");
        age = createAge();

        return "UPDATE Person SET age = " + age + " WHERE ID = " + id + ";";
    }


    public String updatePoliticalParty(int id)
    {
        System.out.println("Enter new political party: ");
        partyID = createPartyID();

        return "UPDATE Person SET partyID = " + partyID + " WHERE ID = " + id + ";";
    }

    public String getPartyName(int id)
    {
        return "SELECT partyName FROM Party WHERE partyID = "+id+";";
    }

    public String updateVote(int id, int candidateID)
    {
        return "UPDATE Person SET candidateID = "+candidateID+" WHERE ID = " + id + ";";
    }



    /*
    ***********************************************
    INTERNAL FUNCTIONS
    GET USER INPUT TO CREATE NEW DATABASE RECORDS
    ***********************************************
    */

    // NEED TO FIX THIS FUNCTION


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

            // must skip next line after entering an int for the next user input
            keyboard.nextLine();

            // chose a valid party option
            if (partyID >= 1 && partyID <= 5)
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

    /*
    ***************************
    GETTER AND SETTER FUNCTIONS
    ***************************
     */

    String getFirstName ()
    {
        return firstName;
    }
    String getLastName ()
    {
        return  lastName;
    }

}
