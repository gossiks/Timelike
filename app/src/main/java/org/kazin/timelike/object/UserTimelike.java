package org.kazin.timelike.object;

/**
 * Created by Alexey on 16.06.2015.
 */
public class UserTimelike {
    public String id;
    public String username;
    public String fullName;
    public String profilPicture;
    public String accessToken;

    public UserTimelike() {
    }

    public UserTimelike(String id, String username, String fullName, String profilPicture, String accessToken) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.profilPicture = profilPicture;
        this.accessToken = accessToken;
    }
}
