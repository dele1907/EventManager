package de.eventmanager.core.export.Management;

import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.export.Exporter;
import de.eventmanager.core.users.User;
import helper.DateOperationsHelper;
import helper.LoggerHelper;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.CuType;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.*;
import java.net.URI;
import java.util.GregorianCalendar;
import java.util.List;
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

    public boolean exportEvents(List<? extends EventModel> eventList) {
        var optionalCalendar = createCalendar(eventList);

        if (optionalCalendar.isEmpty()) {
            LoggerHelper.logInfoMessage(ExportManager.class, "No Calendar found");

            return false;
        }

        Calendar calendar = optionalCalendar.get();

        return Exporter.exportEventToICSFile(calendar);
    }

    //#region Create Calendar
    private Optional<Calendar> createCalendar(List<? extends EventModel> eventList) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(STANDARD_PROD);
        calendar.getProperties().add(CURRENT_VERSION);
        calendar.getProperties().add(STANDARD_SCALE);
        calendar.getComponents().add(V_TIMEZONE_GERMANY);

        return addAllBookedVEventsToCalendar(eventList, calendar);
    }

    private Optional<Calendar> addAllBookedVEventsToCalendar(List<? extends EventModel> eventList, Calendar calendar) {
        boolean allEventsAdded = eventList.stream().
                map(this::convertEventToVEvent).
                filter(Optional::isPresent).
                map(Optional::get).
                allMatch(vEvent -> addVEventToCalendar(vEvent, calendar));

        if (!allEventsAdded) {
            return Optional.empty();
        }

        return Optional.of(calendar);
    }

    private boolean addVEventToCalendar(VEvent vEvent, Calendar calendar) {
        calendar.getComponents().add(vEvent);

        if (!calendar.getComponents().contains(vEvent)) {
            LoggerHelper.logErrorMessage(ExportManager.class, "Event are not in the calendar");

            return false;
        }

        return true;
    }
    //#endregion Create Calendar

    //#region Event-Compatibility
    private Optional<VEvent> convertEventToVEvent(EventModel event) {
        String eventName = event.getEventName();
        Optional<User> eventCreator = CreatorDatabaseConnector.getEventCreator(event.getEventID());

        if (eventCreator.isEmpty()) {return Optional.empty();}

        java.util.Calendar startDate = setEventStartTime(eventName).get();
        java.util.Calendar endDate = setEventEndTime(eventName).get();

        DateTime start = new DateTime(startDate.getTime());
        DateTime end = new DateTime(endDate.getTime());
        VEvent calenderEvent = new VEvent(start, end, event.getEventName());

        addProperties(calenderEvent, event, eventCreator);

        return Optional.of(calenderEvent);
    }
    //#endregion Event-Compatibility

    //#region Attendees
    private Optional<Attendee> createEventCreatorAttendee(Optional<User> eventCreator) {
        String eventCreatorEmail = eventCreator.get().getEMailAddress();

        Attendee creator = new Attendee(URI.create(eventCreatorEmail));
        creator.getParameters().add(Role.CHAIR); //Role of Event-Creator
        creator.getParameters().add(new Cn(eventCreator.get().getFirstName()));
        creator.getParameters().add(CuType.INDIVIDUAL);

        return Optional.of(creator);
    }
    //#endregion Attendees

    //#region Properties
    private void addProperties(VEvent calenderEvent, EventModel event, Optional<User> eventCreator) {
        String eventID = event.getEventID();

        calenderEvent.getProperties().add(new Uid(eventID));
        calenderEvent.getProperties().add(new Description(addDescriptionToVEvent(event)));
        calenderEvent.getProperties().add(new Location(addLocationToVEvent(event)));
        calenderEvent.getProperties().add(createEventCreatorAttendee(eventCreator).get());
    }

    private String addLocationToVEvent(EventModel eventModel) {
        return eventModel.getEventLocation() + " - " + eventModel.getAddress() + ", " + eventModel.getPostalCode() + ", " + eventModel.getCity();
    }

    private String addDescriptionToVEvent(EventModel eventModel) {
        String description = eventModel.getDescription();
        String lastName = CreatorDatabaseConnector.getEventCreator(eventModel.getEventID()).get().getLastName();
        String firstName = CreatorDatabaseConnector.getEventCreator(eventModel.getEventID()).get().getFirstName();

        return "Event-Creator: " + lastName + ", " + firstName + "\n" + description;
    }
    //#endregion Properties

    //#region Time-Settings
    private Optional<java.util.Calendar> setEventStartTime(String eventName) {
        int startYear = DateOperationsHelper.getEventStartValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_YEAR);
        int startMonth = DateOperationsHelper.getEventStartValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_MONTH);
        int startDay = DateOperationsHelper.getEventStartValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_DAY);
        int startHour = DateOperationsHelper.getEventStartValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_HOUR);
        int startMinute = DateOperationsHelper.getEventStartValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_MINUTE);

        return setDateAndTimeForCalendarEvent(startYear, startMonth, startDay, startHour, startMinute);
    }

    private Optional<java.util.Calendar> setEventEndTime(String eventName) {
        int endYear = DateOperationsHelper.getEventEndValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_YEAR);
        int endMonth = DateOperationsHelper.getEventEndValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_MONTH);
        int endDay = DateOperationsHelper.getEventEndValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_DAY);
        int endHour = DateOperationsHelper.getEventEndValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_HOUR);
        int endMinute = DateOperationsHelper.getEventEndValue(eventName, DateOperationsHelper.FORMAT_SPECIFIER_MINUTE);

        return setDateAndTimeForCalendarEvent(endYear, endMonth, endDay, endHour, endMinute);
    }

    private Optional<java.util.Calendar> setDateAndTimeForCalendarEvent(int year, int month, int day, int hour, int minute) {
        java.util.Calendar calendar = new GregorianCalendar(year, (month - 1), day, hour, minute);
        calendar.setTimeZone(TIMEZONE_GERMANY);

        return Optional.of(calendar);
    }
    //#endregion Time-Settings

}
