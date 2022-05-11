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
    JSONParser p = new JSONParser();
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
            ris = reciveParser(postRequest("http://192.168.67.156/funzioni_cinema/src/login.php", json.toJSONString()));
            if (ris.equals("N")){
                System.out.println("Account inesistente, procedere alla registrazione...");
                registrazione();
            }else if(ris.equals("YU")){
                System.out.println("Benvenuto "+json.get("username"));
                return "logged";
            }else if (ris.equals("YA")){
                System.out.println("Ciao "+json.get("username") + ", usa il client dedicato agli admin!");
                return "admin logged";
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
            ris = reciveParser(postRequest("http://192.168.67.156/funzioni_cinema/src/registrazione.php", json.toJSONString()));
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

    public void stmpPalinsesto(){
        JSONObject json_receive; //json utilizzato per ricevere dati specifici
        String response = postRequest("http://192.168.67.156/funzioni_cinema/src/stampaPalinsesto.php", "");

        int spezzaRiga = 0;
        char[] testo = response.toCharArray();

        for (int i = 0; i< testo.length; i++){
            if (testo[i] == '{'){
                testo[i] = '|';
                if (spezzaRiga == 1){
                    testo[i] = '\n';
                    testo[i+1] = '|';
                    spezzaRiga = 0;
                }
            }
            if (testo[i] == '}'){
                spezzaRiga = 1;
                testo[i] = '|';
            }
            if (testo[i] == '"'){
                testo[i] = ' ';
            }
            if (testo[i] == ','){
                testo[i] = ' ';
            }
        }
        System.out.println(testo);
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