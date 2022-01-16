package pl.edu.pk.mag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.repository.OrderRepository;
import pl.edu.pk.mag.repository.ProductRepository;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.WarehouseRepository;
import pl.edu.pk.mag.repository.dto.BasketItem;
import pl.edu.pk.mag.repository.entity.User;
import pl.edu.pk.mag.repository.entity.UserOrder;
import pl.edu.pk.mag.repository.entity.Warehouse;
import pl.edu.pk.mag.repository.entity.enums.OrderStatus;
import pl.edu.pk.mag.requests.BasketItemRequest;
import pl.edu.pk.mag.requests.OrderRequest;
import pl.edu.pk.mag.responses.OrderResponse;
import pl.edu.pk.mag.service.mailHandler.EmailService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    public void createOrder(OrderRequest orderRequest, String username) {
        User user = userRepository.getUserByUsername(username).orElseThrow(AppException.NOT_FOUND_USER::getError);
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(orderRequest.getWarehouseCode()).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        createOrder(orderRequest, user, warehouse);

    }

    private void createOrder(OrderRequest orderRequest, User user, Warehouse warehouse) {
        UserOrder userOrder = new UserOrder();
        userOrder.setUserId(user.getId());
        userOrder.setOrderStatus(OrderStatus.PENDING);
        userOrder.setWarehouseId(warehouse.getId());
        userOrder.setBasketItem(
                orderRequest.getBasketItem().stream()
                        .map(basketItemRequest -> {
                            BasketItem basketItem = new BasketItem();
                            basketItem.setQuantity(basketItemRequest.getQuantity());
                            basketItem.setProductId(productRepository.getProductByCode(basketItemRequest.getProductCode()).orElseThrow(AppException.NOT_FOUND_PRODUCT::getError).getId());
                            return basketItem;
                        }).collect(Collectors.toList())
        );
        UserOrder order = orderRepository.save(userOrder);
        try {


            if (user.getAddress().getEmail() != null) {
                emailService.sendSimpleMessage(
                        user.getAddress().getEmail(), "Złożono zamówienie o numerze " + order.getId(),
                        "Złożono zamównienie na następujące produkty: \n" +
                                orderRequest.getBasketItem().stream().map(basketItem -> "* " + basketItem.getProductCode() + ", ilość: " + basketItem.getQuantity()).collect(Collectors.joining("\n")) + "\n" +
                                "Dziękujemy za złożone zamówienie."
                );

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<OrderResponse> getOrderByWarehouse(String whCode) {
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        return toOrderResponse(orderRepository.getOrderByWarehouseId(warehouse.getId()));
    }

    public List<OrderResponse> getOrderByUsername(String username) {
        User user = userRepository.getUserByUsername(username).orElseThrow(AppException.NOT_FOUND_USER::getError);
        return getOrderByUser(user);
    }

    public List<OrderResponse> getOrderByUser(User user) {
        return toOrderResponse(orderRepository.getOrderByUserId(user.getId()));
    }

    private List<OrderResponse> toOrderResponse(List<UserOrder> orderList) {
        return orderList.stream()
                .map(order -> {
                            OrderResponse orderResponse = new OrderResponse();
                            orderResponse.setOrderStatus(order.getOrderStatus().toString());
                            orderResponse.setBasketItems(
                                    order.getBasketItem().stream().map(basketItem -> {
                                        BasketItemRequest basketItemRequest = new BasketItemRequest();
                                        basketItemRequest.setQuantity(basketItem.getQuantity());
                                        basketItemRequest.setProductCode(
                                                productRepository.getById(basketItem.getProductId()).getCode()
                                        );
                                        return basketItemRequest;
                                    }).collect(Collectors.toList())
                            );
                            orderResponse.setWarehouseCode(warehouseRepository.getWarehouseById(order.getWarehouseId()).getCode());
                            orderResponse.setOrderId(order.getId());
                            orderResponse.setCreateDate(order.getCreatedOn());
                            return orderResponse;
                        }
                ).collect(Collectors.toList());
    }

    public void changeOrderStatus(String orderId, String whCode, OrderStatus approved) {
        long id;
        try {
            id = Long.parseLong(orderId);
        } catch (NumberFormatException e) {
            throw new ApplicationException("NUMBER_FORMAT", 400, "Id zamówienia musi być liczbą.");
        }
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        UserOrder order = orderRepository.getOrderByWarehouseIdAndId(warehouse.getId(), id).orElseThrow(AppException.NOT_FOUND_ORDER::getError);
        if (!order.getOrderStatus().equals(OrderStatus.PENDING))
            throw AppException.INVALID_ORDER_STATUS.getError();
        order.setOrderStatus(approved);
        orderRepository.save(order);
    }
}
