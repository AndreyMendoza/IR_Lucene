
package ir_lucene;

import Model.Tools;
import Controller.Index;
import Model.Article;

public class IR_Lucene {

    
    public static void main(String[] args) throws Exception 
    {
        Index idx = new Index("..\\Geografía");
        String file_path = "..\\Geografia\\América\\Estados_soberanos\\Antigua_y_Barbuda.htm";
        Article ayb = new Article(file_path, "AyB");       

    }
    
}
