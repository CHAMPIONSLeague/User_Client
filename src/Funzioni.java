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

    public String login(JSONObject json){
        return postRequest("http://localhost/Server_Cinema/src/login.php", json.toJSONString());
    }

    public String registrazione(JSONObject json){
        return postRequest("http://localhost/Server_Cinema/src/registrazione.php", json.toJSONString());
    }

    public static String postRequest(String indirizzo, String messaggio){
        String response = "";
        String ris = ""; //stringa che riceve i dati inviati dall'api
        JSONObject json_receive = new JSONObject(); //json utilizzato per ricevere dati specifici
        JSONParser p = new JSONParser();

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
                    DataInputStream input = new DataInputStream(con.getInputStream());
                    String in = "";
                    while((in = input.readLine()) != null){
                        response = in;
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

        try {
            json_receive = (JSONObject) p.parse(response);
            ris = (String) json_receive.get("ris");
        }catch (ParseException e) {
            System.out.println("Errore di conversione");
        }

        return ris; //restituisce il dato richiesto
    }
}
