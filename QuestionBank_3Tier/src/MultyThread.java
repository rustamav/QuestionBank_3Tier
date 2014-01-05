import java.net.*;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class MultyThread {
	static int nClients = 0;

	public static void main(String argv[]) throws IOException {
		ServerSocket ss = new ServerSocket(2008);
		System.out.println("Server Started");
		while (true) {
			MultyThread.nClients++;
			new TinyHttpdConnection(ss.accept());
			System.out.println("New connection started: " + MultyThread.nClients);
		}
	}
} // sending the socket returned from accept to thread

class TinyHttpdConnection extends Thread {
	Socket sock;
	boolean isRunning;
	int counter;
	int TFScore;
	int MCScore;
	QuestionAnswerHolder h;
	int name;
	String user;
	TinyHttpdConnection(Socket s) {
		name = MultyThread.nClients;
		sock = s;
		counter = 1;
		TFScore = 0;
		MCScore = 0;
		isRunning = true;
		user="";
		try {
			h = new QuestionAnswerHolder();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setPriority(NORM_PRIORITY - 1);
		start();

	}

	public void run() {
		Question q = null;
		while (isRunning) {
			try {
				OutputStream out = sock.getOutputStream();
				PrintWriter pw = new PrintWriter(out, true);
				InputStream clientIn = sock.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						clientIn));
				String clientMessage = br.readLine().toUpperCase();
				
				if (clientMessage == null) {
					sock.close();
					System.out.println("Connection " +  name + " closed because client is not responding");
					isRunning = false;
				} else
					switch (clientMessage) {
					case "REGUSER":
						pw.println("Name");
						clientMessage = br.readLine();
						user = clientMessage;
						break;
					case "QUIT":
						sock.close();
						System.out.println("Connection " + name + " closed because client quited");
						isRunning = false;
						break;
					case "NEXTQUESTION":
						if (counter <= 20) {
							q = h.getRandomQuestion(0);
							pw.println(q.getQuestion());
						} else if (counter == 21)
							pw.println("STARTMC");
						else if (counter <= 29) {
							q = h.getRandomQuestion(1);
							pw.println(q.getQuestion() + "#" + q.getA() + "#"
									+ q.getB() + "#" + q.getC() + "#"
									+ q.getD() + "#" + q.getE());
						}
						else if (counter == 30){
							pw.println("STARTSCORE");
							clientMessage = br.readLine();
							if (clientMessage.equalsIgnoreCase("SENDSCORES"))
								pw.println(TFScore + "#" + MCScore + "#" + user);
						}
						counter++;
						// TODO
						break;
					case "TRUE":
					case "FALSE":
						if (clientMessage
								.equalsIgnoreCase(q.getCorrectAnswer())) {
							pw.println("Correct");
							TFScore += 3;
						} else
							pw.println("Wrong");
						break;
					case "GETANSWER":
						if (counter <= 21)
							pw.println(q.getCorrectAnswer());
						else if (counter <= 30 && counter>=22) {
							pw.println(q.getCorrectAnswer() + "#"
									+ q.getExplanation());
							clientMessage = br.readLine();
							if (clientMessage.equalsIgnoreCase(q.getCorrectAnswer()))
								MCScore += 5;
						}
						break;
					}
			} catch (NullPointerException e) {
				isRunning = false;
				System.out.println("Connection " + name + " closed because client session ended.");
			} catch (SocketException e) {
				isRunning = false;
				System.out.println("Connection " + name + " closed.");
				try {
					sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				isRunning = false;
				System.out.println("Database failure");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		} // end of while
	}
}
