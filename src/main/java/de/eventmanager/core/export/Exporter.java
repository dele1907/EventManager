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
            String userHome = System.getProperty("user.home");
            String downloadsPath = Paths.get(userHome, "Downloads", "mycalendar.ics").toString();
            FileOutputStream fout = new FileOutputStream(downloadsPath);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        } catch (IOException | ValidationException e) {
            LoggerHelper.logErrorMessage(Exporter.class, e.getMessage());

            return false;
        }

        return true;
    }

}
