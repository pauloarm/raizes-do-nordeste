package com.example.raizes_do_nordeste.dto;

import java.util.List;

import com.example.raizes_do_nordeste.model.Pedido.CanalPedido;

public record PedidoRequestDTO(
    Long usuarioId,
    Long unidadeId,
    CanalPedido canalPedido,
    List<ItemPedidoDTO> itens
) {

}
