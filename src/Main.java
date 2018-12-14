import java.util.Scanner;

public class Main {

    // create new object to that communicates between database and program
    static DatabaseManager manager = new DatabaseManager();

    static Scanner keyboard = new Scanner(System.in);

    public static void main(String[] args) throws Exception
    {
        String user, password;

        // login to MySQL Workbench
        System.out.println("username: ");
        user = keyboard.nextLine();
        System.out.println("password: ");
        password  = keyboard.nextLine();

        //Candidate cand = new Candidate();

        // generate data into a csv file
        //DataGenerator df = new DataGenerator();
        //df.createData(20);

        // id counts how many people have been added, used for sql queries (easier than having to do query for id of person)
        int numCandidates=0;
        String description, zip;
//        String[] candidateName;
//        String candFirstName, candLastName;

        // menu
        System.out.println("Hello! Welcome to Chappy eVote!");

//        // create candidates, max 3 candidates
//        while(numCandidates < 2)
//        {
//            System.out.println("First you must register as a voter, then we can move on to candidate registration");
//            manager.createPerson(user, password);
//
//            //asks for description, checks max char count
//            cand.createDescription();
//            //adds candidate to all candidate tables
//            //manager.createCandidate(user, password) //need description?
//            numCandidates++;
//
//        }


        boolean continueRunning = true;
        int choice, c;

        while(continueRunning)
        {
            System.out.println("Select an option: ");
            System.out.println("1. Register to vote."); //6a
            System.out.println("2. Vote for a candidate."); //6b
            System.out.println("3. Print information.");
            System.out.println("4. Generate Reports");
            System.out.println("5. Delete a voter");
            System.out.println("6. Withdraw candidate");
            System.out.println("7. Update information (candidate or voter)");
            System.out.println("8. Exit");

            choice = Integer.parseInt(keyboard.nextLine());

            switch(choice)
            {
                // register to vote
                // when creating new person, have to get general info, contact info, and address
                case 1:
                    // counting number of voters created will also be id number of current
                    // voter because ID numbers start at zero
                    // create a new person record
                    manager.createPerson(user, password);
                    break;

                case 2:
                    vote(user, password);
                    break;
                case 3:
                    c = printOptions();
                    executePrintOption(c, user, password);
                    break;
                case 4:
                    printReports(user, password);
                    break;
                case 5:
                    deletePerson(user, password);
                    break;
                case 6:
                    deleteCandidate(user, password);
                    break;
                case 7:
                    updateInformation(user, password);
                    break;
                case 8:
                    System.out.println("Thank you! See you next time.");
                    System.exit(0);
                default:
                    System.out.println("Not a valid option. Must be between 1 and 7");
                    continue;

            }
            // ask user if they want to keep using the database
            continueRunning = completeAnotherTask();
        }

        manager.closeFile();
    }

    /*
    *********************
    HELPER FUNCTIONS FOR THE MAIN
    *********************
     */


//    public static void registerCandidate(String user, String password) throws Exception
//    {
//        //create record in all 3 candidate tables
//        //manager.createCandidate(user, password, id);
//        //manager.createCandidateInfo(user, password, candidateID);
//        //manager.createCandidateNames(user, password, candidateID);
//
//    }

    // asks user what kind of report they want to print before interfacing with the database
    public static void printReports(String user, String password) throws Exception
    {
        int choice = 0;
        // how to break out of while loop
        boolean goAgain = true;

        while(goAgain) {
            System.out.println("What kind of report would you like to print?");
            System.out.println("1. Total Votes per candidate");
            System.out.println("2. Voter contact information");
            System.out.println("3. Candidate contact information");
            choice = Integer.parseInt(keyboard.nextLine());

            switch (choice)
            {
                case 1:
                    break;

                // voter contact info
                case 2:
                    manager.contactInfoReport(user, password);
                    goAgain = false;
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Not a valid option. Try again.");
            }
        }
    }



    // helper function for voting
    public static void vote(String user, String password) throws Exception {

        String[] candidates;
        int selection;

        System.out.println("What is your ID number? ");
        int id = Integer.parseInt(keyboard.nextLine());
        int count =1;

        while(true)
        {

            System.out.println("Options for voting: ");
            System.out.println("----------------------");

            // query to return all candidate names
            candidates = manager.printAllCandidates(user, password);

            // print all candidates to see their options
            for (int k = 0; k < 3; ++k) {
                System.out.println(count + ". " + candidates[k]);
                count++;
            }

            System.out.println("Number of candidate you want to vote for");
            selection = Integer.parseInt(keyboard.nextLine());

            if (selection > 3 || selection < 1)
            {
                System.out.println("Not a valid option. Try again.");
                continue;
            }
            else
            {
                manager.vote(user, password, id, selection);
                break;
            }

        }

    }

