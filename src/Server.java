package user_and_matching;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private List<User> users; // List of Users
    private Matching matching; // Matching object to be called when necessary (call findMatches(int userID) which returns a list of matches)

    public Server() {
        this.users = new ArrayList<>();
        this.matching = new Matching(users);
    }

    public void run() {
    	
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }
}