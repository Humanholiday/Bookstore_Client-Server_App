import java.io.IOException;

public class Main
{

    //Class starts a simple server that runs indefinitely.
    //Can only handle one client at a time.
    //Has basic server side validation
    public static void main(String[] args) throws IOException
    {
//        Read.getAllBooks();

//        HandleResponse.execute("BOOK SEARCH of mice and men");

        System.out.println(Database.getDatabaseTableNames());

        Server.run(Integer.valueOf(args[0]));

    }


}
