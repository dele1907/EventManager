package de.eventmanager.core.users.Management;

import de.eventmanager.core.database.Communication.BookingDatabaseConnector;
import de.eventmanager.core.database.Communication.CreatorDatabaseConnector;
import de.eventmanager.core.database.Communication.EventDatabaseConnector;
import de.eventmanager.core.database.Communication.UserDatabaseConnector;
import de.eventmanager.core.events.EventModel;
import de.eventmanager.core.events.PrivateEvent;
import de.eventmanager.core.events.PublicEvent;
import de.eventmanager.core.export.Management.ExportManager;
import de.eventmanager.core.observer.EventNotificator;
import de.eventmanager.core.observer.UserObserver;
import de.eventmanager.core.roles.Role;
import de.eventmanager.core.users.User;
import helper.ConfigurationDataSupplierHelper;
import helper.LoggerHelper;
import helper.PasswordHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserManagerImpl implements UserManager {

    private final EventNotificator eventNotificator = EventNotificator.getInstance();
    private final ExportManager exportManager = new ExportManager();

    //#region Constant variables
    private final String MISSING_PERMISSION_MESSAGE = "Permission denied!";
    private final String NOT_EVENT_CREATOR_OR_ADMIN_MESSAGE = "Only the Event-Creator or Admins can do this!";
    //#endregion Constant variables

    //#region User related CRUD-Operations
    @Override
    public boolean createNewUser(String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                 String password, String phoneNumber, boolean isAdmin, String loggedUserByID) {

        if (checkUserSelfRegisterOrCreated(loggedUserByID)) {
            LoggerHelper.logErrorMessage(User.class, MISSING_PERMISSION_MESSAGE);

            return false;
        }

        UserDatabaseConnector.createNewUser(new User(firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber, isAdmin));

        return true;
    }

    @Override
    public void editUser(String userID, String firstName,
                         String lastName, String dateOfBirth,
                         String eMailAddress, String password,
                         String phoneNumber, String loggedUserByID
    ) {
        var user = UserDatabaseConnector.readUserByID(userID);
        var loggedUser = getUserByID(loggedUserByID);

        if (!areMultipleUserPresent(user, loggedUser)) {return;}

        if (!loggedUser.get().getRole().equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, MISSING_PERMISSION_MESSAGE);

            return;
        }

        UserDatabaseConnector.updateUser(setEditedValuesForUser(user, firstName, lastName, dateOfBirth, eMailAddress, password, phoneNumber));
    }

    private User setEditedValuesForUser(Optional<User> optionalUser,String firstName, String lastName, String dateOfBirth, String eMailAddress,
                                        String password, String phoneNumber) {
        var userToEdit = optionalUser.get();

        userToEdit.setFirstName(firstName);
        userToEdit.setLastName(lastName);
        userToEdit.setDateOfBirth(dateOfBirth);
        userToEdit.seteMailAddress(eMailAddress);
        userToEdit.setPassword(password);
        userToEdit.setPhoneNumber(phoneNumber);

        return userToEdit;
    }

    @Override
    public boolean deleteUser(String eMailUserToDelete, String loggedUserByID) {
        var loggedUserOptional = UserDatabaseConnector.readUserByID(loggedUserByID);

        if (!isUserPresent(loggedUserOptional)) {return false;};

        var loggedUser = loggedUserOptional.get();

        if (!loggedUser.getRole().equals(Role.ADMIN)){
            LoggerHelper.logErrorMessage(User.class, MISSING_PERMISSION_MESSAGE);

            return false;
        }

        return UserDatabaseConnector.deleteUserByEmail(eMailUserToDelete);
    }

    @Override
    public Optional<User> getUserByID(String userID) {
        return UserDatabaseConnector.readUserByID(userID);
    }

    @Override
    public Optional<User> getUserByEmail(String eMailAddress) {
        return UserDatabaseConnector.readUserByEMail(eMailAddress);
    }

    private boolean checkUserSelfRegisterOrCreated(String loggedUserByID) {
        return !loggedUserByID.equals(ConfigurationDataSupplierHelper.REGISTER_NEW_USER_ID) &&
                !UserDatabaseConnector.readUserByID(loggedUserByID)
                        .map(user -> user.getRole().equals(Role.ADMIN))
                        .orElse(false);
    }

    private boolean isUserPresent(Optional<User> optionalUser) {
        return optionalUser.isPresent();
    }

    private boolean areMultipleUserPresent(Optional<User> optionalUser, Optional<User> secondOptionalUser) {
        return optionalUser.isPresent() && secondOptionalUser.isPresent();
    }
    //#endregion User related CRUD-Operations

    //#region Event related CRUD-Operations
    @Override
    public boolean createNewEvent(String eventName, String eventStart, String eventEnd, String category,
                                  String postalCode, String address, String eventLocation,
                                  String description, int maxParticipants, boolean isPrivateEvent, String loggedUserID) {

        String cityNameByPostalCode = EventDatabaseConnector.getCityNameByPostalCode(postalCode);

        return isPrivateEvent ?
                createPrivateEvent(eventName, eventStart, eventEnd, category, postalCode, cityNameByPostalCode,
                        address, eventLocation,
                        description, loggedUserID).isPresent() :
                createPublicEvent(eventName, eventStart, eventEnd, category, postalCode, cityNameByPostalCode,
                        address, eventLocation,
                        description, maxParticipants, loggedUserID).isPresent();
    }

    private Optional<PrivateEvent> createPrivateEvent(String eventName, String eventStart,
                                                     String eventEnd, String category,
                                                     String postalCode, String city,
                                                     String address, String eventLocation,
                                                     String description, String loggedUserID) {
        PrivateEvent event = new PrivateEvent(eventName, eventStart, eventEnd, category, postalCode, city, address, eventLocation, description);
        EventDatabaseConnector.createNewEvent(event);
        CreatorDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUserID);

        return Optional.of(event);
    }

    private Optional<PublicEvent> createPublicEvent(String eventName, String eventStart,
                                                   String eventEnd, String category,
                                                   String postalCode, String city,
                                                   String address, String eventLocation,
                                                   String description, int maxParticipants,
                                                   String loggedUserID) {
        if (maxParticipants == 0) {
            maxParticipants = -1;
        }

        PublicEvent event = new PublicEvent(eventName, eventStart, eventEnd, category, postalCode, city, address,
                eventLocation, description, maxParticipants);
        EventDatabaseConnector.createNewEvent(event);
        CreatorDatabaseConnector.assignUserAsEventCreator(event.getEventID(), loggedUserID);

        return Optional.of(event);
    }

    @Override
    public Optional<? extends EventModel> getEventByID(String eventID) {
        return EventDatabaseConnector.readEventByID(eventID);
    }

    @Override
    public String getEventInformationByEventID(String eventID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (!isEventPresent(optionalEvent)) {return null;}

        return optionalEvent.get().toString();
    }

    @Override
    public List<String> readPublicEventsByName(String name) {
        return getEventsQueryAsArrayList(EventDatabaseConnector::readPublicEventsByName, name);
    }

    @Override
    public List<String> readPublicEventsByLocation(String location) {
        return getEventsQueryAsArrayList(EventDatabaseConnector::readPublicEventsByLocation, location);
    }

    @Override
    public List<String> readPublicEventByCity(String city) {
        return getEventsQueryAsArrayList(EventDatabaseConnector::readPublicEventByCity, city);
    }

    @Override
    public List<String> getUsersBookedEventsInformation(String userID) {
        var usersBookedEvents = new ArrayList<String>();

        getUsersBookedEvents(userID).forEach(event -> {
            usersBookedEvents.add(event.toString());
        });

        return usersBookedEvents;
    }

    @Override
    public List<EventModel> getUsersBookedEvents(String userID) {
        return EventDatabaseConnector.getUsersBookedEventsByUserID(userID);
    }

    public boolean getUserHasAlreadyBookedEvent(String userID, String eventID) {
        return EventDatabaseConnector.getUsersBookedEventsByUserID(userID)
                .stream()
                .anyMatch(event -> event.getEventID().equals(eventID));
    }

    private List<String> getEventsQueryAsArrayList(Function<String, List<PublicEvent>> queryFunction, String queryParam) {
        var events = new ArrayList<String>();

        queryFunction.apply(queryParam).forEach(event -> {
            events.add(event.toString());
        });

        return events;
    }

    @Override
    public List<String> readCreatedEventsByUserID(String userID) {
        var createdEvents = new ArrayList<String>();

        EventDatabaseConnector.getEventsByCreatorID(userID).forEach(event -> {
            createdEvents.add(event.toString());
        });

        return createdEvents;
    }

    @Override
    public boolean editEvent(String eventID, String eventName,
                             String eventStart, String eventEnd,
                             String category, String postalCode, String address,
                             String eventLocation, String description,
                             String loggedUserID) {

        var optionalEvent = getEventByID(eventID);

        if (!isEventPresent(optionalEvent)) return false;

        if (!checkCanUserPerformEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, NOT_EVENT_CREATOR_OR_ADMIN_MESSAGE);

            return false;
        }

        EventModel eventToEdit = setEditedValuesForEvent(optionalEvent, eventName, eventStart, eventEnd, category,
                postalCode, EventDatabaseConnector.getCityNameByPostalCode(postalCode), address, eventLocation,
                description);

        EventDatabaseConnector.updateEvent(eventToEdit);
        eventNotificator.notifyObservers(eventToEdit);
        LoggerHelper.logInfoMessage(User.class, "Event after editing: " + eventToEdit);

        return true;
    }

    private EventModel setEditedValuesForEvent(Optional<? extends EventModel> optionalEvent,
                                               String eventName, String eventStart,
                                               String eventEnd, String category,
                                               String postalCode, String city, String address,
                                               String eventLocation, String description) {
        EventModel eventToEdit = optionalEvent.get();

        eventToEdit.setEventName(eventName);
        eventToEdit.setEventStart(eventStart);
        eventToEdit.setEventEnd(eventEnd);
        eventToEdit.setCategory(category);
        eventToEdit.setPostalCode(postalCode);
        eventToEdit.setCity(city);
        eventToEdit.setAddress(address);
        eventToEdit.setEventLocation(eventLocation);
        eventToEdit.setDescription(description);

        return eventToEdit;
    }

    @Override
    public boolean deleteEvent(String eventID, String loggedUserID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        var optionalEventCreator = CreatorDatabaseConnector.getEventCreator(eventID);
        var loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);
        String userIDofUserCreatedEvent = "";

        if (!isEventPresent(optionalEvent)) {return false;}
        if (!isUserPresent(loggedUser)) {return false;}

        if (!checkCanUserPerformEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(UserManagerImpl.class, NOT_EVENT_CREATOR_OR_ADMIN_MESSAGE);

            return false;
        }

        if (optionalEventCreator.isPresent()) {
            userIDofUserCreatedEvent = optionalEventCreator.get().getUserID();
        }

        CreatorDatabaseConnector.removeUserAsEventCreator(eventID,userIDofUserCreatedEvent);

        return EventDatabaseConnector.deleteEventByID(eventID);
    }

    @Override
    public ArrayList<String> showEventParticipantList(String eventID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);

        if (!isEventPresent(optionalEvent)) {return null;}

        return optionalEvent.get().getBookedUsersOnEvent();
    }
    //#endregion Event related CRUD-Operations

    //#region Event-Operations
    @Override
    public boolean bookEvent(String eventID, String loggedUserID) {
        var publicEvent = EventDatabaseConnector.readPublicEventByID(eventID);
        var loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!isEventPresent(publicEvent)) {return false;}
        if (!isUserPresent(loggedUser)) {return false;}
        if (publicEvent.get().isPrivateEvent()) {return false;}

        String loggedUserEmail = loggedUser.get().getEMailAddress();
        boolean hasUserBookedTheEvent = publicEvent.get().getBookedUsersOnEvent().contains(loggedUserEmail);

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "Event already booked!");

            return false;
        }

        BookingDatabaseConnector.addBooking(eventID,loggedUserID);
        eventNotificator.addObserver(new UserObserver(loggedUser.get(), publicEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "Event booked successfully!");

        return true;
    }

    @Override
    public boolean cancelEvent(String eventID, String loggedUserID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        var loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!isEventPresent(optionalEvent)) {return false;}
        if (!isUserPresent(loggedUser)) {return false;}

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(loggedUser.get().getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }

        BookingDatabaseConnector.removeBooking(eventID,loggedUserID);
        eventNotificator.removeObserver(new UserObserver(loggedUser.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "Event cancelled successfully!");

        return true;
    }

    @Override
    public boolean addUserToEvent(String eventID, String userEmail, String loggedUserID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        var userToAdd = UserDatabaseConnector.readUserByEMail(userEmail);
        var loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!areMultipleUserPresent(loggedUser, userToAdd)) {return false;}
        if (!isEventPresent(optionalEvent)) {return false;}

        if (!checkCanUserPerformEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(User.class, NOT_EVENT_CREATOR_OR_ADMIN_MESSAGE);

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToAdd.get().getEMailAddress());

        if (hasUserBookedTheEvent){
            LoggerHelper.logErrorMessage(User.class, "User is already booked for this event!");

            return false;
        }

        BookingDatabaseConnector.addBooking(eventID, userToAdd.get().getUserID());
        eventNotificator.addObserver(new UserObserver(userToAdd.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "User added to event successfully!");

        return true;
    }

    @Override
    public boolean removeUserFromEvent(String eventID, String userEmail, String loggedUserID) {
        var optionalEvent = EventDatabaseConnector.readEventByID(eventID);
        var userToRemove = UserDatabaseConnector.readUserByEMail(userEmail);
        var loggedUser = UserDatabaseConnector.readUserByID(loggedUserID);

        if (!isEventPresent(optionalEvent)) {return false;}

        if (!areMultipleUserPresent(loggedUser, userToRemove)) {return false;}

        if (!checkCanUserPerformEventOperations(loggedUserID, eventID)) {
            LoggerHelper.logErrorMessage(User.class, NOT_EVENT_CREATOR_OR_ADMIN_MESSAGE);

            return false;
        }

        boolean hasUserBookedTheEvent = optionalEvent.get().getBookedUsersOnEvent().contains(userToRemove.get().getEMailAddress());

        if (!hasUserBookedTheEvent) {
            LoggerHelper.logErrorMessage(User.class, "You can only cancel events for which you are registered!");

            return false;
        }

        BookingDatabaseConnector.removeBooking(eventID,userToRemove.get().getUserID());
        eventNotificator.addObserver(new UserObserver(userToRemove.get(), optionalEvent.get()));
        LoggerHelper.logInfoMessage(User.class, "User removed from event successfully!");

        return true;
    }

    private boolean isEventPresent(Optional<? extends EventModel> optionalEvent) {
        return optionalEvent.isPresent();
    }
    //#endregion Event-Operations

    //#region Permission-Operations
    @Override
    public void addAdminStatusToUser(User user){
        user.setRoleAdmin(true);

        UserDatabaseConnector.updateUser(user);
    }

    @Override
    public void removeAdminStatusFromUser(User user) {
        user.setRoleAdmin(false);

        UserDatabaseConnector.updateUser(user);
    }

    @Override
    public void addAdminStatusToUserByUserID(String userID, User loggedUser) {
        if (!loggedUser.getRole().equals(Role.ADMIN)) {
            return;
        }

        this.addAdminStatusToUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    @Override
    public void removeAdminStatusFromUserByUserID(String userID, User loggedUser) {
        if (!loggedUser.getRole().equals(Role.ADMIN)) {return;}

        this.removeAdminStatusFromUser(UserDatabaseConnector.readUserByID(userID).get());
    }

    private boolean checkCanUserPerformEventOperations(String loggedUserID, String eventID) {
        return getUserByID(loggedUserID).get().getRole().equals(Role.ADMIN) ||
                CreatorDatabaseConnector.checkIfUserIsEventCreator(eventID, loggedUserID);
    }
    //#endregion Permission-Operations

    //#region Registration & Authentication
    @Override
    public boolean isValidRegistrationPassword(String password, String checkPassword) {
        return isValidPassword(password) && comparingPassword(password, checkPassword);
    }

    private boolean isValidPassword(String password) {
        char[] restrictedCharacters = {' ', '$', '@', '§', '&', '%', 'ä', 'ö', 'ü', 'ß', 'Ä', 'Ü', 'Ö'};

        for (var restricted : restrictedCharacters) {
            if (password.indexOf(restricted) != -1) {
                return false;
            }
        }

        return true;
    }

    private boolean comparingPassword(String password, String checkPassword) {
        if (password.isEmpty() || checkPassword.isEmpty()) {

            LoggerHelper.logErrorMessage(User.class, "Wrong password!");

            return false;
        }

        return checkPassword.equals(password);
    }

    @Override
    public boolean authenticationUserLogin(String email, String password) {
        var userOptional = getUserByEmail(email);

        if (userOptional.isEmpty()) {
            LoggerHelper.logErrorMessage(UserDatabaseConnector.class, "Email address not found");

            return false;

        }

        return PasswordHelper.verifyPassword(password, userOptional.get().getPassword());
    }
    //#endregion Registration & Authentication

    //#region Export-Events
    @Override
    public boolean exportEvents(String loggedUserID) {
        List <EventModel> eventList = getUsersBookedEvents(loggedUserID);

        return exportManager.exportEvents(eventList);
    }
    //#endregion Export-Events
}
