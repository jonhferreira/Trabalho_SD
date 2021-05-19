package br.ufs.dcomp.ChatRabbitMQ;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Chat {
  
  public static String destino = "";
  public static void main(String[] argv) throws Exception {
    
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("18.207.112.89"); 
      factory.setUsername("jonh");
      factory.setPassword("12345"); 
      factory.setVirtualHost("/");

      
      
      Connection connection = factory.newConnection(); 
      Channel channel = connection.createChannel();
      
      Scanner input = new Scanner(System.in);
      
      String msg = ""; 
      String QUEUE_Send = "";

      String Exchange = "";

      // contem as funcoes relacionadas aos comando do chat
      Command command = new Command();

      // fornecera a data e a hora no formato requerido
      DataHora data_hora = new DataHora();
      // formata a mensagem para envio
      FormatMSG format_msg = new FormatMSG();
      
      System.out.print("usuario: "); 
      String QUEUE_NAME = input.nextLine();
      QUEUE_NAME = "@" + QUEUE_NAME; 
      
      //(queue-name, durable, exclusive, auto-delete, params);
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      
      Consumer consumer = new DefaultConsumer(channel) { 
      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
      System.out.println();
      String message = format_msg.RecebeProto(body);
      System.out.println(message);
      System.out.print(destino + ">>>");
      }
    }; 
    //(queue-name, autoAck, consumer); 
    channel.basicConsume(QUEUE_NAME,true, consumer);
    
    System.out.print(">>>");
    msg = input.nextLine();
  
    while (!msg.equals("exit")) {
      String[] msg_par = msg.split(" ");

      if (msg.subSequence(0, 1).equals("@")){
        QUEUE_Send = msg;
        destino = QUEUE_Send;
        Exchange = "";
        
      }

      else if(msg.subSequence(0, 1).equals("#")){
        Exchange = msg.substring(1,msg.length());
        destino = msg;
        QUEUE_Send = "";
      }

      else {

          switch(command.getNumber(msg_par[0])){
            case 1: 
                command.addGroup(channel, msg_par[1], QUEUE_NAME);
                break;
            case 2:
                command.addUser(channel, msg_par[2], msg_par[1]);
                break;
            case 3:
                command.delFromGroup(channel, msg_par[2], msg_par[1]);
                break;
            case 4:
                command.removeGroup(channel, msg_par[1]);
                break;
            default:
                String day_hour = data_hora.data_horaAtual();
                byte[] msg_padrao = format_msg.formatMSG(QUEUE_NAME," ", ByteString.copyFrom(msg.getBytes()), day_hour, Exchange);
  
                channel.queueDeclare(QUEUE_Send, false,   false,     false,       null);
                channel.basicPublish(Exchange, QUEUE_Send, null, msg_padrao);
          }
          
              
          }
  
        System.out.print(destino + ">>>");
        msg = input.nextLine();
      }
       
    channel.close();
    connection.close();  

      
    }

   

  }
