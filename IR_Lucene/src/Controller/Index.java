
package Controller;

import Model.Article;
import Model.Tools;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;


public class Index {
    
    private String collection_path;
    private ArrayList<Article> articles;
    private IndexWriter writer; 
    private String []stopwords;
    
// ----------------------------------------------------------------------------- 
    
    public Index() throws IOException {
        this.stopwords = new String[346];
        articles = new ArrayList<>();
        read_stopwords();
    }
// -----------------------------------------------------------------------------
    
    public Index(String collection_path) 
    {
        this.stopwords = new String[343];
        this.collection_path = collection_path;
    }
    
// -----------------------------------------------------------------------------
    
    public void init_writer(String indexDirectoryPath) throws IOException 
    {        
      Path path = Paths.get("Index\\");
      Directory directory = FSDirectory.open(path);
      StandardAnalyzer analyzer = new StandardAnalyzer();
      
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
        String file_stopwords = Tools.read_file("stopwords.txt");
        Pattern regex = Pattern.compile("[A-Za-zÁÉÍÓÚÜáéíóúüÑñ]+");
        Matcher matches = regex.matcher(file_stopwords);
        
        int counter = 0;
        while (matches.find())
        {
            stopwords[counter] = Tools.delete_accents(matches.group());
            counter++;
        }
    }    
}
