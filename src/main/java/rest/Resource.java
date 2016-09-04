package rest;

/**
 * Created by Дмитрий on 02.09.2016.
 */

import main.Main;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Path("/log")
public class Resource {

    @GET
    @Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "Hello, world!";
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String logIt(@Context HttpServletRequest request){
        LocalDateTime timePoint = LocalDateTime.now();
        Map<String, String[]> parameters = request.getParameterMap();
        Set<String> keys = parameters.keySet();
        for (String key: keys) {
            System.out.println(timePoint+" "+key);
            for (String values: request.getParameterValues(key)) {
                System.out.println(" " + values);
                try { Main.update(timePoint + " key:" + key + " value:" + values + "\r\n"); }
                catch (IOException e) {throw new RuntimeException(e); }
            }
        }
        return "Logged..";
    }

    @GET
    @Path("/file")
    @Produces(MediaType.TEXT_HTML)
    public String allLog(@Context HttpServletRequest request){
        try { String temp = Main.read();
            return temp;}
        catch (IOException e)  { throw new RuntimeException(e);
        }
    }
}
