package com.xml.editor;

import java.util.HashSet;
import java.util.Set;

public class Suggest {

    private SocialNetworkGraph socialNetworkGraph;

    public Suggest() {
        socialNetworkGraph = new SocialNetworkGraph();
    }

    // This method will take the XML string and build the graph
    public void buildGraphFromXML(String xml) {
        socialNetworkGraph.buildGraphFromXML(xml);
    }

    // This method will suggest friends for a given user based on followers of their followers
    public Set<User> suggestFriends(int userId) {
        User user = socialNetworkGraph.getUserById(userId);

        if (user == null) {
            System.out.println("User not found in the graph.");
            return new HashSet<>();
        }

        Set<Integer> friendsOfFriends = new HashSet<>(); // To store suggested friends' IDs
        Set<Integer> userFollowers = new HashSet<>(user.followers); // The user's followers
        Set<Integer> alreadyFriends = new HashSet<>(userFollowers); // Direct friends of the user (followers)

        // For each follower of the user, check their followers (friends of friends)
        for (Integer followerId : userFollowers) {
            User follower = socialNetworkGraph.getUserById(followerId);

            if (follower != null) {
                // Add the followers of this follower (excluding the user and their direct friends)
                for (Integer followerOfFollowerId : follower.followers) {
                    // Skip if it's the user themselves or if it's already a direct friend
                    if (!followerOfFollowerId.equals(userId) && !alreadyFriends.contains(followerOfFollowerId)) {
                        friendsOfFriends.add(followerOfFollowerId);
                    }
                }
            }
        }

        // Convert suggested friend IDs into User objects and return
        Set<User> suggestedUsers = new HashSet<>();
        for (Integer friendId : friendsOfFriends) {
            User suggestedUser = socialNetworkGraph.getUserById(friendId);
            if (suggestedUser != null) {
                suggestedUsers.add(suggestedUser);
            }
        }

        return suggestedUsers;
    }
}
