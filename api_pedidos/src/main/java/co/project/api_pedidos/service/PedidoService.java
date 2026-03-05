package co.project.api_pedidos.service;

import co.project.api_pedidos.config.RabbitMQConfig;
import co.project.api_pedidos.dto.PedidoEvent;
import co.project.api_pedidos.entity.Pedido;
import co.project.api_pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final RabbitTemplate rabbitTemplate;
    private final PedidoRepository pedidoRepository;
    private final ObjectMapper objectMapper;

    public Pedido crearPedido(Long productoId, int cantidad) {
        Pedido pedido = new Pedido();
        pedido.setProductoId(productoId);
        pedido.setCantidad(cantidad);
        pedido.setEstado("PENDIENTE");
        pedido.setCreadoEn(LocalDateTime.now());
        pedido.setActualizadoEn(LocalDateTime.now());
        Pedido saved = pedidoRepository.save(pedido);

        String eventId = UUID.randomUUID().toString();
        String mensaje = objectMapper.writeValueAsString(new PedidoEvent(eventId, productoId, cantidad));

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_PEDIDO_CREADO,
                mensaje);

        log.info("Pedido creado id={} y mensaje publicado: {}", saved.getId(), mensaje);
        return saved;
    }

    public Pedido cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + pedidoId));

        pedido.setEstado("CANCELADO");
        pedido.setActualizadoEn(LocalDateTime.now());
        pedidoRepository.save(pedido);

        String eventId = UUID.randomUUID().toString();
        String mensaje = objectMapper.writeValueAsString(
                new PedidoEvent(eventId, pedido.getProductoId(), pedido.getCantidad()));

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ROUTING_KEY_PEDIDO_CANCELADO,
                mensaje);

        log.info("Pedido id={} cancelado y mensaje publicado: {}", pedidoId, mensaje);
        return pedido;
    }
}