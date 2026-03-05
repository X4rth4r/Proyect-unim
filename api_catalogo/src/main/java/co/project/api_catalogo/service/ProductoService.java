package co.project.api_catalogo.service;

import co.project.api_catalogo.entity.Producto;
import co.project.api_catalogo.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto crearProducto(Producto producto) {
        producto.setCreadoEn(LocalDateTime.now());
        producto.setActualizadoEn(LocalDateTime.now());
        if (producto.getStock() == null) producto.setStock(0);
        if (producto.getActivo() == null) producto.setActivo(true);
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, Producto productoDetails) {
        Producto producto = obtenerProducto(id);
        producto.setNombre(productoDetails.getNombre());
        producto.setDescripcion(productoDetails.getDescripcion());
        producto.setPrecio(productoDetails.getPrecio());
        producto.setStock(productoDetails.getStock());
        producto.setActivo(productoDetails.getActivo());
        producto.setActualizadoEn(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }

    public Producto descontarStock(Long productoId, int cantidad) {
        Producto producto = obtenerProducto(productoId);
        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente para producto id: " + productoId);
        }
        producto.setStock(producto.getStock() - cantidad);
        producto.setActualizadoEn(LocalDateTime.now());
        return productoRepository.save(producto);
    }

    public Producto reponerStock(Long productoId, int cantidad) {
        Producto producto = obtenerProducto(productoId);
        producto.setStock(producto.getStock() + cantidad);
        producto.setActualizadoEn(LocalDateTime.now());
        return productoRepository.save(producto);
    }
}
