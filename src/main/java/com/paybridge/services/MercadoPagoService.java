package com.paybridge.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.paymentmethod.PaymentMethodClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.net.MPResourceList;
import com.mercadopago.resources.paymentmethod.PaymentMethod;
import com.paybridge.mappers.PaymentMapper;
import com.paybridge.repository.ShoppingCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import io.github.cdimascio.dotenv.Dotenv;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.stream.Collectors;
import java.util.Optional;
import com.paybridge.entities.ShoppingCartEntity;
import com.mercadopago.resources.paymentmethod.PaymentMethodFinancialInstitutions;
import com.mercadopago.resources.preference.Preference;
import com.paybridge.mappers.ShoppingCartMapper;
import com.paybridge.dto.ShoppingCartDTO;
import com.paybridge.dto.UserDTO;
import com.paybridge.dto.CartItemDTO;
import com.mercadopago.client.preference.PreferencePayerRequest;
import com.paybridge.mappers.PreferenceItemsRequestMapper;

@Service
public class MercadoPagoService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private PreferenceItemsRequestMapper preferenceItemsRequestMapper;

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

    public String buyShoppingCart(Long shoppingCartId) {
        try {
            Optional<ShoppingCartEntity> shoppingCart = shoppingCartRepository.findById(shoppingCartId);
            if (shoppingCart.isPresent()) {

                ShoppingCartDTO shoppingCartDTO = this.shoppingCartMapper.mapEntityToDTO(shoppingCart.get());
                Long userId = Long.valueOf(shoppingCartDTO.getUserId());
                UserDTO user = this.userService.getUserById(userId);

                List<CartItemDTO> cartItems = this.cartItemService.getCartItemsByUserId(userId);

                List<PreferenceItemRequest> preferenceItems = cartItems.stream().map((cartItem) -> {
                    return this.preferenceItemsRequestMapper.mapToPreferenceItemRequest(cartItem);
                }).collect(Collectors.toList());

                PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                        .items(preferenceItems)
                        .payer(PreferencePayerRequest.builder().email(user.getEmail()).build())
                        .backUrls(PreferenceBackUrlsRequest.builder().success("http://localhost:8080/success")
                                .failure("http://localhost:8080/failure").pending("http://localhost:8080/pending")
                                .build()) // Implementacion de vistas de redireccion para el frontend
                        .build();

                PreferenceClient client = new PreferenceClient();
                Preference preference = client.create(preferenceRequest);

            } else {
                throw new RuntimeException("Shopping cart not found");
            }

            return "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
