package co.project.api_catalogo.listener;

import co.project.api_catalogo.config.RabbitMQConfig;
import co.project.api_catalogo.dto.PedidoEventDto;
import co.project.api_catalogo.entity.ProcessedEvent;
import co.project.api_catalogo.repository.ProcessedEventRepository;
import co.project.api_catalogo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoListener {

    private static final String CANCEL_EVENT_PREFIX = "cancel_";

    private final ProductoRepository productoRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO_CREADO)
    public void procesarPedidoCreado(String mensaje) {
        try {
            PedidoEventDto event = objectMapper.readValue(mensaje, PedidoEventDto.class);

            if (event.getEventId() != null && processedEventRepository.existsByEventId(event.getEventId())) {
                log.info("Evento ya procesado: {}", event.getEventId());
                return;
            }

            productoRepository.findById(event.getProductoId()).ifPresent(producto -> {
                int stockActual = producto.getStock();
                if (stockActual < event.getCantidad()) {
                    log.warn("Stock insuficiente para producto id={}: disponible={}, solicitado={}",
                            event.getProductoId(), stockActual, event.getCantidad());
                }
                producto.setStock(Math.max(stockActual - event.getCantidad(), 0));
                producto.setActualizadoEn(LocalDateTime.now());
                productoRepository.save(producto);
                log.info("Stock descontado para producto id={}: -{}", event.getProductoId(), event.getCantidad());
            });

            if (event.getEventId() != null) {
                processedEventRepository.save(new ProcessedEvent(null, event.getEventId(), LocalDateTime.now()));
            }
        } catch (Exception e) {
            log.error("Error procesando mensaje de pedido creado: {}", e.getMessage(), e);
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PEDIDO_CANCELADO)
    public void procesarPedidoCancelado(String mensaje) {
        try {
            PedidoEventDto event = objectMapper.readValue(mensaje, PedidoEventDto.class);

            String cancelEventId = event.getEventId() != null ? CANCEL_EVENT_PREFIX + event.getEventId() : null;
            if (cancelEventId != null && processedEventRepository.existsByEventId(cancelEventId)) {
                log.info("Evento de cancelación ya procesado: {}", cancelEventId);
                return;
            }

            productoRepository.findById(event.getProductoId()).ifPresent(producto -> {
                producto.setStock(producto.getStock() + event.getCantidad());
                producto.setActualizadoEn(LocalDateTime.now());
                productoRepository.save(producto);
                log.info("Stock repuesto para producto id={}: +{}", event.getProductoId(), event.getCantidad());
            });

            if (cancelEventId != null) {
                processedEventRepository.save(new ProcessedEvent(null, cancelEventId, LocalDateTime.now()));
            }
        } catch (Exception e) {
            log.error("Error procesando mensaje de pedido cancelado: {}", e.getMessage(), e);
        }
    }
}