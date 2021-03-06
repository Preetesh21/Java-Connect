import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.net.*;
import static java.lang.System.out;

public class Server implements Socket_Interface {
    public static Socket client;				//socket for client
    public static ServerSocket socket;			//socket for server
    public static TreeMap<Integer, ClientThread> client_map;	//Hash Map to map client ID with client thread sorted on client ID
    int countClients;
    public Server(){
        client = null;				//socket for client
        socket = null;			//socket for server
        client_map = new TreeMap<>();
        countClients = 0;
    }

    @Override
    public void connection(int Port) {
        //default port
        //System.err.println("Considering Port " + Port);
        try {
            socket = new ServerSocket(Port);
        } catch(IOException e) {
            out.println(e.getMessage());
        }
    }

    @Override
    public void close_connection() {
    }


    @Override
    public void communicate() {
        while(true) {
            try {
                client = socket.accept();
                countClients++;
                ClientThread thr = new ClientThread(client, countClients);			//create new thread for every new client
                client_map.put(countClients, thr);
                thr.start();

            } catch (IOException e) {
                out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int port=1222;
        if (args.length != 1) {
            System.err.println("Considering Port 1222");
            // System.exit(1);
        }
        else {		//input port no as command line argument
            port = Integer.parseInt(args[0]);
        }
        Server server =new Server();
        server.connection(port);
        server.communicate();

    }
}


