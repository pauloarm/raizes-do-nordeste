package com.example.raizes_do_nordeste.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.example.raizes_do_nordeste.dto.PedidoRequestDTO;
import com.example.raizes_do_nordeste.model.Estoque;
import com.example.raizes_do_nordeste.model.ItemPedido;
import com.example.raizes_do_nordeste.model.Pedido;
import com.example.raizes_do_nordeste.model.Produto;
import com.example.raizes_do_nordeste.model.Unidade;
import com.example.raizes_do_nordeste.model.Usuario;
import com.example.raizes_do_nordeste.repository.EstoqueRepository;
import com.example.raizes_do_nordeste.repository.PedidoRepository;
import com.example.raizes_do_nordeste.repository.ProdutoRepository;
import com.example.raizes_do_nordeste.repository.UnidadeRepository;
import com.example.raizes_do_nordeste.repository.UsuarioRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Pedido criarPedido(PedidoRequestDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.usuarioId())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        
        Unidade unidade = unidadeRepository.findById(dto.unidadeId())
            .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));

        Pedido pedido = Pedido.builder()
            .usuario(cliente)
            .unidade(unidade)
            .canalPedido(dto.canalPedido())
            .status(Pedido.StatusPedido.CRIADO)
            .dataCriacao(LocalDateTime.now())
            .valorTotal(BigDecimal.ZERO) // Será calculado depois
            .itens(new ArrayList<>())
            .build();
        
        BigDecimal valorTotal = BigDecimal.ZERO;

        // Para cada item do pedido, verificamos o estoque e calculamos o valor total
        for(var itemDto : dto.itens()){
            Produto produto = produtoRepository.findById(itemDto.produtoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            Estoque estoque = estoqueRepository.findByProdutoIdAndUnidadeId(produto.getId(), unidade.getId())
                .orElseThrow(() -> new RuntimeException("Produto sem registro de estoque para essa unidade"));

            if(estoque.getQuantidade() < itemDto.quantidade()){
                throw new RuntimeException("Estoque insuficiente para o produto: " + produto.getNome());
            }

            estoque.setQuantidade(estoque.getQuantidade() - itemDto.quantidade());
            estoqueRepository.save(estoque);

            ItemPedido item = ItemPedido.builder()
                .pedido(pedido)
                .produto(produto)
                .quantidade(itemDto.quantidade())
                .precoUnitario(produto.getPrecoBase())
                .build();
            pedido.getItens().add(item);
            valorTotal = valorTotal.add(produto.getPrecoBase().multiply(BigDecimal.valueOf(itemDto.quantidade())));
        }

        pedido.setValorTotal(valorTotal);
        return pedidoRepository.save(pedido);

    }
}
