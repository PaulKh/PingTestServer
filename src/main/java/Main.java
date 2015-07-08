import org.eclipse.jetty.server.Server;


/**
 * Created by Paul on 04/07/15.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new RequestHandler());
        server.start();
        server.join();
    }
}
