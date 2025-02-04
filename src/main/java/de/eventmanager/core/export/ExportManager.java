package de.eventmanager.core.export;

import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.users.User;
import helper.DateOperationsHelper;
import helper.LoggerHelper;
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

    //#region constants
    private static final CalScale STANDARD_SCALE = CalScale.GREGORIAN;
    private static final ProdId STANDARD_PROD = new ProdId("-//Ben Fortuna//iCal4j 1.0//EN");
    private static final Version CURRENT_VERSION = Version.VERSION_2_0;
    private final TimeZoneRegistry TIMEZONE_REGISTRY = TimeZoneRegistryFactory.getInstance().createRegistry();
    private final TimeZone TIMEZONE_GERMANY = TIMEZONE_REGISTRY.getTimeZone("Europe/Berlin");
    private final VTimeZone V_TIMEZONE_GERMANY = TIMEZONE_GERMANY.getVTimeZone();
    //#endregion constants

    /**
    Method at the moment unclean, because of experimenting for functionality
     */

    public boolean createCalendar(EventModel event) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(STANDARD_PROD);
        calendar.getProperties().add(CURRENT_VERSION);
        calendar.getProperties().add(STANDARD_SCALE);

        VEvent vEvent = convertEventToCalendarEvent(event);
        if (vEvent == null) {
            LoggerHelper.logErrorMessage(ExportManager.class, "VEvent is null");
            System.out.println("VEvent is null");

            return false;
        }
        calendar.getComponents().add(vEvent);

        if (!calendar.getComponents().contains(vEvent)) {
            LoggerHelper.logErrorMessage(ExportManager.class, "Event are not in the calendar");

            return false;
        }

        LoggerHelper.logInfoMessage(ExportManager.class, vEvent.toString());
        System.out.println(calendar);

        return true;
    }

    public VEvent convertEventToCalendarEvent(EventModel event) {
        String eventID = event.getEventID();
        String eventName = event.getEventName();
        Optional<User> eventCreator = CreatorDatabaseConnector.getEventCreator(eventID);

        if (eventCreator.isEmpty()) {
            System.out.println("Event creator not found");

            return null;
        }

        java.util.Calendar startDate = setEventStartTime(eventName);
        java.util.Calendar endDate = setEventEndTime(eventName);

        if (startDate == null || endDate == null) {
            System.out.println("Start or End Date is null for eventID " + eventID);
            return null;
        }

        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent calenderEvent = new VEvent(start, end,event.getEventName());

        if (V_TIMEZONE_GERMANY != null) {
            calenderEvent.getProperties().add(V_TIMEZONE_GERMANY.getTimeZoneId());
        } else {
            System.out.println("Time zone is null");
            return null;
        }

        calenderEvent.getProperties().add(new Uid(eventID));

        Attendee creator = createEventCreatorAttendee(eventCreator);
        calenderEvent.getProperties().add(creator);

        return calenderEvent;
    }

    private Attendee createEventCreatorAttendee(Optional<User> eventCreator) {
        String eventCreatorEmail = eventCreator.get().getEMailAddress();

        Attendee creator = new Attendee(URI.create(eventCreatorEmail));
        creator.getParameters().add(Role.CHAIR); //Role of Event-Creator
        creator.getParameters().add(new Cn(eventCreator.get().getFirstName()));

        return creator;
    }

    private Attendee createEventParticipantAttendee(Optional<User> eventParticipant) {
        String participantEmail = eventParticipant.get().getEMailAddress();

        Attendee participant = new Attendee(URI.create(participantEmail));
        participant.getParameters().add(Role.OPT_PARTICIPANT);
        participant.getParameters().add(new Cn(eventParticipant.get().getFirstName()));

        return participant;
    }

    private java.util.Calendar setEventStartTime(String eventName) {
        int startYear = DateOperationsHelper.getEventStartYear(eventName);
        int startMonth = DateOperationsHelper.getEventStartMonth(eventName);
        int startDay = DateOperationsHelper.getEventStartDay(eventName);
        int startHour = DateOperationsHelper.getEventStartHour(eventName);
        int startMinute = DateOperationsHelper.getEventStartMinute(eventName);

        System.out.println("startYear: " + startYear);
        System.out.println("Month: " + startMonth);
        System.out.println("Day: " + startDay);
        System.out.println("Hour: " + startHour);
        System.out.println("Minute: " + startMinute);
        return setDateAndTimeForCalendarEvent(startYear, startMonth, startDay, startHour, startMinute);
    }

    private java.util.Calendar setEventEndTime(String eventName) {
        int endYear = DateOperationsHelper.getEventEndYear(eventName);
        int endMonth = DateOperationsHelper.getEventEndMonth(eventName);
        int endDay = DateOperationsHelper.getEventEndDay(eventName);
        int endHour = DateOperationsHelper.getEventEndHour(eventName);
        int endMinute = DateOperationsHelper.getEventEndMinute(eventName);

        System.out.println("endYear: " + endYear
        + "\nMonth: " + endMonth
        + "\nDay: " + endDay
        + "\nHour: " + endHour
        + "\nMinute: " + endMinute);
        return setDateAndTimeForCalendarEvent(endYear, endMonth, endDay, endHour, endMinute);
    }

    private java.util.Calendar setDateAndTimeForCalendarEvent(int year, int month, int day, int hour, int minute) {
        java.util.Calendar calendar = new GregorianCalendar(year, (month-1), day, hour, minute);
        calendar.setTimeZone(TIMEZONE_GERMANY);

        return calendar;
    }

}
