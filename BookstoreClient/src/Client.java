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
        if (args.length != 2) {
            System.err.println("Pass the server IP (should be localhost) as the first argument, and the port for the server as the second (should be 54321)");
            return;
        }

        String ipAddress = args[0];
        Integer port = Integer.valueOf(args[1]);

        // try with resources so it closes after try statement is completed and does not require finally() statement
        try (Socket socket = new Socket(ipAddress, port);) //create a client Socket object on the same port
        {
            //create scanner object for user input
            Scanner scanner = new Scanner(System.in);

            //create buffered reader for complex server string response
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // allows data to be sent to the server
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // server sends automatic response when client connects
            String checkConnect = in.readLine();

            //Output message to terminal if "connected" string is received from server
            if(checkConnect.equals("connected"))
            {
                System.out.println(checkConnect);
                System.out.println("Enter lines of text or type 'quit' to exit \n" +
                        "Command format - 'table action value(s)'\n" +
                        "Example - 'book search dune' or 'book add *values*'"
                );
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
                while ((line = in.readLine()) != null)
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
}
