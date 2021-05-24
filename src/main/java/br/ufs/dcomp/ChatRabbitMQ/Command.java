package br.ufs.dcomp.ChatRabbitMQ;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.*;


public class Command{

    private Map<String,Integer> comando = new HashMap<String,Integer>();
    
    public Command(){
    
        comando.put("!addGroup", 1);
        comando.put("!addUser", 2);
        comando.put("!delFromGroup", 3);
        comando.put("!removeGroup", 4);
        comando.put("!upload", 5);

    }
    
    public void addGroup(Channel channel, String exchange, String myQueue) throws IOException{
        channel.exchangeDeclare(exchange, "fanout");
        channel.queueBind(myQueue, exchange, "");
    }

    public void addUser(Channel channel, String exchange, String Queue) throws IOException{
        channel.queueDeclare(Queue,false,false, false, null);
        channel.queueBind(Queue, exchange, "");
    }

    public void delFromGroup(Channel channel, String exchange, String Queue) throws IOException{
        channel.exchangeUnbind(Queue, exchange, "");
    }

    public void removeGroup(Channel channel, String exchange) throws IOException{
        channel.exchangeDelete(exchange);
    }
    
    // retorna o numero relacionado ao comando do chat
    public int getNumber(String comm){
        try{
            return comando.get(comm);
        } catch(Exception e){
            return -1;
        }
    }

    public void upload(Channel channel_file, String q_name, String q_send, String day, String grupo, byte[] arquivo){
        
        Upload up = new Upload(channel_file, q_name, q_send, day, grupo, arquivo);
        up.start();
    }

}