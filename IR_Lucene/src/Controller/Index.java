
package Controller;

import Model.Article;
import Model.Tools;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.CharArraySet;
import static org.apache.lucene.analysis.StopFilter.makeStopSet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;


public class Index {
    
    private final String index_path = "Index\\";
    private String collection_path;
    private ArrayList<Article> articles;
    private IndexWriter writer; 
    private CharArraySet stopwords;
    
    
// -----------------------------------------------------------------------------
    
    public Index(String collection_path) throws IOException 
    {
        this.collection_path = collection_path;
        articles = new ArrayList<>();
        read_stopwords();
        init_writer();
    }
    
// -----------------------------------------------------------------------------
    
    public void init_writer() throws IOException 
    {        
      Path path = Paths.get(index_path);
      Directory directory = FSDirectory.open(path);
      
      StandardAnalyzer analyzer = new StandardAnalyzer(stopwords);
      IndexWriterConfig config = new IndexWriterConfig(analyzer);
      writer = new IndexWriter(directory, config);
   }

// -----------------------------------------------------------------------------
    
    public Document create_document(Article article)
    {
        Document doc = new Document();
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
        
        stopwords = makeStopSet(aux_stopwords, false);
    }
}
