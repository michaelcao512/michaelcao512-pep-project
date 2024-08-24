package DAO;

import java.sql.*;
import java.util.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    // gets list of all account
    public List<Account> getAllAccounts(){
        List<Account> accounts = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Account acc = new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
                accounts.add(acc);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }


    // creates a new user given account info and returns the account
    public Account insertAccount(Account acc){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, acc.getUsername());
            ps.setString(2, acc.getPassword());

            ps.executeUpdate();

            ResultSet resultsKeySet = ps.getGeneratedKeys();
            if (resultsKeySet.next()){
                int id = (int) resultsKeySet.getLong(1);
                return new Account(id, acc.getUsername(), acc.getPassword());
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

            // returns an account by username
            public Account getAccountByUsername(String username){
                Connection conn = ConnectionUtil.getConnection();
                try {
                    String sql = "SELECT * FROM Account WHERE username = ?;";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    while(rs.next()){
                        Account acc = new Account(
                            rs.getInt("account_id"),
                            rs.getString("username"),
                            rs.getString("password")
                        );
                        return acc;
                    }
                } catch (SQLException e){
                    System.out.println(e.getMessage());
                }
                return null;
            }

        // returns an account by id
        public Account getAccountById(int id){
            Connection conn = ConnectionUtil.getConnection();
            try {
                String sql = "SELECT * FROM Account WHERE account_id = ?;";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Account acc = new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                    return acc;
                }
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
            return null;
        }

}
