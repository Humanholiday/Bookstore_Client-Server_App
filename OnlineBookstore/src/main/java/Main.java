import java.io.IOException;

public class Main
{
    //starting point for application
    public static void main(String[] args) throws IOException
    {
        //call the server run method and pass the port number program argument
        Server.run(Integer.valueOf(args[0]));
    }
}
