package util;

import com.google.api.services.drive.model.File;
import model.LocaleFolder;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static util.GoogleDriveSpider.*;

public class GeneralUtil {

    public static Set<String> screenshotErrors = new LinkedHashSet<>();
    // actual list of allowed locales is here: https://analyst.atlassian.net/wiki/spaces/EUS/pages/1662517350/Video+Preview+App+Store+UA
    private static final List<String> foldersAllowedList = new ArrayList<>(Arrays.asList("ar-SA", "ca", "cs", "da", "de-DE", "el", "en-AU", "en-CA", "en-GB", "en-US", "es-ES", "es-MX", "fi", "fr-CA", "fr-FR", "he", "hi", "hr", "hu", "id", "it", "ja", "ko", "ms", "nl-NL", "no", "pl", "pt-BR", "pt-PT", "ro", "ru", "sk", "sv", "th", "tr", "uk", "vi", "zh-Hans", "zh-Hant"));
    private static final List<String> localesAllowedList = new ArrayList<>(Arrays.asList("ar-SA", "ca", "cs", "da", "de", "el", "en", "en", "en", "en", "es", "mx", "fi", "fr", "fr", "he", "hi", "hr", "hu", "id", "it", "ja", "ko", "ms", "nl", "no", "pl", "br", "pt", "ro", "ru", "sk", "se", "th", "tr", "uk", "vi", "zns", "zht"));
    static Map allowedFolderLocaleDict = IntStream.range(0, Math.min(foldersAllowedList.size(), localesAllowedList.size()))
            .boxed()
            .collect(Collectors.toMap(foldersAllowedList::get, localesAllowedList::get));

    public static void checkLocaleScreenshotAndUpdate(File file, String fileName, String localeFolderId, String[] fileNameParsedArray) {

        LocaleFolder localeFolder = localeFolderRepository.getByLocaleFolderId(localeFolderId);

        try {
            int numberFromName = 0;
            String sizeFromName = "";
            String localeFromName = "";
            String osFromName = "";

            if (fileNameParsedArray.length > 0) numberFromName = Integer.parseInt(fileNameParsedArray[0]);
            if (fileNameParsedArray.length > 1) sizeFromName = fileNameParsedArray[1];
            if (fileNameParsedArray.length > 2) localeFromName = fileNameParsedArray[2];
            if (fileNameParsedArray.length > 3) osFromName = fileNameParsedArray[3];

            // checking size and russian letter 'X'
            int fileWidth = file.getImageMediaMetadata().getWidth();
            int fileHeight = file.getImageMediaMetadata().getHeight();
            if (sizeFromName.contains("х")) {
                russianLetterError(localeFolderId, fileName);
                sizeFromName = sizeFromName.replace("х", "x");
            }
            if (fileWidth != Integer.parseInt(sizeFromName.split("x")[0]) || fileHeight != Integer.parseInt(sizeFromName.split("x")[1])) {
                wrongSizeError(localeFolderId, fileName, fileWidth, fileHeight);
            }

            localeScreenshotRepository.updateSizeList(localeFolderId, numberFromName, sizeFromName, fileName);

            // checking allowed folder name
            if (!allowedFolderLocaleDict.containsKey(localeFolder.getLocaleFolderName()))
                wrongFolderError(localeFolderId, localeFolder.getLocaleFolderName());

            // checking allowed locale name
            if (!allowedFolderLocaleDict.containsValue(localeFromName))
                wrongNameError(localeFolderId, fileName);

            // checking os
            if (!osFromName.equals("ios")) {
                wrongNameError(localeFolderId, fileName);
            }

            // checking ipadPro129
            if (fileNameParsedArray.length > 4) {
                if (!fileNameParsedArray[4].equals("ipadPro129") || !sizeFromName.equals("2732x2048")) {
                    wrongNameError(localeFolderId, fileName);
                }
            }

            // checking extension
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!extension.equals("jpg")) wrongExtensionError(localeFolderId, fileName);

        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            wrongNameError(localeFolderId, fileName);
        } catch (NullPointerException e) {
            corruptedFileError(localeFolderId, fileName);
        }

    }

    public static Instant startTimeFixing() {
        return Instant.now();
    }

    public static String endTimeFixing(Instant start) {
        Instant end = Instant.now();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("HH:mm:ss");
        Duration diff = Duration.between(start, end);
        LocalTime fTime = LocalTime.ofNanoOfDay(diff.toNanos());
        return fTime.format(df);
    }

    public static void wrongNameError(String localeFolderId, String fileName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"File %s has wrong name\")", localeFolderId, fileName);
        screenshotErrors.add(error);
    }

    public static void corruptedFileError(String localeFolderId, String fileName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"File %s is corrupted\")", localeFolderId, fileName);
        screenshotErrors.add(error);
    }

    public static void duplicateNumberError(String localeFolderId, String fileName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"Screenshot number of file %s is duplicated\")", localeFolderId, fileName);
        screenshotErrors.add(error);
    }

    public static void wrongSizeError(String localeFolderId, String fileName, int fileWidth, int fileHeight) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"File %s has wrong size: %s\")", localeFolderId, fileName, fileWidth + "x" + fileHeight);
        screenshotErrors.add(error);
    }

    public static void russianLetterError(String localeFolderId, String fileName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"File %s contains the Russian letter 'x'\")", localeFolderId, fileName);
        screenshotErrors.add(error);
    }

    public static void wrongFolderError(String localeFolderId, String folderName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"Folder name %s is not allowed\")", localeFolderId, folderName);
        screenshotErrors.add(error);
    }

    public static void wrongExtensionError(String localeFolderId, String fileName) {
        String error = String.format("=HYPERLINK(\"https://drive.google.com/drive/u/1/folders/%s\";\"File %s has wrong extension: ." + fileName.substring(fileName.length() - 5).split("\\.")[1] + "\")", localeFolderId, fileName);
        screenshotErrors.add(error);
    }
}
