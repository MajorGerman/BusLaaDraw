package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import tools.DataStorer;
import tools.PairBuilder;


@WebServlet(name = "DrawServlet", urlPatterns = {
    "/sendDataToServer",
    "/getDataFromServer",
    "/getGameCode",
    "/joinGame"
    })
public class DrawServlet extends HttpServlet {
    
    private Map<Integer, DataStorer> map = new HashMap();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session;
        
        String path = request.getServletPath();
        String json = "";
        
        switch (path) {
            case "/sendDataToServer":
                session = request.getSession(false);
                if (session == null){
                    break;
                }
                if (session.getAttribute("gameCode").equals("") ||
                        session.getAttribute("gameMaster").equals(false)){
                    break;
                }
                DataStorer store = map.get(session.getAttribute("gameCode"));
                
                JsonReader jsonReader = Json.createReader(request.getReader());
                
                JsonArray array = jsonReader.readArray();
                JsonObject obj;

                for (int i = 0; i < array.size(); i++) {
                    obj = array.getJsonObject(i);
                    int x = obj.getInt("x");
                    int y = obj.getInt("y");
                    store.append(x, y);
                }                             
                break;
                
            case "/getDataFromServer":
                session = request.getSession(false);
                if (session == null){
                    break;
                }
                if (session.getAttribute("gameCode").equals("")) {
                    break;
                }
                store = map.get(session.getAttribute("gameCode"));
                JsonObjectBuilder job = Json.createObjectBuilder();
                JsonArrayBuilder jab = Json.createArrayBuilder();
                store.getList().forEach((pair) -> {
                    jab.add(new PairBuilder().createJsonPair(pair));
                });
                json = jab.build().toString();
                break;
               
            case "/getGameCode":
                int gameCode;
                do {
                    gameCode = (int)Math.round(Math.random()*10000+10000);
                } while(map.containsKey(gameCode));
                session = request.getSession(true);
                session.setAttribute("gameCode", gameCode);
                session.setAttribute("gameMaster", true);
                map.put(gameCode, new DataStorer());
                
                job = Json.createObjectBuilder();
                
                json = job.add("gameCode", gameCode)
                        .add("response", true)
                        .build()
                        .toString();                   
                break;
             
            case "/joinGame":
                jsonReader = Json.createReader(request.getReader());
                
                obj = jsonReader.readObject();
                
                gameCode = Integer.parseInt(obj.getString("gameCode"));
                
                if (!map.containsKey(gameCode)) {
                    break;
                }
                
                session = request.getSession(true);
                
                session.setAttribute("gameCode", gameCode);
                session.setAttribute("gameMaster", false);
                
                job = Json.createObjectBuilder();
                
                json = job.add("gameCode", gameCode)
                        .add("response", true)
                        .build()
                        .toString();                

        }
        if(json != null && !"".equals(json)) {                    
            try (PrintWriter out = response.getWriter()) {
                out.println(json);           
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
