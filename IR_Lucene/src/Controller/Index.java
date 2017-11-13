
package Controller;

import Model.Article;
import Model.ParragraphAnalyzer;
import Model.ReferenceAnalyzer;
import Model.Tools;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import static org.apache.lucene.analysis.StopFilter.makeStopSet;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.jsoup.nodes.Element;


public class Index {
    
    private final String index_path = "Index\\";
    private String collection_path;
    private ArrayList<Article> articles;
    private IndexWriter writer; 
    private CharArraySet stopwords;
    private PerFieldAnalyzerWrapper analyzer;
    
    
// -----------------------------------------------------------------------------
    
    public Index(String collection_path) throws IOException 
    {
        this.collection_path = collection_path;
        articles = new ArrayList<>();
        read_stopwords();
        init_analyzer();
        init_writer();
    }

// -----------------------------------------------------------------------------
    
    private void init_analyzer()
    {
        Map<String, Analyzer> map = new HashMap<>();
        ParragraphAnalyzer parragraph = new ParragraphAnalyzer();
        ReferenceAnalyzer reference = new ReferenceAnalyzer();
        
        map.put("texto", parragraph);
        map.put("ref", reference);
        
        analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), map);
    }
    
// -----------------------------------------------------------------------------
    
    private void init_writer() throws IOException 
    {        
      Path path = Paths.get(index_path);
      Directory directory = FSDirectory.open(path);
      
      IndexWriterConfig config = new IndexWriterConfig(analyzer);
      writer = new IndexWriter(directory, config);
   }

// -----------------------------------------------------------------------------
    
    public void initial_index()
    {
        ArrayList<String> continents = new ArrayList<>();
        continents.add("América");
        continents.add("Asia");
        index_collection(continents);
    }

// -----------------------------------------------------------------------------
    
    public void final_index()
    {
        ArrayList<String> continents = new ArrayList<>();
        continents.add("África");
        continents.add("Europa");
        continents.add("Oceanía");
        index_collection(continents);
    }
// -----------------------------------------------------------------------------
    
    private void index_collection(ArrayList<String> continents)
    {
        ArrayList<String> continent = new ArrayList<>();

        try 
        {
            articles = read_collection(continents);            
            index_articles();    
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// -----------------------------------------------------------------------------
    
    private void index_articles() throws IOException
    {
        for (Article article : articles)
            writer.addDocument(create_document(article));
        writer.close();
    }
    
// -----------------------------------------------------------------------------
  
    private ArrayList<Article> read_collection(ArrayList<String> continents) throws IOException
    {
        ArrayList<Article> articles = new ArrayList<>();
        for (String continent : continents) 
        {
            File[] country_types = Tools.get_dir_files(collection_path + continent);
            for(File division : country_types)
            {
                File[] countries = Tools.get_dir_files(division.getPath());
                for(File country : countries)
                    articles.add(new Article(country.getPath(), country.getName()));
            }
        }
        return articles;
    }
    
// -----------------------------------------------------------------------------
    
    private Document create_document(Article article)
    {
        Document doc = new Document();
        doc.add(new StoredField("nombre", article.getName()));
        doc.add(new StoredField("ruta", article.getPath()));
        
        // Parrafos
        for (Element e : article.getParragraphs())
            doc.add(new TextField("texto", Tools.delete_accents(e.text()), Field.Store.YES));
        
        // Referencias
        for (Element e : article.getReferences())
            doc.add(new TextField("ref", Tools.delete_accents(e.text()), Field.Store.YES));
        
        return doc;
    }
    
// -----------------------------------------------------------------------------
    
    private void read_stopwords() throws IOException
    {
        String stopwords_file = Tools.read_file("stopwords.txt");
        Pattern regex = Pattern.compile("[A-Za-zÁÉÍÓÚÜáéíóúüÑñ]+");
        Matcher matches = regex.matcher(stopwords_file);
        
        List<String> aux_stopwords = new ArrayList<>();
        while (matches.find())
            aux_stopwords.add(Tools.delete_accents(matches.group()));
        
        stopwords = makeStopSet(aux_stopwords, true);
    }
}
