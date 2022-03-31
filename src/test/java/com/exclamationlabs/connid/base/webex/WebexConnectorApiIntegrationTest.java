/*
    Copyright 2022 Exclamation Labs
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
import com.exclamationlabs.connid.base.connector.configuration.ConfigurationReader;
import com.exclamationlabs.connid.base.connector.test.ApiIntegrationTest;
import com.exclamationlabs.connid.base.webex.attribute.WebexGroupAttribute;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebExConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.identityconnectors.framework.common.exceptions.AlreadyExistsException;
import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;
import org.identityconnectors.framework.common.exceptions.PermissionDeniedException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WebexConnectorApiIntegrationTest extends
        ApiIntegrationTest<WebExConfiguration, WebexConnector> {

    private static String generatedUserId;

    // user administrator
    private static final String testGroupId = "Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZnVsbF9hZG1pbg";

    @Override
    protected WebExConfiguration getConfiguration() {
        return new WebExConfiguration(
                new ConfigurationNameBuilder().withConnector("WEBEX").build()
        );
    }

    @Override
    protected Class<WebexConnector> getConnectorClass() {
        return WebexConnector.class;
    }

    @Override
    protected void readConfiguration(WebExConfiguration webExConfiguration) {
        ConfigurationReader.setupTestConfiguration(webExConfiguration);
    }

    @Before
    public void testSetup() {
        super.setup();
    }

    @Test
    public void test005Test() {
        getConnectorFacade().test();
    }

    @Test(expected = InvalidAttributeValueException.class)
    public void test010UserCreateInvalid()

    {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Nada").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Bad").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Nothing").build());

        Uid newId = getConnectorFacade().create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test
    public void test110UserCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta2").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta2 Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("connectors+beta2rubble@exclamationlabs.com")).build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.ROLES.name()).
                addValue(Collections.singletonList(testGroupId)).build());

        Uid newId = getConnectorFacade().create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
        generatedUserId = newId.getUidValue();
    }

    @Test(expected= AlreadyExistsException.class)
    public void test112UserCreateAlreadyExists() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta2").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta2 Rubble").build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("connectors+beta2rubble@exclamationlabs.com")).build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.ROLES.name()).
                addValue(Collections.singletonList(testGroupId)).build());

        getConnectorFacade().create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
    }

    @Test(expected= InvalidAttributeValueException.class)
    public void test114UserCreateMissingEmail() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Test").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Test Rubble").build());

        getConnectorFacade().create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
    }

    @Test
    public void test120UserModify() {
        Set<AttributeDelta> attributes = new HashSet<>();
        attributes.add(new AttributeDeltaBuilder().setName(
                WebexUserAttribute.FIRST_NAME.name()).addValueToReplace("Beta22").build());
        attributes.add(new AttributeDeltaBuilder().setName(
                WebexUserAttribute.DISPLAY_NAME.name()).addValueToReplace("Beta22 Rubble").build());

        Set<AttributeDelta> response = getConnectorFacade().updateDelta(ObjectClass.ACCOUNT, new Uid(generatedUserId),
                attributes, new OperationOptionsBuilder().build());

        assertNotNull(response);
        assertTrue(response.isEmpty());
    }

    @Test
    public void test130UsersGet() {
        results = new ArrayList<>();
        getConnectorFacade().search( ObjectClass.ACCOUNT, null, getHandler(), new OperationOptionsBuilder().build());
        assertTrue(getResults().size() >= 1);
    }

    @Test
    public void test140UserGet() {
        results = new ArrayList<>();
        Attribute idAttribute = new AttributeBuilder().setName(Uid.NAME).addValue(generatedUserId).build();
        getConnectorFacade().search( ObjectClass.ACCOUNT, new EqualsFilter(idAttribute),
                getHandler(), new OperationOptionsBuilder().build());
        assertEquals(1, getResults().size());
        assertTrue(StringUtils.isNotBlank(getResults().get(0).getUid().getUidValue()));
    }


    @Test(expected = PermissionDeniedException.class)
    public void test210GroupCreate() {
        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValue("Genesis").build());

        getConnectorFacade().create(ObjectClass.GROUP,
                attributes, new OperationOptionsBuilder().build());
    }

    @Test(expected = PermissionDeniedException.class)
    public void test220GroupModify() {
        Set<AttributeDelta> attributes = new HashSet<>();
        attributes.add(new AttributeDeltaBuilder().setName(WebexGroupAttribute.GROUP_NAME.name()).
                addValueToReplace("Genesis2").build());

        getConnectorFacade().updateDelta(
                ObjectClass.GROUP, new Uid(testGroupId), attributes, new OperationOptionsBuilder().build());
    }

    @Test
    public void test230GroupsGet() {
        results = new ArrayList<>();
        getConnectorFacade().search(
                ObjectClass.GROUP, null, getHandler(), new OperationOptionsBuilder().build());
        assertTrue(results.size() >= 1);
        assertTrue(StringUtils.isNotBlank(getResults().get(0).getUid().getUidValue()));
        assertTrue(StringUtils.isNotBlank(getResults().get(0).getName().getNameValue()));
    }

    @Test
    public void test240GroupGet() {
        Attribute idAttribute = new AttributeBuilder().setName(Uid.NAME).addValue(testGroupId).build();
        getConnectorFacade().search(
                ObjectClass.GROUP, new EqualsFilter(idAttribute), getHandler(),
                new OperationOptionsBuilder().build());
        assertEquals(1, getResults().size());
        assertTrue(StringUtils.isNotBlank(getResults().get(0).getUid().getUidValue()));
        assertTrue(StringUtils.isNotBlank(getResults().get(0).getName().getNameValue()));
    }

    @Test(expected = PermissionDeniedException.class)
    public void test290GroupDelete() {
        getConnectorFacade().delete(ObjectClass.GROUP, new Uid(testGroupId), new OperationOptionsBuilder().build());
    }

    @Test
    public void test390UserDelete() {
        getConnectorFacade().delete(ObjectClass.ACCOUNT, new Uid(generatedUserId), new OperationOptionsBuilder().build());
    }
}