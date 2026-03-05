package co.project.api_catalogo.listener;

import co.project.api_catalogo.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoListener {

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO_CREADO)
    public void procesarPedidoCreado(String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
        
        // Parsear JSON (usa Jackson o Gson)
        // Ejemplo: {"productoId": 1, "cantidad": 5}
        
        // Descontar stock en inventario
        // inventarioRepository.descontarStock(productoId, cantidad);
        
        // Registrar movimiento
        // movimientoRepository.save(new Movimiento(...));
    }
}