package com.exclamationlabs.connid.base.webex.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseUsersAdapter;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.List;
import java.util.Set;

import static com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute.*;

public class WebexUsersAdapter extends BaseUsersAdapter<WebexUser, WebexGroup> {
    @Override
    protected WebexUser constructUser(Set<Attribute> attributes, boolean creation) {
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

    @Override
    protected ConnectorObject constructConnectorObject(WebexUser user) {
        return getConnectorObjectBuilder(user)
                .addAttribute(AttributeBuilder.build(USER_ID.name(), user.getId()))
                .addAttribute(AttributeBuilder.build(FIRST_NAME.name(), user.getFirstName()))
                .addAttribute(AttributeBuilder.build(LAST_NAME.name(), user.getLastName()))
                .addAttribute(AttributeBuilder.build(DISPLAY_NAME.name(), user.getDisplayName()))
                .addAttribute(AttributeBuilder.build(ORG_ID.name(), user.getOrgId()))
                .addAttribute(AttributeBuilder.build(AVATAR.name(), user.getAvatar()))
                .addAttribute(AttributeBuilder.build(CREATED.name(), user.getCreated()))
                .addAttribute(AttributeBuilder.build(LAST_MODIFIED.name(), user.getLastModified()))
                .addAttribute(AttributeBuilder.build(LAST_ACTIVITY.name(), user.getLastActivity()))
                .addAttribute(AttributeBuilder.build(TIMEZONE.name(), user.getTimezone()))
                .addAttribute(AttributeBuilder.build(NICKNAME.name(), user.getNickname()))
                .addAttribute(AttributeBuilder.build(STATUS.name(), user.getStatus()))
                .addAttribute(AttributeBuilder.build(TYPE.name(), user.getType()))
                .addAttribute(AttributeBuilder.build(ROLES.name(), user.getRoles()))
                .addAttribute(AttributeBuilder.build(LICENSES.name(), user.getLicenses()))
                .addAttribute(AttributeBuilder.build(PHONE_NUMBERS.name(), user.getPhoneNumbers()))
                .addAttribute(AttributeBuilder.build(INVITE_PENDING.name(), user.getInvitePending()))
                .addAttribute(AttributeBuilder.build(LOGIN_ENABLED.name(), user.getLoginEnabled()))
                .build();

    }

    @Override
    protected boolean groupAdditionControlledByUpdate() {
        return true;
    }
}
