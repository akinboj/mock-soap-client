package net.development.hl7soapClient;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.soap.SOAPBinding;
import net.development.hl7soapServices.HL7SoapServiceImplementation;

import java.net.URL;
import javax.xml.namespace.QName;

public class SendHL7Message {
    public static void main(String[] args) throws Exception {
        URL wsdlURL = new URL("http://192.168.0.17:8090/services/hl7-soap?wsdl");
        QName SERVICE_NAME = new QName("http://hl7soapServices.development.net/", "HL7SoapServiceImplementationService");
        Service service = Service.create(wsdlURL, SERVICE_NAME);

        HL7SoapServiceImplementation soapService = service.getPort(HL7SoapServiceImplementation.class);
        BindingProvider bindingProvider = (BindingProvider) soapService;
        
        // Set SOAP 1.2 binding
        SOAPBinding binding = (SOAPBinding) bindingProvider.getBinding();
        binding.setMTOMEnabled(false);
        
        // Now invoke the service
        String response = soapService.receiveHL7Message("your HL7 message");
        System.out.println(response);
    }
}