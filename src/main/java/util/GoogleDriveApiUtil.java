package util;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static util.GeneralUtil.screenshotErrors;

public class GoogleDriveApiUtil {
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS = System.getenv("googledrive_credentials");
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String RANGE_UPDATE_VIDEOERRORS = "AppStore Screens errors!A:A";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */

    private static final List<String> SCOPES = Arrays.asList(
            SheetsScopes.SPREADSHEETS,
            DriveScopes.DRIVE_READONLY
    );

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        if (CREDENTIALS == null) throw new NullPointerException("Credentials not found");
        InputStream in = new ByteArrayInputStream(CREDENTIALS.getBytes());
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static Drive buildDriveApiClientService() {
        // Build a new authorized API client service.
        Drive service = null;
        try {
            final String APPLICATION_NAME = "Google Drive API Java";
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return service;
    }

    public static Sheets buildSheetsApiClientService() {
        // Build a new authorized API client service.
        Sheets service = null;
        try {
            final String APPLICATION_NAME = "Google Sheets API Java";
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        return service;
    }

    public static FileList getFileListFromDriveAPI(Drive service, String pageToken, String query, String fields) {
        FileList fileList = null;
        try {
            fileList = service.files().list()
                    .setQ(query)
                    .setTeamDriveId("0AFmzqytyRUvKUk9PVA")
                    .setCorpora("drive")
                    .setSupportsTeamDrives(true)
                    .setIncludeTeamDriveItems(true)
                    .setPageSize(1000)
                    .setPageToken(pageToken)
                    .setFields(fields)
                    .setOrderBy("name")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileList;
    }

    public static void clearAndPublishErrorLogOnSpreadsheet(Sheets service, String spreadsheetId, String valueInputOption) {
        try {
            // clear old values
            ClearValuesRequest requestBodyClear = new ClearValuesRequest();
            Sheets.Spreadsheets.Values.Clear request =
                    service.spreadsheets().values().clear(spreadsheetId, RANGE_UPDATE_VIDEOERRORS, requestBodyClear);
            request.execute();

            ValueRange requestBody = new ValueRange();
            requestBody.setRange(RANGE_UPDATE_VIDEOERRORS);
            List<List<Object>> localizationValues = new ArrayList<>();
            AtomicInteger lineIndex = new AtomicInteger();

            screenshotErrors
                    .stream()
                    .forEach(v -> {

                        localizationValues.add(new ArrayList<>());
                        localizationValues.get(lineIndex.get()).add(v);
                        lineIndex.getAndIncrement();

                    });

            requestBody.setValues(localizationValues);

            service.spreadsheets().values().update(spreadsheetId, RANGE_UPDATE_VIDEOERRORS, requestBody)
                    .setValueInputOption(valueInputOption)
                    .execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}