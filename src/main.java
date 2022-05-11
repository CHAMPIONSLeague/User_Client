import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json_send = new JSONObject(); //json utilizzato per inviare dati all'api interessata

        int scelta = -1;
        int m_user = -1;
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
                        if (funzione.login().equals("logged")){

                            //menu utente
                            while (m_user != 0){
                                System.out.println("1. Prenota");
                                System.out.println("2. ");
                            }
                        }else{
                            System.out.println();
                        }
                        break;
                    case 2: //REGISTRAZIONE
                        funzione.registrazione();
                        break;
                    default:
                        System.out.println("Comando non valido");
                        break;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}