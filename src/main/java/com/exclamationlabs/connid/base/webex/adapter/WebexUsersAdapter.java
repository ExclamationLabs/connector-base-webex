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

package com.exclamationlabs.connid.base.webex.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseAdapter;
import com.exclamationlabs.connid.base.connector.attribute.ConnectorAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebExConfiguration;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ObjectClass;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.BOOLEAN;
import static com.exclamationlabs.connid.base.connector.attribute.ConnectorAttributeDataType.STRING;
import static com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.*;
import static org.identityconnectors.framework.common.objects.AttributeInfo.Flags.NOT_CREATABLE;

public class WebexUsersAdapter extends BaseAdapter<WebexUser, WebExConfiguration> {

    @Override
    public ObjectClass getType() {
        return ObjectClass.ACCOUNT;
    }

    @Override
    public Class<WebexUser> getIdentityModelClass() {
        return WebexUser.class;
    }

    @Override
    public Set<ConnectorAttribute> getConnectorAttributes() {
        Set<ConnectorAttribute> result = new HashSet<>();
        result.add(new ConnectorAttribute(USER_ID.name(), STRING, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(FIRST_NAME.name(), STRING));
        result.add(new ConnectorAttribute(LAST_NAME.name(), STRING));
        result.add(new ConnectorAttribute(DISPLAY_NAME.name(), STRING));
        result.add(new ConnectorAttribute(EMAILS.name(), STRING, MULTIVALUED));
        result.add(new ConnectorAttribute(ORG_ID.name(), STRING));
        result.add(new ConnectorAttribute(AVATAR.name(), STRING));
        result.add(new ConnectorAttribute(CREATED.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(LAST_MODIFIED.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(LAST_ACTIVITY.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(TIMEZONE.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(NICKNAME.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(STATUS.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(TYPE.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(ROLES.name(), STRING, MULTIVALUED));
        result.add(new ConnectorAttribute(LICENSES.name(), STRING, MULTIVALUED));
        result.add(new ConnectorAttribute(PHONE_NUMBERS.name(), STRING, NOT_CREATABLE, NOT_UPDATEABLE, MULTIVALUED));
        result.add(new ConnectorAttribute(INVITE_PENDING.name(), BOOLEAN, NOT_CREATABLE, NOT_UPDATEABLE));
        result.add(new ConnectorAttribute(LOGIN_ENABLED.name(), BOOLEAN, NOT_CREATABLE, NOT_UPDATEABLE));
        return result;
    }

    @Override
    protected Set<Attribute> constructAttributes(WebexUser user) {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(AttributeBuilder.build(USER_ID.name(), user.getId()));
        attributes.add(AttributeBuilder.build(FIRST_NAME.name(), user.getFirstName()));
        attributes.add(AttributeBuilder.build(LAST_NAME.name(), user.getLastName()));
        attributes.add(AttributeBuilder.build(DISPLAY_NAME.name(), user.getDisplayName()));
        attributes.add(AttributeBuilder.build(ORG_ID.name(), user.getOrgId()));
        attributes.add(AttributeBuilder.build(AVATAR.name(), user.getAvatar()));
        attributes.add(AttributeBuilder.build(CREATED.name(), user.getCreated()));
        attributes.add(AttributeBuilder.build(LAST_MODIFIED.name(), user.getLastModified()));
        attributes.add(AttributeBuilder.build(LAST_ACTIVITY.name(), user.getLastActivity()));
        attributes.add(AttributeBuilder.build(TIMEZONE.name(), user.getTimezone()));
        attributes.add(AttributeBuilder.build(NICKNAME.name(), user.getNickname()));
        attributes.add(AttributeBuilder.build(STATUS.name(), user.getStatus()));
        attributes.add(AttributeBuilder.build(TYPE.name(), user.getType()));
        attributes.add(AttributeBuilder.build(ROLES.name(), user.getRoles()));
        attributes.add(AttributeBuilder.build(LICENSES.name(), user.getLicenses()));
        attributes.add(AttributeBuilder.build(PHONE_NUMBERS.name(), user.getPhoneNumbers()));
        attributes.add(AttributeBuilder.build(INVITE_PENDING.name(), user.getInvitePending()));
        attributes.add(AttributeBuilder.build(LOGIN_ENABLED.name(), user.getLoginEnabled()));

        return attributes;
    }

    @Override
    protected WebexUser constructModel(Set<Attribute> attributes, Set<Attribute> multiValueAdd,
                                       Set<Attribute> multiValueRemove, boolean isCreation) {
        WebexUser user = new WebexUser();
        user.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

        user.setFirstName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, FIRST_NAME));
        user.setLastName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, LAST_NAME));
        user.setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, DISPLAY_NAME));
        user.setEmails(AdapterValueTypeConverter.getMultipleAttributeValue(List.class,
                attributes, EMAILS));
        user.setOrgId(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, ORG_ID));
        user.setAvatar(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, AVATAR));
        user.setRoles(AdapterValueTypeConverter.getMultipleAttributeValue(List.class,
                attributes, ROLES));
        user.setLicenses(AdapterValueTypeConverter.getMultipleAttributeValue(List.class,
                attributes, LICENSES));

        return user;
    }

}
