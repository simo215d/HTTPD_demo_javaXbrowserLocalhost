package sample;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.stream.Collectors;

public class Server {
    public static void main(String[] args) {

        try {

            System.out.println("Serveren er startet og lytter på port 80");

            ServerSocket serverSocket = new ServerSocket(80); // Serverobjektet instansieres
            Socket socket = serverSocket.accept();                  // Serveren åbner port 80 for forbindelser

            // Vi læser en stream med bogstaver fra browserens request in igennem socketen
            BufferedReader request = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Vi skriver headers som respons vha. dette PrintWriter objekt
            PrintWriter responsHeaders = new PrintWriter(socket.getOutputStream());

            // Vi skriver HTML som respons vha. dette DataOutputStream objekt
            DataOutputStream responseStream = new DataOutputStream(socket.getOutputStream());

            // Vi indlæser alle linjer i den indkomne requests
            while (true) {

                String input = request.readLine();
                System.out.println("" + input);

                // En blank linje i HTTP er det, der slutter requesten
                if (input.isEmpty()) {

                    System.out.println("Request modtaget, svar sendes nu.");

                    // Vi konstruerer og sender en HTTP response linje for linje
                    responsHeaders.println("HTTP/1.1 200 OK"); // HTTP Status kode
                    responsHeaders.println("Content-Type: text/html; charset=utf-8"); // Typen af svaret er text eller html
                    responsHeaders.println(); // Blank line between headers and content, very important !
                    responsHeaders.flush();

                    //denne linje er lavet er Andras og Kelvin
                    //responseStream.writeUTF(new URL("<H1>hey bros</H1>").getFile()); // Indholdet i responsen
                    //næste 3 linjer lavet af mig og man kan udskrive min Test.html fil som er smart.
                    InputStream in = Server.class.getClassLoader().getResourceAsStream("sample/Test.html");
                    String s = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
                    responsHeaders.println(s);
                    responseStream.flush();

                    // Vi lukker begge streams
                    responsHeaders.close();
                    responseStream.close();
                    System.out.println("Svar er sendt til browseren.");

                }
            }
        }
        catch (SocketException e) {
            if (e.getMessage().equals("Socket closed"))
                System.out.println("Forbindelsen afsluttet efter en succesfuld response-request forløb.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
