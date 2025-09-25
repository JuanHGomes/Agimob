package com.example.agimob_v1.controller;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.services.UsuarioService;
import org.springframework.web.bind.annotation.RestController;

@RestController("/agimob/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Usuario novoUsuario(SimulacaoRequestDto simulacaoRequestDto){
        return usuarioService.novoUsuario(simulacaoRequestDto);
    }
}
