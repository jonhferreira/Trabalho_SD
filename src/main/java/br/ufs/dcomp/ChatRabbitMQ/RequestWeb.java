package br.ufs.dcomp.ChatRabbitMQ;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class RequestWeb {

    public String getJason(String rota){
        try {
            
            // JAVA 8 como pré-requisito (ver README.md)
            
            String username = "jonh";
            String password = "12345";
     
            String usernameAndPassword = username + ":" + password;
            String authorizationHeaderName = "Authorization";
            String authorizationHeaderValue = "Basic " + java.util.Base64.getEncoder().encodeToString( usernameAndPassword.getBytes() );
     
            // Perform a request
            String restResource = "http://ec2-52-5-165-193.compute-1.amazonaws.com";
            Client client = ClientBuilder.newClient();
            Response resposta = client.target( restResource )	
                .path(rota)
            	.request(MediaType.APPLICATION_JSON)
                .header( authorizationHeaderName, authorizationHeaderValue ) // The basic authentication header goes here
                .get();     // Perform a post with the form values
           
            if (resposta.getStatus() == 200) {
            	String json = resposta.readEntity(String.class);
                return json;
            } else {
                return "Falha na requisisao";
            }   
		} catch (Exception e) {
			return "erro!";
		}
    }
}

