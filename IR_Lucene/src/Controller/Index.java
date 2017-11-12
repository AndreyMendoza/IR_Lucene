
package Controller;

import Model.Article;
import Model.ParragraphAnalyzer;
import Model.ReferenceAnalyzer;
import Model.Tools;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.apache.lucene.document.StringField;
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
    
    private void init_writer() throws IOException 
    {        
      Path path = Paths.get(index_path);
      Directory directory = FSDirectory.open(path);
      
      IndexWriterConfig config = new IndexWriterConfig(analyzer);
      writer = new IndexWriter(directory, config);
   }

// -----------------------------------------------------------------------------
    
    private void init_analyzer()
    {
        Map<String, Analyzer> map = new HashMap<>();
        ParragraphAnalyzer parragraph = new ParragraphAnalyzer();
        ReferenceAnalyzer reference = new ReferenceAnalyzer();
        
        parragraph.set_stopwords(stopwords);
        reference.set_stopwords(stopwords);
        map.put("texto", parragraph);
        map.put("ref", reference);
        
        analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), map);
    }
    
// -----------------------------------------------------------------------------
    
    private Document create_document(Article article)
    {
        Document doc = new Document();
        doc.add(new StringField("nombre", article.getName(), Field.Store.NO));
        doc.add(new StringField("ruta", article.getPath(), Field.Store.NO));
        
        // Parrafos
        for (Element e : article.getParragraphs())
            doc.add(new TextField("texto", e.text(), Field.Store.YES));
        
        // Referencias
        for (Element e : article.getReferences())
            doc.add(new TextField("ref", e.text(), Field.Store.YES));
        
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
