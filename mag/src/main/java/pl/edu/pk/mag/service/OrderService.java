package pl.edu.pk.mag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.exceptions.AppException;
import pl.edu.pk.mag.exceptions.ApplicationException;
import pl.edu.pk.mag.repository.OrderRepository;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.WarehouseRepository;
import pl.edu.pk.mag.repository.entity.Order;
import pl.edu.pk.mag.repository.entity.User;
import pl.edu.pk.mag.repository.entity.Warehouse;
import pl.edu.pk.mag.repository.entity.enums.OrderStatus;
import pl.edu.pk.mag.requests.OrderRequest;
import pl.edu.pk.mag.responses.OrderResponse;

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

    public void createOrder(OrderRequest orderRequest, String username) {

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

    private List<OrderResponse> toOrderResponse(List<Order> orderList) {
        return orderList.stream()
                .map(order -> {
                            OrderResponse orderResponse = new OrderResponse();
                            orderResponse.setOrderStatus(order.getOrderStatus().toString());
                            orderResponse.setBasketItems(order.getBasketItem());
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
            throw new ApplicationException("NUMBER_FORMAT", 400, "Id zamówienia musi być listą ");
        }
        Warehouse warehouse = warehouseRepository.getWarehouseByCode(whCode).orElseThrow(AppException.NOT_FOUND_WAREHOUSE::getError);
        Order order = orderRepository.getOrderByWarehouseIdAndId(warehouse.getId(), id).orElseThrow(AppException.NOT_FOUND_ORDER::getError);
        if (!order.getOrderStatus().equals(OrderStatus.PENDING))
            throw AppException.INVALID_ORDER_STATUS.getError();
        order.setOrderStatus(approved);
    }
}
