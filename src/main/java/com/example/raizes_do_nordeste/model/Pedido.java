package com.example.raizes_do_nordeste.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "pedidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "itens") // Evita recursão infinita na impressão dos itens do pedido
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalPedido canalPedido;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "unidade_id", nullable = false)
    private Unidade unidade;

    // Relacionamento Um-para-Muitos: Um pedido tem vários itens
    // mappedBy indica que o mapeamento é feito pelo campo 'pedido' na classe ItemPedido

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true) // cascade para persistir itens junto com o pedido, orphanRemoval para remover itens órfãos
    private List<ItemPedido> itens;

    enum StatusPedido{
        CRIADO,
        AGUARDANDO_PAGAMENTO,
        PREPARANDO,
        PRONTO,
        ENTREGUE,
        CANCELADO
    }

    public enum CanalPedido{
        APP, TOTEM, BALCAO, PICKUP, WEB
    }

}