    // calls functions in DB manager class that deletes a
    // person from the Person table, VoterAddressTable, and VoterContactInfo table
    public static void deletePerson(String user, String password) throws Exception
    {
        String name;

        System.out.println("ID of the person you wish to delete: ");
        int id = Integer.parseInt(keyboard.nextLine());

        // print the name of the person that you're deleting
        // have to do it first because if you try to do after, their id won't exist
        name = manager.printNameFromID(user ,password, id);

        // returns an empty string when there's no query response, meaning that the ID didn't exist
        if (name.equals(""))
        {
            System.out.println("ID did not exist. Could not complete delete");
        }

        System.out.println(name + " has been deleted.");

        manager.deletePerson(user, password, id);
    }

    public static void deleteCandidate(String user, String password) throws Exception
    {
        System.out.println("CandidateID of the candidate you wish to delete: ");
        int candidateID = Integer.parseInt(keyboard.nextLine());

        // print the name of the person that you're deleting
        // have to do it first because if you try to do after, their id won't exist
        String name = manager.printCandidateNameFromCID(user, password, candidateID);

        // returns an empty string when there's no query response, meaning that the candidateID didn't exist
        if (name.equals(""))
        {
            System.out.println("candidateID did not exist. Could not complete delete");
        }

        System.out.println(name + " has been deleted.");

        manager.deleteCandidate(user, password, candidateID);


    }


    public static void updateInformation(String user, String password) throws Exception {
        System.out.println("What kind of information would you like to update?");
        System.out.println("1. Voter Information");
        System.out.println("2. Candidate Information");

        int choice = Integer.parseInt(keyboard.nextLine());
        // variable to break while loop
        boolean cont = true;

        System.out.println("ID of the person you want to change: ");
        int id = Integer.parseInt(keyboard.nextLine());


        while(cont)
        {
            switch (choice)
            {
                case 1:
                    updateVoterInfo(user, password, id);
                    cont = false;
                    break;
                case 2:
                    manager.updateCandidateInfo(user, password, id);
                    cont = false;
                    break;
                default:
                    System.out.println("Not a valid option. Select 1 or 2.");
            }
        }
    }
/*
    // needs to be implemented
    // update candidate information
    // helper function to updateInformation
    public static void updateCandidateInfo(String user, String password, int candidateID) throws Exception
    {
        //System.out.println("CandidateID of the candidate you want to change: ");
        //int candidateID = Integer.parseInt(keyboard.nextLine());
        manager.updateCandidate(user, password, candidateID);
    }
    */

    // update voter information
    // helper function to updateInformation
    public static void updateVoterInfo(String user, String password, int id) throws Exception {
        System.out.println("Options to change: ");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Address");
        System.out.println("4. Contact Information");
        System.out.println("5. Political Party");

        int choice = Integer.parseInt(keyboard.nextLine());
        boolean goAgain = true;

        while(goAgain) {
            switch (choice) {
                // change name
                case 1:
                    manager.updateName(user, password, id);
                    goAgain = false;
                    break;

                // change age
                case 2:
                    manager.updateAge(user, password, id);
                    goAgain = false;
                    break;

                // change address
                case 3:
                    manager.updateVoterAddress(user, password, id);
                    goAgain = false;
                    break;

                // change contact info
                case 4:
                    manager.updateVoterContactInfo(user, password, id);
                    goAgain = false;
                    break;

                // change political party
                case 5:
                    manager.updateParty(user, password, id);
                    goAgain = false;
                    break;

                // did not input valid option
                default:
                    System.out.println("Not a valid option. Must be between 1-5.");
            }
        }
    }

    // function to ask user if they want to keep using the database
    public static boolean completeAnotherTask()
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

    // outputs print options when user wants to print information
    public static int printOptions()
    {
        /*
        each output on task list on google doc:
        option 1 = task 1a
        option 2-3 = task 2a, 2b
        option 4 = 9 or 8?
         */

        int choice;

        while(true)
        {
            System.out.println("Select an Option:");
            System.out.println("1. Print all voters");
            System.out.println("2. Print all candidates");
            System.out.println("3. Print how many registered voters from selected party");

            choice = Integer.parseInt(keyboard.nextLine());

            if (choice < 1 || choice >5)
            {
                System.out.println("Not a valid option. Try again.");
                continue;
            }
            else break;
        }
        return choice;
    }

    // once user has decided that they want to print some information, it executes queries based on that choice
    public static void executePrintOption(int choice, String user, String password) throws Exception
    {
        switch (choice)
        {
            case 1:
                manager.selectAllVoters(user, password);
                break;
            case 2:
                manager.selectAllCandidates(user, password);
                break;
            case 3:
                manager.numberRegisteredVotersInParty(user, password);

        }
    }

    public static void updateCandidateInfo(String user, String password, int candidateID) throws Exception
    {
        manager.updateCandidateInfo(user, password, candidateID);
    }


}
