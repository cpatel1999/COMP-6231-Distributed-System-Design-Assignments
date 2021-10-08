package ChatApp;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DataWriteToServerThread extends Thread
{
	public Socket clientSocket;
	public Client c;
	public PrintWriter writer;
	public BufferedReader data_read;
	
	public DataWriteToServerThread(Socket clientSocket, Client c)
	{
		this.clientSocket = clientSocket;
		this.c = c;
		try
		{
			writer = new PrintWriter(clientSocket.getOutputStream(), true);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void run()
	{
		try 
		{
			data_read = new BufferedReader(new InputStreamReader(System.in)); 
			Scanner sc = new Scanner(System.in);
			System.out.println(" ");
			System.out.println("Write STOP to leave group chat...");
			System.out.println(" ");
			System.out.println("Enter your Name: ");
			String clientName = data_read.readLine();
			
			c.setClientName(clientName);
			writer.println(clientName);
			String text;
			System.out.println(" ");
			
			while(true)
			{
				System.out.print("[" + clientName + "]: ");
				text = data_read.readLine();
				writer.println(text);
				if(text.equalsIgnoreCase("stop"))
				{
					break;
				}
			}
			try
			{
				sc.close();
				clientSocket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
}