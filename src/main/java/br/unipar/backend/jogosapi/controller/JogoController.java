package br.unipar.backend.jogosapi.controller;

import br.unipar.backend.jogosapi.model.Jogo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/jogo")
public class JogoController {

    private List<Jogo> jogos = new ArrayList<>();
    private Integer proximoId = 1;

    @GetMapping
    // GET / jogo?genero=RPG&plataforma=switch
    public ResponseEntity<List<Jogo>> listarJogos(@RequestParam(required = false) String nome,
                                                  @RequestParam(required = false) String genero,
                                                  @RequestParam(required = false) String plataforma,
                                                  @RequestParam(required = false) Integer anoLancamento,
                                                  @RequestParam(required = false) Double precoMaximo) {

        List<Jogo> resultado = new ArrayList<>();

        for (Jogo jogo : jogos) {

            if (nome != null && !contem(jogo.getNome(), nome)) {
                continue;
            }
            if (genero != null && !igual(jogo.getGenero(), genero)) {
                continue;
            }
            if (plataforma != null && !igual(jogo.getPlataforma(), plataforma)) {
                continue;
            }
            if (anoLancamento != null && !anoLancamento.equals(jogo.getAnoLancamento())) {
                continue;
            }
            if (precoMaximo != null && (jogo.getPreco() == null || jogo.getPreco() > precoMaximo)) {
                continue;
            }
            resultado.add(jogo);
        }

        return ResponseEntity.ok(resultado);
    }


    // GET /jogo/2
    @GetMapping("/{id}")
    public ResponseEntity<Jogo> buscarJogo(@PathVariable Integer id) {
        Jogo jogo = buscarPorId(id);

        if (jogo == null) {
            return ResponseEntity.notFound().build(); //404
        } else {
            return ResponseEntity.ok(jogo);
        }
    }


    @PostMapping
    public ResponseEntity<Jogo> cadastrarJogo(@RequestBody(required = false) Jogo jogo) {
        if (jogoInvalido(jogo)) {
            return ResponseEntity.badRequest().build();
        } else {
            jogo.setId(proximoId);
            proximoId++;
            jogos.add(jogo);
            return ResponseEntity.status(HttpStatus.CREATED).body(jogo);
        }
    }


    @PutMapping("/{id}")
    // PUT /jogo/1
    public ResponseEntity<Jogo> atualizarJogo(@PathVariable Integer id,
                                              @RequestBody(required = false) Jogo jogoAtualizado) {
        if (jogoInvalido(jogoAtualizado)) {
            return ResponseEntity.badRequest().build();
        }

        Jogo jogo = buscarPorId(id);

        if (jogo == null) {
            return ResponseEntity.notFound().build();
        } else {
            jogo.setNome(jogoAtualizado.getNome());
            jogo.setGenero(jogoAtualizado.getGenero());
            jogo.setPlataforma(jogoAtualizado.getPlataforma());
            jogo.setAnoLancamento(jogoAtualizado.getAnoLancamento());
            jogo.setPreco(jogoAtualizado.getPreco());
            return ResponseEntity.ok(jogo);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirJogo(@PathVariable Integer id) {
        Jogo jogo = buscarPorId(id);

        if (jogo == null) {
            return ResponseEntity.notFound().build();
        } else {
            jogos.remove(jogo);
            return ResponseEntity.noContent().build();
        }
    }

    // ---------- métodos auxiliares----------

    private Jogo buscarPorId(Integer id) {
        for (Jogo jogo : jogos) {
            if (jogo.getId().equals(id)) {
                return jogo;
            }
        }
        return null;
    }

    private boolean jogoInvalido(Jogo jogo) {
        return jogo == null || jogo.getNome() == null || jogo.getNome().isBlank();
    }

    private boolean contem(String texto, String busca) {
        return texto != null && texto.toLowerCase().contains(busca.toLowerCase());
    }

    private boolean igual(String texto, String busca) { //ignora maiúsculas/minúsculas
        return texto != null && texto.equalsIgnoreCase(busca);
    }

}
