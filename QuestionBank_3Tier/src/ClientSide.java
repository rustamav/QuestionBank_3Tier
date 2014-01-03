import java.awt.EventQueue;
import java.io.*;
import java.net.*;

public class ClientSide {
	static Socket clientSocket = null;

	public static void main(String args[]) {
		try {
			clientSocket = new Socket("localhost", 2001);
			
			//  Creates a new PrintWriter, with automatic flushing, from an
			// existing OutputStream
		} catch (ConnectException ce) {
			System.out.println("Cannot connect to the server.");
		} catch (IOException ie) {
			System.out.println("I/O Error.");
		}
		InitialFrame frame = new InitialFrame(clientSocket);
		frame.setVisible(true);

	}
}
