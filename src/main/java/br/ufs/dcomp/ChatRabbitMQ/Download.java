package br.ufs.dcomp.ChatRabbitMQ;

import com.rabbitmq.client.Channel;

public class Download extends Thread{
    

    public byte[] arquivo;
    private String tipo_arq;
    private String msg;

    public Download(byte[] arquivo, String tipo, String msg) {   
        this.arquivo = arquivo; 
        this.tipo_arq = tipo;
        this.msg = msg;
    }


    public void run() {
        Arquivos arq = new Arquivos();
        try{
            String nome_arq = "teste."+this.tipo_arq;
            nome_arq = nome_arq.replace("/",".");
            arq.gravarArquivo(this.arquivo,nome_arq);
            System.out.println(this.msg);
            
        }catch(Exception e){
            System.out.println("erro ao baixar arquivo");
        }
    }


}
