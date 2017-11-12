package Model;

import java.util.regex.Pattern;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
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
    
}
