/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Servlet Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloWorld
*/

package com.tieorange.todoapp.backend;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServlet extends HttpServlet {
  @Override public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    //resp.setContentType("text/plain");
    resp.getWriter().println("Please use the form to POST to this url");

    //PrintWriter writer = resp.getWriter();
    //String searchTerm = req.getParameter("term");
    //writer.println("You searched for: " + searchTerm);
  }

  @Override public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String name = req.getParameter("name");
    resp.setContentType("text/plain");
    if (name == null) {
      resp.getWriter().println("Please enter a name");
    }
    resp.getWriter().println("Hello " + name);
  }


}
