package com.exclamationlabs.connid.base.webex.adapter;

import com.exclamationlabs.connid.base.connector.adapter.AdapterValueTypeConverter;
import com.exclamationlabs.connid.base.connector.adapter.BaseGroupsAdapter;
import com.exclamationlabs.connid.base.webex.model.WebexGroup;
import com.exclamationlabs.connid.base.webex.model.WebexUser;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.ConnectorObject;

import java.util.Set;

import static com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute.*;

public class WebexGroupsAdapter extends BaseGroupsAdapter<WebexUser, WebexGroup> {

    @Override
    protected WebexGroup constructGroup(Set<Attribute> attributes, boolean creation) {
        WebexGroup group = new WebexGroup();
        group.setId(AdapterValueTypeConverter.getIdentityIdAttributeValue(attributes));
        group.setName(AdapterValueTypeConverter.getSingleAttributeValue(
                String.class, attributes, GROUP_NAME));

        return group;
    }

    @Override
    protected ConnectorObject constructConnectorObject(WebexGroup group) {
        return getConnectorObjectBuilder(group)
                .addAttribute(AttributeBuilder.build(GROUP_ID.name(), group.getId()))
                .addAttribute(AttributeBuilder.build(GROUP_NAME.name(), group.getName()))
                .build();
    }
}
