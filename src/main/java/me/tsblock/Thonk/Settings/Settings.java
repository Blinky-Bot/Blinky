package me.tsblock.Thonk.Settings;

public class Settings {
    private String botSecret;
    private String ownerID;
    private String dataBaseURL;
    private String prefix;
    private API API;

    public API getAPI() {
        return API;
    }

    public void setAPI(API API) {
        this.API = API;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String[] adminList;
    
    public String getBotSecret() {
        return botSecret;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getDataBaseURL() {
        return dataBaseURL;
    }

    public String[] getAdminList() {
        return adminList;
    }

    public void setBotSecret(String botSecret) {
        this.botSecret = botSecret;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setDataBaseURL(String dataBaseURL) {
        this.dataBaseURL = dataBaseURL;
    }

    public void setAdminList(String[] adminList) {
        this.adminList = adminList;
    }
}
