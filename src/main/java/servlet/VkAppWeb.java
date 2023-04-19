package servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.net.URIBuilder;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.Thread;



import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class VkAppWeb extends HttpServlet {

    private static User user;
    private static String token;
    private static List<User> userlist;
    private static Database database;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();

        user = new User("не указано", 0);
        token = "21d0273e21d0273e21d0273e7622c31bc5221d021d0273e45cf71ee1461612f7bde0402";
        userlist = new ArrayList<User>();

        database = new Database();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Enter doGet");

        String action = request.getParameter("action");
        request.setAttribute("user", user);

        if ("update".equals(action))
            request.getRequestDispatcher("/update.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //System.out.println("Enter doPost");

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        
        if ("submit".equals(action)) {
            Integer id = 0;
            try {
                id = (request.getParameter("id").equals("") ? 0 : Integer.parseInt(request.getParameter("id")));
                System.out.println(id);
            } catch (java.lang.NumberFormatException e) {
                e.printStackTrace();
            }
            user = new User("не указано", id);
            setProfile(user);
            user.setFriends(getFriends(user.getId()));
            user.setGroups(getGroups(user.getId()));
            
            request.setAttribute("user", user);
            userlist.add(user);
            System.out.println(userlist);
            
            try {
                List<String> commons = getCommon(user);
                request.setAttribute("commongroups", commons.get(0));
                request.setAttribute("commoncity", commons.get(1));
                request.setAttribute("commonwork", commons.get(2));
                request.setAttribute("commonuniversity", commons.get(3));
                request.setAttribute("commonschool", commons.get(4));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            request.setAttribute("groups", user.getGroups().stream().map(g -> "<li class=\"sub2\"><a href=\"https://vk.com/"+g.getDomain()+"\">"+g.getName()+"</a></li>").collect(Collectors.joining("")));
            request.setAttribute("friends", user.getFriends().stream().map(f -> "<li class=\"sub2\"><a href=\"https://vk.com/id"+f.getId()+"\">"+f.getName()+"</a>&nbsp;id:"+f.getId()+"</li>").collect(Collectors.joining("")));
            request.setAttribute("gcount", user.getGroups().size());
            request.setAttribute("fcount", user.getFriends().size());

            
            if (!userlist.isEmpty()) {
                String data = getUserData(user);
                System.out.println(data);
                request.setAttribute("data", data);
            }
            else
                request.setAttribute("data", "");

            request.getRequestDispatcher("/person.jsp").forward(request, response);
        }
        else if ("history".equals(action)) {
            request.setAttribute("userlist", userlist.stream().map(u -> "<li><a href=\"https://vk.com/id"+u.getId()+"\">"+u.getName()+"</a>&nbsp;id:"+u.getId()+"</li>").collect(Collectors.joining("")));
            request.getRequestDispatcher("/history.jsp").forward(request, response);
        }
        else if ("signin".equals(action)) {
            String authname = request.getParameter("username");
            String authpass = request.getParameter("pass");

            System.out.println(authname);
            System.out.println(authpass);

            if (database.authenticate(authname, authpass)) {
                System.out.println("Authentication successful.");
            } else {
                System.out.println("Authentication failed.");
            }

            request.getRequestDispatcher("/update.jsp").forward(request, response);
        }
        else if ("signup".equals(action)) {
            String authname = request.getParameter("username");
            String authpass = request.getParameter("pass");

            System.out.println(authname);
            System.out.println(authpass);

            database.addUser(authname, authpass);

            System.out.println("User added successfully.");
            request.getRequestDispatcher("/update.jsp").forward(request, response);
        }
        else if ("update".equals(action)) {
            request.getRequestDispatcher("/update.jsp").forward(request, response);
        }
        else if ("updateclear".equals(action)) {
            userlist = new ArrayList<User>();
            request.getRequestDispatcher("/update.jsp").forward(request, response);
        }
    }

    String getUserData(User u) {
        String data = "id,name,city,work,university,school,type,groups,friends\n";

        data += u.getId().toString() + "," +
                u.getName() + "," +
                u.getCity() + "," +
                u.getWork() + "," +
                u.getUniversity() + "," +
                u.getSchool() + "," +
                u.getType() + ",";

        List<User> friendsStorage = u.getFriends();
        List<Group> groupStorage = u.getGroups();

        if (friendsStorage.isEmpty() && !groupStorage.isEmpty()) {
            Group g = groupStorage.remove(groupStorage.size() - 1);
            data += g.getName() + "(https://vk.com/"+g.getDomain() + "),\n";
        }
        else if (!friendsStorage.isEmpty() && groupStorage.isEmpty()) {
            User f = friendsStorage.remove(friendsStorage.size() - 1);
            data += "," + f.getName() + "(https://vk.com/id" + f.getId() + ")\n";
        }
        else if (friendsStorage.isEmpty() && groupStorage.isEmpty())
            data += ",,\n";
        else {
            Group g = groupStorage.remove(groupStorage.size() - 1);
            User f = friendsStorage.remove(friendsStorage.size() - 1);
            data += g.getName() + "(https://vk.com/"+g.getDomain() + ")," + f.getName() + "(https://vk.com/id" + f.getId() + ")\n";
        }

        while (!(friendsStorage.isEmpty() || groupStorage.isEmpty())) {
            Group g = groupStorage.remove(groupStorage.size() - 1);
            User f = friendsStorage.remove(friendsStorage.size() - 1);
            data += ",,,,,,," + g.getName() + "(https://vk.com/"+g.getDomain() + ")," + f.getName() + "(https://vk.com/id" + f.getId() + ")\n";
        }

        if (friendsStorage.isEmpty() && !groupStorage.isEmpty())
            while (!groupStorage.isEmpty()) {
                Group g = groupStorage.remove(groupStorage.size() - 1);
                data += ",,,,,,," + g.getName() + "(https://vk.com/"+g.getDomain() + "),\n";
            }
        else if (!friendsStorage.isEmpty() && groupStorage.isEmpty())
            while (!friendsStorage.isEmpty()) {
                User f = friendsStorage.remove(friendsStorage.size() - 1);
                data += ",,,,,,,," + f.getName() + "(https://vk.com/id" + f.getId() + ")\n";
            }

        return data;
    }

    void setProfile(User u){
        String result = "error";

        try {
            Response response = Request.get("https://api.vk.com/method/users.get?user_ids="+
                    u.getId().toString()+"&lang=ru&fields=city,occupation,education,schools&v=5.131&access_token="+token).execute();
            JSONParser parser = new JSONParser();

            try {
                JSONObject resp = (JSONObject) parser.parse(response.returnContent().asString());

                if (resp.containsKey("error"))
                    return;

                JSONArray arr = (JSONArray) resp.get("response");
                if (arr.size() == 0)
                    return;

                JSONObject person = (JSONObject) arr.get(0);

                if (person.containsKey("city")) {
                    JSONObject city = (JSONObject) person.get("city");
                    u.setCity(city.get("title").toString());
                }

                if (person.containsKey("university")) {
                    if (!person.get("university").toString().equals("0"))
                        u.setUniversity(person.get("university_name").toString());
                }

                if (person.containsKey("schools")) {
                    //System.out.println(person.get("schools"));
                    JSONArray schools = (JSONArray) person.get("schools");
                    if (schools.size() > 0) {
                        JSONObject school = (JSONObject) schools.get(0);
                        u.setSchool(school.get("name").toString());
                    }
                }

                if (person.containsKey("occupation")) {
                    JSONObject occupation = (JSONObject) person.get("occupation");
                    if (occupation.get("type").toString().equals("university"))
                        u.setUniversity(occupation.get("name").toString());
                    else
                        if (occupation.get("type").toString().equals("school"))
                            u.setSchool(occupation.get("name").toString());
                        else
                            u.setWork(occupation.get("name").toString());
                }

                result = person.get("first_name").toString() + " " + person.get("last_name").toString();
                if ((Boolean) person.get("is_closed"))
                    u.setType("Закрытый");
                else
                    u.setType("Открытый");
            } catch (org.json.simple.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        u.setName(result);
    }

    static List<String> getCommon(User u) throws InterruptedException {
        String commong = "";
        String commonc = "";
        String commonw = "";
        String commonu = "";
        String commons = "";
        Integer groupscount = 0;
        Integer citycount = 0;
        Integer workcount = 0;
        Integer universitycount = 0;
        Integer schoolcount = 0;

        for (User friend:u.getFriends()) {
            friend.setGroups(getGroups(friend.getId()));
            List<Group> common = new ArrayList<Group>();
            for (Group g:friend.getGroups()) {
                if (u.getGroups().stream().map(Group::getId).toList().contains(g.getId()))
                    common.add(g);
            }
            if (!common.isEmpty()) {
                commong += "<li class=\"sub3\"><a href=\"#\">" + friend.getName() + "&nbsp;-&nbsp;" +
                        common.stream().map(Group::getName).collect(Collectors.joining(",")) + "</a></li>";
                groupscount += 1;
            }
            if (friend.getCity().equals(u.getCity())) {
                commonc += "<li class=\"sub3\"><a href=\"https://vk.com/id"+friend.getId()+"\">"+friend.getName()+"</a></li>";
                citycount += 1;
            }

            if (friend.getWork().equals(u.getWork())) {
                commonw += "<li class=\"sub3\"><a href=\"https://vk.com/id"+friend.getId()+"\">"+friend.getName()+"</a></li>";
                workcount += 1;
            }

            if (friend.getUniversity().equals(u.getUniversity())) {
                commonu += "<li class=\"sub3\"><a href=\"https://vk.com/id"+friend.getId()+"\">"+friend.getName()+"</a></li>";
                universitycount += 1;
            }
            
            if (friend.getSchool().equals(u.getSchool())) {
                commons += "<li class=\"sub3\"><a href=\"https://vk.com/id"+friend.getId()+"\">"+friend.getName()+"</a></li>";
                schoolcount += 1;
            }

            Thread.sleep(180);
        }

        //System.out.println(u.getWork().equals("не указано") ? "" : ("<li><a href=\"#\">По работе("+workcount.toString()+")</a><ul>"+commonw+"</ul></li>"));

        commong = (groupscount == 0) ? "" : ("<li class=\"sub2\"><a href=\"#\">По группам("+groupscount.toString()+")</a><ul>"+commong+"</ul></li>");
        commonc = (u.getCity().equals("не указано")) ? "" : ("<li class=\"sub2\"><a href=\"#\">По городу("+citycount.toString()+")</a><ul>"+commonc+"</ul></li>");
        commonw = (u.getWork().equals("не указано")) ? "" : ("<li class=\"sub2\"><a href=\"#\">По работе("+workcount.toString()+")</a><ul>"+commonw+"</ul></li>");
        commonu = (u.getUniversity().equals("не указано")) ? "" : ("<li class=\"sub2\"><a href=\"#\">По университету("+universitycount.toString()+")</a><ul>"+commonu+"</ul></li>");
        commons = (u.getSchool().equals("не указано")) ? "" : ("<li class=\"sub2\"><a href=\"#\">По школе("+schoolcount.toString()+")</a><ul>"+commons+"</ul></li>");


        //System.out.println(List.of(commong, commonc, commonw, commonu, commons));
        return List.of(commong, commonc, commonw, commonu, commons);
    }

    static List<Group> getGroups(int ID) {
        List<Group> listID = new ArrayList<>();

        try {
            Response response = Request.get(getSubscriptionsLink(token, ID)).execute();
            JSONParser parser = new JSONParser();

            try {
                JSONObject jsonObjResponse = (JSONObject) parser.parse(response.returnContent().asString());
                if (jsonObjResponse.containsKey("error"))
                    return listID;
                jsonObjResponse = (JSONObject) jsonObjResponse.get("response");

                for (Object item:(JSONArray) jsonObjResponse.get("items")) {
                    JSONObject i = (JSONObject) item;
                    Group group = new Group("", 0);
                    if (i.get("type").toString().equals("page") | i.get("type").toString().equals("group")) {
                        group.setName((String) i.get("name"));
                        group.setId((int)(long) i.get("id"));
                        group.setDomain((String) i.get("screen_name"));
                    } else {
                        group.setName(i.get("first_name").toString()+" "+i.get("last_name").toString());
                        group.setId((int)(long) i.get("id"));
                        group.setDomain("id"+i.get("id").toString());
                    }

                    listID.add(group);
                }
            } catch (org.json.simple.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listID;
    }

    static String getSubscriptionsLink(String ACCESS_TOKEN, int ID) {
        URIBuilder link = new URIBuilder();
        link.setScheme("https").setHost("api.vk.com").setPath("/method/users.getSubscriptions")
                .setParameter("user_id", Integer.toString(ID))
                .setParameter("access_token", ACCESS_TOKEN)
                .setParameter("extended", "1")
                .setParameter("count", "200")
                .setParameter("v", "5.131");

        return link.toString();
    }

    static List<User> getFriends(int ID) {
        List<User> listID = new ArrayList<>();

        try {
            Response response = Request.get(getFriendsLink(token, ID)).execute();
            JSONParser parser = new JSONParser();

            try {
                JSONObject jsonObjResponse = (JSONObject) parser.parse(response.returnContent().asString());
                if (jsonObjResponse.containsKey("error"))
                    return listID;
                //System.out.println(jsonObjResponse);
                jsonObjResponse = (JSONObject) jsonObjResponse.get("response");
                for (Object item:(JSONArray) jsonObjResponse.get("items"))
                {
                    JSONObject i = (JSONObject) item;
                    User friend = new User((String) i.get("first_name") + " " + (String) i.get("last_name"), (int) (long) i.get("id"));
                    //System.out.println(friend.getName());
                    if (i.containsKey("city")) {
                        JSONObject city = (JSONObject) i.get("city");
                        friend.setCity((String) city.get("title"));
                    }

                    if (i.containsKey("university")) {
                        //System.out.println(i.get("university"));
                        if (!i.get("university").toString().equals("0"))
                            friend.setUniversity(i.get("university_name").toString());
                    }

                    if (i.containsKey("schools")) {
                        JSONArray schools = (JSONArray) i.get("schools");
                        if (schools.size() > 0) {
                            JSONObject school = (JSONObject) schools.get(0);
                            friend.setSchool(school.get("name").toString());
                        }
                        
                    }

                    if (i.containsKey("occupation")) {
                        JSONObject occupation = (JSONObject) i.get("occupation");
                        if (occupation.get("type").toString().equals("university"))
                            friend.setUniversity(occupation.get("name").toString());
                        else
                            if (occupation.get("type").toString().equals("school"))
                                friend.setSchool(occupation.get("name").toString());
                            else
                                friend.setWork(occupation.get("name").toString());
                    }

                    listID.add(friend);
                }

            } catch (org.json.simple.parser.ParseException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return listID;
    }

    static String getFriendsLink(String ACCESS_TOKEN, int ID) {
        URIBuilder link = new URIBuilder();
        link.setScheme("https").setHost("api.vk.com").setPath("/method/friends.get")
                .setParameter("user_id", Integer.toString(ID))
                .setParameter("access_token", ACCESS_TOKEN)
                .setParameter("extended", "1")
                .setParameter("fields", "city,occupation,education,schools")
                .setParameter("v", "5.131")
                .setParameter("lang", "ru");

        return link.toString();
    }

}