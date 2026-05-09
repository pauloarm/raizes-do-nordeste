package com.example.raizes_do_nordeste.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.raizes_do_nordeste.model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCanalPedido(Pedido.CanalPedido canalPedido);
}
