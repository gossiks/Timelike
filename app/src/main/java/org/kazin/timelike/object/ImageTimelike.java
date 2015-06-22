package org.kazin.timelike.object;

import java.util.ArrayList;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ImageTimelike {

    public final static String TYPE_IMAGE = "image";
    public final static String TYPE_VIDEO = "video";
    public final static String TYPE_USER = "user";

    String imageUrl;
    String imageId;
    String username;
    String avatarUrl;
    String description;
    long timelike;
    String type = TYPE_IMAGE;
    ArrayList<Comment> comments;

    private boolean isPinned = false;

    public ImageTimelike(){
    }

    public ImageTimelike(String imageUrl, String imageId, String username, long timelike) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.username = username;
        this.timelike = timelike;
    }

    public ImageTimelike(String imageUrl, String imageId, String username, long timelike, String avatarUrl, String description,  String type) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.timelike = timelike;
        this.type = type;
    }

    public ImageTimelike(String imageUrl, String imageId, String username, long timelike, String avatarUrl, String description,  String type, ArrayList<Comment> comments) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.timelike = timelike;
        this.type = type;
        this.comments = comments;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageId() {
        return imageId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setTimelike(long timelike) {
        this.timelike = timelike;
    }

    public long getTimelike() {
        return timelike;
    }



    public boolean isPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String[] getCommentsStringArray() {
        String[] commentsString = new String[comments.size()];
        int i = 0;
        for(Comment comm:comments){
            commentsString[i] = "@"+comm.getUsername()+" "+comm.getText();
            i++;
        }
        return commentsString;
    }

    public String[] getCommentsStringArray(int numberOfComments) {
        String[] commentsString = new String[comments.size()];
        int i = 0;
        for(Comment comm:comments){
            commentsString[i] = "@"+comm.getUsername()+" "+comm.getText();
            i++;
            if(i>numberOfComments){return commentsString;}
        }
        return commentsString;
    }
    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public static class Comment{
        String username;
        String avatar;
        String text;
        long createdTime;

        public Comment(String username, String avatar, String text, long createdTime) {
            this.username = username;
            this.avatar = avatar;
            this.text = text;
            this.createdTime = createdTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public long getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
        }

        @Override
        public String toString() {
            return "@"+username+"  "+text;
        }
    }
}
