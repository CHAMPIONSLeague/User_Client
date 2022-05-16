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
                        String risp = funzione.login();
                        if (risp.equals("logged")){

                            //TODO: Menu utente
                            while (m_user != 0){

                                System.out.println("1. Stampa Palinsesto");
                                System.out.println("2. Nuova prenotazione");
                                System.out.println("3. Cancella prenotazione");
                                System.out.println("4. Cambio username");
                                System.out.println("0. Exit");

                                m_user = Integer.parseInt(tastiera.readLine());
                                switch (m_user){
                                    case 1:
                                        funzione.stmpPalinsesto();
                                        break;
                                    case 2:
                                        //nuova prenotazione
                                        funzione.prenotazione();
                                        break;
                                    case 3:
                                        funzione.annullamentoPrenotazione();
                                        break;
                                    case 4:
                                        funzione.changeUser();
                                    default:
                                        System.out.println("Comando non valido");
                                        break;
                                }
                            }
                        }else if(risp.equals("admin logged")){
                            return;
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