package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class WriteMessage extends Thread 
{
	private volatile boolean running = true;
	PrintWriter outputStream = null;
	ClientConnect clientConnect;
	
	public WriteMessage(ClientConnect cc)
	{
		try 
		{
			this.outputStream = new PrintWriter(cc.getSocket().getOutputStream());
		}
		catch (IOException e) {e.printStackTrace();}
		clientConnect = cc;
	}
	
	
	public void run()
	{
		try 
		{
			clientConnect.broadCastMessage("USERNAME");
			clientConnect.broadCastMessage(clientConnect.getUserName());
	
		} catch (IOException e1) {e1.printStackTrace();}

		ConsoleInput con = new ConsoleInput(1, 5, TimeUnit.SECONDS);

		while (running) 
		{
			String input = null;
			try 
			{
				input = con.readLine();
				if(input != null)
				{
					if(!isCommand(input))
					{
						clientConnect.broadCastMessage(input);
					}
				}
			} 
			catch (InterruptedException e) {/*e.printStackTrace();*/} 
			catch (IOException e) {/*e.printStackTrace();*/}
		}
	}
	
	public synchronized void shutDown()
	{
		running = false;
		outputStream.close();
		this.interrupt();
	}
	
	public boolean isRunning()
	{
		return running;
	}

	public boolean isCommand(String line)
	{
		if(line.charAt(0) == '!' || line.equalsIgnoreCase("quit"))
		{
			if(line.charAt(0) == '!')
				line = line.substring(1);
			String [] commands = line.split(" ");
			System.out.println(commands[0]);
			doCommand(commands);
			return true;
		}
		return false;
	}

	private void doCommand(String [] args)
	{
		outputStream.println("COMMAND");
		outputStream.flush();

		if(args[0].equalsIgnoreCase("dm") || 
		   args[0].equalsIgnoreCase("kick") || 
		   args[0].equalsIgnoreCase("who") || 
		   args[0].equalsIgnoreCase("help")||
		   args[0].equalsIgnoreCase("quit")){

			try {
				clientConnect.broadCastMessage(joinString(args,0));
			} catch (IOException e) {e.printStackTrace();}
		}
	}


	public String joinString(String [] arr, int start)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = start; i < arr.length; i ++)
        {
            builder.append(arr[i]);
            if(i != arr.length - 1)
            {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

}
