package ch.axa;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.inject.Singleton;
import javax.sound.midi.Synthesizer;

import org.wildfly.security.audit.SyslogAuditEndpoint;

import ch.axa.ServiceQuizzes.UpdatedQuiz;

//CDI Framework
@Singleton
public class QuizDao {

	private List<Quiz> quizzes;
	private List<Game> games;

	public QuizDao() {

		quizzes = new ArrayList<>();
		games = new ArrayList<>();

		List<Question> questions = new ArrayList<>();
		Map<String, Boolean> answers = new HashMap<>();

		answers.put("yes", false);
		answers.put("no", true);
		answers.put("i don't know", false);
		answers.put("Sugma", true);

		questions.add(new Question("Is the earth round?", answers, 30));
		questions.add(new Question("Is Harambe alive?", answers, 10));
		questions.add(new Question("What's Ligma?", answers, 20));

		quizzes.add(new Quiz("React.js", new Date(), 1, questions));
		quizzes.add(new Quiz("PL/I", new Date(), 2, questions));
		quizzes.add(new Quiz("Java Datentypen", new Date(), 3, questions));
		quizzes.add(new Quiz("Codierungen", new Date(), 4, questions));
		quizzes.add(new Quiz("Datenanalyse", new Date(), 5, questions));
		quizzes.add(new Quiz("JavaScript", new Date(), 6, questions));

	}

	public Quiz createClonedQuiz(Quiz original) {
		List<Question> questions = new ArrayList<>();
		original.getQuestions().forEach(question -> questions
				.add(new Question(question.getQuestion(), question.getAnswers(), question.getTime())));

		return new Quiz(original.getTitle(), original.getDescription(), original.getDate(), original.getId(), questions,
				original.getCountPlayed());
	}

	public List<Quiz> getQuizzes() {
		return quizzes;
	}

	public Optional<Quiz> getQuizById(int id) {
		return quizzes.stream().filter(q -> q.getId() == id).findFirst();

	}

	public Optional<Question> getQuestionById(int id) {
		Optional<Question> found = Optional.empty();
		for (Quiz q : quizzes) {
			found = q.getQuestions().stream().filter(question -> question.getId() == id).findFirst();
		}
		return found;
	}

	public Optional<Game> createGame(int id) {
		Optional<Quiz> answer = getQuizById(id);

		if (answer.isPresent()) {
			Quiz quiz = answer.get();
			Quiz newQuiz = createClonedQuiz(quiz);

			// TODO: Fix the problem with status changing, NICELY
			Game createdGame = new Game(createPin(), newQuiz);
			games.add(createdGame);
			return Optional.of(createdGame);
		}
		return Optional.empty();
	}

	public int createPin() {
		Random r = new Random();

		// TODO: Define a better Loop
		while (true) {
			int pin = r.nextInt(900000) + 100000;
			if (!games.stream().filter(g -> g.getPin() == pin).findFirst().isPresent()) {
				return pin;
			}
		}

	}

	public Optional<Game> findGameByPin(int pin) {
		return games.stream().filter(g -> g.getPin() == pin).findFirst();
	}

	// TODO: Also check for Valid Status of Game
	public Optional<Player> validatePin(int pin, String name) {

		// Game with Given Pin exists and is waiting for players
		if (games.stream().filter(g -> g.getPin() == pin && g.getStatus() == 0).findFirst().isPresent()) {
			// Get the Game
			Game game = findGameByPin(pin).get();
			// Create New Player
			Player newPlayer = new Player(name, pin, generatePlayerId(game));
			game.addPlayer(newPlayer);

			return Optional.of(newPlayer);
		}
		System.out.println("game not found or already in proggress");
		return Optional.empty();
	}

	public int generatePlayerId(Game game) {
		Random r = new Random();

		while (true) {
			int id = r.nextInt(400) + 1;
			if (!game.getPlayers().stream().filter(p -> p.getId() == id).findFirst().isPresent()) {
				return id;
			}
		}
	}

