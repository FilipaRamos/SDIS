import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Server{
	// id of the server
	public int id;
	// control address and port
	public String controlAddress;
	public int controlPort;
	// backup address and port
	public String backupAddress;
	public int backupPort;
	// restore address and port
	public String restoreAddress;
	public int restorePort;
	// disk
	public String disk;
	// the multicast object of the server
	public Multicast multicast;
	// the disk manager object of the server
	public ManageDisk manager;
	// operation to run
	public String operation;

	// server constructor
	public Server(int id, String controlAddress, int controlPort, String backupAddress, int backupPort,
			String restoreAddress, int restorePort, String disk) {

		this.id = id;
		this.controlAddress = controlAddress;
		this.controlPort = controlPort;
		this.backupAddress = backupAddress;
		this.backupPort = backupPort;
		this.restoreAddress = restoreAddress;
		this.restorePort = restorePort;
		this.disk = disk;

	}

	// main
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		if (args.length != 8) {
			System.out.println("Wrong number of arguments!");
			System.exit(1);
		}
		
		Server server = new Server(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), args[3],
				Integer.parseInt(args[4]), args[5], Integer.parseInt(args[6]), args[7]);

		System.out.println(" =================== ");

		System.out.println("What's the operation to perform?");

		Scanner input = new Scanner(System.in);
		String option = input.nextLine();

		server.multicast = new Multicast(server.controlAddress, server.controlPort, 
				server.backupAddress, server.backupPort, 
				server.restoreAddress, server.restorePort);
		server.ServerEngine(server, option);

		// ServerFile file = new ServerFile(1, "ficheiro.pl", 128000, "Ana");

		//Browser browser = new Browser();

	}

	// the engine of the server which calls the needed procedures
	public void ServerEngine(Server server, String request) {
		
		ThreadEngine threadManager1 = new ThreadEngine(request, this.multicast);
		threadManager1.CreateThread(threadManager1);
		
		//ThreadEngine threadManager2 = new ThreadEngine("request", this.multicast);
		//threadManager2.CreateThread(threadManager2);
	
	}

}
