package com.example.agimob_v1.services;

import com.example.agimob_v1.exceptions.ClienteAgiNaoEncontradoException;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private Usuario novoUsuario(String email){
        return usuarioRepository.save(new Usuario(email));
    }

    public Usuario validarUsuarioPeloEmail(String emailUsuario){
        return usuarioRepository.findByEmail(emailUsuario).orElseGet(() -> novoUsuario(emailUsuario));
    }

    public Usuario localizarUsuarioPorCpf(@CPF String cpf){
        return usuarioRepository.findByCpf(cpf).orElseThrow(() -> new ClienteAgiNaoEncontradoException());
    }










}
