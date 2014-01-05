/**
 * Question class holds true-false and multiple choice questions.
 * 
 * @author Rustam Alashrafov, Abdykerim Erikov
 * 
 */
public class Question {
	private String question;
	private String a;
	private String b;
	private String c;
	private String d;
	private String e;
	private String correctAnswer;
	private String explanation;
	private int type;

	/**
	 * True-false question constructor.
	 * 
	 * @param question
	 *            The question
	 * @param answer
	 *            The answer to the question
	 */
	public Question(String question, String answer) {
		this.question = question;
		this.correctAnswer = answer;
		this.type = 0;
	}

	/**
	 * Multiple choice question constructor.
	 * 
	 * @param question
	 *            The question
	 * @param a
	 *            The variant A
	 * @param b
	 *            The variant B
	 * @param c
	 *            The variant C
	 * @param d
	 *            The variant D
	 * @param e
	 *            The variant E
	 * @param explanation
	 *            The explanation to the answer
	 * @param correctAnswer
	 *            The answer to the question
	 */
	public Question(String question, String a, String b, String c, String d,
			String e, String explanation, String correctAnswer) {
		this.correctAnswer = a;
		this.question = question;
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
		this.explanation = explanation;
		this.correctAnswer = correctAnswer;
		this.type = 1;
	}

	/**
	 * Get the question.
	 * 
	 * @return question
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Set the question.
	 * 
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	/**
	 * Get the variant A.
	 * 
	 * @return variant A
	 */
	public String getA() {
		return a;
	}

	/**
	 * Set the variant A.
	 * 
	 * @param a
	 */
	public void setA(String a) {
		this.a = a;
	}

	/**
	 * Get the variant B.
	 * 
	 * @return variant B
	 */
	public String getB() {
		return b;
	}

	/**
	 * Set the variant B.
	 * 
	 * @param b
	 */
	public void setB(String b) {
		this.b = b;
	}

	/**
	 * Get the variant C.
	 * 
	 * @return variant C
	 */
	public String getC() {
		return c;
	}

	/**
	 * Get the variant C.
	 * 
	 * @param c
	 */
	public void setC(String c) {
		this.c = c;
	}

	/**
	 * Get the variant D.
	 * 
	 * @return variant D
	 */
	public String getD() {
		return d;
	}

	/**
	 * Set the variant D.
	 * 
	 * @param d
	 */
	public void setD(String d) {
		this.d = d;
	}

	/**
	 * Get the variant E.
	 * 
	 * @return variant E
	 */
	public String getE() {
		return e;
	}

	/**
	 * Set the variant E.
	 * 
	 * @param e
	 */
	public void setE(String e) {
		this.e = e;
	}

	/**
	 * Get the correct Answer.
	 * 
	 * @return the correct answer
	 */
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	/**
	 * Set the correct answer.
	 * 
	 * @param correctAnswer
	 */
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	/**
	 * Get the explanation.
	 * 
	 * @return explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * Set the explanation.
	 * 
	 * @param explanation
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	/**
	 * Get the type of the question.
	 * 
	 * @return type (0 or 1)
	 */
	public int getType() {
		return type;
	}

	/**
	 * Set the type of the question.
	 * 
	 * @param type
	 *            (0 or 1)
	 */
	public void setType(int type) {
		this.type = type;
	}
}
