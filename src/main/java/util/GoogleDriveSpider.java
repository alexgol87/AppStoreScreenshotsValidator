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
                    //System.out.println(file.getId() + " " + file.getName().toLowerCase() + " " + file.getWebViewLink());
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
                FileList result = GoogleDriveApiUtil.getFileListFromDriveAPI(service, pageToken, "'" + localeFolder.getlocalefolderid() + "' in parents and mimeType contains 'image/' and trashed = false", "nextPageToken, files(id, name, imageMediaMetadata, lastModifyingUser, parents)");
                List<File> files = result.getFiles();
                if (files == null || files.isEmpty()) {
                    System.out.println("No files found.");
                } else {
                    //System.out.println("\n" + localeFolder.getLocaleFolderName());
                    for (File file : files) {
                        //if (file.getName().contains("2688х1242_de_ios")) {
                            String fileName = file.getName().replace(".jpg", "");
                            String localeFolderId = file.getParents().get(0);
                            String[] fileNameParsedArray = fileName.split("_");
                            if (!localeScreenshotRepository.ifContainsLocaleScreenshot(localeFolderId))
                                localeScreenshotRepository.add(localeFolderId, localeFolder.getLocaleFolderLink(), localeFolder.getLocaleFolderName());
                            //localeScreenshotRepository.update(videoNumber + "_" + fileNameParsedArray[2], fileNameParsedArray[0], file.getThumbnailLink());
                            //System.out.println(fileName);
                            GeneralUtil.checkLocaleScreenshotAndUpdate(file, file.getName(), localeFolderId, fileNameParsedArray);
                            //System.out.println(fileNameParsedArray[0] + " " + fileNameParsedArray[1] + " " + fileNameParsedArray[2] + " " + fileNameParsedArray[3]);
                       // }
                    }
                    pageToken = result.getNextPageToken();
                    if (pageToken == null) break;
                }
            }
        }
    }
}