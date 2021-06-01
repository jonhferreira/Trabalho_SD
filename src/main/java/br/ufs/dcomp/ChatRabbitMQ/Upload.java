package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.protobuf.ByteString;

public class Upload extends Thread {
    
    public String QUEUE_NAME;
    public String QUEUE_SEND;
    public String Day_hour;
    public String Grupo;
    public Channel channel_file;
    public String cam_arquivo;
    private String msg_text_inicial;
    private String msg_text_final;

    public Upload(Channel channel_file, String q_name, String q_send, String day, String grupo, String arquivo) {
        this.channel_file = channel_file;
        this.QUEUE_NAME = q_name;
        this.QUEUE_SEND = q_send;
        this.Day_hour = day;
        this.Grupo = grupo;
        this.cam_arquivo = arquivo;
        
        this.msg_text_inicial = "Enviando "+ this.cam_arquivo + " para " + this.QUEUE_SEND + "#" + grupo;
        this.msg_text_final = "Arquivo " + this.cam_arquivo + "foi enviado para " +this.QUEUE_SEND + "#" + grupo;
    }

    public void run() {
        System.out.println();
        System.out.println(this.msg_text_inicial);
        FormatMSG msg = new FormatMSG();
        Arquivos arq = new Arquivos();
        byte[] arquivo;

        try {
            
            arquivo = arq.lerArquivo(this.cam_arquivo);
            Path source = Paths.get(this.cam_arquivo);
            String tipoMime = Files.probeContentType(source);
            
    
            byte[] msg_padrao = msg.formatMSG_send(this.QUEUE_NAME, tipoMime, ByteString.copyFrom(arquivo), this.Day_hour, this.Grupo);
            
            channel_file.queueDeclare(this.QUEUE_SEND, false, false, false, null);
            channel_file.basicPublish(this.Grupo, this.QUEUE_SEND, null, msg_padrao);
            
            System.out.println();
            System.out.println(this.msg_text_final);
        } catch (Exception e) {
            System.out.println("Erro!");
        }

    }


}
