import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

public class ClientThread extends Thread {

	protected Socket socket;
	protected QuestionAnswerHolder h;

	public ClientThread(Socket client) {
		this.socket = client;
	}

	@Override
	public void run() {
		// InputStream in = null;
		BufferedReader in = null;
		DataOutputStream out = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			return;
		}
		String line = null;
		try {
			h = new QuestionAnswerHolder();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Question q = null;
		String question = null;
		while (true) {
			System.out.println("infinite loop");
			try {
				line = in.readLine();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			if (line.equalsIgnoreCase("START")) {
				try {
					System.out.println("Start recieved");
					q = h.getRandomQuestion(0);
					question = q.getQuestion();
					out.writeBytes(question);
					out.flush();
					line = in.readLine();
					System.out.println("Message recieved");
					if (line.equalsIgnoreCase(q.getCorrectAnswer()))
						out.writeBytes("Correct");
					else if ((line == null) || line.equalsIgnoreCase("QUIT")) {
						socket.close();
						return;
					} else
						out.writeBytes(q.getCorrectAnswer());

				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				int nTF = 0;
				while (nTF < 19) {

					try {
						q = h.getRandomQuestion(0);
						line = in.readLine();
						if ((line == null) || line.equalsIgnoreCase("QUIT")) {
							socket.close();
							return;
						} else {
							question = q.getQuestion();
							out.writeBytes(question);
							out.flush();
						}
					} catch (SQLException e) {
						e.printStackTrace();
						return;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} // end of nTF WHILE
			}else{ // if START
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			
		}// end of infinite While
	}
}
