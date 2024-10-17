package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner read = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String URL = "https://www.omdbapi.com/?t=";
    private final String KEY = "&apikey=7aef2dc6";

    public void exibeMenu() {
        // Busca por nome da série.
        System.out.println("Digite o nome da série:");

        var serie = read.nextLine();
        var json = consumo.obterDados(URL + serie.replace(" ", "+") + KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalSeasons(); i++) {
            var jsonSerieEps = consumo.obterDados(URL + serie.replace(" ", "+") + "&season=" + i + KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(jsonSerieEps, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        temporadas.forEach(System.out::println);
        temporadas.forEach(t -> t.eps().forEach(e -> System.out.println(e.title())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream().flatMap(t -> t.eps().stream()).collect(Collectors.toList());
        dadosEpisodios.stream().filter(e -> !e.rating().equalsIgnoreCase("N/A")).sorted(Comparator.comparing(DadosEpisodio::rating).reversed()).limit(10).map(e -> e.title().toUpperCase()).forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.eps().stream().map(d -> new Episodio(t.season(), d))).collect(Collectors.toList());
        episodios.forEach(System.out::println);

//        BUSCA UM EPISÓDIO
//        System.out.println("Digite um título");
//
//        var titleToSearch= read.nextLine();
//
//        Optional<Episodio> epSearched = episodios.stream().filter(e -> e.getTitle().toUpperCase().contains(titleToSearch.toUpperCase())).findFirst();
//
//        if (epSearched.isPresent()) {
//            System.out.println("Episódio encontrado:" + epSearched.get().getTitle() + epSearched.get().getSeason() );
//        } else {
//            System.out.println("Episódio não encontrado.");
//        }

//        BUSCA PELO ANO
//        System.out.println("Digite o ano:");
//        var year = read.nextInt();
//        read.nextLine();
//
//        LocalDate dateSearch = LocalDate.of(year, 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream().filter(e -> e.getReleased() != null && e.getReleased().isAfter(dateSearch)).forEach(e -> System.out.println("Temporada: " + e.getSeason() + " Episódio: " + e.getTitle() + "Data Lançamento: " + e.getReleased().format(formatter)));

        Map<Integer, Double> ratingBySeason = episodios.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getSeason, Collectors.averagingDouble(Episodio::getRating)));

        System.out.println(ratingBySeason);

        DoubleSummaryStatistics statistics = episodios.stream()
                .filter(e -> e.getRating() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getRating));

        System.out.println(statistics.getAverage());
        System.out.println(statistics.getMin());
        System.out.println(statistics.getMax());

    }
}
