package com.example.agimob_v1.services;

import com.example.agimob_v1.exceptions.ClienteAgiNaoEncontradoException;
import com.example.agimob_v1.model.Simulacao;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private Usuario novoUsuario(String email){
        return usuarioRepository.save(new Usuario(email));
    }

    public Usuario validarUsuarioPeloEmail(String emailUsuario){
        return usuarioRepository.findByEmail(emailUsuario).orElseGet(() -> novoUsuario(emailUsuario));
    }

    public Usuario localizarUsuarioPorCpf(String cpf){
        return usuarioRepository.findByCpf(cpf).orElseThrow(() -> new ClienteAgiNaoEncontradoException());
    }





}
