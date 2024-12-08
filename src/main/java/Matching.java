import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Matching {
	// List of Users
    private List<User> users;
    // Constructor
    public Matching(List<User> users) {
        this.users = users;
    }
    // Algorithm (returns list of matches)
    public List<Match> findMatches(int userID) {
        User currentUser = null;
        for (User user : users) {
            if (user.getUserID() == userID) {
                currentUser = user;
                break;
            }
        }
        // If User DNE, return empty list
        if (currentUser == null) {
            return new ArrayList<>();
        }
        // Set of currentUser's hobbies (normalized to lower case)
        Set<String> currentUserHobbiesLower = currentUser.getHobbies().stream()
                .map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<Match> bestMatches = new ArrayList<>();
        // Iterate through all users
        for (User user : users) {
        	// Skip currentUser
            if (user.getUserID() == currentUser.getUserID()) {
                continue;
            }
            
            int score = 0;
            // If same majors, add 3 to score
            if (user.getMajor().equalsIgnoreCase(currentUser.getMajor())) {
                score += 3;
            }
            // If same hometowns, add 2 to score
            if (user.getHometown().equalsIgnoreCase(currentUser.getHometown())) {
                score += 2;
            }
            // Set of user's hobbies (normalized to lower case)
            Set<String> userHobbiesLower = user.getHobbies().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());

            // Calculate the number of shared hobbies
            Set<String> sharedHobbies = new HashSet<>(currentUserHobbiesLower);
            sharedHobbies.retainAll(userHobbiesLower);
            score += sharedHobbies.size();
            
            // If they both use Instagram, add 1
            if (user.getInstagram() != "" && currentUser.getInstagram() != "") {
            	score++;
            }
            
            // Add Match to the list if score is positive
            if (score > 0) {
                bestMatches.add(new Match(user, score));
            }
        }
        // Sort the matches by score descending
        bestMatches.sort(Comparator.comparingInt(Match::getScore).reversed());
        return bestMatches;
    }

    public static class Match {
        private User user;
        private int score;

        public Match(User user, int score) {
            this.user = user;
            this.score = score;
        }

        public User getUser() {
            return user;
        }

        public int getScore() {
            return score;
        }
    }
}
