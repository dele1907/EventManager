package de.eventmanager.core.export;

import de.eventmanager.core.events.EventModel;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import java.util.GregorianCalendar;
import java.util.Optional;

public class ExportManager {
    private static final CalScale STANDARD_SCALE = CalScale.GREGORIAN;
    private static final ProdId STANDARD_PROD = new ProdId("-//Ben Fortuna//iCal4j 1.0//EN");
    private static final Version CURRENT_VERSION = Version.VERSION_2_0;
    private final TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    private final TimeZone timezone = registry.getTimeZone("Germany/Berlin");
    private final VTimeZone vTimeZone = timezone.getVTimeZone();


    public void createCalendar() {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(STANDARD_PROD);
        calendar.getProperties().add(CURRENT_VERSION);
        calendar.getProperties().add(STANDARD_SCALE);

    }

    public void convertEventToCalendarEvent(Optional<EventModel> event) {

        if (event.isEmpty()) {
            return;
        }

        java.util.Calendar startDate = new GregorianCalendar();
        setDateAndTimeForCalendarEvent(startDate, 2025, 1,1,12,0);

        java.util.Calendar endDate = new GregorianCalendar();
        setDateAndTimeForCalendarEvent(endDate, 2025, 1,1,13,30);

        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent calenderEvent = new VEvent(start, end,event.get().getEventName());
        calenderEvent.getProperties().add(vTimeZone.getTimeZoneId());
        calenderEvent.getProperties().add(event.get().getEventID());



    }

    private void setDateAndTimeForCalendarEvent(java.util.Calendar calendar, int year, int month, int day, int hour, int minute) {
        calendar.setTimeZone(timezone);
        calendar.set(java.util.Calendar.YEAR, year);
        calendar.set(java.util.Calendar.MONTH, month - 1);
        calendar.set(java.util.Calendar.DAY_OF_MONTH, day);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, hour);
        calendar.set(java.util.Calendar.MINUTE, minute);
    }


}
