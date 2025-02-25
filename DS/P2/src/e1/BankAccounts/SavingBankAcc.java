package e1.BankAccounts;

import e1.Clients.Client;
import e1.Clients.PrefClient;
import e1.Clients.StandardClient;
import e1.Clients.VIPClient;

public class SavingBankAcc extends BankAcc {

    public SavingBankAcc(String IBAN, Client client) {
        super(IBAN, client);
    }

    @Override
    public void depositBalance(long n) {

        //minimum deposit for Standard and Preferential clients
        double min_deposit = this.client instanceof StandardClient ? 100000 : 50000;

        if (n < 0) throw new IllegalArgumentException("Zero or negative numbers are not valid.");

        //extra OR condition for VIP
        if (n >= min_deposit || this.client instanceof VIPClient) this.balance += n;

        //exceptions for deposits below minimum
        else throw new IllegalArgumentException(this.client instanceof StandardClient ?
                "Minimum deposit for standard client is 1000EUR." :
                "Minimum deposit for preferential client is 500EUR.");
    }

    @Override
    public void withdrawBalance(long n) {

        double commission, allowed_debt = 0;
        /*VIP's allowed_debt is unnecessary, but it has to be initialized*/

        if (n < 0) throw new IllegalArgumentException("Zero or negative numbers are not valid.");

        //commission and allowed debt for Standard and Preferential clients
        if (this.client instanceof StandardClient) {
            commission = ((n * 0.04 <= 300) ? 300 : (double) n * 0.04);
            allowed_debt = 0;
        } else if (this.client instanceof PrefClient) {
            commission = ((n * 0.02 <= 100) ? 100 : (double) n * 0.02);
            allowed_debt = -100000;
        } else commission = 0;


        //extra OR condition for VIP clients
        if (this.balance - n - commission >= allowed_debt || this.client instanceof VIPClient)
            this.balance -= (long) (n + commission);

        //exceptions for debts below allowed
        else throw new IllegalArgumentException(this.client instanceof StandardClient ?
                "No debt allowed for standard client." :
                "Maximum debt for preferential client is 1000EUR");
    }
}