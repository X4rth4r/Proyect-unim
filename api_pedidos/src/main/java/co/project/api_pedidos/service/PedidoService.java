package co.project.api_pedidos.service;

import co.project.api_pedidos.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class PedidoService {

    private final RabbitTemplate rabbitTemplate;

    public PedidoService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void crearPedido(Long productoId, int cantidad) {
        // 1. Guardar pedido en BD (tu lógica)
        
        // 2. Publicar evento para descontar stock
        String mensaje = String.format("{\"productoId\": %d, \"cantidad\": %d}", productoId, cantidad);
        
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE,
            RabbitMQConfig.ROUTING_KEY_PEDIDO_CREADO,
            mensaje
        );
        
        System.out.println("Mensaje publicado: " + mensaje);
    }
}