package ChatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DataReadFromServerThread extends Thread{

	public Socket clientSocket;
	public Client c;
	
	public DataReadFromServerThread(Socket clientSocket, Client c)
	{
		this.clientSocket = clientSocket;
		this.c = c;
	}
	
	public void run()
	{
		BufferedReader dataRead;
		while(true)
		{
			try {
				dataRead = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String serverResponse;
				serverResponse = dataRead.readLine();
				System.out.println("\n");
				System.out.println(serverResponse);
				
				  if (c.getClientName() != null) {
	                  System.out.print("[" + c.getClientName() + "]: ");
	              } 
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}	
	}
}
