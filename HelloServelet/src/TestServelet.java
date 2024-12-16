import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
//Extract Mapping
@WebServlet(urlPatterns = "/test")
public class TestServelet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Test Servlet</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; }");
        out.println("h1 { color: blue; }");
        out.println("h2 { color: green; }");
        out.println("h3 { color: red; }");
        out.println("button { color: pink; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hello Servlet</h1>");
        out.println("<h2>Hello Servlet man awaaaaaaa</h2>");
        out.println("<h3>Happy birthday pasn ayiya</h3>");
        out.println("<button>I love youu</button>");
        out.println("</body>");
        out.println("</html>");
    }
}
