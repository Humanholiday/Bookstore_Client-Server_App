import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server
{

    /* SERVER CLIENT COMMUNICATION METHOD*/
    /* RECEIVES AND SENDS DATA TO CLIENT */

    public static void run(Integer port) throws IOException {

        String runningMessage = "The server is running on port " + port;

        // try with resources so it closes after try statement is completed and does not require finally() statement
        try (ServerSocket listener = new ServerSocket(port)) //create a ServerSocket object
        {
            System.out.println(runningMessage); //print running message

            while (true)  // while loop keeps server process running
            {
                try(Socket socket = listener.accept())
                {
                    // prints to server terminal when connection is made
                    System.out.println("Client connected.");

                    // receive and print a message from the client
                    Scanner in = new Scanner(socket.getInputStream());

                    // OutputStream sends data to client, PrintWriter enables text to be sent
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    //send string to client to confirm connection
                    out.println("connected");

                    //while input is received from the client do this
                    while(in.hasNext())
                    {
                        // save client data as a String variable
                        String clientData = in.nextLine().trim();

                        // pass clientData to the HandleResponse execute method and save returned value
                        String response = HandleResponse.execute(clientData);

                        // add characters to end of response so client reader loop is restarted
                        String serverResponse = response + "\n~~/END/~~";

                        //send serverResponse to client
                        out.println(serverResponse);

                        // HandleResponse.closeConnection();
                    }
                }
                catch(Exception e)
                {
                    System.out.println("Server exception: " + e.getMessage());
                }
                // when a client connection is ended do this
                finally
                {
                    try
                    {
                        System.out.println("The client has disconnected");
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error- " + e);
                    }
                }
            }
        }
    }
}