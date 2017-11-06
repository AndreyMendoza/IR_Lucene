
package Model;

import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Article {
    
    private String path;
    private String name;
    private Elements parragraphs;
    private Elements references;

// -----------------------------------------------------------------------------
    
    public Article(String path, String name) throws IOException {
        this.path = path;
        this.name = name;
        init_article();
    }
    
// -----------------------------------------------------------------------------
    
    private void init_article() throws IOException
    {
        Document doc = Tools.read_html(path);
        parragraphs = doc.getElementsByTag("p");
        references = doc.getElementsByTag("a");         
    }
    
// -----------------------------------------------------------------------------

    public String getPath() {
        return path;
    }

// -----------------------------------------------------------------------------
    
    public String getName() {
        return name;
    }

// -----------------------------------------------------------------------------
    
    public Elements getParragraphs() {
        return parragraphs;
    }

// -----------------------------------------------------------------------------
    
    public Elements getReferences() {
        return references;
    }    
}
