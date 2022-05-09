import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json_send = new JSONObject(); //json utilizzato per inviare dati all'api interessata
        String risp = "";
        String msg = "";

        int scelta = 100;
        while(scelta != 0){
            try{
                System.out.println("1. Login");
                System.out.println("2. Registrazione");
                System.out.println("0. Exit");
                scelta = Integer.parseInt(tastiera.readLine());

                //le funzioni mandano i dati al server, ma sanno gia' dove devono andare
                //le post request avranno link specifici in base alla destinazione dei dati
                //es. mando user e pass, nel post request le mando al login DAL CLIENT
                switch (scelta){
                    case 0: //Chiusura
                        System.out.println("Client in chiusura...");
                        break;
                    case 1: //LOGIN
                        try{
                            System.out.println("Inserire l'username:");
                            msg = tastiera.readLine();
                            json_send.put("username", msg);

                            System.out.println("Inserire password:");
                            msg = tastiera.readLine();
                            json_send.put("password", msg);

                            //fa il login richiamando il metodo postRequest
                            risp = funzione.login(json_send);
                            //System.out.println(risp);

                            //se non trova l'account lo fa registrare
                            if(risp.equals("N")){
                                System.out.println("Account inesistente, procedere alla registrazione...");
                                System.out.println("Username e password inseriti in precedenza non saranno da reinserire");

                                System.out.println("Inserire l'email: ");
                                msg = tastiera.readLine();
                                json_send.put("email", msg);

                                risp = funzione.registrazione(json_send);
                                if (risp.equals("N")){
                                    System.out.println("Utente non registrato!");
                                }else{
                                    System.out.println("Registrazione completata!");
                                }
                            }else{
                                System.out.println("Benvenuto!");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 2: //REGISTRAZIONE
                        try {
                            System.out.println("Inserire l'email:");
                            msg = tastiera.readLine();
                            json_send.put("email",msg);

                            System.out.println("Inserire l'username:");
                            msg = tastiera.readLine();
                            json_send.put("username", msg);

                            System.out.println("Inserire password:");
                            msg = tastiera.readLine();
                            json_send.put("password", msg);

                            //fa la registrazione richiamando la funzione
                            funzione.registrazione(json_send);

                        }catch (IOException e){
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
