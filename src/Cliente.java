import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {
	
	static final String HOST = "localhost"; //Si no se pone nada coge por defecto localhost
	static final int PUERTO = 5000; //Puerto por el que escucha el servidor Es todo oidos :)
	Scanner entrada = new Scanner(System.in); //Recogera la cadena dada por el usuario pero podria ser cualquier cosa
	
	public Cliente() throws UnknownHostException {
		Socket skCliente; //El objeto compartido entre cliente y servidor
		String msg = null;
		try {
			skCliente = new Socket(HOST, PUERTO); //Prepara el socket
			PrintWriter escritor = new PrintWriter(skCliente.getOutputStream(), true); //Establece el flujo de escritura
			msg = dameMensaje(); //Solicita el mensaje al usuario
			escritor.println(msg); //Lo escribe en el socket
		
			
			//Prepara el flujo de lectura para la respuesta del servidor
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(skCliente.getInputStream(), "utf-8"));
			//Lee la respuesta y la imprime
			msg = bufferedReader.readLine();
			System.out.println("Cadena de respuesta: " + msg);
			
			//Dejamos todo limpito y cerrado
			escritor.flush();
			escritor.close();
			bufferedReader.close();
		} catch (IOException e) {
			System.err.println("Error de conexión al socket");
		}
	}
	
	private String dameMensaje() {
		System.out.println("Introduce el mensaje a enviar: ");
		String mensaje = entrada.nextLine();
		return mensaje;
	}

	public static void main(String[] args) {
		try {
			new Cliente();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
