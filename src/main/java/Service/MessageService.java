package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountDAO accountDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();

    }

    public Message createMessage(Message msg){
        if (msg.getMessage_text().length() >= 255 ||
            msg.getMessage_text().length() == 0 ||
            accountDAO.getAccountById(msg.getPosted_by()) == null){
            return null;
        }
        return messageDAO.insertMessage(msg);
    }

    public List<Message> getALlMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id){
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageById(Message msg, int id){
        String messageText = msg.getMessage_text();
        if (messageDAO.getMessageById(id) == null || messageText.length() == 0 || messageText.length() >= 255){
            return null;
        }
        return messageDAO.updateMessage(messageText, id);
    }

    public List<Message> getMessagesByAccount(int id){
        return messageDAO.getMessagesByAccount(id);
    }
}
