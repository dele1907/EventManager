package de.eventmanager.core.export;

import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.users.User;
import helper.DateOperationsHelper;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;


import java.net.URI;
import java.util.GregorianCalendar;
import java.util.Optional;

public class ExportManager {
    private static final CalScale STANDARD_SCALE = CalScale.GREGORIAN;
    private static final ProdId STANDARD_PROD = new ProdId("-//Ben Fortuna//iCal4j 1.0//EN");
    private static final Version CURRENT_VERSION = Version.VERSION_2_0;
    private final TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
    private final TimeZone timezone = registry.getTimeZone("Europe/Berlin");
    private final VTimeZone vTimeZone = timezone.getVTimeZone();

    /**
    Method at the moment unclean, because of experimenting for functionality
     */


    public void createCalendar(VEvent event) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(STANDARD_PROD);
        calendar.getProperties().add(CURRENT_VERSION);
        calendar.getProperties().add(STANDARD_SCALE);
        //calendar.getComponents().add(event);

        System.out.println(calendar);
    }

    public VEvent convertEventToCalendarEvent(Optional<? extends EventModel> optionalEvent) {
        if (optionalEvent.isEmpty()) {
            return null;
        }

        EventModel event = optionalEvent.get();
        String eventID = event.getEventID();
        Optional<User> eventCreator = CreatorDatabaseConnector.getEventCreator(eventID);

        if (eventCreator.isEmpty()) {
            return null;
        }

        int startYear = DateOperationsHelper.getEventStartYear(eventID);
        int startMonth = DateOperationsHelper.getEventStartMonth(eventID);
        int startDay = DateOperationsHelper.getEventStartDay(eventID);
        int startHour = DateOperationsHelper.getEventStartHour(eventID);
        int startMinute = DateOperationsHelper.getEventStartMinute(eventID);

        java.util.Calendar startDate = setDateAndTimeForCalendarEvent(startYear, startMonth, startDay, startHour, startMinute);

        java.util.Calendar endDate = new GregorianCalendar();
        /*
        int endYear = DateOperationsHelper.getEventEndYear(eventID);
        int endMonth = DateOperationsHelper.getEventEndMonth(eventID);
        int endDay = DateOperationsHelper.getEventEndDay(eventID);
        int endHour = DateOperationsHelper.getEventEndHour(eventID);
        int endMinute = DateOperationsHelper.getEventEndMinute(eventID);
         */
        //Todo: Change to endYear, endMonth,... if its implemented in DateOperationsHelper
        setDateAndTimeForCalendarEvent(startYear, startMonth, startDay, startHour, startMinute);

        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent calenderEvent = new VEvent(start, end,event.getEventName());
        calenderEvent.getProperties().add(vTimeZone.getTimeZoneId());

        Uid uid = new Uid(eventID);
        calenderEvent.getProperties().add(uid);

        Attendee creator = createEventCreatorAttendee(eventCreator);
        calenderEvent.getProperties().add(creator);

        return calenderEvent;
    }

    private java.util.Calendar setDateAndTimeForCalendarEvent(int year, int month, int day, int hour, int minute) {
        java.util.Calendar calendar = new GregorianCalendar(year, (month-1), day, hour, minute);
        calendar.setTimeZone(timezone);

        return calendar;
    }

    private Attendee createEventCreatorAttendee(Optional<User> eventCreator) {
        String eventCreatorEmail = eventCreator.get().getEMailAddress();

        Attendee creator = new Attendee(URI.create(eventCreatorEmail));
        creator.getParameters().add(Role.CHAIR); //Role of Event-Creator
        creator.getParameters().add(new Cn(eventCreator.get().getFirstName()));

        return creator;
    }




}
