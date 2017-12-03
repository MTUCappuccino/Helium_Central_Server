
import java.net.ServerSocket;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Central Server mediates between individual servers and clients 
 * @author Doogie Warner
 */
public class CentralServer implements Runnable {

    private int port;
    private ServerSocket listener;

    private HashMap<String, ServerSpec> hashRegister;  //hashmap to a server, input: code, output: server

    private String pubMessage = "request_public_servers";
    private ArrayList<String> pubCodes;

    CentralServer(int portNum) {

        port = portNum;

        hashRegister = new HashMap<>();
        pubCodes = new ArrayList<>();
        
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
            return true;
        } catch (IOException ex) {
            Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    private String getNewServerCode() {
        Random rand = new Random();

        int char1 = rand.nextInt(36);
        int char2 = rand.nextInt(36);
        int char3 = rand.nextInt(36);
        int char4 = rand.nextInt(36);
        int char5 = rand.nextInt(36);

        char c1 = char1 < 10 ? (char) (char1 + 48) : (char) (char1 + 55);
        char c2 = char2 < 10 ? (char) (char2 + 48) : (char) (char2 + 55);
        char c3 = char3 < 10 ? (char) (char3 + 48) : (char) (char3 + 55);
        char c4 = char4 < 10 ? (char) (char4 + 48) : (char) (char4 + 55);
        char c5 = char5 < 10 ? (char) (char5 + 48) : (char) (char5 + 55);

        String code = new String(new char[]{c1, c2, c3, c4, c5});

        if (hashRegister.get(code) != null) {
            return getNewServerCode();
        }
        return code;
    }

    //reads the client code and connects the client with the server specified by the by shareing the server information with the client
    private void incomingClient(String code, Socket s) throws IOException {
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

    private void requestPubServers(Socket s) throws IOException {
        String outputString = "";
        int A;
        int B;
        int C;
        int D;
        int userReq = 0;
        int passReq = 0;
        int isPub = 1;
        String E;
        String F;
        String G;
        String H;
        for (int i = 0; i < pubCodes.size(); i++) {
            E = hashRegister.get(pubCodes.get(i)).getName();
            F = hashRegister.get(pubCodes.get(i)).getpURL();
            G = hashRegister.get(pubCodes.get(i)).getURL();
            H = hashRegister.get(pubCodes.get(i)).getPort();
            if (hashRegister.get(pubCodes.get(i)).getReqUser()) {
                userReq = 1;
            }
            if (hashRegister.get(pubCodes.get(i)).getReqPass()) {
                passReq = 1;
            }
            A = E.length();
            B = F.length();
            C = G.length();
            D = H.length();

            outputString = outputString + pubCodes.size() + A + B + C + D + userReq + passReq + isPub + E + F + G + F;
        }

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        output.write(outputString + "\n");
        output.flush();
    }

    private void incomingServer(String message, Socket s) throws IOException {

        String[] params = message.split(",");

        int A = Integer.parseInt(params[0]);
        int B = Integer.parseInt(params[1]);
        int C = Integer.parseInt(params[2]);
        int D = Integer.parseInt(params[3]);

        int lengthOfPrefix = params[0].length() + params[1].length() + params[2].length()
                + params[3].length() + 4;

        boolean username = message.charAt(lengthOfPrefix) == '1';
        boolean password = message.charAt(lengthOfPrefix + 1) == '1';
        boolean isPublic = message.charAt(lengthOfPrefix + 2) == '1';

        String E = message.substring(lengthOfPrefix + 4, lengthOfPrefix + 4 + A);
        String F = message.substring(lengthOfPrefix + 4 + A, lengthOfPrefix + 4 + A + B);
        String G = message.substring(lengthOfPrefix + 4 + A + B, lengthOfPrefix + 4 + A + B + C);
        String H = message.substring(lengthOfPrefix + 4 + A + B + C, lengthOfPrefix + 4 + A + B + C + D);

        //stores the new specs in order to register
        ServerSpec newServer = new ServerSpec(E, F, G, H, username, password, isPublic);

        //registers and receives the code to output
        String code = getNewServerCode();
        hashRegister.put(code, newServer);

        if (isPublic) {
            pubCodes.add(code);
        }

        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        output.write(code + "\n");
        output.flush();
    }

    //TO DO: implement changes to run method from server to allow central server to mediate between many different servers and clients
    @Override
    public void run() {
        try {
            listener = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                Socket s = listener.accept();

                new Thread(() -> {
                    try {
                        BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));
                        String message = input.readLine();
                        
                        System.out.println("Processing message: " + message);
                        
                        if (message.equals(pubMessage)) {
                            requestPubServers(s);
                        } else if (message.length() > 5) {
                            incomingServer(message, s);
                        } else {
                            incomingClient(message, s);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }).start();

            } catch (IOException ex) {
                Logger.getLogger(CentralServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
