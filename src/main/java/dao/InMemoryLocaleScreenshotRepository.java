package dao;

import model.LocaleScreenshot;

import java.util.*;
import java.util.stream.Collectors;

import static util.GeneralUtil.*;


public class InMemoryLocaleScreenshotRepository {

    static Map<String, LocaleScreenshot> localeScreenshotMap = new TreeMap<>();

    public void add(String localeFolderId, String localeFolderLink, String localeFolderName) {
        localeScreenshotMap.put(localeFolderId, new LocaleScreenshot(localeFolderLink, localeFolderName, new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList(), new HashSet()));
    }

    public LocaleScreenshot getByLocaleFolderId(String localeFolderId) {
        return localeScreenshotMap.get(localeFolderId);
    }

    public List<LocaleScreenshot> getAll() {
        return localeScreenshotMap.values().stream().collect(Collectors.toList());
    }

    public void updateSizeList(String localeFolderId, int screenshotNumber, String size, String fileName) {
        LocaleScreenshot tmpLocaleScreenshot = getByLocaleFolderId(localeFolderId);
        List<Integer> tmpSize = new ArrayList();
        switch (size) {
            case "2208x1242":
                tmpSize = tmpLocaleScreenshot.getSize2208x1242();
                if (tmpSize.contains(screenshotNumber) && !fileName.toLowerCase().contains("ipadpro129")) duplicateNumberError(localeFolderId, fileName);
                tmpSize.add(screenshotNumber);
                tmpLocaleScreenshot.setSize2208x1242(tmpSize);
                break;
            case "2688x1242":
                tmpSize = tmpLocaleScreenshot.getSize2688х1242();
                if (tmpSize.contains(screenshotNumber) && !fileName.toLowerCase().contains("ipadpro129")) duplicateNumberError(localeFolderId, fileName);
                tmpSize.add(screenshotNumber);
                tmpLocaleScreenshot.setSize2688х1242(tmpSize);
                break;
            case "2732x2048":
                tmpSize = tmpLocaleScreenshot.getSize2732х2048();
                if (tmpSize.contains(screenshotNumber) && !fileName.toLowerCase().contains("ipadpro129")) duplicateNumberError(localeFolderId, fileName);
                tmpSize.add(screenshotNumber);
                tmpLocaleScreenshot.setSize2732х2048(tmpSize);
                break;
            case "2732x2048_ipadPro129":
                tmpSize = tmpLocaleScreenshot.getSize2732х2048_ipadPro129();
                if (tmpSize.contains(screenshotNumber) && !fileName.toLowerCase().contains("ipadpro129")) duplicateNumberError(localeFolderId, fileName);
                tmpSize.add(screenshotNumber);
                tmpLocaleScreenshot.setSize2732х2048_ipadPro129(tmpSize);
                break;
            default:
                wrongNameError(localeFolderId, fileName);
                break;
        }

        localeScreenshotMap.put(localeFolderId, tmpLocaleScreenshot);
    }

    public void updateScreenshotSet(String localeFolderId, int screenshotNumber) {
        LocaleScreenshot tmpLocaleScreenshot = getByLocaleFolderId(localeFolderId);
        Set tmpScreenshotSet = tmpLocaleScreenshot.getScreenshotSet();
        tmpScreenshotSet.add(screenshotNumber);
        tmpLocaleScreenshot.setScreenshotSet(tmpScreenshotSet);
        localeScreenshotMap.put(localeFolderId, tmpLocaleScreenshot);
    }

    public boolean ifContainsLocaleScreenshot(String localeFolderId) {
        return localeScreenshotMap.containsKey(localeFolderId);
    }
}

