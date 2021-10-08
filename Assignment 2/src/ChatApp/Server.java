package ChatApp;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Server  {

	public int portno;
	public List<String> clientNames = new LinkedList<String>();
	public List<ClientThread> clientThreads = new LinkedList<>();
	
	public Server(int portno)
	{
		this.portno = portno;
	}
	public void clientThreadCreation() {
				
		ServerSocket serverSoc;
		try
		{
			serverSoc = new ServerSocket(portno);
			while(true)
			{
				Socket soc = serverSoc.accept();
				System.out.println("New user connected to the server.");
				ClientThread ct = new ClientThread(soc,clientNames,this);
				ct.start();
				clientThreads.add(ct);			
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}	
	
	public void printMsg(String msg)
	{
		System.out.println(msg);
	}
	
	public void deleteClientData(String userName, ClientThread ct) 
	{
		clientThreads.remove(ct);
	}
	
	List<String> getClientNames() {
        return this.clientNames;
    }
	
	public void sendToAll(String msg, ClientThread ct)
	{
		ClientThread clnt_thrd;
		
		ListIterator<ClientThread> iterator = clientThreads.listIterator();
		while(iterator.hasNext())
		{
			clnt_thrd = (ClientThread)iterator.next();
			if(clnt_thrd != ct)
			{
				clnt_thrd.sendMsg(msg);
			}
		}
	}
	
	public static void main(String[] args) { 
		if(args.length<1)
		{
			System.out.println("Insert Required values.");
			System.out.println("Please insert in following format : java server port-number");
			System.exit(1);
		}	
		Server server = new Server(Integer.parseInt(args[0]));
		System.out.println("Server started...");
		server.clientThreadCreation();
	}
}
