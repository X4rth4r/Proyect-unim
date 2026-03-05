package co.project.api_catalogo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEventDto {

    private String eventId;
    private Long productoId;
    private Integer cantidad;
}
