
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.in;
import static java.lang.System.out;

public class Client implements Socket_Interface {
    public static Socket socket;				//socket for client
    //public static ServerSocket socket;			//socket for server
    public Client(){
        //client = null;				//socket for client
        socket = null;			//socket for server
    }


    @Override
    public void connection(int port) {
        try {
            socket = new Socket("localhost", port);
            ClientFunction thr = new ClientFunction(socket);		//listen on socket for any incoming msg from server
            thr.start();
        }
        catch (Exception e)
        {
            out.println(e.getMessage());
        }
    }

    @Override
    public void close_connection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void communicate() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String inp;
            try {
                while ((inp = stdIn.readLine()) != null) {        //read data from standard input and send to server
                    if (inp.equals("Close")) {
                        close_connection();
                    } else {
                        out.println(inp);                            //output data received from server
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        catch(Exception e)
        {
            out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        //default host and port

        int port = 1222;

        if (args.length != 2) {
            System.err.println("Considering localhost and Port 1222");
            // System.exit(1);
        } else {        //input host and port as command line arguments
            port = Integer.parseInt(args[0]);
        }
        Client client = new Client();
        client.connection(port);
        client.communicate();
    }
}
