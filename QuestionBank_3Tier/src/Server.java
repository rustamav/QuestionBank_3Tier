import java.net.*;
import java.sql.SQLException;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket server = null;
		Socket client;
		QuestionAnswerHolder h = null;
		try {
			h = new QuestionAnswerHolder();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			server = new ServerSocket(2001);
			// 1234 is an unused port number
		} catch (IOException ie) {
			System.out.println("Cannot open socket.");
			System.exit(1);
		}

		while (true) {
			try {
				client = server.accept(); // returns to new socket
			} finally {
				// server.close();
			}
			// new thread for a client
			new ClientThread(client).start();
		}
	}
}