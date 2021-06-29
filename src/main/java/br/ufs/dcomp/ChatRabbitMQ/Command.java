package br.ufs.dcomp.ChatRabbitMQ;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.rabbitmq.client.*;


public class Command{


    private Map<String,Integer> comando = new HashMap<String,Integer>();
    
    // utilizada para manipulacao de arquivos
    //private Arquivos arq = new Arquivos();
    
    public Command(){
    
        comando.put("!addGroup", 1);
        comando.put("!addUser", 2);
        comando.put("!delFromGroup", 3);
        comando.put("!removeGroup", 4);
        comando.put("!upload", 5);
        comando.put("!listUsers",6);
        comando.put("!listGroups",7);

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
        channel.queueUnbind(Queue, exchange, "");
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

    public void upload(Channel channel_file, String q_name, String q_send, String day, String grupo, String caminho_arq) throws FileNotFoundException{
        Upload up = new Upload(channel_file, q_name, q_send, day, grupo, caminho_arq);
        up.start();
    }

    public String listUsers(String exchange){
        // requisicao
        RequestWeb req = new RequestWeb();
        String rota = "/api/exchanges/%2f/"+exchange+"/bindings/source";
        String json_res = req.getJason(rota);
        
        // conversao de json para Member
        Gson gson = new Gson();
        Member[] list_users = gson.fromJson(json_res, Member[].class);

        // formatacao de resultado
        
        String lista = "";
        int tam = list_users.length;

        for(int i = 0; i < tam -1 ;i++){
            lista = lista + " " + list_users[i].destination + ","; 
        }

        lista = lista +" "+list_users[tam-1].destination;

        return lista;

        
    }

    public String listGroups(String user){
        try{
            // requisicao
            RequestWeb req = new RequestWeb();
            String rota = "/api/bindings/%2f";
            String json_res = req.getJason(rota);

            Gson gson = new Gson();
            Bindings[] list_groups = gson.fromJson(json_res, Bindings[].class);

            String lista = "";
            int tam = list_groups.length;

            String source;
            String dest;

            for(int i = 0; i < tam ;i++){
                source = list_groups[i].source;
                dest = list_groups[i].destination;

                if (dest.equals(user) && !source.isEmpty()){
                    lista = lista + " " + source + ","; 
                }
            }

        
            // uso subString pra eliminar a ultima virgula
            return lista.substring(0, lista.length()-1);
            } catch(Exception e){
                return " ";
            }

    }

}