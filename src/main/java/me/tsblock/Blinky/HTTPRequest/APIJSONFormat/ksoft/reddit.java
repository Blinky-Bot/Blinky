package me.tsblock.Blinky.HTTPRequest.APIJSONFormat.ksoft;

public class reddit {
    private String title;
    private String image_url;
    private String source;
    private String subreddit;
    private String upvotes;
    private String downvotes;
    private String comments;
    private long timestamp;
    private boolean nsfw;

    public String getTitle() {
        return title;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getSource() {
        return source;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getUpvotes() {
        return upvotes;
    }

    public String getDownvotes() {
        return downvotes;
    }

    public String getComments() {
        return comments;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isNsfw() {
        return nsfw;
    }
}
