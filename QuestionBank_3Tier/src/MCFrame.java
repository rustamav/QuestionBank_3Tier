import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

/**
 * MCFrame creates window to ask multiple choice questions. When answered, it
 * checks the correct answer and provides feedback. Each correct answer is worth
 * 5 points.
 * 
 * @author Rustam Alashrafov, Abdykerim Erikov
 * 
 */
public class MCFrame extends JFrame implements ActionListener {

	Socket clientSocket;
	boolean answerRequested;
	private PrintWriter pw;
	private BufferedReader br;
	String serverMessage = null;

	private Question q;
	private int counter;
	private String userAnswer;

	private JRadioButton rb1;
	private JRadioButton rb2;
	private JRadioButton rb3;
	private JRadioButton rb4;
	private JRadioButton rb5;
	private ButtonGroup bGroup;
	private JLabel lblQuestion;
	private JButton btnShowAnswer;
	private JCheckBox cbExpl;
	private JLabel lblCorrectAnswer;
	private JTextArea textArea;
	private JButton btnNextQuestion;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public void run() {
		try {
			MCFrame frame = new MCFrame(clientSocket);
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame.
	 */
	public MCFrame(Socket clientSocket) {
		super("Multiple Choice Questions");
		answerRequested = false;
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

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, Global.SCREEN_WIDTH, Global.SCREEN_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		counter = 1;

		pw.println("NEXTQUESTION");
		try {
			serverMessage = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] parsedMessage = serverMessage.split("#", 6);

		rb1 = new JRadioButton(parsedMessage[1]);
		rb2 = new JRadioButton(parsedMessage[2]);
		rb3 = new JRadioButton(parsedMessage[3]);
		rb4 = new JRadioButton(parsedMessage[4]);
		rb5 = new JRadioButton(parsedMessage[5]);

		rb1.setActionCommand("rb1");
		rb2.setActionCommand("rb2");
		rb3.setActionCommand("rb3");
		rb4.setActionCommand("rb4");
		rb5.setActionCommand("rb5");

		rb1.addActionListener(this);
		rb2.addActionListener(this);
		rb3.addActionListener(this);
		rb4.addActionListener(this);
		rb5.addActionListener(this);

		rb1.setSelected(true);
		userAnswer = rb1.getText();

		bGroup = new ButtonGroup();
		bGroup.add(rb1);
		bGroup.add(rb2);
		bGroup.add(rb3);
		bGroup.add(rb4);
		bGroup.add(rb5);
		lblQuestion = new JLabel(counter + ". " + parsedMessage[0]);

		btnShowAnswer = new JButton("Show Answer");
		btnShowAnswer.addActionListener(this);

		cbExpl = new JCheckBox("Show Explanation");
		cbExpl.setActionCommand("ExplBox");
		cbExpl.addActionListener(this);
		cbExpl.setEnabled(false);

		lblCorrectAnswer = new JLabel();
		lblCorrectAnswer.setVisible(false);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setVisible(false);
		textArea.setToolTipText("");

		btnNextQuestion = new JButton("Next Question");
		btnNextQuestion.addActionListener(this);
		GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout
				.setHorizontalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(55)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addComponent(
																lblQuestion)
														.addComponent(rb1)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								rb2)
																						.addComponent(
																								rb3)
																						.addComponent(
																								rb4)
																						.addComponent(
																								rb5))
																		.addGap(38)
																		.addGroup(
																				groupLayout
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblCorrectAnswer)
																						.addComponent(
																								textArea,
																								GroupLayout.PREFERRED_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								groupLayout
																										.createSequentialGroup()
																										.addGroup(
																												groupLayout
																														.createParallelGroup(
																																Alignment.TRAILING)
																														.addComponent(
																																btnShowAnswer)
																														.addComponent(
																																cbExpl))
																										.addGap(18)
																										.addComponent(
																												btnNextQuestion)))))
										.addGap(46)));
		groupLayout
				.setVerticalGroup(groupLayout
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								groupLayout
										.createSequentialGroup()
										.addGap(18)
										.addComponent(lblQuestion)
										.addGap(39)
										.addComponent(rb1)
										.addGap(2)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addComponent(
																				rb2)
																		.addGap(3)
																		.addComponent(
																				rb3)
																		.addGap(3)
																		.addComponent(
																				rb4)
																		.addGap(3)
																		.addComponent(
																				rb5))
														.addGroup(
																groupLayout
																		.createSequentialGroup()
																		.addGap(17)
																		.addComponent(
																				lblCorrectAnswer)
																		.addGap(2)
																		.addComponent(
																				cbExpl)
																		.addGap(2)
																		.addComponent(
																				textArea,
																				GroupLayout.PREFERRED_SIZE,
																				71,
																				GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(
												ComponentPlacement.RELATED, 32,
												Short.MAX_VALUE)
										.addGroup(
												groupLayout
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																btnShowAnswer)
														.addComponent(
																btnNextQuestion))
										.addContainerGap()));
		contentPane.setLayout(groupLayout);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String[] parsedMessage;
		String bName = e.getActionCommand();
		if (cbExpl.isSelected())
			textArea.setVisible(true);
		else
			textArea.setVisible(false);
		switch (bName) {
		case "rb1":
			userAnswer = rb1.getText();
			break;
		case "rb2":
			userAnswer = rb2.getText();
			break;
		case "rb3":
			userAnswer = rb3.getText();
			break;
		case "rb4":
			userAnswer = rb4.getText();
			break;
		case "rb5":
			userAnswer = rb5.getText();
			break;

		case "Show Answer":
			rb1.setEnabled(false);
			rb2.setEnabled(false);
			rb3.setEnabled(false);
			rb4.setEnabled(false);
			rb5.setEnabled(false);
			pw.println("GETANSWER");
			try {
				serverMessage = br.readLine();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			parsedMessage = serverMessage.split("#", 2);
			pw.println(userAnswer);
			lblCorrectAnswer.setText(parsedMessage[0]);
			lblCorrectAnswer.setVisible(true);
			textArea.setText(parsedMessage[1]);
			cbExpl.setVisible(true);
			cbExpl.setEnabled(true);
			answerRequested = true;
			btnShowAnswer.setEnabled(false);

			break;
		case "Next Question":

			if (!answerRequested) {
				pw.println("GETANSWER");
				try {
					serverMessage = br.readLine();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				parsedMessage = serverMessage.split("#", 2);
				pw.println(userAnswer);
				lblCorrectAnswer.setText(parsedMessage[0]);
				lblCorrectAnswer.setVisible(true);
				cbExpl.setText(parsedMessage[1]);
				cbExpl.setEnabled(true);
				// answerRequested = true;

			}
			pw.println("NEXTQUESTION");
			try {
				serverMessage = br.readLine();
			} catch (IOException e1) {

				e1.printStackTrace();
			}

			System.out.println(serverMessage);
			if (serverMessage.equalsIgnoreCase("STARTSCORE")) {

				ScoreFrame sFrame = new ScoreFrame(clientSocket);

				sFrame.setVisible(true);
				this.dispose();
			} else {
				counter++;
				parsedMessage = serverMessage.split("#", 6);
				lblQuestion.setText(counter + ". " + parsedMessage[0]);
				lblCorrectAnswer.setVisible(false);
				textArea.setVisible(false);
				cbExpl.setEnabled(false);
				cbExpl.setSelected(false);
				rb1.setText(parsedMessage[1]);
				rb2.setText(parsedMessage[2]);
				rb3.setText(parsedMessage[3]);
				rb4.setText(parsedMessage[4]);
				rb5.setText(parsedMessage[5]);
				userAnswer = rb1.getText();
				rb1.setEnabled(true);
				rb2.setEnabled(true);
				rb3.setEnabled(true);
				rb4.setEnabled(true);
				rb5.setEnabled(true);
				rb1.setSelected(true);
				cbExpl.setVisible(false);
				btnShowAnswer.setEnabled(true);
				answerRequested = false;
			}
			break;
		}

	}

}
