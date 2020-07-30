package util;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import dao.InMemoryLocaleFolderRepository;
import dao.InMemoryLocaleScreenshotRepository;
import model.LocaleFolder;

import java.time.Instant;
import java.util.List;

public class GoogleDriveSpider implements Runnable {

    public static String execTime;
    final static InMemoryLocaleFolderRepository localeFolderRepository = new InMemoryLocaleFolderRepository();
    final static InMemoryLocaleScreenshotRepository localeScreenshotRepository = new InMemoryLocaleScreenshotRepository();

    public GoogleDriveSpider() {

        Instant start = GeneralUtil.startTimeFixing();

        Drive serviceDrive = GoogleDriveApiUtil.buildDriveApiClientService();
        getLocaleFolderIdNameDictionaryFromGoogleDrive(serviceDrive);
        LocaleScreenshotRepositoryFilling(serviceDrive);

        Sheets serviceSheets = GoogleDriveApiUtil.buildSheetsApiClientService();
        GoogleDriveApiUtil.clearAndPublishErrorLogOnSpreadsheet(serviceSheets, "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "USER_ENTERED");
        GoogleDriveApiUtil.clearAndPublishNewTableOnSpreadsheet(serviceSheets, "1SC92tKYXQDqujUcvZVYMmmNiJp35Q1b22fKg2C7zeQI", "USER_ENTERED");

        execTime = GeneralUtil.endTimeFixing(start);
    }

    @Override
    public void run() {

    }

    public void getLocaleFolderIdNameDictionaryFromGoogleDrive(Drive service) {
        String pageToken = null;
        while (true) {
            FileList result = GoogleDriveApiUtil.getFileListFromDriveAPI(service, pageToken, "'1ZAkIwswpRnIgzPNO1NO-VF3eoJVkSVx2' in parents and mimeType='application/vnd.google-apps.folder' and trashed = false", "nextPageToken, files(id, name, webViewLink)");
            List<File> files = result.getFiles();
            if (files == null || files.isEmpty()) {
                System.out.println("No files found.");
            } else {
                for (File file : files) {
                    localeFolderRepository.add(file.getId(), file.getName(), file.getWebViewLink());
                }
            }
            pageToken = result.getNextPageToken();
            if (pageToken == null) break;
        }
    }

    public void LocaleScreenshotRepositoryFilling(Drive service) {
        for (LocaleFolder localeFolder : localeFolderRepository.getAll()) {
            String pageToken = null;
            while (true) {
                FileList result = GoogleDriveApiUtil.getFileListFromDriveAPI(service, pageToken, "'" + localeFolder.getLocaleFolderId() + "' in parents and mimeType contains 'image/' and trashed = false", "nextPageToken, files(id, name, imageMediaMetadata, lastModifyingUser, parents)");
                List<File> files = result.getFiles();
                if (files == null || files.isEmpty()) {
                    System.out.println("No files found.");
                } else {
                    for (File file : files) {
                        String fileName = file.getName().replace(".jpg", "");
                        String localeFolderId = file.getParents().get(0);
                        String[] fileNameParsedArray = fileName.split("_");
                        if (!localeScreenshotRepository.ifContainsLocaleScreenshot(localeFolderId))
                            localeScreenshotRepository.add(localeFolderId, localeFolder.getLocaleFolderLink(), localeFolder.getLocaleFolderName());
                        GeneralUtil.checkLocaleScreenshotAndUpdate(file, file.getName(), localeFolderId, fileNameParsedArray);
                    }
                    pageToken = result.getNextPageToken();
                    if (pageToken == null) break;
                }
            }
        }
    }
}