package ch.axa;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Question {

	private static final int ANSWERING = 0;
	private static final int SHOWINGRESULTS = 1;
	private static final int SHOWINGRANKING = 2;

	private int id;
	private int status;
	private String question;
	private Map<String, Boolean> answers;
	private List<String> submittedAnswers;
	private int time;

	public Question(String question, Map<String, Boolean> answers, int time) {
		super();
		this.id = this.hashCode();
		this.status = 0;
		this.question = question;
		this.answers = answers;
		this.status = ANSWERING;
		this.time = time;

		this.submittedAnswers = new ArrayList<>();
	}

	public int getId() {
		return id;
	}


	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getSubmittedAnswers() {
		return submittedAnswers;
	}

	public void setSubmittedAnswers(List<String> submittedAnswers) {
		this.submittedAnswers = submittedAnswers;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Map<String, Boolean> getAnswers() {
		return answers;
	}

	public void setAnswers(Map<String, Boolean> answers) {
		this.answers = answers;
	}

}
