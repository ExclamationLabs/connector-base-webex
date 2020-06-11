package com.exclamationlabs.connid.base.webex.adapter;

import com.ciscospark.Person;
import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseUsersAdapter;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute.*;

public class WebexUsersAdapter extends BaseUsersAdapter<WebexUser, WebexGroup> {
    @Override
    protected WebexUser constructUser(Set<Attribute> attributes, boolean creation) {
        WebexUser user = new WebexUser(new Person());
        user.getPerson().setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));

        user.getPerson().setFirstName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, FIRST_NAME));
        user.getPerson().setLastName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, LAST_NAME));
        user.getPerson().setDisplayName(AdapterValueTypeConverter.getSingleAttributeValue
                (String.class, attributes, DISPLAY_NAME));

        Object[] emails = AdapterValueTypeConverter.getMultipleAttributeValue(List.class,
                attributes, EMAILS).toArray();
        List<String> emailStrings = new ArrayList<>();
        for (Object current : emails) {
            emailStrings.add(current.toString());
        }
        user.getPerson().setEmails(emailStrings.toArray(new String[0]));

        return user;
    }

    @Override
    protected ConnectorObject constructConnectorObject(WebexUser user) {
        return getConnectorObjectBuilder(user)
                .addAttribute(AttributeBuilder.build(USER_ID.name(), user.getPerson().getId()))
                .addAttribute(AttributeBuilder.build(FIRST_NAME.name(), user.getPerson().getFirstName()))
                .addAttribute(AttributeBuilder.build(LAST_NAME.name(), user.getPerson().getLastName()))
                .addAttribute(AttributeBuilder.build(DISPLAY_NAME.name(), user.getPerson().getDisplayName()))
                .build();
    }
}
