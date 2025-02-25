package e1;

import e1.BankAccounts.CheckingBankAcc;
import e1.BankAccounts.SavingBankAcc;
import e1.Clients.PrefClient;
import e1.Clients.StandardClient;
import e1.Clients.VIPClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BankAccTest {
    static StandardClient strd_client;
    static PrefClient pref_client;
    static VIPClient vip_client;
    static CheckingBankAcc checking_acc_strd;
    static CheckingBankAcc checking_acc_pref;
    static CheckingBankAcc checking_acc_vip;
    static SavingBankAcc saving_acc_strd;
    static SavingBankAcc saving_acc_pref;
    static SavingBankAcc saving_acc_vip;

    @BeforeAll
    static void setup() {
        strd_client = new StandardClient("00000000S");
        pref_client = new PrefClient("00000000P");
        vip_client = new VIPClient("00000000V");

        checking_acc_strd = new CheckingBankAcc("ES0000000000000000000000", strd_client);
        checking_acc_pref = new CheckingBankAcc("ES0000000000000000000001", pref_client);
        checking_acc_vip = new CheckingBankAcc("ES0000000000000000000002", vip_client);

        saving_acc_strd = new SavingBankAcc("ES0000000000000000000003", strd_client);
        saving_acc_pref = new SavingBankAcc("ES0000000000000000000004", pref_client);
        saving_acc_vip = new SavingBankAcc("ES0000000000000000000005", vip_client);
    }

    @Test
    void testAll() {
        //deposit checking
        checking_acc_vip.depositBalance(120000); //+1.2K = 1.2K | no min, success
        checking_acc_pref.depositBalance(150000); //+1.5K = 1.5K | no min, success
        checking_acc_strd.depositBalance(200000); //+2K = 2K | no min, success

        assertEquals(checking_acc_vip.getBalance(), 120000);
        assertEquals(checking_acc_pref.getBalance(), 150000);
        assertEquals(checking_acc_strd.getBalance(), 200000);

        //withdraw checking
        checking_acc_vip.withdrawBalance(100000000); //-1M = -998.8K | unlimited debt, success
        assertThrows(IllegalArgumentException.class, () -> checking_acc_pref.withdrawBalance(300000)); //-3K = -1.5K | 1K max allowed debt, fail
        checking_acc_pref.withdrawBalance(200000); //-2K = -500 | 1K max allowed debt, success
        assertThrows(IllegalArgumentException.class, () -> checking_acc_strd.withdrawBalance(210000)); //-2.1K = -100 | no debt allowed, fail
        checking_acc_strd.withdrawBalance(190000); //-1.9K = 100 | no debt allowed, success

        assertEquals(checking_acc_vip.getBalance(), -99880000);
        assertEquals(checking_acc_pref.getBalance(), -50000);
        assertEquals(checking_acc_strd.getBalance(), 10000);


        saving_acc_vip.depositBalance(100); //+1 = 1 | no min, success
        assertThrows(IllegalArgumentException.class, () -> saving_acc_pref.depositBalance(30000)); //+300 = 300 | 500 min, fail
        saving_acc_pref.depositBalance(70000); //+700 = 700 | 500 min, success
        assertThrows(IllegalArgumentException.class, () -> saving_acc_strd.depositBalance(70000)); //+700 = 700 | 1K min, fail
        saving_acc_strd.depositBalance(120000); //+1.2K = 1.2K | 1K min, success

        assertEquals(saving_acc_vip.getBalance(), 100);
        assertEquals(saving_acc_pref.getBalance(), 70000);
        assertEquals(saving_acc_strd.getBalance(), 120000);

        saving_acc_vip.withdrawBalance(1000000); //-10000 = -9999 | unlimited debt, success
        saving_acc_vip.withdrawBalance(100); //-1(min comm) = -10000 | unlimited debt, success
        assertThrows(IllegalArgumentException.class, () -> saving_acc_pref.withdrawBalance(200000)); //-2K-(2K*0.02) = -1.340K | 1K max allowed debt, fail
        saving_acc_pref.withdrawBalance(150000); //-1.5K-(1.5K*0.02 > 1) = -830 | 1K max allowed debt, success
        saving_acc_pref.withdrawBalance(100); //-1-(1)(min comm) = -832 | 1K max allowed debt, success
        assertThrows(IllegalArgumentException.class, () -> saving_acc_strd.withdrawBalance(150000)); //-1.5K-(1.5K*0.02) = -360 | no debt allowed, fail
        saving_acc_strd.withdrawBalance(100000); //-1K-(1K*0.02 > 3) = 160 | no debt allowed, success
        saving_acc_strd.withdrawBalance(100); //-1-(3)(min comm) = 156 | no debt allowed, success

        assertEquals(saving_acc_vip.getBalance(), -1000000);
        assertEquals(saving_acc_pref.getBalance(), -83200);
        assertEquals(saving_acc_strd.getBalance(), 15600);
    }
}