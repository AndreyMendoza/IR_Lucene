package Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import static org.apache.lucene.analysis.StopFilter.makeStopSet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.pattern.PatternTokenizer;


public class ReferenceAnalyzer extends Analyzer{
    
    private CharArraySet stopwords;
    private final Pattern pattern = Pattern.compile("[A-Za-zÁÉÍÓÚÜáéíóúüÑñ]");

// -----------------------------------------------------------------------------
    
    @Override
    protected TokenStreamComponents createComponents(String string) 
    {     
        try {
            read_stopwords();
        } catch (IOException ex) {
            Logger.getLogger(ReferenceAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Tokenizer tokenizer = new PatternTokenizer(pattern, 0);       
        TokenStream filter = new LowerCaseFilter(tokenizer);             
        filter = new StopFilter(filter, stopwords);
        
        return new TokenStreamComponents(tokenizer, filter);
    }
    
// -----------------------------------------------------------------------------
    
    public void set_stopwords(CharArraySet stopwords)
    {
        this.stopwords = stopwords;
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
