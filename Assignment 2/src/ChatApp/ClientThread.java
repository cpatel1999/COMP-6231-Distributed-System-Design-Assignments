package ChatApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread{

	public Socket soc;
	public Server server;
	public PrintWriter printWriter;
	public List<String> clientNames;
	
	public ClientThread(Socket soc,List<String> clientNames, Server server) {
		// TODO Auto-generated constructor stub
		this.soc = soc;
		this.server = server;
		this.clientNames = clientNames;
		
	}
	
	public void run()
	{
		try {
			BufferedReader data_in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			printWriter = new PrintWriter(soc.getOutputStream(), true);
			
			
			onlineUsers();
			String name = (String)data_in.readLine();
			clientNames.add(name);	
			
			String newUserConnectedMessage = "New user "+name+" is connected";
			server.sendToAll(newUserConnectedMessage, this);
			
			String messageFromClient = " ";	
			while(true)
			{
				messageFromClient = data_in.readLine();
				if(!messageFromClient.equalsIgnoreCase("stop"))
				{
					String message = name+" : "+messageFromClient;
					server.sendToAll(message, this);
				}
				else
				{
					break;
				}
			}
			server.deleteClientData(name,this);
			server.printMsg(name+" left the group chat");
			soc.close();
			clientNames.remove(name);
			
			String str = name+" has left the group chat";
			server.sendToAll(str, this);		
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onlineUsers()
	{
		
			if(server.getClientNames().isEmpty())
			{
				printWriter.println("No user connected");
			}
			else
			{
				printWriter.println("Connected users : "+server.getClientNames());		
			}
	}
	public void sendMsg(String msg) {
			printWriter.println(msg);
	}

}
