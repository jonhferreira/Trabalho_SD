package br.ufs.dcomp.ChatRabbitMQ;
import java.io.*;

import com.google.protobuf.ByteString;

import br.ufs.dcomp.ChatRabbitMQ.MensagemProto.Mensagem;


public class FormatMSG {
    
    public byte[] formatMSG_send(String emissor, String tipo, ByteString msg, String day_hour, String grupo){
        // String resul = day_hour +" "+ user.subSequence(1,user.length()) + " diz: " + msg; 
        // return resul;

        MensagemProto.Conteudo.Builder conteudo = MensagemProto.Conteudo.newBuilder();
        conteudo.setTipo(tipo);
        conteudo.setCorpo(msg);

        MensagemProto.Mensagem.Builder MsgBiulder = MensagemProto.Mensagem.newBuilder();
        MsgBiulder.setEmissor(emissor);
        MsgBiulder.setDatahora(day_hour);
        MsgBiulder.setGrupo(grupo);
        MsgBiulder.setConteudo(conteudo);

        MensagemProto.Mensagem Msg = MsgBiulder.build();
        byte[] buffer = Msg.toByteArray();
        
        return buffer;
    }

    

    public String formatMSG_receive(MensagemProto.Mensagem Msg) throws UnsupportedEncodingException{
        String day_hour = Msg.getDatahora();
        String grupo = Msg.getGrupo();
        
        String user = Msg.getEmissor();
        user = user.substring(1,user.length());

        MensagemProto.Conteudo conteudo = Msg.getConteudo();
        
        byte[] msg_by = conteudo.getCorpo().toByteArray();
        String msg = new String(msg_by, "UTF-8");
        

        String resul = day_hour +" "+ user +"#"+ grupo + " diz: " + msg; 
    
        return resul;
    }


    public String formatFILE_receive(MensagemProto.Mensagem Msg_file){
        String day_hour = Msg_file.getDatahora();
        String grupo = Msg_file.getGrupo();
            
        String user = Msg_file.getEmissor();

        String resul = day_hour+" Arquivo " + "recebido de "  + user +"#"+ grupo; 
        
        return resul;
    }


}
