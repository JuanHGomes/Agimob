package com.example.agimob_v1.services;

import com.example.agimob_v1.dto.UsuarioRequestDTO;
import com.example.agimob_v1.dto.UsuarioResponseDTO;
import com.example.agimob_v1.model.Usuario;
import com.example.agimob_v1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    //conversao para DTO de resposta
    private UsuarioResponseDTO toResponseDTO(Usuario usuario){
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getIdUsuario());
        dto.setEmail(usuario.getEmail());

        return dto;
    }

    public List<UsuarioResponseDTO> listarUsuarios(){
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UsuarioResponseDTO buscar(Long id){
        Usuario usuario = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario nao encontrado"));
        return toResponseDTO(usuario);
    }

    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        Usuario salvo = repository.save(usuario);

        return toResponseDTO(salvo);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto){
        Usuario usuario = repository.findById(id)
                .orElseThrow(()-> new RuntimeException("Usuario não encontrado"));
        usuario.setEmail(dto.getEmail());
        Usuario atualizado = repository.save(usuario);

        return toResponseDTO(atualizado);
    }

    public void deletar(Long id){
        repository.deleteById(id);
    }
}