	public Optional<Game> updateGame(int pin, int status) {

		// Get the Game
		Optional<Game> result = findGameByPin(pin);

		// Game with given Pin was found -> update Game Status
		if (result.isPresent()) {
			Game foundGame = result.get();
			foundGame.setStatus(status);

			return Optional.of(foundGame);
		}
		// Game doesn't Exist
		return Optional.empty();
	}

	public Optional<Game> updateQuestion(int pin, int questionStatus) {

		Optional<Game> game = findGameByPin(pin);

		// Game with given Pin was found -> update Question Status
		if (game.isPresent()) {
			Game foundGame = game.get();
			foundGame.getQuiz().getCurrentQuestion().setStatus(questionStatus);
			System.out.println("updateQuestion-> game Pin " + foundGame.getPin());

			return Optional.of(foundGame);
		}
		return Optional.empty();

	}

	public Optional<Game> submitPlayerAnswer(int pin, String answer) {
		Optional<Game> game = findGameByPin(pin);
		if (game.isPresent()) {
			Game g = game.get();
			g.getQuiz().getCurrentQuestion().getSubmittedAnswers().add(answer);

			for (String s : g.getQuiz().getCurrentQuestion().getSubmittedAnswers()) {
				System.out.println(s);
			}
			return Optional.of(g);
		}
		return Optional.empty();
	}

	public Optional<Player> updatePlayerScore(int pin, int score, int id) {
		Optional<Game> game = findGameByPin(pin);

		if (game.isPresent()) {
			Game g = game.get();

			for (Player p : g.getPlayers()) {
				if (p.getId() == id) {
					p.setScore(score);
					System.out.println("updating player score");

					return Optional.of(p);
				}
			}
		}
		return Optional.empty();
	}

	public Optional<Game> updateCurrentQuestion(int pin) {
		Optional<Game> game = findGameByPin(pin);

		if (game.isPresent()) {
			Quiz quiz = game.get().getQuiz();
			// CurrentQuestion has not been updated at this time
			int cqIndex = quiz.getQuestions().indexOf(quiz.getCurrentQuestion());

			System.out.println("cq Index : " + cqIndex);
			System.out.println("size: " + quiz.getQuestions().size());

			// Update the current Question
			if (cqIndex < quiz.getQuestions().size() - 1) {
				// set new Current Quetion
				quiz.setCurrentQuestion(quiz.getQuestions().get(cqIndex + 1));

				System.out.println(quiz.getCurrentQuestion().getQuestion());

			}
			// Finish game by setting status
			else {
				game.get().setStatus(2);

			}
			return game;
		}

		return Optional.empty();
	}

	public Optional<Quiz> updateQuiz(UpdatedQuiz quiz) {
		Optional<Quiz> foundQuiz = getQuizById(quiz.id);

		if (foundQuiz.isPresent()) {

			Quiz found = foundQuiz.get();

			for (Quiz q : quizzes) {
				if (q.getId() == quiz.id) {
					quizzes.remove(q);
					found.setTitle(quiz.title);
					found.setDescription(quiz.description);
					found.setQuestions(quiz.questions);
					quizzes.add(found);
				}
			}
			return Optional.of(found);
		}
		return Optional.empty();
	}

	public Optional<Question> deleteQuestion(int quizId, int questionId) {
		Optional<Quiz> foundQuiz = getQuizById(quizId);

		if (foundQuiz.isPresent()) {
			System.out.println("found the quiz");
			Optional<Question> foundQuestion = getQuestionById(questionId);

			if (foundQuestion.isPresent()) {
				System.out.println("found the question");
				foundQuiz.get().getQuestions().remove(foundQuestion.get());
				
				for (Quiz q :  quizzes) {
					System.out.println(q.getQuestions().size());
				}
				
				return foundQuestion;
			}
		}

		return Optional.empty();
	}
}
