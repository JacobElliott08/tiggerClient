package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;

public class ClientConnect
{
	Socket socket = null;
	String line = null;
	
	BufferedReader bufferedReader = null;
	PrintWriter outputStream = null;
	
	ExecutorService readService = null;
	ReadMessage readThread = null;
	
	ExecutorService writeService = null;
	WriteMessage writeThread = null;
	
	ClientController controller = null;
	String name;
	
	
    public static void main(String [] args) throws IOException
    {
    	new ClientConnect();
    }
    
    public ClientConnect() throws IOException
    {

    	 name = getName();

    	InetAddress address = InetAddress.getLocalHost();


    	try 
    	{
    		socket = new Socket(address, 8000);
    		
    		bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    		
    		readThread = new ReadMessage(this); 
    		readService = Executors.newSingleThreadScheduledExecutor();
    		readService.submit(readThread);
    		System.out.println("Starting Reading");
    		
    		writeThread = new WriteMessage(this); 
    		writeService= Executors.newSingleThreadScheduledExecutor();
    		writeService.submit(writeThread);
    		System.out.println("Starting Writer");
    		
    		controller = new ClientController(this);
    		controller.start();
    		System.out.println("Starting Controller");

    	}catch (IOException e){e.printStackTrace();}

    	System.out.println("Client Address : " + address);
    	System.out.println("Enter Data to echo Server ( Enter QUIT to end):");

    }


    public void broadCastMessage(String message) throws IOException
    {
    	socket.getOutputStream().write((message+"\n").getBytes());
    	socket.getOutputStream().flush();
    }
    
    public String getName() {
        Scanner sc = new Scanner(System.in);
        String name = null; 
        while(name == null || name.equalsIgnoreCase(""))
        {
            System.out.print("Name : ");
            name = sc.nextLine();
        }
        sc = null;
        return name.replace(" ", "");
    }

	public Socket getSocket() 
	{
		return socket;
	}

	public String getUserName() {
		return name;
	}
	public void setUserName(String newUser)
	{
		this.name = newUser;
	}

}
