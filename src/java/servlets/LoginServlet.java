package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import models.User;
import services.AccountService;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get the session
        HttpSession session = request.getSession();

        // Check if it is a logout requisition
        String logoutParam = request.getParameter("logout");
        if (logoutParam != null) {
            // Destroy the session
            session.invalidate();
        } else {

            // Gets user from the session (if not logged it will be null)
            User user = (User) session.getAttribute("user");

            if (user != null) {
                // User is Logged in
                response.sendRedirect("home");
                return; // Stop code call after redirect it
            }
        }

        // load the JSP
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        return; // Stop code call after send it to a jsp
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String message = "";

        if (!username.isEmpty() && !password.isEmpty()) {
            AccountService accountService = new AccountService();
            User user = accountService.login(username, password);
            if (user == null) {
                request.setAttribute("message", "Invalid credentials");
            } else {
                // Get the session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("home");
                return; // Stop code call after redirect it
            }
        }

        // load the JSP
        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        return;
    }
}
