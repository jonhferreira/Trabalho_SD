package br.ufs.dcomp.ChatRabbitMQ;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Arquivos {
    public byte[] lerArquivo(String caminho) throws FileNotFoundException{
        
        try {
            InputStream arquivo = new FileInputStream(caminho); 
        
            byte[] buffer;
            buffer = arquivo.readAllBytes();
            arquivo.close();
            
            return buffer;
        } catch (IOException e) {
            return "erro!".getBytes();
        }
        
    }

    public void gravarArquivo(byte[] arquivo, String nome){
        try {
            OutputStream arq = new FileOutputStream(nome);
            arq.write(arquivo);
            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
