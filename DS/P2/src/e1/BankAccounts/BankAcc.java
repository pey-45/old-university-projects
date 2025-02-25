package e1.BankAccounts;

import e1.Clients.Client;

public abstract class BankAcc {
    protected final String IBAN;
    protected final Client client;
    protected long balance;

    protected BankAcc(String IBAN, Client client){
        this.IBAN = IBAN;
        this.client = client;
        this.balance = 0;
    }

    public long getBalance() {
        return this.balance;
    }

    protected abstract void depositBalance(long n);

    protected abstract void withdrawBalance(long n);
}