package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;

public class Download extends Thread{
    

    public String QUEUE_NAME;
    public Channel channel_file;
    public byte[] body;

    public Download(Channel channel_file, String q_name, byte[] body) {
        this.channel_file = channel_file;
        this.QUEUE_NAME = q_name;    
        this.body = body; 
    }


    public void run() {
        Arquivos arq = new Arquivos();
        arq.gravarArquivo(this.body, "teste.pdf");
    }


}
