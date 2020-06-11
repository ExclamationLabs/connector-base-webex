package com.exclamationlabs.connid.base.webex;

import com.exclamationlabs.connid.base.connector.util.ConnectorTestUtils;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.objects.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static com.exclamationlabs.connid.base.connector.configuration.ConnectorProperty.CONNECTOR_BASE_AUTH_DIRECT_TOKEN;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebexConnectorIntegrationTest {

    private WebexConnector connector;

    private static String generatedUserId;
    private static String generatedGroupId;

    @Before
    public void setup() {
        connector = new WebexConnector();
        WebexConfiguration configuration = new WebexConfiguration();
        configuration.setMidPointConfigurationFilePath("src/test/resources/testWebexConfiguration.properties");
        connector.init(configuration);
    }

    @Test
    public void test110UserCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Betty").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Betty Rubble").build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("mneugebauer+bettyrubble@exclamationlabs.com")).build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test
    public void test120UserModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Barney2").build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid(generatedUserId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test130UsersGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, "", resultsHandler, new OperationOptionsBuilder().build());
        assertTrue(idValues.size() >= 1);
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test140UserGet() {
        generatedUserId = "Y2lzY29zcGFyazovL3VzL1BFT1BMRS9mZGNkMDhkNy01ZmJkLTQzYWEtYWMxMy01YTBhNWIwZTI0ZWM";

        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, generatedUserId, resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
    }

    @Test
    public void test210GroupCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValue("Genesis").build());

        Uid newId = connector.create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedGroupId = newId.getUidValue();
    }

    @Test
    public void test220GroupModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValue("Genesis2").build());

        Uid newId = connector.update(ObjectClass.GROUP, new Uid(generatedGroupId), attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test230GroupsGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, "", resultsHandler, new OperationOptionsBuilder().build());
        assertTrue(idValues.size() >= 1);
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test240GroupGet() {
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, generatedGroupId, resultsHandler, new OperationOptionsBuilder().build());

        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test290GroupDelete() {
        connector.delete(ObjectClass.GROUP, new Uid(generatedGroupId), new OperationOptionsBuilder().build());
    }

    @Test
    public void test390UserDelete() {
        connector.delete(ObjectClass.ACCOUNT, new Uid(generatedUserId), new OperationOptionsBuilder().build());
    }
}
