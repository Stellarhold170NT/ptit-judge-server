package vn.ptit.judge.soap;

import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.xpath;
import static org.springframework.ws.test.server.RequestCreators.withPayload;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import vn.ptit.judge.repository.SessionRepository;
import vn.ptit.judge.repository.UserRepository;

@SpringBootTest
class DataServiceEndpointTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SessionRepository sessionRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @BeforeEach
    void setUp() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    void testGetData() throws Exception {
        StringSource requestPayload = new StringSource(
            "<ns2:getDataRequest xmlns:ns2=\"http://ptit.vn/judge/soap/data\">" +
            "  <ns2:studentCode>test-student</ns2:studentCode>" +
            "  <ns2:qCode>yyhHZUpt</ns2:qCode>" +
            "</ns2:getDataRequest>"
        );

        Map<String, String> nsMap = Map.of("ns2", "http://ptit.vn/judge/soap/data");

        mockClient
            .sendRequest(withPayload(requestPayload))
            .andExpect(noFault())
            .andExpect(xpath("//ns2:data", nsMap).exists());
    }

    @Test
    void testSubmitDataInt() throws Exception {
        StringSource dataRequest = new StringSource(
            "<ns2:getDataRequest xmlns:ns2=\"http://ptit.vn/judge/soap/data\">" +
            "  <ns2:studentCode>test-submit</ns2:studentCode>" +
            "  <ns2:qCode>yyhHZUpt</ns2:qCode>" +
            "</ns2:getDataRequest>"
        );

        mockClient
            .sendRequest(withPayload(dataRequest))
            .andExpect(noFault());

        StringSource submitRequest = new StringSource(
            "<ns2:submitDataIntRequest xmlns:ns2=\"http://ptit.vn/judge/soap/data\">" +
            "  <ns2:studentCode>test-submit</ns2:studentCode>" +
            "  <ns2:qCode>yyhHZUpt</ns2:qCode>" +
            "  <ns2:sum>0</ns2:sum>" +
            "</ns2:submitDataIntRequest>"
        );

        Map<String, String> nsMap = Map.of("ns2", "http://ptit.vn/judge/soap/data");

        mockClient
            .sendRequest(withPayload(submitRequest))
            .andExpect(noFault())
            .andExpect(xpath("//ns2:status", nsMap).exists());
    }

    @Test
    void testSubmitWithoutPriorData() throws Exception {
        StringSource submitRequest = new StringSource(
            "<ns2:submitDataIntRequest xmlns:ns2=\"http://ptit.vn/judge/soap/data\">" +
            "  <ns2:studentCode>unknown-student</ns2:studentCode>" +
            "  <ns2:qCode>yyhHZUpt</ns2:qCode>" +
            "  <ns2:sum>0</ns2:sum>" +
            "</ns2:submitDataIntRequest>"
        );

        Map<String, String> nsMap = Map.of("ns2", "http://ptit.vn/judge/soap/data");

        mockClient
            .sendRequest(withPayload(submitRequest))
            .andExpect(noFault())
            .andExpect(xpath("//ns2:status", nsMap).exists());
    }
}
