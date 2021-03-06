package org.kazin.timelike.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexey on 16.06.2015.
 */
public class ImageTimelike implements Parcelable{

    public final static String TYPE_IMAGE = "image";
    public final static String TYPE_VIDEO = "video";
    public final static String TYPE_USER = "user";

    String imageUrl;
    String imageId;
    String username;
    String userId;
    String avatarUrl;
    String description;
    long timelike;
    String type = TYPE_IMAGE;
    ArrayList<Comment> comments=null;

    private HashMap<String, String> hashUsernameVsId;

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

    public ImageTimelike(String imageUrl, String imageId, String username, String userId, long timelike, String avatarUrl, String description,  String type, ArrayList<Comment> comments) {
        this.imageUrl = imageUrl;
        this.imageId = imageId;
        this.username = username;
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.description = description;
        this.timelike = timelike;
        this.type = type;
        setComments(comments);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageId(String imageId){
        this.imageId = imageId;
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

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        hashUsernameVsId = new HashMap<>();
        for (Comment c: comments){
            hashUsernameVsId.put(c.getUsername(), c.getUserId());
        }
    }

    public String getIdByUsername(String username){
        if(hashUsernameVsId==null & comments!=null){
            setComments(comments);
        }

        if(hashUsernameVsId.containsKey(username)) {
            return hashUsernameVsId.get(username);
        }
        else {
            return "empty";
        }
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
        ArrayList<String> commentsString = new ArrayList<>();
        if(comments.size()==0){
            return commentsString.toArray(new String[0]);
        }

        if(numberOfComments>comments.size()){
            numberOfComments = comments.size();
        }

        for(int i = 0; i<numberOfComments; i++){
            commentsString.add(" @"+comments.get(i).getUsername()+" "+comments.get(i).getText());
        }
        return commentsString.toArray(new String[numberOfComments]);
    }


    public static class Comment implements Parcelable{
        String username;
        String avatar;
        String text;
        String userId;
        long createdTime;

        public Comment(String username, String avatar, String text, String userId, long createdTime) {
            this.username = username;
            this.avatar = avatar;
            this.text = text;
            this.createdTime = createdTime;
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public String getUserId() {
            return userId;
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


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.username);
            dest.writeString(this.avatar);
            dest.writeString(this.text);
            dest.writeLong(this.createdTime);
        }

        protected Comment(Parcel in) {
            this.username = in.readString();
            this.avatar = in.readString();
            this.text = in.readString();
            this.createdTime = in.readLong();
        }

        public static final Creator<Comment> CREATOR = new Creator<Comment>() {
            public Comment createFromParcel(Parcel source) {
                return new Comment(source);
            }

            public Comment[] newArray(int size) {
                return new Comment[size];
            }
        };
    }

    //Parcelable interface for ImageTimeLike

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageId);
        dest.writeString(this.username);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.description);
        dest.writeLong(this.timelike);
        dest.writeString(this.type);
        dest.writeList(this.comments);
        dest.writeByte(isPinned ? (byte) 1 : (byte) 0);
    }

    protected ImageTimelike(Parcel in) {
        this.imageUrl = in.readString();
        this.imageId = in.readString();
        this.username = in.readString();
        this.avatarUrl = in.readString();
        this.description = in.readString();
        this.timelike = in.readLong();
        this.type = in.readString();
        this.comments = new ArrayList<Comment>();
        in.readList(this.comments, Comment.class.getClassLoader());
        this.isPinned = in.readByte() != 0;
    }

    public static final Creator<ImageTimelike> CREATOR = new Creator<ImageTimelike>() {
        public ImageTimelike createFromParcel(Parcel source) {
            return new ImageTimelike(source);
        }

        public ImageTimelike[] newArray(int size) {
            return new ImageTimelike[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
