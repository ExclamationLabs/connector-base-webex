package com.exclamationlabs.connid.base.webex.adapter;

import com.ciscospark.Team;
import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseGroupsAdapter;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.text.SimpleDateFormat;
import java.util.Set;

import static com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute.*;

public class WebexGroupsAdapter extends BaseGroupsAdapter<WebexUser, WebexGroup> {

    private static final SimpleDateFormat DATE_FORMATTER;

    static {
        DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    @Override
    protected WebexGroup constructGroup(Set<Attribute> attributes, boolean creation) {
        WebexGroup group = new WebexGroup(new Team());
        group.getTeam().setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
        group.getTeam().setName(AdapterValueTypeConverter.getSingleAttributeValue(
                String.class, attributes, GROUP_NAME));

        return group;
    }

    @Override
    protected ConnectorObject constructConnectorObject(WebexGroup group) {
        return getConnectorObjectBuilder(group)
                .addAttribute(AttributeBuilder.build(GROUP_ID.name(), group.getTeam().getId()))
                .addAttribute(AttributeBuilder.build(GROUP_NAME.name(), group.getTeam().getName()))
                .addAttribute(AttributeBuilder.build(CREATED_DATE.name(),
                        DATE_FORMATTER.format(group.getTeam().getCreated())))
                .build();
    }
}
