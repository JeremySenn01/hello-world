package ch.axa;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/games")
@Singleton
public class ServiceGames {

	@Inject
	QuizDao dao;

	@GET
	@Path("/")
	@Produces({ "application/json" })
	public Response getGames() {
		return Response.status(404).build();
	}

	@GET
	@Path("/{pin}")
	@Produces({ "application/json" })
	public Response getGame(@PathParam("pin") int pin) {
		Optional<Game> game = dao.findGameByPin(pin);

		if (game.isPresent()) {
			return Response.status(200).entity(game.get()).build();
		}
		return Response.status(404).build();

	}

	@POST
	@Path("/")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response createGame(ReqQuiz quiz) {
		Optional<Game> createdGame = dao.createGame(quiz.getId());

		if (createdGame.isPresent()) {
			return Response.status(200).entity(createdGame.get()).build();
		}
		return Response.status(404).build();
	}

	@POST
	@Path("/enter")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response enterGameWithPin(PlayerReg playerReg) {
		Optional<Player> answer = dao.validatePin(playerReg.pin, playerReg.name);
		
		if (answer.isPresent()) {
			
			//Create Player and return it
			Player playerAnswer = answer.get();
			PlayerReg newPlayer = new PlayerReg();
			newPlayer.id = playerAnswer.getId();
			newPlayer.name = playerAnswer.getName();
			newPlayer.pin = playerAnswer.getGamePin();
			
			return Response.status(200).entity(newPlayer).build();
		} else {
			return Response.status(404).build();
		}
	}

	@POST
	@Path("/lobby")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response updateGameWithPin(UpdatedGame game) {
		Optional<Game> answer = dao.updateGame(game.pin, game.status);

		if (answer.isPresent()) {
			return Response.status(200).entity(answer.get()).build();
		}
		return Response.status(404).build();
	}

	@POST
	@Path("/question")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response updateQuestionState(UpdatedQuestion question) {
		Optional<Game> answer = dao.updateQuestion(question.pin, question.questionStatus);

		if (answer.isPresent()) {
			return Response.status(200).entity(answer.get()).build();
		}
		return Response.status(404).build();
	}
	
	@POST
	@Path("/submit")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response submitPlayerAnswer(PlayerAnswer answer) {
		Optional<Game> returned = dao.submitPlayerAnswer(answer.pin, answer.answer);

		if (returned.isPresent()) {
			return Response.status(200).entity(returned.get()).build();
		}
		return Response.status(404).build();
	}
	
	@POST
	@Path("/updatePlayer")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Response updatePlayerScore(PlayerReg player) {
		Optional<Player> updatedPlayer = dao.updatePlayerScore(player.pin, player.score, player.id);

		if (updatedPlayer.isPresent()) {
			player.score = updatedPlayer.get().getScore();
			player.name = updatedPlayer.get().getName();
			
			System.out.println("found");
			return Response.status(200).entity(player).build();
		}
		System.out.println("not found");
		return Response.status(404).build();
	}
	
	@GET
	@Path("/ucq/{pin}")
	@Produces({ "application/json" })
	public Response updateCurrentQuestion(@PathParam("pin") int pin) {
		Optional<Game> game = dao.updateCurrentQuestion(pin);

		if (game.isPresent()) {
			return Response.status(200).entity(game.get()).build();
		}
		return Response.status(404).build();

	}
	
}

class PlayerAnswer {
	
	public int pin;
	public String answer;
}

class UpdatedQuestion {
	
	public int pin;
	public int questionStatus;
}

class UpdatedGame {

	public int pin;
	public int status;

}

class PlayerReg {

	public int pin;
	public String name;
	public int score;
	public int id;

}

class ReqQuiz {

	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
