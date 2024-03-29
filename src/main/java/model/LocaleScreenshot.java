package model;

import java.util.List;

public class LocaleScreenshot {
    private List size2208x1242;
    private List size2688x1242;
    private List size2732x2048;
    private List size2732x2048_ipadPro129;
    private String localeFolderLink;
    private String localeFolderName;

    public LocaleScreenshot(String localeFolderLink, String localeFolderName, List size2208x1242, List size2688x1242, List size2732x2048, List size2732x2048_ipadPro129) {
        this.size2208x1242 = size2208x1242;
        this.size2688x1242 = size2688x1242;
        this.size2732x2048 = size2732x2048;
        this.size2732x2048_ipadPro129 = size2732x2048_ipadPro129;
        this.localeFolderLink = localeFolderLink;
        this.localeFolderName = localeFolderName;
    }

    public String getLocaleFolderName() {
        return localeFolderName;
    }

    public List getSize2208x1242() {
        return size2208x1242;
    }

    public void setSize2208x1242(List size2208x1242) {
        this.size2208x1242 = size2208x1242;
    }

    public List getSize2688x1242() {
        return size2688x1242;
    }

    public void setSize2688x1242(List size2688x1242) {
        this.size2688x1242 = size2688x1242;
    }

    public List getSize2732x2048() {
        return size2732x2048;
    }

    public void setSize2732x2048(List size2732х2048) {
        this.size2732x2048 = size2732х2048;
    }

    public List getSize2732x2048_ipadPro129() {
        return size2732x2048_ipadPro129;
    }

    public void setSize2732x2048_ipadPro129(List size2732x2048_ipadPro129) {
        this.size2732x2048_ipadPro129 = size2732x2048_ipadPro129;
    }

    public String getLocaleFolderLink() {
        return localeFolderLink;
    }


}
