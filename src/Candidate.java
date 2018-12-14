import java.util.*;

//class returns sql statements
public class Candidate extends Person
{
    Scanner keyboard = new Scanner(System.in);
    int candidateID,numberVotes;
    String description,fName, lName;


    public String selectAllCandidateNames()
    {
        return "SELECT c.firstName, c.lastName, i.description FROM CandidateNames c, CandidateInfo i where c.candidateID = i.candidateID;";
    }

    public String getCandidateNameFromID(int candidateID)
    {
        return "SELECT firstName, lastName FROM CandidateNames WHERE candidateID = "+candidateID+";";
    }


    public String getCandidateName(int candidateID)
    {
        String fName = "SELECT p.firstName, p.lastName FROM Person p, Candidate c WHERE c.ID = p.ID";
        return fName;

        //need result set

    }

    public String updateDescription(int candidateID)
    {
        String sql;
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

    public String getNumberVotesForCandidate(int candidateID)
    {
        return "SELECT numberVotes FROM CandidateInfo WHERE candidateID = "+candidateID+";";
    }



    public String addVote(int candidateID, int newNumberVotes)
    {
        newNumberVotes++;
        return "UPDATE CandidateInfo SET numberVotes = " + newNumberVotes + " WHERE candidateID = " + candidateID+";";
    }

    public String removeVote(int candidateID, int newNumberVotes)
    {
        newNumberVotes--;
        return "UPDATE CandidateInfo SET numberVotes = " + newNumberVotes + " WHERE candidateID = " + candidateID+";";
    }

    public String printCandidateInformation()
    {
        return "SELECT firstName, lastName FROM CandidateNames;";
    }



}
