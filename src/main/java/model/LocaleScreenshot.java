package model;

import java.util.List;
import java.util.Set;

public class LocaleScreenshot {
    private List<Integer> size2208x1242;
    private List<Integer> size2688х1242;
    private List<Integer> size2732х2048;
    private List<Integer> size2732х2048_ipadPro129;
    private String localeFolderLink;
    private String localeFolderName;
    private Set<Integer> screenshotSet;

    public LocaleScreenshot(String localeFolderLink, String localeFolderName, List size2208x1242, List size2688х1242, List size2732х2048, List size2732х2048_ipadPro129, Set screenshotSet) {
        this.size2208x1242 = size2208x1242;
        this.size2688х1242 = size2688х1242;
        this.size2732х2048 = size2732х2048;
        this.size2732х2048_ipadPro129 = size2732х2048_ipadPro129;
        this.localeFolderLink = localeFolderLink;
        this.localeFolderName = localeFolderName;
        this.screenshotSet = screenshotSet;
    }

    public String getLocaleFolderName() {
        return localeFolderName;
    }

    public void setLocaleFolderName(String localeFolderName) {
        this.localeFolderName = localeFolderName;
    }

    public List getSize2208x1242() {
        return size2208x1242;
    }

    public void setSize2208x1242(List size2208x1242) {
        this.size2208x1242 = size2208x1242;
    }

    public List getSize2688х1242() {
        return size2688х1242;
    }

    public void setSize2688х1242(List size2688х1242) {
        this.size2688х1242 = size2688х1242;
    }

    public List getSize2732х2048() {
        return size2732х2048;
    }

    public void setSize2732х2048(List size2732х2048) {
        this.size2732х2048 = size2732х2048;
    }

    public List getSize2732х2048_ipadPro129() {
        return size2732х2048_ipadPro129;
    }

    public void setSize2732х2048_ipadPro129(List size2732х2048_ipadPro129) {
        this.size2732х2048_ipadPro129 = size2732х2048_ipadPro129;
    }

    public String getLocaleFolderLink() {
        return localeFolderLink;
    }

    public void setLocaleFolderLink(String localeFolderLink) {
        this.localeFolderLink = localeFolderLink;
    }

    public Set getScreenshotSet() {
        return screenshotSet;
    }

    public void setScreenshotSet(Set screenshotSet) {
        this.screenshotSet = screenshotSet;
    }
}