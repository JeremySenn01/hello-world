package ch.axa;

import java.util.Date;
import java.util.List;

public class Quiz {

	private String title;
	private String description;
	private Date date;
	private int id;
	private int countPlayed;
	private List<Question> questions;
	private Question currentQuestion;

	public Quiz(String title, Date date, int id, List<Question> questions) {
		super();
		this.title = title;
		this.date = date;
		this.id = id;
		this.description = "";
		this.countPlayed = 0;
		this.questions = questions;
		
		this.currentQuestion = this.questions.get(0);
}
	
	public Quiz(String title, String description, Date date, int id, List<Question> questions, int countPlayed) {
		this(title, date, id, questions);
		this.description = description;
		this.countPlayed = countPlayed;
		
		this.currentQuestion = this.questions.get(0);

	}

public Question getCurrentQuestion() {
	return currentQuestion;
}

public void setCurrentQuestion(Question currentQuestion) {
	this.currentQuestion = currentQuestion;
}

public List<Question> getQuestions() {
	return this.questions;
}

public void setQuestions(List<Question> q) {
	this.questions = q;
}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCountPlayed() {
		return countPlayed;
	}

	public void setCountPlayed(int countPlayed) {
		this.countPlayed = countPlayed;
	}

}
