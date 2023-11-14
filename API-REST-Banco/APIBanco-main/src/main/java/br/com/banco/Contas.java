package br.com.banco;
import GlobalExceptionHandler.*;
import jakarta.ws.rs.*;
import models.ContaCorrente;
import services.ContaService;
import services.ContaCorrenteServiceImpl;
import java.util.List;
import java.util.ArrayList;
import jakarta.ws.rs.core.MediaType;

@Path("/contacorrente")
public class Contas {

    List<ContaCorrente> listaContas = new ArrayList<>();
    ContaService contaService = new ContaCorrenteServiceImpl(listaContas);

    @GET
    @Path("/listaContas")
    @Produces(MediaType.TEXT_PLAIN)
    public String listarContas() {
        return listaContas.toString();
    }
    @PUT
    @Path("/criarconta")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String criarConta (@FormParam("nome") String nome, @FormParam("cpf") String cpf) {
        try {
            ContaCorrente contaCorrente = contaService.criarConta(nome, cpf);
            return "Conta criada com sucesso: \n" + contaCorrente.toString();
        } catch (ContaInvalidaException e) {
            return "Conta inválida";
        }

    }
    @PUT
    @Path("/depositar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String depositar (@FormParam("numeroConta") String numeroConta, @FormParam("valorDeposito") double valorDeposito){
        try {
            contaService.depositar(numeroConta, valorDeposito);
            return "Depósito no valor de R$ " + valorDeposito + " na conta de número: " + numeroConta;
        } catch (ContaInvalidaException e) {
            return "Conta inválida. Impossível realizar o depósito!";

        }

    }


    @PUT
    @Path("/sacar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String sacar (@FormParam("numeroConta") String numeroConta, @FormParam("valorSaque") double valorSaque){
        try {
            contaService.sacar(numeroConta, valorSaque);
            return "Saque no valor de " + valorSaque + " realizado na conta: " + numeroConta;
        } catch (ContaInvalidaException e) {
            return "Número de conta inválido";
        } catch (SaldoInsuficienteException e) {
            return "Saldo disponível insuficiente.";
        }
    }

    @PUT
    @Path("/transferir")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String transferir (@FormParam("contaOrigem") String contaOrigem,
                              @FormParam("contaDestino") String contaDestino,
                              @FormParam("valorTransferencia") double valorTransferencia) {

        try {
            contaService.transferir(contaOrigem, contaDestino, valorTransferencia);
            return "Transferência realizada com sucesso:\n Conta de origem: " + contaOrigem +"\n" +
                    "Conta de destino: " + contaDestino +"\n" +
                    "Valor transferido: R$ " + valorTransferencia;
        } catch (ContaInvalidaException e) {
            return "Conta de origem ou de destino inválida. Favor, verifique os números.";
        } catch (SaldoInsuficienteException e) {
            return "Saldo da conta de origem insuficiente para realizar a transferência.";

        }
    }


}
