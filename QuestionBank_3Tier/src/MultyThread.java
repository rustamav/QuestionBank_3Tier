import java.net.*;
import java.io.*;
import java.sql.SQLException;

//import java.util.*;

/**
 * MultyThread class creates multithread server which is responsible for
 * responding to clients.
 * 
 * @author Abdykerim Erikov, Rustam Alashrafov
 * 
 */
public class MultyThread {
	static int nClients = 0;

	/**
	 * Main function for server launch.
	 * 
	 * @throws IOException
	 */
	public static void main(String argv[]) throws IOException {
		ServerSocket ss = new ServerSocket(2014);
		System.out.println("Server Started");
		while (true) {
			MultyThread.nClients++;
			new TinyHttpdConnection(ss.accept());
			System.out.println("New connection started: "
					+ MultyThread.nClients);
		}
	}
} // sending the socket returned from accept to thread

/**
 * TinyHttpdConnection class is responsible for a connection with a client. It
 * feeds the clients with questions from a database, receives their response,
 * calculates the score and sends it to a client at the end.
 * 
 * @author Abdykerim Erikov, Rustam Alashrafov
 * 
 */
class TinyHttpdConnection extends Thread {
	private Socket sock;
	private boolean isRunning;
	private int counter;
	private int TFScore;
	private int MCScore;
	private QuestionAnswerHolder h;
	private int clientNo;
	private String clientName;

	/**
	 * Default constructor for TinyHttpdConnection class.
	 * 
	 * @param s
	 *            Socket number
	 */
	TinyHttpdConnection(Socket s) {
		clientNo = MultyThread.nClients;
		sock = s;
		counter = 1;
		TFScore = 0;
		MCScore = 0;
		isRunning = true;
		clientName = "";
		try {
			h = new QuestionAnswerHolder();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setPriority(NORM_PRIORITY - 1);
		start();

	}
	/**
	 * Launch the connection.
	 */
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
					System.out.println("Connection " + clientNo
							+ " closed because client is not responding.");
					isRunning = false;
				} else
					switch (clientMessage) {
					case "REGUSER":
						pw.println("Name");
						clientMessage = br.readLine();
						clientName = clientMessage;
						break;
					case "QUIT":
						sock.close();
						System.out.println("Connection " + clientNo
								+ " closed because client quited.");
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
						} else if (counter == 30) {
							pw.println("STARTSCORE");
							clientMessage = br.readLine();
							if (clientMessage.equalsIgnoreCase("SENDSCORES"))
								pw.println(TFScore + "#" + MCScore + "#"
										+ clientName);
						}
						counter++;
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
						else if (counter <= 30 && counter >= 22) {
							pw.println(q.getCorrectAnswer() + "#"
									+ q.getExplanation());
							clientMessage = br.readLine();
							if (clientMessage.equalsIgnoreCase(q
									.getCorrectAnswer()))
								MCScore += 5;
						}
						break;
					}
			} catch (NullPointerException e) {
				isRunning = false;
				System.out.println("Connection " + clientNo
						+ " closed because client session ended.");
			} catch (SocketException e) {
				isRunning = false;
				System.out.println("Connection " + clientNo + " closed.");
				try {
					sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (SQLException e) {
				isRunning = false;
				System.out.println("Database failure.");
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		} // end of while
	}
}
