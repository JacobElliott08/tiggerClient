package client;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ClientController extends Thread
{
	private volatile boolean running = true;

	final ReadMessage rm;
	final WriteMessage wm;
	final Socket socket;
	final ExecutorService rs;
	final ExecutorService ws;

	ClientConnect clientConnect;

	public ClientController(ClientConnect cc) {
		this.rm = cc.readThread;
		this.rs = cc.readService;
		this.wm = cc.writeThread;
		this.ws = cc.writeService;
		this.socket = cc.getSocket();
		clientConnect = cc;
	}

	public void run(){

		while(running){

			if(!rm.isRunning() && !wm.isRunning()){
				shutDown();
				System.out.println("Destroying Controller");
			}
			
			if(!wm.isRunning()) {
				rm.shutDown();
				rs.shutdownNow();
				try{
					wm.interrupt();
					socket.close();
				}catch(Exception e){e.printStackTrace();}

				System.out.println("Destroying Read");
			}

			if(!rm.isRunning()) {
				System.out.println("Destroying Write");
				wm.shutDown();
				ws.shutdownNow();
				
			}

			try{
				this.sleep(100);
			}catch(Exception e){}
		}


	}


	public synchronized void shutDown() {
		this.interrupt();
        running = false;
        try
        {
        	socket.close();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    public synchronized boolean isRunning() {
    	return running;
    }


}