import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void  main(String[] args) throws  IOException
    {
        //check that there are 2 program arguments only
        if (args.length != 2) {
            System.err.println("Pass the server IP (should be localhost) as the first argument, " +
                    "and the port for the server as the second (should be 54321)");
            return;
        }

        // assign program arguments to variables
        String ipAddress = args[0];
        Integer port = Integer.valueOf(args[1]);

        //define and print running message
        String runningMessage = "Client is attempting to connect to " + ipAddress + " on port " + port;
        System.out.println(runningMessage);

        //create a client Socket object on the same port
        Socket socket = new Socket(ipAddress, port);

        //create scanner object for user input
        Scanner scanner = new Scanner(System.in);

        // create scanner object for incoming server input
        Scanner in = new Scanner(socket.getInputStream());

        // allows data to be sent to the server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // client expects to receive server response when connection is made
        String mainMenu = in.nextLine();

        //Output server returned main menu to terminal if "connected" string is received from server
        if(mainMenu.equals("connected"))
        {
            // Read and print multiple lines from server, when line = end then break out of loop
            while ((mainMenu = in.nextLine()) != null)
            {
                if (mainMenu.equals("~~/END/~~"))
                {
                    break;
                }
                else
                {
                    System.out.println(mainMenu);
                }
            }
        }
        //If auto message is not received from server it is not available so close the socket
        else
        {
            System.out.println("Server not available");
            socket.close();
        }

        //while user terminal input is received do this
        while (scanner.hasNext())
        {
            //save user input as a string variable, trim whitespace
            String userData = scanner.nextLine().trim();

            //send the user inputted value to the server
            out.println(userData);

            // if the user types quit then break out of the loop, exit try statement and close the socket
            if (userData.equals("quit")) {
                System.out.println("Closing client socket.....");
                break;
            }

            String line;
            // Read and print multiple lines from server, when line = end then break out of loop
            while ((line = in.nextLine()) != null)
            {
                if (line.equals("~~/END/~~"))
                {
                    break;
                }
                else
                {
                    System.out.println(line);
                }
            }
        }
    }
}
