/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.Tools;
import View.Main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListModel;

/**
 *
 * @author aleep
 */
public class DTOIndex {
    
   private Index index;
   
// -----------------------------------------------------------------------------
   
   public DTOIndex() {
   }
// -----------------------------------------------------------------------------
   
   public void loadFolders(JLabel pathlab, JList<String> listadir) throws IOException {
       try{
           File[] files = Tools.get_dir_files(pathlab.getText());
           DefaultListModel<String> model = new DefaultListModel<>();
           for(File file: files){
               model.addElement(file.getName());
           }
           listadir.setModel(model);
       }catch(IOException e){
           JOptionPane.showMessageDialog(null, "An error occurred while loading directories");
           pathlab.setText("");
       }
       index = new Index(pathlab.getText()+"\\");
    }

// -----------------------------------------------------------------------------
   
    public void addToIndex(JList<String> listadir, JList<String> listaindex) {
        String x = listadir.getSelectedValue();
        DefaultListModel<String> model;
        try{
            model = (DefaultListModel<String>) listaindex.getModel();
        }
        catch(Exception e){
            model = new DefaultListModel<>();

        }
        if(x!=null){
            model.addElement(x);
            listaindex.setModel(model);
        }
        
    }

// -----------------------------------------------------------------------------    
    
    public void removeFrom(JList<String> listaindex) {
        try{
            int i = listaindex.getSelectedIndex();
            DefaultListModel<String> model = (DefaultListModel<String>) listaindex.getModel();
            model.remove(i);
        }
        catch(Exception e){
                
        }
    }

// -----------------------------------------------------------------------------    
    
    public void index(JList<String> listaindex) {
        if(index!=null){
            ArrayList<String> continents;
            DefaultListModel<String> model;
            try{
                model = (DefaultListModel<String>) listaindex.getModel();
            }catch(Exception e){
                 model = new DefaultListModel<>();
            }
            continents = Collections.list(model.elements());
            if(!continents.isEmpty()){
                index.index_collection(continents);
                model.removeAllElements();
                listaindex.setModel(model);
                JOptionPane.showMessageDialog(null, "Index created successfully");
            }
            else
               JOptionPane.showMessageDialog(null, "No directories were selected");
        }
    }
    
}
