import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception
    {

        Scanner keyboard = new Scanner(System.in);

        // login to MySQL Workbench
        System.out.println("username: ");
        String user = keyboard.nextLine();
        System.out.println("password: ");
        String password  = keyboard.nextLine();

        // create new object to that communicates between database and program
        DatabaseManager manager = new DatabaseManager();

        // generate data into a csv file
        //DataGenerator df = new DataGenerator();
        //df.createData(20);

        // id counts how many people have been added, used for sql queries (easier than having to do query for id of person)
        int id = 1, numCandidates=0;
        String description;


        // menu
        System.out.println("Hello! Welcome to Chappy eVote!");
        System.out.println("Before anything, the candidates must be registered.");

        /*
        // create candidates
        while(true)
        {
            System.out.println("What is your ID number? ");
            id =  Integer.parseInt(keyboard.nextLine());

            // fetch first nme and last name;

            //System.out.println("Welcome " + firstName + lastName);

            System.out.println("Please provide a desription to your voters about your beliefs. (40 characters)");
            // check amount entered isnt over limit
            description = keyboard.next();

            System.out.println("Thank you! You are now a registered candidate.");
            // add them to database

            numCandidates++;
            break;
        }
*/
        boolean continueRunning = true, anotherTask;
        int choice;

        while(continueRunning)
        {
            System.out.println("Select an option");
            System.out.println("1. Register to vote.");
            System.out.println("2. Vote for a candidate.");
            System.out.println("3. Print information.");

            choice = Integer.parseInt(keyboard.nextLine());

            switch(choice)
            {
                // register to vote
                // when creating new person, have to get general info, contact info, and address
                case 1:
                    manager.createPerson(user, password);
                    // counting number of voters created will also be id number of current
                    // voter because ID numbers start at zero
                    id++;
                    manager.createVoterContactInfo(user, password, id);
                    // add to database
                    break;
                case 2:
                    // ask for id
                    // vote using candidate id
                    // add to database
                    break;
                case 3:
                    break;
            }
            // ask user if they want to keep using the database
            continueRunning = completeAnotherTask(keyboard);
        }
    }

    // function to ask user if they want to keep using the database
    public static boolean completeAnotherTask(Scanner keyboard)
    {
        String anotherTask;
        boolean continueRunning;

        // ask user if they want to end the program or complete another task in the database
        while(true) {
            System.out.println("Is there anything else you'd like to do? (Y/N)");
            anotherTask = keyboard.nextLine();

            // complete another task
            if (anotherTask.toUpperCase().equals("Y")) {
                continueRunning = true;
                break;
            }
            // end program
            else if (anotherTask.toUpperCase().equals("N")) {
                System.out.println("Thank you for participating! See you next time.");
                continueRunning = false;
                break;
            }

            // not a valid option
            else {
                System.out.println("Not a valid option. Must choose Y or N");
                System.out.println("Try again.");
            }
        }
        return continueRunning;
    }

}
