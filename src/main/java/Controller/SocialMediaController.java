package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

import Service.AccountService;
import Service.MessageService;
/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;
    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        app.post("/register", this::postAccountHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("messages/{message_id}", this::getMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessageAccountHandler);
        return app;
    }


    /**
     * User Registration handler to create a new account
     * status 200 
     * if succesful
     * username is not blank and doesnt exist in already in db
     * password is at least 4 characters
     * response body: account with account_id
     * 
     * status 400
     * if registration is not successful
     * @param ctx
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postAccountHandler(Context ctx) throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account addedAcc = accountService.createUser(acc);
        if (addedAcc != null){
            ctx.json(addedAcc);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Login handler to verify login
     * status 200 
     * if succesful: username and password matches real account    
     * response body: account with account_id
     * @param ctx
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void loginHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account acc = mapper.readValue(ctx.body(), Account.class);
        Account loggedAccount = accountService.loginUser(acc);
        if(loggedAccount != null){
            ctx.json(loggedAccount);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Message handler to create a new message
     * status 200 
     * if succesful:
     * message_text not blank and under 255 characters
     * posted_by refers to real existing users
     * response body: message with message_id

     * @param ctx
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void postMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        Message messageCreated = messageService.createMessage(msg);
        if(messageCreated != null){
            ctx.json(messageCreated);
        } else {
            ctx.status(400);
        }
        
    }

    /**
     * Get all messages handler to get json list of all messages
     * status 200: always successful
     * response body: list of messages
     * @param ctx
     */
    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getALlMessages();
        ctx.json(messages);
    }

    /**
     * Get message handler to get a message by message id
     * status 200: always successful
     * response body: message
     * @param ctx
     */
    private void getMessageHandler(Context ctx){
        int id =Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.getMessageById(id);
        if(msg != null)
        {
            ctx.json(msg);
        }

    }

    /**
     * Delete message handler to delete a message
     * status 200
     * if successful: if it exists and is deleted
     * response body: deleted message
     * 
     * status 400
     * if unsucessful
     * response body: empty
     * @param ctx
     */
    private void deleteMessageHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.deleteMessageById(id);
        if(msg != null){
            ctx.json(msg);
        }
    }

    /**
     * Update message handler given a message id
     * status 200
     * if successful
     * message_id exists
     * new message_text is not blank and not over 255 characters
     * response body: fully updated messae
     * 
     * status 400
     * if unsuccessful
     * @param ctx
     * @throws JsonProcessingException 
     * @throws JsonMappingException 
     */
    private void updateMessageHandler(Context ctx) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message msg = mapper.readValue(ctx.body(), Message.class);
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageById(msg, id);
        if(updatedMessage != null){
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Get all messages from a user account id
     * status 200
     * response body: list of messages
     */
    private void getAllMessageAccountHandler(Context ctx){
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccount(id);
        ctx.json(messages);
    }
}