package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;
import com.google.protobuf.ByteString;

public class Upload extends Thread {
    
    public String QUEUE_NAME;
    public String QUEUE_SEND;
    public String Day_hour;
    public String Grupo;
    public Channel channel_file;
    public byte[] arquivo;

    public Upload(Channel channel_file, String q_name, String q_send, String day, String grupo, byte[] arquivo) {
        this.channel_file = channel_file;
        this.QUEUE_NAME = q_name;
        this.QUEUE_SEND = q_send;
        this.Day_hour = day;
        this.Grupo = grupo;
        this.arquivo = arquivo;
    }

    public void run() {
        FormatMSG msg = new FormatMSG();
        
        byte[] msg_padrao = msg.formatMSG_send(this.QUEUE_NAME, "", ByteString.copyFrom(this.arquivo), this.Day_hour, this.Grupo);
        try {
            channel_file.queueDeclare(this.QUEUE_SEND, false, false, false, null);
            channel_file.basicPublish(this.Grupo, this.QUEUE_SEND, null, msg_padrao);
        } catch (Exception  e) {
            System.err.println(e);
        }

    }


}
