package ChatApp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	public String host;
	public int portNo;
	InetAddress id;
	String clientName;
		
	public Client(String host, int portNo)
	{
		this.host = host;
		this.portNo = portNo;
	}
	 public void createClient()
	 {
		 try {
			Socket clientSocket = new Socket(host,portNo);
			System.out.println(" ");
			System.out.println("Successfully connected to server");
			
			DataReadFromServerThread read = new DataReadFromServerThread(clientSocket,this);
			read.start();
				
			DataWriteToServerThread write = new DataWriteToServerThread(clientSocket, this);
			write.start();
		 
		 }
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	 }
	 
	 void setClientName(String clientName) {
	        this.clientName = clientName;
	    }
	 
	    String getClientName() {
	        return this.clientName;
	    }
	 
	public static void main(String[] args) {
		
		if(args.length<2)
		{
			System.out.println("Insert Required values.");
			System.out.println("Please insert in following format : java host-name port-number");
			System.exit(1);
		}
			Client c = new Client(args[0],Integer.parseInt(args[1]));
			c.createClient();
		}
}
