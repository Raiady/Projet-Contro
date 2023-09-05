package ifmt.cba;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.namespace.QName;

import ifmt.cba.servico.ProdutoVO;
import ifmt.cba.servico.ServicoControleEstoque;
import jakarta.xml.ws.Service;

/**
 * Hello world!
 *
 */
public class App 
{   private static ServicoControleEstoque controleEstoque;
    public static void main( String[] args )
    {
        URL url;

try {
    url = new URL("http://localhost:8083/servico/estoque?wsdl");
    QName qname = new QName("http://servico.cba.ifmt/", "ServicoControleEstoqueImplService");
    Service service = Service.create(url, qname);

    controleEstoque = service.getPort(ServicoControleEstoque.class);
} catch (MalformedURLException e) {
    e.printStackTrace();
}
if (controleEstoque != null) {
    String menuOp =
    "===== Menu Controle de Estoque =====\n"+
    "1 - Adicionar Produto\n"+
    "2 - Remover Produto\n"+
    "3-  Adicionar Estoque\n"+
    "4 - Remover Estoque\n"+
    "5 - Contar Produtos\n"+
    "6 - Estoque Fisico\n"+
    "7 - Listar Produtos Cadastrados\n"+
    "8 - Totalizar Estoque Físico\n"+
    "9 - Estoque de produto especifico\n"+
    "0 - Sair\n\n"+
    "=======================================\n";

    int opcao;
do{
    opcao = Integer.parseInt(JOptionPane.showInputDialog(null,menuOp));
        switch (opcao) {
            case 1:
                addProduto();
                break;
            case 2:
                cleanProduto();
                break; 
            case 3:
                addEstoque();
                break; 
            case 4:
                baixarEstoque();
                break; 
            case 5:
                contarProdutos();
                break;
            case 6:
                contarEstoqueProdutos();
                break; 
            case 7:
                listProdutos();
                break; 
            case 8:
                TotalEstoque();
                break;
            case 9:
                TvalorEstoqueProduto(opcao);
                break;
            case 0 :
                
                break;
            default:
                break;
        }
      } while (opcao != 8);
}
}

private static void addProduto() {
    ProdutoVO produtoVOTemp = null;
    int codigo;
    String nome;
    boolean sair = false;
    
    do {
        try {
            codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca o codigo do produto"));
            nome = JOptionPane.showInputDialog(null, "Forneca o nome do produto");
            
            produtoVOTemp = new ProdutoVO();
            produtoVOTemp.setCodigo(codigo);
            produtoVOTemp.setNome(nome);
            
            controleEstoque.adicionarProduto(produtoVOTemp);
            
            sair = true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar a operacao: " + ex.getMessage());
        }
    } while (!sair);
}
private static void cleanProduto() {
    ProdutoVO produtoVOTemp = null;
    int codigo;
    
    try {
        codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca o codigo do produto"));
        produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
        
        if (produtoVOTemp != null) {
            controleEstoque.removerProduto(produtoVOTemp);
        } else {
            JOptionPane.showMessageDialog(null, "Produto nao localizado");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Dados inconsistentes: " + ex.getMessage());
    }
}
private static void addEstoque() {
    ProdutoVO produtoVOTemp = null;
    int codigo;
    int quantidade;
    boolean sair = false;
    
    do {
        try {
            codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca o codigo do produto"));
            produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
            
            if (produtoVOTemp != null) {
                quantidade = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca a quantidade a ser adicionada ao estoque"));
                controleEstoque.adicionarEstoqueProduto(produtoVOTemp, quantidade);
                sair = true;
            } else {
                JOptionPane.showMessageDialog(null, "Produto nao localizado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar a operacao: " + ex.getMessage());
        }
    } while (!sair);
}
private static void baixarEstoque() {
    ProdutoVO produtoVOTemp = null;
    int codigo;
    int quantidade;
    boolean sair = false;
    
    do {
        try {
            codigo = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca o codigo do produto"));
            produtoVOTemp = controleEstoque.buscarProdutoPorCodigo(codigo);
            
            if (produtoVOTemp != null) {
                quantidade = Integer.parseInt(JOptionPane.showInputDialog(null, "Forneca a quantidade a ser baixada do estoque"));
                controleEstoque.baixarEstoqueProduto(produtoVOTemp, quantidade);
                sair = true;
            } else {
                JOptionPane.showMessageDialog(null, "Produto nao localizado");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao executar a operacao: " + ex.getMessage());
        }
    } while (!sair);
}
private static void contarProdutos() {
    int quantidadeProdutos = controleEstoque.contadorProduto();

    JOptionPane.showMessageDialog(null, "Quantidade de Produtos: " + quantidadeProdutos);
}

private static void contarEstoqueProdutos() {
    int totalEstoqueFisico = controleEstoque.totalEstoqueFisico();

    JOptionPane.showMessageDialog(null, "Total Estoque Fisico dos Produtos: " + totalEstoqueFisico);
}


private static void listProdutos() {
    List<ProdutoVO> listaProduto = controleEstoque.listaProduto();
        
    StringBuilder mensagem = new StringBuilder();
    for (ProdutoVO produtoTemp : listaProduto) {
        mensagem.append("-------------------------------\n");
        mensagem.append("Codigo: ").append(produtoTemp.getCodigo()).append("\n");
        mensagem.append("Nome: ").append(produtoTemp.getNome()).append("\n");
        mensagem.append("Estoque: ").append(produtoTemp.getEstoque()).append("\n");
    }
    
    JOptionPane.showMessageDialog(null, mensagem.toString(), "Lista de Produtos", JOptionPane.INFORMATION_MESSAGE);
}

private static void TotalEstoque() {
    List<ProdutoVO> listaProduto = controleEstoque.listaProduto();
    double valorTotal = 0.0;

    for (ProdutoVO produtoTemp : listaProduto) {
        valorTotal += produtoTemp.getEstoque() * produtoTemp.getValorUnitario(); // Supondo que o ProdutoVO tenha um método getPrecoUnitario()
    }

    JOptionPane.showMessageDialog(null, "O valor total do estoque é de R$ " + valorTotal);
}

private static void TvalorEstoqueProduto(int codigoProduto) {
    ProdutoVO produto = controleEstoque.buscarProdutoPorCodigo(codigoProduto);
    
    if (produto != null) {
        double valorTotal = produto.getEstoque() * produto.getValorUnitario();
        
        JOptionPane.showMessageDialog(null, "O valor total do estoque do produto é de R$ " + valorTotal);
    } else {
        JOptionPane.showMessageDialog(null, "O produto não foi encontrado");
    }
    
}


}