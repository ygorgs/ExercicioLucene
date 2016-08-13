package util;

import java.io.IOException;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Classe respons�vel por armazenar e gerenciar a estrutura de �ndices geradas atrav�s do Lucene
 *  
 * @author Ygor Santos
 *
 */
public class Lucene {

	/**
	 * Estrutura de �ndices do Lucene
	 */
	private IndexWriter index;
	
	/**
	 * Construtor
	 * 
	 * @throws IOException
	 */
	public Lucene() throws IOException{
		Directory dir = new RAMDirectory(); // local f�sico de armazenamento do �ndice
		IndexWriterConfig config = new IndexWriterConfig(new BrazilianAnalyzer()); // define o Analyzer usado (do Brasil).
		config.setSimilarity(new BM25Similarity()); // define o modelo de ranking (BM25/Okapi)
		index = new IndexWriter(dir, config); // inicializa a estrutura de �ndices
	}

	/**
	 * Cria e adiciona um documento no IndexWriter.
	 * 
	 * @param numero
	 * 			N�mero de identifica��o do documento
	 * @param titulo
	 * 			T�tulo do documento
	 * @param texto
	 * 			Conteudo do documento
	 * 
	 * @throws IOException
	 */
	public void addDocumento(String numero, String titulo, String texto) throws IOException{
		Document doc = new Document(); // cria um conteiner para os campos indexados (documento)
		doc.add(new StringField("numero", numero, Field.Store.YES)); // atribui o numero
		doc.add(new StringField("titulo", titulo, Field.Store.YES)); // atribui o titulo
		doc.add(new TextField("texto", texto, Field.Store.YES)); // atribui o conteudo/texto
		index.addDocument(doc); // adiciona o documento no indexWriter
	}
	
	/**
	 * Realiza a busca e imprime os top-n resultados.
	 * 
	 * @param busca
	 * 			Termo a ser buscado
	 * @param n
	 * 			N�mero de resultados que devem ser mostrados
	 * 
	 * @throws IOException
	 * @throws ParseException
	 */
	public void busca(String busca, int n) throws IOException, ParseException{
		QueryParser qp = new QueryParser("texto", new BrazilianAnalyzer()); //incializa o QueryParser
		IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(index)); //incializa o IndexSearch
		searcher.setSimilarity(new BM25Similarity()); //define o modelo de ranking (BM25/Okapi)
		Query query = qp.parse(busca); //passa o termo da busca para o objeto Query ser gerado
		TopDocs topDocs = searcher.search(query, n); //pesquisa os documentos 
		ScoreDoc[] hits = topDocs.scoreDocs; //recupera os documentos resultantes da busca
		
		int posicao = 1; //posi��o dos documentos
		for (int i = 0; i < hits.length; i++) {
			Document doc = searcher.doc(hits[i].doc); // recupera documento na posi��o i
			System.out.println(posicao + "� - " + doc.get("numero"));
			posicao++; //incrementa a posi��o
		}
	}	
}