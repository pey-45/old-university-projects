package e1.BankAccounts;

import e1.Clients.Client;
import e1.Clients.StandardClient;
import e1.Clients.VIPClient;

public class CheckingBankAcc extends BankAcc{

    public CheckingBankAcc(String IBAN, Client client) {
        super(IBAN, client);
    }

    @Override
    public void depositBalance(long n) {

        if (n < 0) throw new IllegalArgumentException("Zero or negative numbers are not valid.");

        this.balance += n;
    }

    @Override
    public void withdrawBalance(long n) {

        //allowed debt for Standard and Preferential clients
        double allowed_debt = this.client instanceof StandardClient ? 0 : -100000;

        if (n < 0) throw new IllegalArgumentException("Zero or negative numbers are not valid.");

        //extra OR condition for VIP clients
        if (this.balance - n >= allowed_debt || this.client instanceof VIPClient) this.balance -= n;
        else throw new IllegalArgumentException(this.client instanceof StandardClient ?
                "No debt allowed for standard client." :
                "Maximum debt for preferential client is 1000EUR.");
    }
}