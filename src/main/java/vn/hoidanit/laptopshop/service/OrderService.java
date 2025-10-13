package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.domain.OrderDetail;
import vn.hoidanit.laptopshop.repository.OrderDetailRepository;
import vn.hoidanit.laptopshop.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }

    public Order getOrderById(long id) {
        return this.orderRepository.findById(id);

    }

    public List<OrderDetail> getAllOrderDetails() {
        return this.orderDetailRepository.findAll();
    }

    public List<OrderDetail> getOrderDetailById(Order order) {
        return this.orderDetailRepository.findByOrder(order);
    }

    public Order handleSaveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    public void deleteOrderById(long id) {
        Order orderOptional = this.getOrderById(id);
        if (orderOptional != null) {
            Order order = this.getOrderById(id);
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for (OrderDetail orderDetail : orderDetails) {
                this.orderDetailRepository.deleteById(orderDetail.getId());
            }
        }
        this.orderRepository.deleteById(orderOptional.getId());
    }

}
