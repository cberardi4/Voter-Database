public class ZipCodeInfo
{
    public String createZipCodeInfo(int zip, String state)
    {
        return "INSERT INTO ZipCodeInfo(zip, state) VALUES(" + zip + ", '" + state + "');";
    }

    public String checkIfZipAlreadyInDB(int zip)
    {
        return "SELECT zip FROM ZipCodeInfo;";
    }

    /*
    ****************************
    INTERNAL FUNCTIONS
    ****************************
    */

}
