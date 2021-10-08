package assignment;

public abstract class BankAccount {

	protected int accNum;
	protected double balance;
	protected static int serialNum = 0;
	
	/** Default constructor 
	 * 
	 */
	public BankAccount()
	{
        // TODO
	    // check the balance
		balance = 0.0;
		// check the account Number
		accNum = ++serialNum;
	}
	
	/** Overloaded constructor
	 */
	public BankAccount( double startBalance) throws Exception
	{
        // TODO
	    //deposit the balance
		balance = startBalance;
        //check the account number
		accNum = ++serialNum;
	}
	
	/** accessor for balance
	 * 
	 */
	public double getBalance()
	{
        // TODO
        // get the balance
		return balance;
    }
	
	/* accessor for account number
	 * 
	 */
	public int getAccNum()
	{
		return accNum;
	}
	
	/** Deposit amount to account
	 * 
	 */
	public void deposit( double amount ) throws Exception
	{
        // TODO
        // deposit amount of money, if it is legal/valid amount
		if(amount >= 0.0)
			balance += amount;
		else
			throw new Exception("Invalid Deposite Amount");
	}
	
	/** withdraw amount from account
	 * 
	 */
	public void withdraw( double amount ) throws Exception
	{
		if(amount >= 0.0 && amount <= balance)
			balance -= amount;
		
		else
			throw new Exception("Insufficient Balance");
	}

	/**Override toString()
	 *
	 */
	public String toString()
	{
		// TODO: print the balance amount for specific account type displaying the account number.
		String str = "The balance is of the "+accType()+" "+ getAccNum() +" is "+ getBalance();
		return str;	
	}
	
	public abstract void applyComputation();
	public abstract String accType();
}