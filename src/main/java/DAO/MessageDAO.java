package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.h2.command.Prepared;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    // create message and returns the message if successfully created
    public Message insertMessage(Message msg){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?);";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()){
                int id = (int) rs.getLong(1);
                return new Message(id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    // returns list of all messages
    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message;";
            Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(msg);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    // returns a message by message_id
    public Message getMessageById(int id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // deletes a message and returns the message of the deleted message
    public Message deleteMessageById(int id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                String sqlDelete = "DELETE FROM Message WHERE message_id = ?;";
                PreparedStatement psDelete= conn.prepareStatement(sqlDelete);
                psDelete.setInt(1, id);
                int numRows = psDelete.executeUpdate();
                if (numRows == 1){
                    return msg;
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // updates a message and returns the updated message if successful
    public Message updateMessage(String newMessage, int id){
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    newMessage,
                    rs.getLong("time_posted_epoch")
                );
                String sqlUpdate = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
                PreparedStatement psUpdate= conn.prepareStatement(sqlUpdate);
                psUpdate.setString(1, newMessage);
                psUpdate.setInt(2, id);
                int numRows = psUpdate.executeUpdate();
                if (numRows == 1){
                    return msg;
                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    // returns list of messages from an account by account_id
    public List<Message> getMessagesByAccount(int id){
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message msg = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
                messages.add(msg);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
