package co.project.api_catalogo.repository;

import co.project.api_catalogo.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
