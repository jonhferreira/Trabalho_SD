package br.ufs.dcomp.ChatRabbitMQ;

public class ReceberProto {
    private FormatMSG formato = new FormatMSG();
    
    public String RecebeProto(byte[] buffer) {
        try {
            MensagemProto.Mensagem Msg = MensagemProto.Mensagem.parseFrom(buffer);
            
            String msg_recebida = this.formato.formatMSG_receive(Msg);
            return msg_recebida;
        } catch (Exception e) {
            System.err.println(e);
        }
        return "Deu erro";
    }

    public String RecebeProtoFile(byte[] buffer){
        
        try{
            MensagemProto.Mensagem Msg_file = MensagemProto.Mensagem.parseFrom(buffer);
       
            MensagemProto.Conteudo conteudo_file = Msg_file.getConteudo();
            byte[] arquivo_rec = conteudo_file.getCorpo().toByteArray();

            String tipo = conteudo_file.getTipo();

            String msg = this.formato.formatFILE_receive(Msg_file);

            Download file = new Download(arquivo_rec,tipo, msg);
            file.start();
            
            return msg;
        }catch(Exception e){
            return "erro de download";
        }
    }
}
