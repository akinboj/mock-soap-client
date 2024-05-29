package net.development.hl7soapClient;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.development.hl7soapServices.HL7SoapServiceImplementation;

public class SendHL7Message {
    private static final Logger logger = LoggerFactory.getLogger(SendHL7Message.class);

    public static void main(String[] args) {
        try {
            // The URL of the WSDL file
            URL url = new URL("http://localhost:8080/hl7-soap-service?wsdl");
            // The QName of the service (namespace URI and local part)
            QName qname = new QName("http://hl7soapServices.development.net/", "HL7SoapServiceImplementationService");

            // Create a service object
            Service service = Service.create(url, qname);

            // Extract the endpoint interface, the service "port"
            HL7SoapServiceImplementation hl7Service = service.getPort(HL7SoapServiceImplementation.class);

            // The HL7 message to be sent
            String hl7Message = "MSH|^~\\&|AccMgr|1|||20050110045504||ADT^A01|599102|P|2.3|||\n"
                                + "EVN|A01|20050110045502|||||\n"
                                + "PID|1||10006579^^^1^MRN^1||DUCK^DONALD^D||19241010|M||1|111 DUCK ST^^FOWL^CA^999990000^^M|1|8885551212|8885551212|1|2||40007716^^^AccMgr^VN^1|123121234|||||||||||NO\n"
                                + "NK1|1|DUCK^HUEY|SO|3583 DUCK RD^^FOWL^CA^999990000|8885552222||Y||||||||||||||\n"
                                + "PV1|1|I|PREOP^101^1^1^^^S|3|||37^DISNEY^WALT^^^^^^AccMgr^^^^CI|||01||||1|||37^DISNEY^WALT^^^^^^AccMgr^^^^CI|2|40007716^^^AccMgr^VN|4|||||||||||||||||||1||G|||20050110045253||||||\n"
                                + "GT1|1|8291|DUCK^DONALD^D||111^DUCKST^^FOWL^CA^999990000|8885551212||19241010|M||1|123121234||||#Cartoon Ducks Inc|111^DUCK ST^^FOWL^CA^999990000|8885551212||PT|\n"
                                + "DG1|1|I9|71596^OSTEOARTHROS NOS-L/LEG ^I9|OSTEOARTHROS NOS-L/LEG ||A| \n"
                                + "IN1|1|MEDICARE|3|MEDICARE|||||||Cartoon Ducks Inc|19891001|||4|DUCK^DONALD^D|1|19241010|111 DUCK ST^^FOWL^CA^999990000|||||||||||||||||123121234A||||||PT|M|111 DUCK ST^^FOWL^CA^999990000|||||8291\n"
                                + "IN2|1||123121234|Cartoon Ducks Inc|||123121234A|||||||||||||||||||||||||||||||||||||||||||||||||||||||||8885551212\n"
                                + "IN1|2|NON-PRIMARY|9|MEDICAL MUTUAL CALIF.|PO BOX 94776^^HOLLYWOOD^CA^441414776||8003621279|PUBSUMB|||Cartoon Ducks Inc||||7|DUCK^DONALD^D|1|19241010|111 DUCK ST^^FOWL^CA^999990000|||||||||||||||||056269770||||||PT|M|111 DUCK ST^^FOWL^CA^999990000|||||8291\n"
                                + "IN2|2||123121234|Cartoon Ducks Inc||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||8885551212\n"
                                + "IN1|3|SELF PAY|1|SELF PAY|||||||||||5||1";

            // Send the HL7 message and receive the ACK
            String response = hl7Service.receiveHL7Message(hl7Message);
            logger.info("Received ACK: {}", response);

        } catch (Exception e) {
            logger.error("Error occurred while sending HL7 message: {}", e.getMessage(), e);
        }
    }
}
