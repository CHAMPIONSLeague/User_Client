import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Funzioni {
    public BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
    public JSONObject json = new JSONObject();
    JSONParser p = new JSONParser();
    private String ris = "";
    private String msg = "";
    private final String address = "http://clowncinema.altervista.org/src/user/";

    //todo: 18/05 funziona
    public String login(){
        try{
            System.out.println("Inserire l'username:");
            msg = tastiera.readLine();
            json.put("username", msg);

            System.out.println("Inserire password:");
            msg = tastiera.readLine();
            json.put("password", msg);

            //post request mi da {ris: "N"} che receiveParser mi returna come "N"
            ris = reciveParser(postRequest("http://clowncinema.altervista.org/src/login.php", json.toJSONString()));
            switch (ris) {
                case "N" -> {
                    System.out.println("Account inesistente, procedere alla registrazione...");
                    registrazione();
                }
                case "YU" -> {
                    System.out.println("Benvenuto " + json.get("username"));
                    return "logged";
                }
                case "YA" -> {
                    System.out.println("Ciao " + json.get("username") + ", usa il client dedicato agli admin!");
                    return "admin logged";
                }
                default -> System.out.println("Credenziali errate");
            }
        }catch(Exception e){
            System.out.println("Formato credenziali non valido");
        }
        return "not logged";
    }

    //todo: 18/05 funziona
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
            ris = reciveParser(postRequest("http://clowncinema.altervista.org/src/registrazione.php", json.toJSONString()));
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

    //todo: 18/05 funziona
    public void prenotazione(){
        stmpPalinsesto();
        try{
            System.out.println("Inserire il codice dello spettacolo:");
            msg = tastiera.readLine();
            json.put("cod_sp", msg);
            ris = reciveParser(postRequest(address+"prenotazione.php", json.toJSONString()));

            if(ris.equals("Y")){
                System.out.println("Prenotazione aggiunta");
            }else if (ris.equals("N")){
                System.out.println("Lo spettacolo scelto non esiste");
            }else {
                System.out.println(ris);
            }
        }catch (IOException e){
            System.out.println("Lo spettacolo scelto non esiste");
        }
    }

    //todo: 18/05 funziona
    public void changeUser(){
        try {
            System.out.println("Inserire la password dell'account");
            msg = tastiera.readLine();
            //username già caricato
            json.put("password", msg);
            json.put("cmd", "checkPass");

            ris = reciveParser(postRequest(address+"change_username.php",json.toJSONString()));

            if (ris.equals("Y")){ //credenziali corrette
                // permette al server di fare il cambio username
                json.put("cmd", "ch_user");

                System.out.println("Inserire il nuovo Username");
                msg = tastiera.readLine();
                json.put("new_user", msg);

                ris = reciveParser(postRequest(address+"change_username.php",json.toJSONString()));
                if (ris.equals("Y")){
                    System.out.println("Username aggiornato");
                    json.put("username", json.get("new_user"));
                }else if (ris.equals("N")){
                    System.out.println("Errore nel cambio di username");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Password errata");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e){
            System.out.println("Errore nel cambio di username");
        }
    }

    //todo: 18/05 funziona
    public void changeEmail(){
        try{
            System.out.println("Inserire la password");
            msg = tastiera.readLine();
            json.put("password", msg);
            json.put("cmd", "checkPass");

            ris = reciveParser(postRequest(address+"change_email.php", json.toJSONString()));

            if (ris.equals("Y")){ //credenziali corrette
                json.put("cmd", "ch_email");

                System.out.println("Inserire la nuova email");
                msg = tastiera.readLine();
                json.put("new_email", msg);

                ris = reciveParser(postRequest(address+"change_email.php", json.toJSONString()));
                if (ris.equals("Y")){
                    System.out.println("Email aggiornata con successo");
                    json.put("email", json.get("new_user"));
                }else if (ris.equals("N")){
                    System.out.println("Email occupata da qualcun'altro");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Password errata");
            }else {
                System.out.println(ris);
            }
        }catch (Exception e){
            System.out.println(ris);
        }

    }

    //todo: 18/05 funziona
    public void stmpPalinsesto(){
        String response = postRequest("http://clowncinema.altervista.org/src/stampaPalinsesto.php", "");
        response = response.replace("{","").replace("/", "").replace('}','\n').replace('"',' ').replace(","," ");
        System.out.println(response);
        postiDisp();
    }

    //todo: 23/05 funziona
    public void ricercaFilm(){
        JSONObject json_receive;
        try{
            System.out.println("Inserire il nome del film");
            msg = tastiera.readLine();
            json.put("nome_film", msg);

            String response = postRequest("http://clowncinema.altervista.org/src/stampa_film.php", json.toJSONString());
            json_receive = (JSONObject) p.parse(response);
            ris = reciveParser(response);
            if (ris.equals("Y")){
                System.out.println("ID: "+(String) json_receive.get("codice_film"));
                System.out.println("Nome: "+(String) json_receive.get("nome_film"));
                System.out.println("Durata: "+(String) json_receive.get("durata"));
                System.out.println("Descrizione: "+(String) json_receive.get("descrizione"));
                System.out.print("\n");
            }else{
                System.out.println(ris);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //todo: 23/05 funziona
    public void postiDisp(){
        JSONObject json_receive;
        try {
            String response = postRequest("http://clowncinema.altervista.org/src/stampa_posti_liberi_spettacolo.php", "");
            ris = reciveParser(response);
            if (ris.equals("Y")){
                json_receive = (JSONObject) p.parse(response);
                System.out.println("Spettacolo: "+json_receive.get("id")+" | Posti liberi: "+json_receive.get("posti_liberi")+"\n");
            }else{
                System.out.println(ris);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //todo: 23/05 funziona
    public void stmpPrenotazioni(){
        String response = postRequest(address+"stampa_prenotazioni.php", json.toJSONString());
        response = response.replace("{","").replace("/", "").replace('}','\n').replace('"',' ').replace(","," ");
        System.out.println(response);
    }

    //todo: 23/05 funziona
    public void annullamentoPrenotazione(){
        // fa la stampa delle prenotazioni tramite il nome dell'utente
        // poi bisogna chiedere il codice di quella che si vuole eliminare
        stmpPrenotazioni();
        try {
            System.out.println("Inserire il codice dello spettacolo relativo alla prenotazione da eliminare: ");
            msg = tastiera.readLine();
            json.put("cod_sp", msg);
            json.put("cmd", "ch_pr");

            ris = reciveParser(postRequest(address+"delete_prenotazione.php", json.toJSONString()));

            if (ris.equals("Y")){
                json.put("cmd", "del_pr");
                System.out.println("del_pr");
                ris = reciveParser(postRequest(address+"delete_prenotazione.php", json.toJSONString()));

                if (ris.equals("Y")){
                    System.out.println("Prenotazione eliminata!");
                }else if (ris.equals("N")){
                    System.out.println("Errore nell'eliminazione della prenotazione");
                }else{
                    System.out.println(ris);
                }
            }else if (ris.equals("N")){
                System.out.println("Non ci sono prenotazioni esistenti");
            }else{
                System.out.println(ris);
            }
        }catch (Exception e){
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
        }
        return response; //restituisce il dato richiesto
    }

    public String reciveParser(String s_response){
        JSONObject json_receive; //json utilizzato per ricevere dati specifici
        try {
            json_receive = (JSONObject) p.parse(s_response);
            ris = (String) json_receive.get("ris");
        }catch (ParseException e) {
            System.out.println("Errore di conversione");
        }
        return ris;
    }
}