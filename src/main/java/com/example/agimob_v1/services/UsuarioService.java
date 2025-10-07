package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.exceptions.UsuarioNaoEncontradoException;
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

    public Usuario novoUsuario(String emailUsuario){
        return usuarioRepository.save(new Usuario(emailUsuario));
    }

    public Usuario validarUsuario(String emailUsuario){
        return usuarioRepository.findByEmail(emailUsuario).orElseGet(() -> novoUsuario(emailUsuario));
    }
}
