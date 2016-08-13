import java.io.IOException;
import java.util.Scanner;

import org.apache.lucene.queryparser.classic.ParseException;

import util.LeitorDeArquivo;
import util.Lucene;


/**
 * Classe contendo o main da aplicação
 * 
 * @author Ygor Santos
 *
 */
public class main {
	
	public static Scanner input = new Scanner(System.in);  // Reading from System.in
	
	public static LeitorDeArquivo leitor;
	
	public static Lucene lucene;
	
	public static final Integer qtdResultados = 10;
	
	/**
	 * Fluxo principal da aplicação
	 */
	public static void main(String[] args){
		//carregar dados na aplicação
		inicializar();
		
		System.out.println("\nAtividade 01 - CheckPoint 3 - Lucene e Análise de Relevância");
		
		String opcao;
		do {
			System.out.println();
			System.out.println("1 - Realizar Busca");
			System.out.println("2 - Sair");
			System.out.print("Escolha uma opção: ");
			opcao = input.nextLine();
			
			switch (opcao) {
			case "1":
				realizarBusca();
				break;
			default:
				break;
			}
		} while (!opcao.equals("2"));
	}
	
	/**
	 * Operações necessárias para a inicialização (ler o arquivo e cria a estrutura do índice)
	 */
	private static void inicializar() {
		try {
			System.out.println("Inicializando...");
			leitor = new LeitorDeArquivo();
			lucene = new Lucene();
			
			//ler o arquivo base e recupera a lista de documentos
			leitor.ler(lucene);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Método responsável por realizar a busca, o usuário deve digitar o termo que deseja buscar
	 * e selecionar qual algoritmo a aplicação deverá usar.
	 */
	private static void realizarBusca() {
		try {
			System.out.print("\nDigite a Busca: ");

			String termo = input.nextLine();
			termo = leitor.limpar(termo);
			lucene.busca(termo, qtdResultados);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
