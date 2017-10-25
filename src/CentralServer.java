import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.IOException;

/*
 * Central Server mediates between individual servers and clients 
 * @author Doogie Warner
 */

public class CentralServer implements Runnable
{
	// Tracks if server open
		private boolean open = true;

    // Port to run on
    private int port;
    private int defaultPort = 9090;
    private boolean autoPort = false;
    private ServerSocket listener;
    private String serverName = "Central";
	private String password = "";
    private String hexColor = "000000";
	private String customBack = "NULL";
	
	private boolean isPublic; //keeps track of which server wants to be public or private
	private boolean isClient; //keeps track if the communication is with client or not
	
	CentralServer(int portNum) 
	{
		setPort(portNum);
	}

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
	
	//TO DO: implement changes to run method from server to allow central server to mediate between many different servers and clients
	public void run(){}
}
