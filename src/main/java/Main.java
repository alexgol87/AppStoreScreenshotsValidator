import util.GoogleDriveApiUtil;
import util.GoogleDriveSpider;

import java.time.Instant;

import static util.GeneralUtil.*;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Instant start = startTimeFixing();
        Runnable task = GoogleDriveSpider::new;
        Thread thread = new Thread(task);
        thread.start();
        thread.join();
        for (String error : screenshotErrors) {
            System.out.println(error);
        }
        System.out.println(endTimeFixing(start));
    }
}