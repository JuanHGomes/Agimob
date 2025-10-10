package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.SimulacaoRequestDto;
import com.example.agimob_v1.exceptions.UsuarioNaoEncontradoException;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private Usuario novoUsuario(String email){
        return usuarioRepository.save(new Usuario(email));
    }

    private Usuario novoUsuario(String email, String cpf){
        return usuarioRepository.save(new Usuario(email,  cpf));
    }

    public Usuario validarUsuario(String emailUsuario){
        return usuarioRepository.findByEmail(emailUsuario).orElseGet(() -> novoUsuario(emailUsuario));
    }
    public boolean validarSeClienteAgi(String emailUsuario, String cpf){
       if(usuarioRepository.findByCpf(cpf).isEmpty()){
            novoUsuario(emailUsuario, cpf);
           return false;
       }
       return true;
    }




}
