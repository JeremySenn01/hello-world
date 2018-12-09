package ch.axa;

import java.util.Map;
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

@Path("/quizzes")
@Singleton
public class ServiceQuizzes {

	@Inject
	QuizDao dao;

	@GET
	@Path("/")
	@Produces({ "application/json" })
	public Response getQuizzes() {
		return Response.status(200).entity(dao.getQuizzes()).build();
	}

	@GET
	@Path("/{id}")
	@Produces({ "application/json" })
	public Response getQuiz(@PathParam("id") int id) {

		Optional<Quiz> foundQuiz = dao.getQuizById(id);
		// "not null"
		if (foundQuiz.isPresent()) {
			Quiz q = foundQuiz.get();
			return Response.status(200).entity(q).build();

		}
		return Response.status(404).build();

	}

	@POST
	@Path("/edit")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response updateQuestion(UpdatedQuiz quiz) {

		Optional<Quiz> foundQuiz = dao.updateQuiz(quiz);
		// "not null"
		if (foundQuiz.isPresent()) {
			Quiz q = foundQuiz.get();
			return Response.status(200).entity(q).build();

		}
		return Response.status(404).build();

	}

	@GET
	@Path("/edit/{quizId}+{questionId}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Response deleteQuestion(@PathParam("quizId") int quizId, @PathParam("questionId") int questionId) {
		Optional<Question> deletedQuestion = dao.deleteQuestion(quizId, questionId);
		
		if (deletedQuestion.isPresent()) {
			return Response.status(200).build();
		}
		return Response.status(404).build();
	}

	class UpdatedQuestion {

		public String title;
		public String description;
		public Map<String, Boolean> answers;
		public int time;
		
	}

}
