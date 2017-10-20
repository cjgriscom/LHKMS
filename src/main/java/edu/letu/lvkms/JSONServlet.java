package edu.letu.lvkms;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class JSONServlet
 */
@WebServlet("/datahandler")
public class JSONServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final JSONObject testViews = new JSONObject();
	private static final JSONObject LongviewHall_Left = new JSONObject();
	private static final JSONObject LongviewHall_Right = new JSONObject();
	static {
		testViews.append("views", new JSONArray(Arrays.asList(new String[] {
			"LongviewHall_Left",
			"LongviewHall_Right"
		})));
		
		LongviewHall_Left.append("buttonBox", new JSONArray(Arrays.asList(new JSONObject[] {
			new JSONObject("{\"Slideshows\":\"\"}"),
			new JSONObject("{\"TETRIS\":\"\"}")
		})));
	}
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JSONServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletOutputStream os = response.getOutputStream();
		String command = request.getParameter("command");
		System.out.println(command);
		if (command != null) {
			if (command.equals("getViews")) {

				os.println(testViews.toString(2));
			} else {
				os.println(new JSONObject("{\"error\":\"invalid command\"}").toString());
			}
			
		} else {
			os.println(new JSONObject("{\"error\":\"no command specified\"}").toString());
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// protected void doPost(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {

	// }

}
