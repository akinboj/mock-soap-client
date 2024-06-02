package net.development.hl7soapClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.MimeHeaders;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPConstants;
import jakarta.xml.soap.SOAPMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoapClientHelper {
    private static final Logger logger = LoggerFactory.getLogger(SoapClientHelper.class);

    public static String sendSOAPRequest(String endpointUrl, String hl7Message) throws Exception {
        logger.info("Sending HL7 message to SOAP server at {}", endpointUrl);

        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Sanitize HL7 message
        hl7Message = sanitizeHL7Message(hl7Message);

        // Create the SOAP message
        SOAPMessage soapMessage = createSOAPMessage(hl7Message);

        // Log the SOAP request message
        ByteArrayOutputStream soapRequestStream = new ByteArrayOutputStream();
        soapMessage.writeTo(soapRequestStream);
        logger.info("<=*=* Outgoing HL7 SOAP Request:\n {}", new String(soapRequestStream.toByteArray()));

        // Set the Content-Type header to application/soap+xml
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.setHeader("Content-Type", "application/soap+xml");

        // Send the SOAP message and get the response
        SOAPMessage soapResponse = soapConnection.call(soapMessage, endpointUrl);

        // Process the SOAP response
        String response = getSOAPResponse(soapResponse);

        soapConnection.close();
        return response;
    }

    private static String sanitizeHL7Message(String hl7Message) {
        // Remove control characters not allowed in XML
        return hl7Message.replaceAll("[^\\x09\\x0A\\x0D\\x20-\\x7E]", "");
    }

    private static SOAPMessage createSOAPMessage(String hl7Message) throws Exception {
    	String soapMessageString = "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:hl7=\"http://hl7soapServices.development.net/\">\n" +
                "<soapenv:Header/>\n" +
                "<soapenv:Body>\n" +
                "<hl7:receiveHL7Message>\n" +
                "<arg0>\n" +
                "<![CDATA[\n" + 
                hl7Message + "\n" + 
                "]]>\n" +
                "</arg0>\n" +
                "</hl7:receiveHL7Message>\n" +
                "</soapenv:Body>\n" +
                "</soapenv:Envelope>\r";

        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        InputStream is = new ByteArrayInputStream(soapMessageString.getBytes(StandardCharsets.UTF_8));
        return messageFactory.createMessage(new MimeHeaders(), is);
    }

    private static String getSOAPResponse(SOAPMessage soapResponse) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        soapResponse.writeTo(outputStream);
        return new String(outputStream.toByteArray());
    }
}
