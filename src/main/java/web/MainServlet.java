package web;

import util.GeneralUtil;
import util.GoogleDriveSpider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class MainServlet extends HttpServlet {

    private static final Runnable task = GoogleDriveSpider::new;
    private static Thread thread = null;
    private static final String TRUE = "true";
    private static final String FALSE = "false";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ((thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED) && req.getParameter("runUpdate").equals("yes")) {
            if (thread.getState() == Thread.State.TERMINATED) thread = new Thread(task);
            thread.start();
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        } else if ((thread.getState() == Thread.State.NEW || thread.getState() == Thread.State.TERMINATED)) {
            req.setAttribute("lockUpdate", FALSE);
            req.setAttribute("tableReady", TRUE);
            req.setAttribute("execTime", GoogleDriveSpider.execTime);
            req.setAttribute("errors", GeneralUtil.screenshotErrors.size());
        } else if ((thread.getState() == Thread.State.RUNNABLE)) {
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        }
        req.getRequestDispatcher("main.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (thread == null) thread = new Thread(task);
        else if ((thread.getState() == Thread.State.RUNNABLE)) {
            req.setAttribute("lockUpdate", TRUE);
            req.setAttribute("tableReady", FALSE);
        } else {
            req.setAttribute("lockUpdate", FALSE);
        }
        //TODO Google authorization https://coderoad.ru/15938514/Java-%D0%B8-Google-Spreadsheets-API-%D0%B0%D0%B2%D1%82%D0%BE%D1%80%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D1%8F-%D1%81-OAuth-2-0
        req.getRequestDispatcher("main.jsp").forward(req, resp);
    }
}
