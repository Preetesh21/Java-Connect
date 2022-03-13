import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientFunction extends Thread implements ClientJobs {			//listen for any incoming msgs from server
    private Socket socket = null;

    public ClientFunction(Socket sk) {
        socket = sk;
    }

    public void run() {
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String inp;
            while ((inp = in.readLine()) != null) {
                System.out.println(inp);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}