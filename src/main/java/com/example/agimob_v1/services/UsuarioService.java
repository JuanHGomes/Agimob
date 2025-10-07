package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public Usuario novoUsuario(SimulacaoRequestDto simulacaoRequest){
        return usuarioRepository.save(new Usuario(simulacaoRequest.email()));
    }
}
