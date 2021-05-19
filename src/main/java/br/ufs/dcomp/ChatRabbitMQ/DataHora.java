package br.ufs.dcomp.ChatRabbitMQ;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class DataHora {
    

    public String data_horaAtual(){
        DateTimeFormatter padrao = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String resul = padrao.format(LocalDateTime.now());
        resul = "("+resul.substring(0, 10) + " as" + resul.subSequence(10,resul.length())+")";
        return resul;
    }

}


