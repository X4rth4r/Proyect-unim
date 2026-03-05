package co.project.api_pedidos.controller;

import co.project.api_pedidos.dto.PedidoRequest;
import co.project.api_pedidos.entity.Pedido;
import co.project.api_pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/crear")
    public ResponseEntity<Pedido> crearPedido(@RequestBody PedidoRequest request) {
        Pedido pedido = pedidoService.crearPedido(request.getProductoId(), request.getCantidad());
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Pedido> cancelarPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(pedido);
    }
}