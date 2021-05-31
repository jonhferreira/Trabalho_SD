package br.ufs.dcomp.ChatRabbitMQ;

import com.google.protobuf.ByteString;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Scanner;

public class Chat {
  
  public static String destino = "";
  public static void main(String[] argv) throws Exception {
    
      ConnectionFactory factory = new ConnectionFactory();
      factory.setHost("52.3.255.23"); 
      factory.setUsername("jonh");
      factory.setPassword("12345"); 
      factory.setVirtualHost("/");

      
      
      Connection connection = factory.newConnection();

      // define o canal para envio de mensagens e outro para envio de arquivos 
      Channel channel_msg = connection.createChannel();
      Channel channel_file = connection.createChannel();
      
      Scanner input = new Scanner(System.in);
      
      String msg = ""; 

      // fila atual de envio e fila de arquivo
      String QUEUE_Send = "";
      String queue_file = "";

      // exchange de envio atual e exchange de arquivo
      String Exchange = "";
      String exchange_file = "";

      // utilizada para manipulacao de arquivos
      Arquivos arq = new Arquivos();
      

      // contem as funcoes relacionadas aos comando do chat
      Command command = new Command();

      // fornecera a data e a hora no formato requerido
      DataHora data_hora = new DataHora();
      // formata a mensagem para envio
      FormatMSG format_msg = new FormatMSG();
      
      // define fila para recebimento de mensagens
      System.out.print("usuario: "); 
      String QUEUE_NAME = input.nextLine();
      QUEUE_NAME = "@" + QUEUE_NAME;

      //(queue-name, durable, exclusive, auto-delete, params);
      channel_msg.queueDeclare(QUEUE_NAME, false, false, false, null); 

      // define fila para recebimento de arquivos
      String QUEUE_FILE = QUEUE_NAME+"_file";
      channel_file.queueDeclare(QUEUE_FILE, false, false, false, null); 
      
      
      
      // definindo consumidores de mensagens e arquivos
      Consumer consumer_msg = new DefaultConsumer(channel_msg) { 
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
          System.out.println();
          String message = format_msg.RecebeProto(body);
          System.out.println(message);
          System.out.print(destino + ">>>");
        }
      }; 
      
      Consumer consumer_file = new DefaultConsumer(channel_file){
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {    
          byte[] arquivo_rec = format_msg.RecebeProtoFile(body);
          Download file = new Download(channel_file, QUEUE_FILE, arquivo_rec);
          file.start();
          
          System.out.println("Salvo");
          System.out.print(destino + ">>>");
        }
      };
      
      
      //(queue-name, autoAck, consumer); 
      channel_msg.basicConsume(QUEUE_NAME,true, consumer_msg);
      channel_file.basicConsume(QUEUE_FILE,true, consumer_file);
      
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
            command.addGroup(channel_msg, msg_par[1], QUEUE_NAME);
                exchange_file = msg_par[1] + "_file";
                command.addGroup(channel_file, exchange_file, QUEUE_FILE);
                break;
            case 2:
                command.addUser(channel_msg, msg_par[2], msg_par[1]);
                queue_file = msg_par[1]+"_file";
                exchange_file = msg_par[2] + "_file";
                command.addUser(channel_file, exchange_file, queue_file);
                
                break;
            case 3:
                command.delFromGroup(channel_msg, msg_par[2], msg_par[1]);
                queue_file = msg_par[1]+"_file";
                exchange_file = msg_par[2] + "_file";
                command.delFromGroup(channel_file, exchange_file, queue_file);
                break;
            case 4:
                command.removeGroup(channel_msg, msg_par[1]);
                exchange_file = msg_par[2] + "_file";
                command.removeGroup(channel_file, exchange_file);
                break;
            case 5:

                if (Exchange.equals("")){
                  System.out.println("grupo vazio");
                  exchange_file = "";
                  queue_file = QUEUE_Send + "_file";
                } else{
                  System.out.println("fila vazia");
                  exchange_file = Exchange + "_file";
                  queue_file = "";
                }
               
                
                String day_hour = data_hora.data_horaAtual();
                
                byte[] arquivo = arq.lerArquivo(msg_par[1]);
                
                command.upload(channel_file, QUEUE_NAME, queue_file, day_hour, exchange_file, arquivo);
                
                break;
            
            // aqui vai a parte de formatacao que falta
                // tambem tem que ver a parte de recebimento
                
            default:
                String day_hour2 = data_hora.data_horaAtual();
                byte[] msg_padrao = format_msg.formatMSG_send(QUEUE_NAME," ", ByteString.copyFrom(msg.getBytes()), day_hour2, Exchange);
                
                channel_msg.queueDeclare(QUEUE_Send, false,   false,     false,       null);
                channel_msg.basicPublish(Exchange, QUEUE_Send, null, msg_padrao);
          }
          
          
        }
        
        System.out.print(destino + ">>>");
        msg = input.nextLine();
      }
      
      channel_msg.close();
      channel_file.close();
      connection.close();  
      
      
    }

   

  }
