package br.edu.fatec.Produto;

import java.io.IOException;

import br.edu.fatec.Produto.Service.ConsomeApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
public class ProdutoApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProdutoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		ConsomeApi consomeApi = new ConsomeApi();
		String dados = consomeApi.obterDados("http://api.escuelajs.co/api/v1/products/");
		System.out.println("Produtos da API:");
		System.out.println(dados);


		List<String> produtosEmPromocao = filtrarProdutos(dados);
		System.out.println("\nProdutos em promoção e imperdíveis:");
		produtosEmPromocao.forEach(System.out::println);
		System.out.println("Total de produtos em promoção: " + produtosEmPromocao.size());
	}
	public static List<String> filtrarProdutos(String dados) {
		ObjectMapper objMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objMapper.readTree(dados);
			return jsonNode.findValuesAsText("title").stream()
					.filter(nome -> {

						JsonNode precoNode = jsonNode.findParent(nome);
						double preco = precoNode != null ? precoNode.asDouble() : 0;
						return preco < 30;
					})
					.map(String::toUpperCase)
					.collect(Collectors.toList());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}
}
