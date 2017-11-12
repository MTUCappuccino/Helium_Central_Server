
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.HashMap;
import java.lang.String;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Central Server mediates between individual servers and clients 
 * @author Doogie Warner
 */
public class CentralServer implements Runnable {
    // Tracks if server open

    private boolean open = true;

    // Port to run on
    private int port;
    private int defaultPort = 9090; //default value for the port
    private boolean autoPort = false;
    private ServerSocket listener;
    private String serverName = "Central";  //name of the server
    private String password = "";
    private String hexColor = "000000";
    private String customBack = "NULL";

    private ServerSpec serverSpecific;  //server specific object

    private ArrayList<String> pubServers;  //list of public servers
    private HashMap<String, ServerSpec> hashRegister;  //hashmap to a server, input: code, output: server

    private boolean isPublic; //keeps track of which server wants to be public or private
    private boolean isClient; //keeps track if the communication is with client or not

    CentralServer(int portNum) {

        hashRegister = new HashMap<>();

        // Hardcoded, for now:
        hashRegister.put("ABCDE", new ServerSpec("Test Server 1", null, "localhost", "9090", true, true, true));
        hashRegister.put("A1BBB", new ServerSpec("Test Server 2", null, "141.219.201.62", "6060", true, true, true));
    }

    //opens the server
    public boolean openServer() {
        (new Thread(new CentralServer(port))).start();
        return true;
    }

    public void setPort(int num) {
        port = num;
    }

    public int getPort() {
        return port;
    }

    //close the server to prevent further access
    public boolean closeServer() {
        try {
            listener.close();
            open = false;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    //registers the server with the central server, stores a code that the client will match for access to the individual server
    private String registerServer(Socket s, ServerSpec spec) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String code = input.readLine();
        hashRegister.put(code, spec);
        return code;
    }

    //reads the client code and connects the client with the server specified by the by shareing the server information with the client
    private void incomingClient(Socket s) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String code = input.readLine();

        System.out.println("Read: " + code);

        //write back to the client
        String D = hashRegister.get(code).getName();
        String E = hashRegister.get(code).getURL();
        String F = hashRegister.get(code).getPort(); 

        int A = D.length();
        int B = E.length();
        int C = F.length();

        String outputString = ("" + A) + ("," + B) + ("," + C) + "," + D + E + F;

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        output.write(outputString + "\n");
        output.flush();
    }
    
    private void incomingServer(Socket s) throws IOException 
    {
        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String serverInfo = input.readLine();
        
        System.out.println("Read: " + serverInfo);

        //processes the information the server has sent
        int A = (int) serverInfo.charAt(0);
        int B = (int) serverInfo.charAt(1);
        int C = (int) serverInfo.charAt(2);
        int D = (int) serverInfo.charAt(3);
        boolean I = ((int) serverInfo.charAt(4) == 1); 
        boolean II = ((int) serverInfo.charAt(5) == 1); 
        boolean III = ((int) serverInfo.charAt(6) == 1);
        String E = serverInfo.substring(7, 7+A);
        String F = serverInfo.substring(7+A, 7+A+B);
        String G = serverInfo.substring(7+A+B, 7+A+C);
        String H = serverInfo.substring(7+A+B+C, 7+A+B+C+D);
        
        //stores the new specs in order to register
        ServerSpec newServer = new ServerSpec(E, F, G, H, I, II, III);
        
        //registers and receives the code to output
        String code = registerServer(s, newServer);

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        output.write(code + "\n");
        output.flush();
    }
    

    //TO DO: implement changes to run method from server to allow central server to mediate between many different servers and clients
    public void run() {
        try {
            listener = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                Socket s = listener.accept();
                if(isClient)
                   incomingClient(s);
                else
                   incomingServer(s);
                s.close();
            } catch (IOException ex) {
                Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
