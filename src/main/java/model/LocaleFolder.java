package model;

public class LocaleFolder {
    private String localeFolderId;
    private String localeFolderName;

    public String getLocaleFolderId() {
        return localeFolderId;
    }

    public String getLocaleFolderName() {
        return localeFolderName;
    }

    public String getLocaleFolderLink() {
        return localeFolderLink;
    }

    public LocaleFolder(String localeFolderID, String localeFolderName, String localeFolderLink) {
        this.localeFolderId = localeFolderID;
        this.localeFolderName = localeFolderName;
        this.localeFolderLink = localeFolderLink;
    }

    private String localeFolderLink;
}
