import org.json.simple.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class main {
    public static void main(String[] args) {
        Funzioni funzione = new Funzioni();
        BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
        JSONObject json_send = new JSONObject(); //json utilizzato per inviare dati all'api interessata
        String risp = "";

        int scelta = -1;
        int m_user = -1;
        while(scelta != 0){
            try{
                System.out.println("1. Login");
                System.out.println("2. Registrazione");
                System.out.println("0. Exit");
                scelta = Integer.parseInt(tastiera.readLine());

                switch (scelta) {
                    case 0 -> System.out.println("Client in chiusura...");
                    case 1 -> {
                        risp = funzione.login();
                        if (risp.equals("logged")) {

                            //TODO: Menu utente
                            while (m_user != 0) {

                                System.out.println("1. Stampa Palinsesto");
                                System.out.println("2. Cerca un film");
                                System.out.println("3. Nuova prenotazione");
                                System.out.println("4. Cancella prenotazione");
                                System.out.println("5. Stampa prenotazioni");
                                System.out.println("6. Cambio username");
                                System.out.println("7. Cambio email");
                                System.out.println("0. Exit");

                                m_user = Integer.parseInt(tastiera.readLine());
                                switch (m_user) {
                                    case 0 -> System.out.println("LOGOUT");
                                    case 1 -> funzione.stmpPalinsesto();
                                    case 2 -> funzione.ricercaFilm();
                                    case 3 -> funzione.prenotazione();
                                    case 4 -> funzione.annullamentoPrenotazione();
                                    case 5 -> funzione.stmpPrenotazioni();
                                    case 6 -> funzione.changeUser();
                                    case 7 -> funzione.changeEmail();
                                    default -> System.out.println("Comando non valido");
                                }
                            }
                        } else if (risp.equals("admin logged")) {
                            return;
                        }
                    }
                    case 2 -> //REGISTRAZIONE
                            funzione.registrazione();
                    default -> System.out.println("Comando non valido");
                }
            }catch (Exception e){
                System.out.println("Comando non valido");
            }
        }
    }
}