package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Classe respons�vel por fazer a leitura do arquivo e tratar cada uma das linhas
 * 
 * @author Ygor Santos
 */
public class LeitorDeArquivo {
	
	/**
	 * Construtor.
	 */
	public LeitorDeArquivo(){}
	
	/**
	 * Realiza a leitura e tratamento dos documentos HTML contidos no arquivo.
	 * 
	 * @param index
	 * 			objeto respons�vel por idexar os documentos.
	 */
	public void ler(Lucene index){
		try {
			String path = System.getProperty("user.dir") + "\\data\\ptwiki-v2.trec";
			FileReader arquivo = new FileReader(path);
			BufferedReader lerArq = new BufferedReader(arquivo);
			String linha = lerArq.readLine(); 
			
			while (linha != null) {
				//identifica onde come�a um documento
				if(linha.equals("<DOC>")){
					//recupera o n�mero
					linha = lerArq.readLine();
					int numero = Integer.parseInt(limpar(linha));
					
					//recupera o titulo
					linha = lerArq.readLine();
					String titulo = limpar(linha);
					
					//recupera o conte�do
					linha = lerArq.readLine();
					linha = lerArq.readLine();
					String texto = limpar(linha);
					
					//adiciona o novo documento na lista
					index.addDocumento(String.valueOf(numero), titulo, texto);
				}
			    linha = lerArq.readLine();
			};
			arquivo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Limpa uma linha do arquivo removendo todos os caracteres especiais
	 * 
	 * @param str
	 * 			Linha que ir� ser limpa
	 * @return Linha formatada
	 */
	public String limpar(String str){
		String aux;
		str = str.toLowerCase();
		str = str.replaceAll("&.{2,4};", " ");
		str = str.replaceAll("\\{\\{!\\}\\}", " ");

		int start, end;
		start = str.indexOf("{{");
		while(start != -1){
			aux = str.substring(start);
			
			start = aux.indexOf("{{");
			end = aux.indexOf("}}");
			if(start == -1 || end == -1){
				break;
			}
			aux = aux.substring(start, end+2);
			aux = removerChaves(aux);
			str = str.replace(aux, " ");
			start = str.indexOf("{{");
		}
				
		start = str.indexOf("<");
		while(start != -1){
			end = str.indexOf(">");
			aux = str.substring(start, end+1);
			str = str.replace(aux, "");
			start = str.indexOf("<");
		}
		
		str = str.replaceAll("[^a-z0-9������������-]", " ");
		
		return(str);
	}
	
	/**
	 * Procura a cadeia de caracteres entre chaves duplas - ex: {{texto}}
	 * Esse m�todo tambem trata casos onde se inicia com {{ mas n�o h� o fechamento das chaves duplas - ex: {{ alguma coisa
	 * ou quando se tem termos como esse dentro de outro - ex: {{ultimo {{exemplo}}}}
	 * 
	 * @param linha
	 * 			cadeia de strings que ser� analisada
	 * @return cadeia de strings entre chaves duplas
	 */
	private String removerChaves(String linha){
		String aux = linha.substring(2);
		if(aux.contains("{{")){
			int index = aux.indexOf("{{");
			aux = linha.substring(index+2);
			aux = removerChaves(aux);
			return aux;
		}
		return linha;
	}
}