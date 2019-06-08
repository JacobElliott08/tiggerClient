package client;

import java.io.*;

public class ReadMessage extends Thread 
{
	BufferedReader inputStream;
	private volatile boolean running = true;
	ClientConnect clientConnect;

	public ReadMessage(ClientConnect cc) 
	{
		try 
		{
			inputStream = new BufferedReader(new InputStreamReader(cc.getSocket().getInputStream()));
		} 
		catch (IOException e) {e.printStackTrace();}
		clientConnect = cc;
	}
	@Override
	public void run()
	{
		while(running)
		{
			String input = null;
			try 
			{
				input = inputStream.readLine();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			if(input != null) 
			{	
				if(input.equalsIgnoreCase("invalid username"))	
				{
					System.out.println("New UserName is required.");
					clientConnect.setUserName(clientConnect.getName());
					try 
					{
						clientConnect.broadCastMessage("USERNAME");
						clientConnect.broadCastMessage(clientConnect.getUserName());
					} catch (IOException e1) {e1.printStackTrace();}
				}
				else if(input.equalsIgnoreCase("kick"))
				{
					this.shutDown();
				}
				else
				{
					System.out.println("> " + input);
				}
			}
		}
	}
	
	public synchronized void shutDown()
	{
		running = false;
		this.interrupt();
		try 
		{
			inputStream.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public boolean isRunning()
	{
		return running;
	}
	
}
