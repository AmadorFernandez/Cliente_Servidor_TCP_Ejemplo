import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	
	
	static final int PUERTO = 5000;

	public Servidor() throws IOException {
		ServerSocket skSRV = null;
		try {
		
			skSRV = new ServerSocket(PUERTO); //Amarramos el servidor al puerto
			System.out.println("Oreja del servidor pegada a: " + skSRV.getLocalSocketAddress().toString());
			
			int nCli = 0;
			while (true) { 
				Socket skAtencion = skSRV.accept(); //Bloquea a la espera de una solicitud
				
				nCli++;
				System.out.println("Escuchando al cliente número " + nCli);
				
				new HiloServidor(skAtencion, nCli).start(); //Crea un nuevo hilo y le pasa el trabajo con el cliente
			}
		} catch (IOException e) {
			System.err.println("Error: puerto ocupado");
			skSRV.close();
		}
	}
	
	class HiloServidor extends Thread {
		Socket elSocket = null;
		int id = 0;
		String msg = "";
		
		public HiloServidor(Socket skAtencion, int nCli) {
			//
			this.elSocket = skAtencion;
			this.id = nCli;
		}
		
		@Override
		public void run() {
			PrintWriter escritor = null;
			try {
				//Engancha al hilo servidor para la lectura del socket
				InputStreamReader inputStreamReader = new InputStreamReader(elSocket.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(inputStreamReader);
				//Lee el mensaje
				msg = br.readLine();
				System.out.println("Mensaje recibido: " + msg);
				
				//Engancha a la entrada y escribe la respuesta y como el cliente esta escuchando leera
				escritor = new PrintWriter(elSocket.getOutputStream(), true);
				escritor.println(this.id + " :" + msg + " devuelto desde el servidor");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			if (escritor != null) {
				escritor.flush();
				escritor.close();
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			new Servidor();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
