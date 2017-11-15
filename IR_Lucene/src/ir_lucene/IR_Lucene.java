
package ir_lucene;

import Controller.Index;
import java.util.concurrent.TimeUnit;

public class IR_Lucene {

    
    public static void main(String[] args) throws Exception 
    {
        
        long start = System.currentTimeMillis();
        
        Index idx = new Index("..\\Geografia\\");
        idx.initial_index();      
        
        long finish = System.currentTimeMillis();
        long elapsed_time = TimeUnit.MILLISECONDS.toSeconds(finish - start);
        
        System.out.println("Finalizado. Duración: " + elapsed_time);
        
        
    }
    
}
