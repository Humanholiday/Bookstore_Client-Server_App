import java.sql.SQLException;

public class Utils
{
    // method to return a formatted sql exception message

    public static String SQLExceptionMSg (SQLException e)
    {
        // create a string builder for the error message to be sent to the client
        StringBuilder errorMessage = new StringBuilder();

        // add error message details to the stringbuilder
        errorMessage.append("An error occurred, details - " +
                "\nSQLState: " + ((SQLException) e).getSQLState() +
                "\nError Code: " + ((SQLException) e).getErrorCode() +
                "\nMessage: " + e.getMessage());

        // return the error message as a string
        return errorMessage.toString();
    }
}
