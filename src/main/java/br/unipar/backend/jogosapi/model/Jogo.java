package br.unipar.backend.jogosapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Jogo {

    private Integer id;
    private String nome;
    private String genero;
    private String plataforma;
    private Integer anoLancamento;
    private Double preco;

}
