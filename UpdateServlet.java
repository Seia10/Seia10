package project.simplechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/project/simplechat/update")
public class UpdateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		ServletContext context = this.getServletContext();
		Chat chat = (Chat) context.getAttribute("chat");
		if (chat == null) {
			chat = new Chat();
			context.setAttribute("chat", chat);
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		if (user != null) {
			ServletContext context = this.getServletContext();
			Chat chat = (Chat) context.getAttribute("chat");
			StringBuilder builder = new StringBuilder();
			builder.append("{");
			builder.append("\"user\":");
			toJson(user, builder);
			builder.append(",");
			builder.append("\"statementList\":[");
			Iterator<Statement> statementIterator = chat.getStatementList().iterator();
			if (statementIterator.hasNext()) {
				Statement statement = statementIterator.next();
				toJson(statement, builder);
			}
			while (statementIterator.hasNext()) {
				builder.append(",");
				Statement statement = statementIterator.next();
				toJson(statement, builder);
			}
			builder.append("]");
			builder.append("}");
			String json = builder.toString();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter writer = response.getWriter();
			writer.append(json);
			writer.flush();
		}
	}

	private static void toJson(User user, StringBuilder builder) {
		builder.append("{");
		builder.append("\"name\":\"").append(user.getName()).append("\"");
		builder.append("}");
	}

	private static void toJson(Statement statement, StringBuilder builder) {
		builder.append("{");
		builder.append("\"user\":");
		toJson(statement.getUser(), builder);
		builder.append(",");
		builder.append("\"message\":\"").append(statement.getMessage()).append("\",");
		builder.append("\"img\":\"").append(statement.getImgBase64()).append("\"");
		builder.append("}");
	}

}
