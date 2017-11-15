/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 *
 * @author aleep
 */
public class DTOSearch {
    
    Search searcher = new Search();
    
// -----------------------------------------------------------------------------    
    
    public DTOSearch () {

    }

    public DTOSearch(JLabel labindexname) {
        init(labindexname);
    }
    
// ----------------------------------------------------------------------------- 
    
    public void search(String query, int hits, JTable table, JLabel results){
        try {        
            TopDocs docs = searcher.search_docs(query, hits);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            results.setText(String.valueOf(docs.totalHits));
            int i = 1;
            for(ScoreDoc result : docs.scoreDocs){
                Document finaldoc = searcher.getDoc(result);
                String name = finaldoc.get("nombre");
                String path = finaldoc.get("ruta");
                model.addRow(new Object[]{i, name, path});
                i++;
            }
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(null, "Index is not initialized or is damaged");
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Query format exception");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "An error occurred while proccessing the query");
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Table error");
        }
    }

// -----------------------------------------------------------------------------    
    
    public void loadIndex(JLabel labindexname) {
        try{
            String f = labindexname.getText();
            Path path = Paths.get(f);
            searcher.load_index(path);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null, "Cannot open the selected folder or "
                                                + "it doesn't contain a valid index");
            labindexname.setText("<<No index has been selected yet>>");
        }
    }
    
// -----------------------------------------------------------------------------   
    
    private void init(JLabel init){
        try{
            Path path = Paths.get("Index\\");
            searcher.load_index(path);
            init.setText("...\\Index\\");
        }catch(Exception e){
            
        }
    }
    
    
}
