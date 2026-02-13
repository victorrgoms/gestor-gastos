package org.victor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.victor.service.CompraService;


//ACESSE http://localhost:8080/swagger-ui.html

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private CompraService gerenciador;

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);
    }

    public void run(String... args) throws Exception{

        System.out.println("Conectando ao postgres...");
        System.out.println("Conectado com sucesso!");
        System.out.println("Sistema rodando...");

        //INSTANCIANDO A CLASSE PESSOA
        /*
        Pessoa victor = new Pessoa(1L, "Victor");
        Pessoa debora = new Pessoa(2L, "Débora");
        Pessoa mae = new Pessoa(3L, "Mãe");

        //INSTANCIANDO A CLASSE CARTAO
        Cartao nubank = new Cartao(1L, "Nubank", new BigDecimal("6250.00"), victor, 10);
        Cartao bancoDoBrasil = new Cartao(1L, "Banco do Brasil", new BigDecimal("1099.99"), victor, 10);



        LocalDate diaPizza = LocalDate.of(2025, 2, 12);
        //Compra pizza = new Compra(0L, "Pizza 4 Stylos", new BigDecimal("80.52"), diaPizza, nubank, debora);
        //Compra uber = new Compra(1L, "Uber moto", new BigDecimal("6.34"), LocalDate.now(), nubank, victor);
        //Compra pastel = new Compra(1L, "Pastel Tia", new BigDecimal("22.00"), LocalDate.now(), nubank, victor);

        //gerenciador.adicionarCompra(pizza);
        //gerenciador.adicionarCompra(uber);
        //gerenciador.adicionarCompra(pastel);

        System.out.println("Listando compras vindas do Banco de Dados:");
        gerenciador.listarTodas();

        System.out.println("-----------------");
        System.out.println("Total Geral: " + gerenciador.somarTotalGeral());

        BigDecimal totalVictor = gerenciador.somarTotalPorPessoa(victor);
        System.out.println("Total do Victor: " + totalVictor);

        BigDecimal totalDebora = gerenciador.somarTotalPorPessoa(debora);
        System.out.println("Total da Débora: " + totalDebora);
        // System.out.println("Compra de " + pizza.getDescricao() + " no valor de: " + pizza.getValor() + " no cartão " + pizza.getCartao().getApelido() + " feita por " + pizza.getComprador().getNome());
    */
    }

}