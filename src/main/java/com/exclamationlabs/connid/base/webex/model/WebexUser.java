/*
    Copyright 2020 Exclamation Labs
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
        http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.exclamationlabs.connid.base.webex.model;

import com.exclamationlabs.connid.base.connector.model.UserIdentityModel;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;

import java.util.List;

public class WebexUser implements UserIdentityModel {

    private String id;
    private List<String> emails;
    private String displayName;
    private String firstName;
    private String lastName;
    private String orgId;
    private String created;
    private String lastModified;
    private String lastActivity;
    private String status;
    private String type;
    private Boolean invitePending;
    private Boolean loginEnabled;
    private String avatar;
    private List<String> roles;
    private List<String> licenses;
    private String timezone;
    private String nickname;
    private List<String> phoneNumbers;

    @Override
    public String getAssignedGroupsAttributeName() {
        return WebexUserAttribute.ROLES.name();
    }

    @Override
    public List<String> getAssignedGroupIds() {
        return getRoles();
    }

    @Override
    public String getIdentityIdValue() {
        return getId();
    }

    @Override
    public String getIdentityNameValue() {
        return getDisplayName();
    }

    public void prepareForUpdate(WebexUser modifiedUser) {
        // Null fields not applicable for update
        setPhoneNumbers(null);
        setNickname(null);
        setTimezone(null);
        setLoginEnabled(null);
        setInvitePending(null);
        setType(null);
        setStatus(null);
        setCreated(null);
        setLastActivity(null);
        setLastModified(null);
        if (modifiedUser.getFirstName() != null) {
            setFirstName(modifiedUser.getFirstName());
        }
        if (modifiedUser.getLastName() != null) {
            setLastName(modifiedUser.getLastName());
        }
        if (modifiedUser.getDisplayName() != null) {
            setLastName(modifiedUser.getDisplayName());
        }
        if (modifiedUser.getEmails() != null) {
            setEmails(modifiedUser.getEmails());
        }
        if (modifiedUser.getRoles() != null) {
            setRoles(modifiedUser.getRoles());
        }
        if (modifiedUser.getLicenses() != null) {
            setLicenses(modifiedUser.getLicenses());
        }
        if (modifiedUser.getOrgId() != null) {
            setOrgId(modifiedUser.getOrgId());
        }
        if (modifiedUser.getAvatar() != null) {
            setAvatar(modifiedUser.getAvatar());
        }

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInvitePending() {
        return invitePending;
    }

    public void setInvitePending(Boolean invitePending) {
        this.invitePending = invitePending;
    }

    public Boolean getLoginEnabled() {
        return loginEnabled;
    }

    public void setLoginEnabled(Boolean loginEnabled) {
        this.loginEnabled = loginEnabled;
    }

    public String getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(String lastActivity) {
        this.lastActivity = lastActivity;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getLicenses() {
        return licenses;
    }

    public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
