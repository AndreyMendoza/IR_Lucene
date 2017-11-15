/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.ParragraphAnalyzer;
import Model.ReferenceAnalyzer;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author aleep
 */
public class Search {
    
    private Path current_indexPath;
    private IndexSearcher current_index;
    private PerFieldAnalyzerWrapper analyzer;
    private QueryParser parser;

// -----------------------------------------------------------------------------
    public Search() {
    }
    
// -----------------------------------------------------------------------------
    
    public void load_index(Path path) throws IOException{
        current_indexPath = path;
        Directory dir = FSDirectory.open(current_indexPath);
        IndexReader reader = DirectoryReader.open(dir);
        this.current_index = new IndexSearcher(reader);
        setAnalyzer();
    }

// -----------------------------------------------------------------------------

    private void setAnalyzer()
    {
        Map<String, Analyzer> map = new HashMap<>();
        ParragraphAnalyzer parragraph = new ParragraphAnalyzer();
        ReferenceAnalyzer reference = new ReferenceAnalyzer();
        
        map.put("texto", parragraph);
        map.put("ref", reference);
        
        this.analyzer = new PerFieldAnalyzerWrapper(new WhitespaceAnalyzer(), map);
        this.parser = new QueryParser("texto", this.analyzer);
    }
    
// -----------------------------------------------------------------------------
    
    public TopDocs search_docs(String consulta, int hits) throws NullPointerException, ParseException, IOException{
        Query query = parser.parse(consulta);
        return this.current_index.search(query, hits);
    }
    
    public Document getDoc(ScoreDoc sd) throws IOException{
        return current_index.doc(sd.doc);
    }
    
}
