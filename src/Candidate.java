import java.util.*;

//class returns sql statements
public class Candidate extends Person
{
    Scanner keyboard = new Scanner(System.in);
    int candidateID,numberVotes;
    String description,fName, lName, sql;

    // creates a new Candidate record and gathers user input, then sends SQL query to DB manager to execute
//    public String createCandidate(int id)
//    {
////        System.out.println("--------------------");
////        System.out.println("Candidate Information:");
////        System.out.println("--------------------");
////        System.out.println();
//
//        candidateID++;
//        //description = createDescription();
//
//        String sql = "INSERT INTO Candidate (ID, candidateID)" +
//                "VALUES (" + id + ", '" +  candidateID + ");";
//        return sql;
//    }
//
//    public String populateCandidateInfo(int candidateID)
//    {
//        //set number of votes initially to 0, then update once voters vote
//        numberVotes = 0;
//        String sql = "INSERT INTO CandidateInfo (candidateID, description, numberVotes)" +
//                "VALUES (" + candidateID + ",'" + description + ",'" + numberVotes + ");";
//
//        return sql;
//    }
//
//    public String populateCandidateNames(int candidateID)
//    {
//
//        String sql = "INSERT INTO CandidateNames (candidateID, firstName, lastName)" +
//                "VALUES (" + candidateID + ",'" + getCandidateFirstName(candidateID) + ",'" + getLastName() + ");";
//
//        return sql;
//    }
//
//    /*
//    ***************************
//    HELPER METHODS
//    ***************************
//    */
//
//    //gets user input to create candidate description and validates max characters
//    public void createDescription()
//    {
//        String desc;
//        while(true)
//        {
//            System.out.println("Please enter a brief description of your values and beliefs (MAX 40 char).");
//            desc = keyboard.next();
//            desc += keyboard.nextLine();
//
//            if(desc.length()<=40)
//            {
//                System.out.println("Thank you! You are now a registered candidate.");
//                break;
//            }
//            else
//            {
//                System.out.println("Please shorten your description to 40 characters or less.");
//            }
//        }
//    }


    public String deleteCandidate(int candidateID)
    {
        String deleteCand = "DELETE FROM Candidate c WHERE c.candidateID = " + String.valueOf(candidateID);
        return deleteCand;
    }

    public String deleteCandidateInfo(int candidateID)
    {
        String deleteCandInfo = "DELETE FROM CandidateInfo WHERE candidateID = " + candidateID + ";";
        return deleteCandInfo;
    }

    public String deleteCandNames(int candidateID)
    {
        String deleteCandNames = "DELETE FROM CandidateNames WHERE candidateID = " + candidateID + ";";
        return deleteCandNames;
    }

    public String printCandidateFromCID(int candidateID)
    {
        return "SELECT firstName, lastName FROM CandidateNames WHERE candidateID = " + candidateID;
    }

    public String printCandidateInfo()
    {
        return "SELECT n.firstName, n.lastName, i.description FROM CandidateNames n, CandidateInfo i";
    }


    public String updateDescription(int candidateID)
    {
        while(true)
        {
            System.out.println("Please enter a new description (Limit 100 char): ");
            description = keyboard.nextLine();
            if(description.length()<=100)
            {
                sql = "UPDATE CandidateInfo SET description = '" + description + "' WHERE candidateID = " + candidateID + ";";
                break;
            }

            else
            {
                System.out.println("Please shorten your description to 100 characters or less.");
            }
        }

        return sql;
    }



}
