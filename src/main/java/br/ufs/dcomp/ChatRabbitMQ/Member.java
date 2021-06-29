package br.ufs.dcomp.ChatRabbitMQ;

public class Member {
    String source;
    String vhost;
    String destination;
    String destination_type;
    String routing_key;
    Object arguments;
    String properties_key;
    
    public Member() {
        this.source = "";
        this.vhost = "";
        this.destination = "";
        this.destination_type = "";
        this.routing_key = "";
        this.arguments = new Object();
        this.properties_key = "";
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestination_type() {
        return destination_type;
    }

    public void setDestination_type(String destination_type) {
        this.destination_type = destination_type;
    }

    public String getRouting_key() {
        return routing_key;
    }

    public void setRouting_key(String routing_key) {
        this.routing_key = routing_key;
    }

    public Object getArguments() {
        return arguments;
    }

    public void setArguments(Object arguments) {
        this.arguments = arguments;
    }

    public String getProperties_key() {
        return properties_key;
    }

    public void setProperties_key(String properties_key) {
        this.properties_key = properties_key;
    }
    
}
