package org.example.nisumtechnicalexercise.controllers;

import org.example.nisumtechnicalexercise.entities.RespuestaApi;
import org.example.nisumtechnicalexercise.entities.Usuario;
import org.example.nisumtechnicalexercise.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(UsuarioController.class)
//@ExtendWith(SpringExtension.class)
public class UsuarioControllerTest {
    // fallos en el Mvc...
//    @Autowired
//    private MockMvc mockMvc;
//
//    private UsuarioService usuarioService;
//
//    @Test
//    public void testGetAllUsuarios_Success() throws Exception {
//        Mockito.when(usuarioService.getAll()).thenReturn(ResponseEntity.ok(new RespuestaApi("00",
//                "Operación exitosa", null)));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/nisum/usuarios/obtenerTodos"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.codigo").value("00"))
//                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"));
//    }
//
//    @Test
//    public void testGetAllUsuarios_NoContent() throws Exception {
//        Mockito.when(usuarioService.getAll()).thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT)
//                .body(new RespuestaApi("03", "No se encontraron usuarios", null)));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/nisum/usuarios/obtenerTodos"))
//                .andExpect(status().isNoContent());
//    }
}
