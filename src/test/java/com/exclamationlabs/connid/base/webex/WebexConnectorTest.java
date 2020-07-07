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

import com.exclamationlabs.connid.base.connector.test.util.ConnectorMockRestTest;
import com.exclamationlabs.connid.base.connector.test.util.ConnectorTestUtils;
import com.exclamationlabs.connid.base.webex.attribute.WebexUserAttribute;
import com.exclamationlabs.connid.base.webex.configuration.WebexConfiguration;
import com.exclamationlabs.connid.base.webex.driver.rest.WebexDriver;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.spi.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class WebexConnectorTest extends ConnectorMockRestTest {

    private WebexConnector connector;

    @Before
    public void setup() {
        connector = new WebexConnector() {
            @Override
            public void init(Configuration configuration) {
                setAuthenticator(null);
                setDriver(new WebexDriver() {
                    @Override
                    protected HttpClient createClient() {
                        return stubClient;
                    }
                });
                super.init(configuration);
            }
        };
        WebexConfiguration configuration = new WebexConfiguration();
        configuration.setTestConfiguration();
        connector.init(configuration);
    }

    @Test
    public void test110UserCreate() {
        final String responseData = "{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS8yNGQxYjcwNS0zZGFiLTQ4ZDYtODlkMC0wMzI4NmU5YzY3NDM\",\"emails\":[\"test+betarubble@exclamationlabs.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test+betarubble@testelabs.calls.webex.com\",\"primary\":true}],\"displayName\":\"Beta Rubble\",\"nickName\":\"Beta\",\"firstName\":\"Beta\",\"lastName\":\"Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[],\"created\":\"2020-06-19T20:10:47.464Z\",\"lastModified\":\"2020-06-19T20:10:49.584Z\",\"status\":\"pending\",\"invitePending\":true,\"loginEnabled\":false,\"type\":\"person\"}";
        prepareMockResponse(responseData);

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.LAST_NAME.name()).addValue("Rubble").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta Rubble").build());

        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.EMAILS.name()).
                addValue(Collections.singletonList("connectors+betarubble@exclamationlabs.com")).build());

        Uid newId = connector.create(ObjectClass.ACCOUNT, attributes, new OperationOptionsBuilder().build());
        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test120UserModify() {
        // modify requires a get prior to execution
        final String responseData = "{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS8yNGQxYjcwNS0zZGFiLTQ4ZDYtODlkMC0wMzI4NmU5YzY3NDM\",\"emails\":[\"test+betarubble@exclamationlabs.com\"],\"displayName\":\"Beta Rubble\",\"firstName\":\"Beta2\",\"lastName\":\"Beta2 Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[]}\n";
        prepareMockResponse(responseData);

        Set<Attribute> attributes = new HashSet<>();
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.FIRST_NAME.name()).addValue("Beta2").build());
        attributes.add(new AttributeBuilder().setName(WebexUserAttribute.DISPLAY_NAME.name()).addValue("Beta2 Rubble").build());

        Uid newId = connector.update(ObjectClass.ACCOUNT, new Uid("1234"), attributes, new OperationOptionsBuilder().build());

        assertNotNull(newId);
        assertNotNull(newId.getUidValue());
    }

    @Test
    public void test130UsersGet() {
        String responseData = "{\"notFoundIds\":null,\"items\":[{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS82MDFmMDAxOC02MDcyLTQ1YjMtOWI4Zi02NDMwN2E1YTMxNTE\",\"emails\":[\"test+alpharubble@exclamationlabs.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test+alpharubble@testelabs.calls.webex.com\",\"primary\":true}],\"displayName\":\"Alpha Rubble\",\"nickName\":\"Alpha2\",\"firstName\":\"Alpha2\",\"lastName\":\"Alpha2 Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[],\"created\":\"2020-06-12T15:30:42.237Z\",\"lastModified\":\"2020-06-12T15:30:50.717Z\",\"status\":\"pending\",\"invitePending\":true,\"loginEnabled\":false,\"type\":\"person\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS9mNzZlMjk2Ni00ODE1LTQzMWUtOTkyYi1mMWQwYTI2MDUyOGY\",\"emails\":[\"test+bambamrubble@exclamationlabs.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test+bambamrubble@testelabs.calls.webex.com\",\"primary\":true}],\"displayName\":\"Bam Bam Rubble\",\"nickName\":\"Bam Bam\",\"firstName\":\"Bam Bam\",\"lastName\":\"Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[],\"created\":\"2020-06-12T15:01:15.785Z\",\"lastModified\":\"2020-06-12T15:01:19.459Z\",\"status\":\"pending\",\"invitePending\":true,\"loginEnabled\":false,\"type\":\"person\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS8yNGQxYjcwNS0zZGFiLTQ4ZDYtODlkMC0wMzI4NmU5YzY3NDM\",\"emails\":[\"test+betarubble@exclamationlabs.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test+betarubble@testelabs.calls.webex.com\",\"primary\":true}],\"displayName\":\"Beta Rubble\",\"nickName\":\"Beta2\",\"firstName\":\"Beta2\",\"lastName\":\"Beta2 Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[],\"created\":\"2020-06-19T20:10:47.464Z\",\"lastModified\":\"2020-06-19T20:10:56.155Z\",\"status\":\"pending\",\"invitePending\":true,\"loginEnabled\":false,\"type\":\"person\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS84ZjcxODg2MS1hMjAwLTQ5NzUtOTVkZi1kZmJmMjQ5NmNhNTE\",\"emails\":[\"test+bettyrubble@exclamationlabs.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test+bettyrubble@testelabs.calls.webex.com\",\"primary\":true}],\"displayName\":\"Betty Rubble\",\"nickName\":\"Betty\",\"firstName\":\"Betty\",\"lastName\":\"Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[],\"created\":\"2020-06-11T17:08:10.107Z\",\"lastModified\":\"2020-06-11T17:09:20.225Z\",\"status\":\"unknown\",\"invitePending\":false,\"loginEnabled\":true,\"type\":\"person\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS84NDQ2MDYwNy03ZTgxLTRiMzEtODdiZS0yMTI2MDQwMWUyYjE\",\"emails\":[\"test2@yahoo.com\"],\"sipAddresses\":[{\"type\":\"cloud-calling\",\"value\":\"test2@testelabs.calls.webex.com\",\"primary\":true},{\"type\":\"personal-room\",\"value\":\"1297575445@neugey.webex.com\",\"primary\":false},{\"type\":\"personal-room\",\"value\":\"neugey24@neugey.webex.com\",\"primary\":false}],\"displayName\":\"neugey24@yahoo.com\",\"nickName\":\"neugey24@yahoo.com\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZnVsbF9hZG1pbg\"],\"licenses\":[\"Y2lzY29zcGFyazovL3VzL0xJQ0VOU0UvMTc2YTY1ZjUtODg1NS00YTA3LWEwMDUtMWFiMjk1OWM5OTYyOkVFXzUzYTVlYTBkLWFmYTItNGUxMS05NGU2LWU3NjA1MGQzYmYyZF9uZXVnZXkud2ViZXguY29t\",\"Y2lzY29zcGFyazovL3VzL0xJQ0VOU0UvMTc2YTY1ZjUtODg1NS00YTA3LWEwMDUtMWFiMjk1OWM5OTYyOkNNUl9hNWNjNmQ5Zi0xOGE3LTRiMTctYmE1Mi0xN2UwNjZiODgyYWFfbmV1Z2V5LndlYmV4LmNvbQ\"],\"created\":\"2020-06-11T13:39:54.209Z\",\"lastModified\":\"2020-06-12T19:20:46.562Z\",\"status\":\"unknown\",\"invitePending\":false,\"loginEnabled\":true,\"type\":\"person\"}]}";
        prepareMockResponse(responseData);

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
        final String responseData = "{\"id\":\"Y2lzY29zcGFyazovL3VzL1BFT1BMRS8yNGQxYjcwNS0zZGFiLTQ4ZDYtODlkMC0wMzI4NmU5YzY3NDM\",\"emails\":[\"test+betarubble@exclamationlabs.com\"],\"displayName\":\"Beta Rubble\",\"firstName\":\"Beta2\",\"lastName\":\"Beta2 Rubble\",\"orgId\":\"Y2lzY29zcGFyazovL3VzL09SR0FOSVpBVElPTi8xNzZhNjVmNS04ODU1LTRhMDctYTAwNS0xYWIyOTU5Yzk5NjI\",\"roles\":[],\"licenses\":[]}\n";
        prepareMockResponse(responseData);

        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.ACCOUNT, "1234", resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
    }




    @Test
    public void test230GroupsGet() {
        String responseData = "{\"items\":[{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfdXNlcl9hZG1pbg\",\"name\":\"User Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLmFwcGxpY2F0aW9u\",\"name\":\"Application Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvc3Bhcmsuc3luY2ttcw\",\"name\":\"KMS Sync Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLmJpbGxpbmc\",\"name\":\"Billing Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfcmVhZG9ubHlfYWRtaW4\",\"name\":\"Read-only Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLnN1cHBvcnQ\",\"name\":\"Support Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLmFsbA\",\"name\":\"Portal Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLnJlcG9ydHM\",\"name\":\"Reports Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZGV2aWNlX2FkbWlu\",\"name\":\"Device Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLnBhcnRuZXIuc2FsZXNhZG1pbg\",\"name\":\"Sales Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvYXRsYXMtcG9ydGFsLnBhcnRuZXIuaGVscGRlc2s\",\"name\":\"Help Desk Administrator\"},{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZnVsbF9hZG1pbg\",\"name\":\"Full Administrator\"}]}";
        prepareMockResponse(responseData);
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
        String responseData = "{\"id\":\"Y2lzY29zcGFyazovL3VzL1JPTEUvaWRfZnVsbF9hZG1pbg\",\"name\":\"Full Administrator\"}";
        prepareMockResponse(responseData);
        List<String> idValues = new ArrayList<>();
        List<String> nameValues = new ArrayList<>();
        ResultsHandler resultsHandler = ConnectorTestUtils.buildResultsHandler(idValues, nameValues);

        connector.executeQuery(ObjectClass.GROUP, "1234", resultsHandler, new OperationOptionsBuilder().build());
        assertEquals(1, idValues.size());
        assertTrue(StringUtils.isNotBlank(idValues.get(0)));
        assertTrue(StringUtils.isNotBlank(nameValues.get(0)));
    }

    @Test
    public void test390UserDelete() {
        prepareMockResponseEmpty();
        connector.delete(ObjectClass.ACCOUNT, new Uid("1234"), new OperationOptionsBuilder().build());
    }


}
