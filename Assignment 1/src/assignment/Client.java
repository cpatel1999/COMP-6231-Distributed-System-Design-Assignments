package assignment;

import java.util.ArrayList;
import java.util.ListIterator;



public class Client {

	public static void main(String args[]) throws Exception    {

	// TODO: create 3 account objects (one checking and two saving accounts): ca, sa1, sa2
	BankAccount ca = new CheckingAccount();
	BankAccount sa1 = new SavingsAccount();
	BankAccount sa2 = new SavingsAccount();
	
	
	//TODO: create a generic list called: accounts
	ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();
		
	//TODO: add all the three accounts to the list "accounts"
	accounts.add(ca);
	accounts.add(sa1);
	accounts.add(sa2);
	
	//TODO: print the information of all the three accounts
	
	
	ListIterator<BankAccount> itr = accounts.listIterator(); 

	while (itr.hasNext()) {
			System.out.println(itr.next());
		
	}
	}
}
