package com.paybridge.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.paymentmethod.PaymentMethodClient;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.paymentmethod.PaymentMethod;
import com.paybridge.mappers.PaymentMapper;
import com.paybridge.dto.PaymentBodyDTO;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.paybridge.repository.ShoppingCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import io.github.cdimascio.dotenv.Dotenv;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.stream.Collectors;
import java.util.Optional;
import com.paybridge.entities.ShoppingCartEntity;
import com.mercadopago.resources.paymentmethod.PaymentMethodFinancialInstitutions;
import com.paybridge.mappers.ShoppingCartMapper;
import com.paybridge.dto.ShoppingCartDTO;
import com.paybridge.dto.UserDTO;
import com.mercadopago.client.payment.PaymentClient;
import java.math.BigDecimal;
import com.paybridge.exceptions.UserNotFoundException;

@Service
public class MercadoPagoService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserService userService;

    private final Dotenv dotenv = Dotenv.load();

    public String getAllPayMethods() throws MPException, MPApiException {
        try {
            MercadoPagoConfig.setAccessToken(dotenv.get("ACCESS_TOKEN"));
            PaymentMethodClient client = new PaymentMethodClient();
            MPResourceList<PaymentMethod> paymentMethods = client.list();
            Stream<PaymentMethod> stream = paymentMethods.getResults().stream();
            List<PaymentMapper> paymentObjects = stream.map(paymentMethod -> {
                String secureLogoURL = paymentMethod.getSecureThumbnail();
                String bankingEntityName = paymentMethod.getFinancialInstitutions().stream()
                        .map(PaymentMethodFinancialInstitutions::getDescription).collect(Collectors.joining(", "));
                PaymentMapper paymentObject = new PaymentMapper(paymentMethod.getName(), bankingEntityName,
                        secureLogoURL);
                return paymentObject;
            }).collect(Collectors.toList());
            try {
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

    public String buyShoppingCart(PaymentBodyDTO paymentBodyDTO) throws UserNotFoundException {
        try {
            Optional<ShoppingCartEntity> shoppingCart = shoppingCartRepository
                    .findById(paymentBodyDTO.getShoppingCartId());
            if (shoppingCart.isPresent()) {

                ShoppingCartDTO shoppingCartDTO = this.shoppingCartMapper.mapEntityToDTO(shoppingCart.get());
                Long userId = Long.valueOf(shoppingCartDTO.getUserId());
                UserDTO userDTO = this.userService.getUserById(userId);

                if (userDTO != null && shoppingCartDTO != null) {
                    BigDecimal price = new BigDecimal(shoppingCartDTO.getPrice());
                    String PAYMENT_TOKEN = paymentBodyDTO.getPaymentToken();
                    PaymentCreateRequest paymentRequest = buildPaymentRequest(shoppingCartDTO, userDTO, price,
                            PAYMENT_TOKEN);

                    try {
                        MercadoPagoConfig.setAccessToken(dotenv.get("ACCESS_TOKEN"));
                        PaymentClient paymentClient = new PaymentClient();
                        Payment payment = paymentClient.create(paymentRequest);
                        return payment.getStatus();

                    } catch (MPException | MPApiException e) {
                        throw new RuntimeException(e);
                    }

                }

            } else {
                throw new UserNotFoundException();
            }

            return "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PaymentCreateRequest buildPaymentRequest(ShoppingCartDTO shoppingCartDTO, UserDTO userDTO, BigDecimal price,
            String PAYMENT_TOKEN) {
        return PaymentCreateRequest.builder()
                .transactionAmount(price)
                .token(PAYMENT_TOKEN)
                .description(shoppingCartDTO.getDescription())
                .installments(1)
                .paymentMethodId("visa")
                .payer(PaymentPayerRequest.builder()
                        .email(userDTO.getEmail())
                        .build())
                .build();
    }

}
