package de.eventmanager.core.export;


import helper.LoggerHelper;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

public class Exporter {

    public static boolean exportEvent(Calendar calendar) {
        try {
            String path;
            if (System.getenv("CI") != null) {
                // GitHub Actions: Safe in /tmp
                path = Paths.get(System.getProperty("java.io.tmpdir"), "mycalendar.ics").toString();
            } else {
                // Locale Device: Safe in Downloads
                String userHome = System.getProperty("user.home");
                path = Paths.get(userHome, "Downloads", "mycalendar.ics").toString();
            }

            FileOutputStream fout = new FileOutputStream(path);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        } catch (IOException | ValidationException e) {
            LoggerHelper.logErrorMessage(Exporter.class, e.getMessage());
            return false;
        }

        return true;
    }


}
