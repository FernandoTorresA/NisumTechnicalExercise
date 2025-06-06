package org.example.nisumtechnicalexercise.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class RespuestaApi {
    private String codigo;
    private String mensaje;

    //esto excluye el campo cuendo es null...
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    public RespuestaApi(String codigo, String mensaje, Object data) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.data = data;
    }
}
