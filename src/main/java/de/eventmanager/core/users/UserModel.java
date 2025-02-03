package de.eventmanager.core.users;

import de.eventmanager.core.roles.Role;
import java.util.Objects;

public abstract class UserModel {
    String userID;
    String firstName;
    String lastName;
    String dateOfBirth;
    String eMailAddress;
    String password;
    String phoneNumber;
    Role role;

    //#region getter
    public Role getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getEMailAddress() {
        return eMailAddress;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserID() {
        return userID;
    }
    //#endregion getter

    //#region setter
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setRoleAdmin(boolean hasAdminRole) {
        this. role = hasAdminRole ? Role.ADMIN : Role.USER;
    }
    //#endregion setter

    @Override
    public boolean equals(Object object) {
        if (this == object) {

            return true;
        }
        if (object == null || getClass() != object.getClass()) {

            return false;
        }

        UserModel other = (UserModel) object;

        return userID.equals(other.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID);
    }

}
