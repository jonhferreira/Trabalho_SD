package br.ufs.dcomp.ChatRabbitMQ;
import java.io.*;

import com.google.protobuf.ByteString;


public class FormatMSG {
    
    public byte[] formatMSG(String emissor, String tipo, ByteString msg, String day_hour, String grupo){
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

    public String RecebeProto(byte[] buffer) {
        try {
            MensagemProto.Mensagem Msg = MensagemProto.Mensagem.parseFrom(buffer);
            
            MensagemProto.Conteudo Cont = Msg.getConteudo();
            ByteString Corpo = Cont.getCorpo();
            String msg_recebida = Corpo.toString();
            return msg_recebida;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "Deu erro";
    }
}
