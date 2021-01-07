package com.devsuperior.dsdeliver.services;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.devsuperior.dsdeliver.dto.OrderDTO;
import com.devsuperior.dsdeliver.dto.ProductDTO;
import com.devsuperior.dsdeliver.entities.Order;
import com.devsuperior.dsdeliver.entities.OrderStatus;
import com.devsuperior.dsdeliver.entities.Product;
import com.devsuperior.dsdeliver.repositories.OrderRepository;
import com.devsuperior.dsdeliver.repositories.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductRepository ProductRepository;

    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        List<Order> list = repository.findOrdersWithProducts();
        //the content returned by stream() cannot be a list,
        // because of this, use Collectors.toList()
        return list.stream().map(x -> new OrderDTO(x)).collect(Collectors.toList());
    }

    // I think that i dont understand totally the part of line 43 - 46
    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order(null, dto.getAddress(), dto.getLatitude(), dto.getLongitude(),
        Instant.now(), OrderStatus.PENDING);

        for(ProductDTO p : dto.getProducts()) {
            Product product = ProductRepository.getOne(p.getId());
            order.getProducts().add(product); 
        }
        order = repository.save(order);
        return new OrderDTO(order);
    }
}
