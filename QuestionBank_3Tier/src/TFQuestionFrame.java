import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * TFQuestionFrame creates window to ask true-false questions that are requested
 * from the server. When answered, the server checks the correct answer and
 * provides feedback. Each correct answer is worth 3 points.
 * 
 * @author Rustam Alashrafov, Abdykerim Erikov
 * 
 */
public class TFQuestionFrame extends JFrame implements ActionListener {

	private JPanel pTFQuestion;
	private JLabel lblQuestion;
	private JButton bTrue;
	private JButton bFalse;
	private JButton bNext;
	private int counter;
	private TFQuestionFrame frame;
	private JLabel lFeedback;
	private Socket clientSocket;
	private PrintWriter pw;
	private BufferedReader br;
	private String serverMessage = null;

	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			frame = new TFQuestionFrame(clientSocket);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 * 
	 * @param clientSocket
	 */
	public TFQuestionFrame(Socket clientSocket) {
		super("True/False Questions");
		this.clientSocket = clientSocket;

		InputStream clientIn = null;
		OutputStream clientOut = null;
		try {
			clientIn = clientSocket.getInputStream();
			clientOut = clientSocket.getOutputStream();
		} catch (IOException e) {

			e.printStackTrace();
		}

		pw = new PrintWriter(clientOut, true);

		br = new BufferedReader(new InputStreamReader(clientIn));

		boolean noResponse = true;
		String serverMessage = null;
		while (noResponse) {
			try {
				pw.println("NextQuestion");

				serverMessage = br.readLine();

			} catch (IOException e) {

				e.printStackTrace();
			}

			if (serverMessage != null)
				noResponse = false;
		}
		counter = 1;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
		pTFQuestion = new JPanel();
		pTFQuestion.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(pTFQuestion);

		lblQuestion = new JLabel(counter + " " + serverMessage);

		bTrue = new JButton("True");
		bTrue.addActionListener(this);

		bFalse = new JButton("False");
		bFalse.addActionListener(this);

		bNext = new JButton("Next");
		bNext.addActionListener(this);

		lFeedback = new JLabel();
		lFeedback.setVisible(false);
		lFeedback.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout gl_pTFQuestion = new GroupLayout(pTFQuestion);
		gl_pTFQuestion
				.setHorizontalGroup(gl_pTFQuestion
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_pTFQuestion
										.createSequentialGroup()
										.addGroup(
												gl_pTFQuestion
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_pTFQuestion
																		.createSequentialGroup()
																		.addGap(212)
																		.addComponent(
																				bTrue,
																				GroupLayout.PREFERRED_SIZE,
																				339,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																gl_pTFQuestion
																		.createSequentialGroup()
																		.addGap(212)
																		.addComponent(
																				bFalse,
																				GroupLayout.PREFERRED_SIZE,
																				339,
																				GroupLayout.PREFERRED_SIZE))
														.addGroup(
																gl_pTFQuestion
																		.createSequentialGroup()
																		.addGap(212)
																		.addGroup(
																				gl_pTFQuestion
																						.createParallelGroup(
																								Alignment.TRAILING,
																								false)
																						.addComponent(
																								lFeedback,
																								Alignment.LEADING,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								bNext,
																								Alignment.LEADING,
																								GroupLayout.DEFAULT_SIZE,
																								339,
																								Short.MAX_VALUE)))
														.addComponent(
																lblQuestion,
																GroupLayout.DEFAULT_SIZE,
																964,
																Short.MAX_VALUE))
										.addContainerGap()));
		gl_pTFQuestion.setVerticalGroup(gl_pTFQuestion.createParallelGroup(
				Alignment.LEADING).addGroup(
				gl_pTFQuestion
						.createSequentialGroup()
						.addGap(1)
						.addComponent(lblQuestion, GroupLayout.PREFERRED_SIZE,
								147, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(bTrue, GroupLayout.PREFERRED_SIZE, 62,
								GroupLayout.PREFERRED_SIZE)
						.addGap(51)
						.addComponent(bFalse, GroupLayout.PREFERRED_SIZE, 63,
								GroupLayout.PREFERRED_SIZE)
						.addGap(45)
						.addComponent(bNext, GroupLayout.PREFERRED_SIZE, 66,
								GroupLayout.PREFERRED_SIZE)
						.addGap(43)
						.addComponent(lFeedback, GroupLayout.PREFERRED_SIZE,
								34, GroupLayout.PREFERRED_SIZE).addGap(33)));
		pTFQuestion.setLayout(gl_pTFQuestion);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean noResponse = true;
		String buttonName = e.getActionCommand();
		switch (buttonName) {
		case "True":
		case "False":
			bTrue.setEnabled(false);
			bFalse.setEnabled(false);
			noResponse = true;
			while (noResponse) {
				try {
					pw.println(buttonName);
					serverMessage = br.readLine();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				if (serverMessage != null)
					noResponse = false;
			}
			if (serverMessage.equalsIgnoreCase("CORRECT")) {
				lFeedback.setText("Correct!");
				lFeedback.setVisible(true);
			} else if (serverMessage.equalsIgnoreCase("WRONG")) {
				noResponse = true;
				while (noResponse) {
					try {
						pw.println("GetAnswer");
						serverMessage = br.readLine();
					} catch (IOException e1) {

						e1.printStackTrace();
					}

					if (serverMessage != null)
						noResponse = false;
				}

				lFeedback.setText("Wrong! Correct answer is " + serverMessage);
				lFeedback.setVisible(true);

			}
			break;

		case "Next":
			counter++;
			noResponse = true;
			while (noResponse) {
				try {
					pw.println("NextQuestion");
					serverMessage = br.readLine();
				} catch (IOException e1) {

					e1.printStackTrace();
				}

				if (serverMessage != null)
					noResponse = false;
			}

			if (serverMessage.equalsIgnoreCase("STARTMC")) {
				MCFrame mcFrame = new MCFrame(clientSocket);
				mcFrame.setVisible(true);
				this.dispose();
			}

			lFeedback.setVisible(false);
			lblQuestion.setText(counter + " " + serverMessage);
			bTrue.setEnabled(true);
			bFalse.setEnabled(true);
			break;
		}

	}
} // end of actionPerformed()
