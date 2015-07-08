import com.google.gson.Gson;
import model.Ping;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by Paul on 04/07/15.
 */
public class RequestHandler extends AbstractHandler {

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("application/json;charset=utf-8");
        if (request.getPathInfo().startsWith("/ping")){
            handlePing(baseRequest, request, response);
//            response.setStatus(HttpServletResponse.SC_OK);
//            baseRequest.setHandled(true);
//            Ping ping = new Ping(120, InetAddress.getByName("10.10.10.1"), InetAddress.getByName("10.10.10.2"));
//            response.getWriter().println(new Gson().toJson(ping));
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            response.getWriter().println("{\"error\":\"Bad request\"}");
        }
    }

    private void handlePing(Request baseRequest,
                            HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        if (request.getPathInfo().equals("/ping/all") && request.getMethod().equals("GET")){
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            List<Ping> pings = DatabaseHandler.instance().getAll();
            response.getWriter().println(new Gson().toJson(pings));
        }
        else if(request.getPathInfo().equals("/ping/new") && request.getMethod().equals("POST")){
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            Ping ping = new Gson().fromJson(readPostData(request), Ping.class);
            DatabaseHandler.instance().addNewPing(ping);
            response.getWriter().println("{\"success\":\"OK\"}");
        }
        else{
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            baseRequest.setHandled(true);
            response.getWriter().println("{\"error\":\"Bad request\"}");
        }
    }
    private String readPostData(HttpServletRequest request){
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = request.getReader();
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String data = buffer.toString();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
