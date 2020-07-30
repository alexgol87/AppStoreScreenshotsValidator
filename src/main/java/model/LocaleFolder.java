package model;

public class LocaleFolder {
    private String localeFolderId;
    private String localeFolderName;

    public String getlocalefolderid() {
        return localeFolderId;
    }

    public void setlocalefolderid(String localefolderid) {
        this.localeFolderId = localefolderid;
    }

    public String getLocaleFolderName() {
        return localeFolderName;
    }

    public void setLocaleFolderName(String localeFolderName) {
        this.localeFolderName = localeFolderName;
    }

    public String getLocaleFolderLink() {
        return localeFolderLink;
    }

    public void setLocaleFolderLink(String localeFolderLink) {
        this.localeFolderLink = localeFolderLink;
    }

    public LocaleFolder(String localeFolderID, String localeFolderName, String localeFolderLink) {
        this.localeFolderId = localeFolderID;
        this.localeFolderName = localeFolderName;
        this.localeFolderLink = localeFolderLink;
    }

    private String localeFolderLink;
}
