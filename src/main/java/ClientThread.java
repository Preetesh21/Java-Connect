import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

class ClientThread extends Thread implements ClientJobs{
    private Socket client = null;		//Socket
    private int clientNo = 0;			//Client ID
    public BufferedReader inp = null;	//Input to Client
    public PrintWriter outp = null;		//Output to Client

    public ClientThread(Socket socket, int n) throws IOException {
        clientNo = n;
        client = socket;
        inp = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outp = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            System.out.printf("Client %d Connected\n", clientNo);

            String input;
            String[] words;
            while ((input = inp.readLine()) != null) {

                words = input.split(" ");

                if (words[0].equals("Client")) {			//Msg of type -> Client X,Y: Msg
                    try {
                        String clients = words[1].substring(0, words[1].indexOf(":"));		//Find clients
                        String[] clientsNo = clients.split(",");
                        for (String i : clientsNo) {
                            int no = Integer.parseInt(i);
                            if (Server.client_map.containsKey(no)) {
                                PrintWriter targetOut = Server.client_map.get(no).outp;
                                targetOut.printf("Received from Client %d: %s\n", clientNo, input.substring(input.indexOf(":")+2, input.length()));
                            }
                            else {
                                outp.printf("Server: Client %d does not exist\n", no);
                            }
                        }
                    }
                    catch (Exception e) {
                        outp.println("Server: Invalid Input...Correct Format -> Client X,Y,Z: Msg");
                    }

                }

                else if (words[0].contains("All")) {		//Send msg to all the acitve clients
                    PrintWriter targetOut;
                    Set<Map.Entry<Integer, ClientThread> > values = Server.client_map.entrySet();

                    for (Map.Entry<Integer, ClientThread> me : values) {
                        targetOut = me.getValue().outp;
                        targetOut.printf("Received from Client %d: %s\n", clientNo, input.substring(input.indexOf(":")+2, input.length()));
                    }
                }

                else if (words[0].contains("Server") && words[1].equals("List") && words[2].equals("All")) {		//Ask server to list the active clients
                    Set <Map.Entry<Integer, ClientThread> > values = Server.client_map.entrySet();

                    String clients = "";
                    for (Map.Entry<Integer, ClientThread> me : values) {
                        clients += me.getKey() + ", ";
                    }
                    outp.println("Server: " + clients.substring(0, clients.length()-2));		//print comma separated list of active clients
                }

                else {
                    outp.println("Server: Invalid Input");
                }
            }

            inp.close();
            outp.close();
            System.out.printf("Client %d Disconnected\n", clientNo);
            client.close();
            Server.client_map.remove(clientNo);			//remove the client once the connection is closed
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}