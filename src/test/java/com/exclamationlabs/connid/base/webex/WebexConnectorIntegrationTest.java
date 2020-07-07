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

package com.exclamationlabs.connid.base.webex;

import com.exclamationlabs.connid.base.connector.configuration.ConfigurationNameBuilder;
import com.exclamationlabs.connid.base.connector.test.IntegrationTest;
import com.exclamationlabs.connid.base.connector.test.util.ConnectorTestUtils;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.exceptions.AlreadyExistsException;
import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;
import org.identityconnectors.framework.common.exceptions.PermissionDeniedException;
import org.identityconnectors.framework.common.objects.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebexConnectorIntegrationTest extends IntegrationTest {

    private WebexConnector connector;

    private static String generatedUserId;

    // user administrator
    private static final String testGroupId = "Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZnVsbF9hZG1pbg";

    @Override
    public String getConfigurationName() {
        return new ConfigurationNameBuilder().withConnector("WEBEX").build();
    }

    @Before
    public void setup() {
        connector = new WebexConnector();
        setup(connector, new WebexConfiguration(getConfigurationName()));

    }

    @Test(expected = InvalidAttributeValueException.class)
    public void test010UserCreateInvalid() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Nada").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Bad").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Nothing").build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test
    public void test110UserCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta Rubble").build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("connectors+betarubble@exclamationlabs.com")).build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.ROLES.name()).
                addValue(Collections.singletonList(testGroupId)).build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test(expected=AlreadyExistsException.class)
    public void test112UserCreateAlreadyExists() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta Rubble").build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("connectors+betarubble@exclamationlabs.com")).build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.ROLES.name()).
                addValue(Collections.singletonList(testGroupId)).build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test(expected=InvalidAttributeValueException.class)
    public void test114UserCreateMissingEmail() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Test").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Test Rubble").build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test
    public void test120UserModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta2").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta2 Rubble").build());
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
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, generatedUserId, resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
    }

    @Test(expected = PermissionDeniedException.class)
    public void test210GroupCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValue("Genesis").build());

        connector.create(ObjectClass.GROUP, attributes, new OperationOptionsBuilder().build());
    }

    @Test(expected = PermissionDeniedException.class)
    public void test220GroupModify() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValue("Genesis2").build());

        connector.update(ObjectClass.GROUP, new Uid(testGroupId), attributes, new OperationOptionsBuilder().build());
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

        connector.executeQuery(ObjectClass.GROUP, testGroupId, resultsHandler, new OperationOptionsBuilder().build());

        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test(expected = PermissionDeniedException.class)
    public void test290GroupDelete() {
        connector.delete(ObjectClass.GROUP, new Uid(testGroupId), new OperationOptionsBuilder().build());
    }

    @Test
    public void test390UserDelete() {
        connector.delete(ObjectClass.ACCOUNT, new Uid(generatedUserId), new OperationOptionsBuilder().build());
    }

}
