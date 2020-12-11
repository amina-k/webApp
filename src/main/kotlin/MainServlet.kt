
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

//@WebServlet(name = "Home", value = ["/hello"])
class MainServlet : HttpServlet() {
    override fun doGet(req: HttpServletRequest, res: HttpServletResponse) {
        //res.writer.write("Hello, World!")
        res.setContentType("text/html")
        val out = res.getWriter()

        out.print("<html><body>")
        out.print("<h3>Fruit Mart</h3>")
        out.print("<h4>Servlet App Using Kotlin</h4>")
        out.print("</body></html>")

    }
}