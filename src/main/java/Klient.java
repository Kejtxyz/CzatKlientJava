import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Klient {

    public static final int PORT = 5000;
    public static final String IP = "192.168.1.2";

    BufferedReader bufferedReader;
    String imie;

    // start programu
    public static void main(String[] args) {
        Klient k = new Klient();
        k.startKlienta();
    }

    // socket - gniazdo przechowujace polaczenia
    // uruchomienie klienta
    public void startKlienta() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Podaj imie");
        imie = sc.nextLine();
        // operacje ryzykowne, wyjatki
        try {
            Socket socket = new Socket(IP, PORT);
            System.out.println("Podlaczono do" + socket);

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread t = new Thread(new Odbiorca());
            t.start();

// obsluga wysylania
            while (true) {
                System.out.print(">> ");
                String str = sc.nextLine();
                if (!str.equalsIgnoreCase("q")) {
                    printWriter.println(imie + ":" + str);
                    printWriter.flush();
                } else {
                    printWriter.println(imie + "rozlaczyl sie");
                    printWriter.flush();
                    printWriter.close();
                    sc.close();
                    socket.close();
                }
            }
        } catch (Exception e) {
        }
    }

    class Odbiorca implements Runnable {
        @Override
        public void run() {
            String wiadomosc;
            try {
                while ((wiadomosc = bufferedReader.readLine()) != null) {
                    String subString[] = wiadomosc.split(":");
                    if (!subString[0].equals(imie)) {
                        System.out.println(wiadomosc);
                        System.out.println(">> ");
                    }
                    }
            } catch (Exception e) {
                System.out.println("Polaczenie zakonczone ");
            }
        }
    }
}