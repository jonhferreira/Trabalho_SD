package br.ufs.dcomp.ChatRabbitMQ;

public class FormatMSG {
    
    public String formatMSG(String msg, String day_hour, String user){
        String resul = day_hour +" "+ user.subSequence(1,user.length()) + " diz: " + msg; 
    
        return resul;
    }
}
