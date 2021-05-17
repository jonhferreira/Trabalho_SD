package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Chat {
  public static void main(String[] argv) throws Exception {
    
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("54.160.68.39"); 
      factory.setUsername("jonh");
      factory.setPassword("12345"); 
      factory.setVirtualHost("/");

      
      
      Connection connection = factory.newConnection(); 
      Channel channel = connection.createChannel();
      
      Scanner input = new Scanner(System.in);
      
      String msg = ""; 
      String QUEUE_Send = "";

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
      String message = new String(body, "UTF-8"); 
      System.out.println(message);
      System.out.print(">>>");
      } 
    }; 
    //(queue-name, autoAck, consumer); 
    channel.basicConsume(QUEUE_NAME,true, consumer);
    
    System.out.print(QUEUE_Send + ">>>");
    msg = input.nextLine();
  
    while (!msg.equals("exit")) {
      
      if (msg.subSequence(0, 1).equals("@")){
        QUEUE_Send = msg;
        
      }else{ 
        String day_hour = data_hora.data_horaAtual();
        String msg_padrao = format_msg.formatMSG(msg, day_hour, QUEUE_NAME);

        channel.queueDeclare(QUEUE_Send, false,   false,     false,       null);
        channel.basicPublish("", QUEUE_Send, null, msg_padrao.getBytes("UTF-8"));
      }

      System.out.print(QUEUE_Send + ">>>");
      msg = input.nextLine();

      
    }

    channel.close();
    connection.close();

  }
}