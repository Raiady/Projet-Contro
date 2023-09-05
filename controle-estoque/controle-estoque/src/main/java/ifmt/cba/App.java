package ifmt.cba;

import ifmt.cba.servico.ServicoControleEstoqueImpl;
import jakarta.xml.ws.Endpoint;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       ServicoControleEstoqueImpl estoqueImpl =  new ServicoControleEstoqueImpl();
       Endpoint.publish("http://localhost:8083/servico/estoque", estoqueImpl);
       System.out.println("Servico publicado com sucesso");

    }
}
