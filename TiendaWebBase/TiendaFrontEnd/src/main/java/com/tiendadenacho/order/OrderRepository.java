package com.tiendadenacho.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiendadenacho.entidades.order.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
