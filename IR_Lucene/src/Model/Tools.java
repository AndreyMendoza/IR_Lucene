
package Model;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Tools {


// -----------------------------------------------------------------------------
    
    public File[] get_dir_files(String dir_path) 
    {
        File cur_dir = new File(dir_path);
        File[] file_list = cur_dir.listFiles();
        
        return file_list;
    }
    
// -----------------------------------------------------------------------------
    
    
    public static String read_file(String file_path) throws IOException 
    {
        File file = new File(file_path);
        String content = new Scanner(file).useDelimiter("\\Z").next();
        return content;
    }
    
// -----------------------------------------------------------------------------
    
    public static Document read_html(String html_path) throws IOException
    {
        File file = new File(html_path);
        Document doc = Jsoup.parse(file, "UTF-8");
        return doc;
    }
    
// -----------------------------------------------------------------------------
    
    public static String delete_accents(String input)
    {
        String result = input.replaceAll("ñ", "<n>");
        result = Normalizer.normalize(result, Normalizer.Form.NFKD);
        result = result.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        result = result.replaceAll("<n>", "ñ");
        return result;
    }
    
// -----------------------------------------------------------------------------
   
    
}
