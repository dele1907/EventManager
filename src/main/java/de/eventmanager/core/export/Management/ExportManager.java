package de.eventmanager.core.export.Management;

import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.export.Exporter;
import de.eventmanager.core.users.User;
import helper.DateOperationsHelper;
import helper.LoggerHelper;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.model.property.Contact;

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
     * Method at the moment unclean, because of experimenting for functionality
     */

    public Optional<Calendar> createCalendar(EventModel event) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(STANDARD_PROD);
        calendar.getProperties().add(CURRENT_VERSION);
        calendar.getProperties().add(STANDARD_SCALE);
        calendar.getComponents().add(V_TIMEZONE_GERMANY);

        Optional<VEvent> optionalVEvent = convertEventToCalendarEvent(event);
        if (optionalVEvent.isEmpty()) {
            LoggerHelper.logErrorMessage(ExportManager.class, "VEvent is null");
            System.out.println("VEvent is null");

            return Optional.empty();
        }

        VEvent vEvent = optionalVEvent.get();

        calendar.getComponents().add(vEvent);

        if (!calendar.getComponents().contains(vEvent)) {
            LoggerHelper.logErrorMessage(ExportManager.class, "Event are not in the calendar");

            return Optional.empty();
        }

        return Optional.of(calendar);
    }

    public boolean exportEvents(Calendar calendar) {
        return Exporter.exportEvent(calendar);
    }

    public Optional<VEvent> convertEventToCalendarEvent(EventModel event) {
        String eventID = event.getEventID();
        String eventName = event.getEventName();
        Optional<User> eventCreator = CreatorDatabaseConnector.getEventCreator(eventID);

        if (eventCreator.isEmpty()) {
            System.out.println("Event creator not found");

            return Optional.empty();
        }

        java.util.Calendar startDate = setEventStartTime(eventName);
        java.util.Calendar endDate = setEventEndTime(eventName);

        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent calenderEvent = new VEvent(start, end, event.getEventName());

        calenderEvent.getProperties().add(new Uid(eventID));
        calenderEvent.getProperties().add(new Description(event.getDescription()));

        Attendee creator = createEventCreatorAttendee(eventCreator);
        calenderEvent.getProperties().add(creator);

        calenderEvent = addAllParticipantAttendees(event, calenderEvent);

        calenderEvent.getProperties().add(new Location(addLocationToVEvent(event, calenderEvent)));
        //calenderEvent.getProperties().add(new Telephone)

        return Optional.of(calenderEvent);
    }

    //#region helper-methods
    private Attendee createEventCreatorAttendee(Optional<User> eventCreator) {
        String eventCreatorEmail = eventCreator.get().getEMailAddress();

        Attendee creator = new Attendee(URI.create(eventCreatorEmail));
        creator.getParameters().add(Role.CHAIR); //Role of Event-Creator
        creator.getParameters().add(new Cn(eventCreator.get().getFirstName()));

        return creator;
    }

    private Attendee createEventParticipantAttendee(Optional<User> eventParticipant, String participantEmail) {
        Attendee participant = new Attendee(URI.create(participantEmail));
        participant.getParameters().add(Role.OPT_PARTICIPANT);
        participant.getParameters().add(new Cn(eventParticipant.get().getFirstName()));

        return participant;
    }

    private VEvent addAllParticipantAttendees(EventModel event, VEvent vEvent) {
        System.out.println("Booked Users: " + event.getBookedUsersOnEvent());
        for (String participantEmail : event.getBookedUsersOnEvent()) {
            System.out.println("Participant email: " + participantEmail);
            Optional<User> eventParticipant = UserDatabaseConnector.readUserByEMail(participantEmail);
            if (eventParticipant.isPresent()) {
                Attendee participant = createEventParticipantAttendee(eventParticipant, participantEmail);
                vEvent.getProperties().add(participant);
            }
        }

        return vEvent;
    }

    private String addLocationToVEvent(EventModel eventModel, VEvent vEvent) {
        String postalCode = eventModel.getPostalCode();
        String city = eventModel.getCity();
        String address = eventModel.getAddress();
        String location = eventModel.getEventLocation();

        return location + " - " + address + ", " + postalCode + ", " + city + ", " + address;
    }



    private java.util.Calendar setEventStartTime(String eventName) {
        int startYear = DateOperationsHelper.getEventStartYear(eventName);
        int startMonth = DateOperationsHelper.getEventStartMonth(eventName);
        int startDay = DateOperationsHelper.getEventStartDay(eventName);
        int startHour = DateOperationsHelper.getEventStartHour(eventName);
        int startMinute = DateOperationsHelper.getEventStartMinute(eventName);

        return setDateAndTimeForCalendarEvent(startYear, startMonth, startDay, startHour, startMinute);
    }

    private java.util.Calendar setEventEndTime(String eventName) {
        int endYear = DateOperationsHelper.getEventEndYear(eventName);
        int endMonth = DateOperationsHelper.getEventEndMonth(eventName);
        int endDay = DateOperationsHelper.getEventEndDay(eventName);
        int endHour = DateOperationsHelper.getEventEndHour(eventName);
        int endMinute = DateOperationsHelper.getEventEndMinute(eventName);

        return setDateAndTimeForCalendarEvent(endYear, endMonth, endDay, endHour, endMinute);
    }

    private java.util.Calendar setDateAndTimeForCalendarEvent(int year, int month, int day, int hour, int minute) {
        java.util.Calendar calendar = new GregorianCalendar(year, (month - 1), day, hour, minute);
        calendar.setTimeZone(TIMEZONE_GERMANY);

        return calendar;
    }
    //#endregion helper-methods
}
