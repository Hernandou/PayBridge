package com.paybridge.services;
import java.util.List;
import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.paymentmethod.PaymentMethodClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.resources.paymentmethod.PaymentMethod;
import com.paybridge.mappers.PaymentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import io.github.cdimascio.dotenv.Dotenv;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.stream.Collectors;
import com.mercadopago.resources.paymentmethod.PaymentMethodFinancialInstitutions;
@Service
public class MercadoPagoService {

    private final Dotenv dotenv = Dotenv.load();

    public String getAllPayMethods() throws MPException, MPApiException {
        try {
            MercadoPagoConfig.setAccessToken(dotenv.get("ACCESS_TOKEN"));
            PaymentMethodClient client = new PaymentMethodClient();
            MPResourceList<PaymentMethod> paymentMethods = client.list();
            Stream<PaymentMethod> stream = paymentMethods.getResults().stream();
            List<PaymentMapper> paymentObjects = stream.map(paymentMethod -> {
                String secureLogoURL = paymentMethod.getSecureThumbnail();
                String bankingEntityName = paymentMethod.getFinancialInstitutions().stream().map(PaymentMethodFinancialInstitutions::getDescription).collect(Collectors.joining(", "));
                PaymentMapper paymentObject = new PaymentMapper(paymentMethod.getName(),bankingEntityName, secureLogoURL);
                return paymentObject;
            }).collect(Collectors.toList());
            try{
                return mapToJSON(paymentObjects);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        } catch (MPException e) {
            throw new RuntimeException(e);
        }
    }

    public String mapToJSON(List<PaymentMapper> paymentObject) throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(paymentObject);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    

}
