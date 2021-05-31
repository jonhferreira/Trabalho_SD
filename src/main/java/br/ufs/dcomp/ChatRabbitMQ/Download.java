package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;

public class Download extends Thread{
    

    public String QUEUE_NAME;
    public Channel channel_file;
    public byte[] arquivo;

    public Download(Channel channel_file, String q_name, byte[] arquivo) {
        this.channel_file = channel_file;
        this.QUEUE_NAME = q_name;    
        this.arquivo = arquivo; 
    }


    public void run() {
        Arquivos arq = new Arquivos();
        try{
            arq.gravarArquivo(this.arquivo,"teste");
            
        }catch(Exception e){
            System.out.println("erro ao baixar arquivo");
        }
    }


}
