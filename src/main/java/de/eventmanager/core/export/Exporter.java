package de.eventmanager.core.export;


import helper.LoggerHelper;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exporter {

    public static void exportEvent(Calendar calendar) {
        try {
            FileOutputStream fout = new FileOutputStream("mycalendar.ics");
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
        } catch (IOException | ValidationException e) {
            LoggerHelper.logErrorMessage(Exporter.class, e.getMessage());
        }
    }
}
