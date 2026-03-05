package co.project.api_pedidos.controller;

import co.project.api_pedidos.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearPedido(
            @RequestParam Long productoId,
            @RequestParam int cantidad) {
        
        pedidoService.crearPedido(productoId, cantidad);
        
        return ResponseEntity.ok("Pedido creado. Mensaje enviado a RabbitMQ.");
    }
}