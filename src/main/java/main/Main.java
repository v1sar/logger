package main;

/**
 * Created by Дмитрий on 02.09.2016.
 */
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.*;


public class Main {
    private static String text = "This is a logger\r\nCreated via Java\r\n\r\n";
    private static String fileName = "log.txt";

    public static void main(String[] args) throws Exception {

        ServletHolder sh = new ServletHolder(ServletContainer.class);
        sh.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        sh.setInitParameter("com.sun.jersey.config.property.packages", "rest");
        sh.setInitParameter("com.sun.jersey.api.json.POJOMappingFeature", "true");

        Server server = new Server(9999);
        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);
        context.addServlet(sh, "/*");
        write(fileName, text);
        server.start();
        server.join();
    }

    public static void write(String fileName, String text) {
        File file = new File(fileName);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(file.getAbsoluteFile());
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String read() throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader( file.getAbsoluteFile()));
            try {
                String s;
                while ((s = in.readLine()) != null) {
                    sb.append(s);
                    sb.append("\n");
                }
            } finally {
                in.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    public static void update(String newText) throws FileNotFoundException {
        File file = new File(fileName);
        if (!file.exists()){
            throw new FileNotFoundException(file.getName());
        }
        StringBuilder sb = new StringBuilder();
        String oldFile = read();
        sb.append(oldFile);
        sb.append(newText);
        write(fileName, sb.toString());
    }

    public static String getFileName() {
        return fileName;
    }
}
