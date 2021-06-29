package br.ufs.dcomp.ChatRabbitMQ;

public class Bindings {
    String source;
    String vhost;
    String destination;
    String destination_type;
    String routing_key;
    Object arguments;
    String properties_key;

    public Bindings(){
        this.source = "";
        this.vhost = "";
        this.destination = "";
        this.destination_type = "";
        this.routing_key = "";
        this.arguments = new Object();
        this.properties_key = "";
    }
}
