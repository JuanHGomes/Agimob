package com.example.agimob_v1.controller;


import com.example.agimob_v1.dto.UsuarioRequestDTO;
import com.example.agimob_v1.dto.UsuarioResponseDTO;
import com.example.agimob_v1.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")

public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service){
        this.service=service;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listar(){
        return service.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO buscar(@PathVariable Long id){
        return service.buscar(id);
    }

    @PostMapping
    public UsuarioResponseDTO salvar(@RequestBody UsuarioRequestDTO dto){
        return service.salvar(dto);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(@PathVariable Long id, @RequestBody UsuarioRequestDTO dto){
        return service.atualizar(id,dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id){
        service.deletar(id);
    }
}
