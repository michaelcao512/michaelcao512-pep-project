package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    // returns a user if created and null if unsuccessful
    public Account createUser(Account acc){
        // checking username exists and isn't in database
        // and password is greater than 4 characters
        String username = acc.getUsername();
        String password = acc.getPassword();
        System.out.println(username + " : " + password);
        if (username.length() == 0 || password.length() < 4 || accountDAO.getAccountByUsername(username) != null){
            return null;
        }

        return accountDAO.insertAccount(acc);
    }

    // returns the user if login successful and null if not
    public Account loginUser(Account login){
        String username = login.getUsername();
        String password = login.getPassword();

        Account account = accountDAO.getAccountByUsername(username);
        // if the account doesn't exist
        // if the account password doesn't match the login password
        if(account == null || !account.getPassword().equals(password)){
            return null;
        }
        return account;


    }
}
