import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Funzioni {
    public BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    public JSONObject json = new JSONObject();
    private String ris = "";

    public String login(){
        try{
            System.out.println("Inserire l'username:");
            String msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            //post request mi da {ris: "N"} che receiveParser mi returna come "N"
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/login.php", json.toJSONString()));
            if (ris.equals("N")){
                System.out.println("Account inesistente, procedere alla registrazione...");
                registrazione();
            }else if(ris.equals("Y")){
                System.out.println("Benvenuto "+json.get("username"));
                return "logged";
            }
        }catch(Exception e){
            System.out.println("Formato credenziali non valido");
        }
        return "not logged";
    }

    public void registrazione(){
        try{
            System.out.println("Inserire l'username:");
            String msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            System.out.println("Inserire l'email: ");
            msg = tastiera.readLine();
            json.put("email", msg);

            //post request mi da {ris: "N"} che receiveParser mi returna come "N"
            ris = reciveParser(postRequest("http://localhost/Server_Cinema/src/registrazione.php", json.toJSONString()));
            if (ris.equals("N")){
                System.out.println("Utente non registrato!");
            }else if (ris.equals("Y")){
                System.out.println("Registrazione completata!");
            }
        }catch(Exception e){
            System.out.println("Formato credenziali errato");
            e.printStackTrace();
        }
    }

    public static String postRequest(String indirizzo, String messaggio){
        String response = "";

        if(messaggio != null){
            //messaggio pieno
            try {
                URL uri = new URL(indirizzo);
                HttpURLConnection con = (HttpURLConnection) uri.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setDoOutput(true);

                OutputStream output = con.getOutputStream();
                output.write(messaggio.getBytes());
                output.flush(); // Dichiara la fine del body
                output.close();
                int rCode = con.getResponseCode();
                if(rCode == 200){
                    DataInputStream input = new DataInputStream(con.getInputStream()); //dati server
                    String in = "";
                    while((in = input.readLine()) != null){
                        response = in; //risposta server
                    }
                    input.close();
                }else{
                    System.out.println("Richiesta non andata a buon fine");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //messaggio vuoto
            System.out.println("Messaggio vuoto");
            //TODO: spiegare meglio cosa si intende per messaggio vuoto
        }

        return response; //restituisce il dato richiesto
    }

    public String reciveParser(String s_response){
        JSONObject json_receive; //json utilizzato per ricevere dati specifici
        JSONParser p = new JSONParser();
        String ris = "";
        
        try {
            json_receive = (JSONObject) p.parse(s_response);
            ris = (String) json_receive.get("ris");
        }catch (ParseException e) {
            System.out.println("Errore di conversione");
        }
        return ris;
    }

}