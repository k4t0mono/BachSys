/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufla.dcc.ppoo.componentes;

import br.ufla.dcc.ppoo.model.Musica;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author aluno
 */
public class Tabela extends JTable {
    private static int colunaCheckBox;

    public static int getColunaCheckBox() {
        return colunaCheckBox;
    }
    
    public Tabela(List<Musica> musicas, String[] col, Boolean mostrarCheckBox){
        super(formatarMusicas(musicas, col), formataColunas(col));
        getTableHeader().setReorderingAllowed(false);
        
        if(!mostrarCheckBox) {
            esconderColuna(colunaCheckBox);
        }
        
    }
    
    private static String[] formataColunas(String[] col) {
        String[] colunas = new String[col.length + 1];
        for(int i = 0; i < col.length; i++) {
            colunas[i] = col[i]; 
        }
        colunas[colunas.length - 1] = "";
        
        return colunas;
    }
    
    private static Object[][] formatarMusicas(List<Musica> musicas, String[] col){ 
        colunaCheckBox = col.length;
        
        if(musicas == null){
            Object objetos[][] = new Object[1][colunaCheckBox + 1];
            System.out.println("Chegou null");
            return objetos;
        }
        
        
        Object objetos[][] = new Object[musicas.size()][colunaCheckBox + 1];
        
        for(int i = 0; i < musicas.size(); i++){
            objetos[i][0] = musicas.get(i).getNome();
            objetos[i][1] = musicas.get(i).getAutor();
            objetos[i][2] = musicas.get(i).getAlbum();
            objetos[i][3] = musicas.get(i).getGenero();
            objetos[i][4] = musicas.get(i).getAno();
        }
        return objetos;
    } 
    
    @Override
    public boolean isCellEditable(int row, int column){  
        if(column == colunaCheckBox) {
            return true;
        }  
        return false;
    }
    
    private void esconderColuna(int indice) {
        TableColumn coluna = getColumnModel().getColumn(indice);
        coluna.setMinWidth(0);
        coluna.setMaxWidth(0);
        coluna.setWidth(0);
        coluna.setPreferredWidth(0);
        doLayout();
        
    }
    
    @Override
    public Class getColumnClass(int indice) {
        if(indice == colunaCheckBox) {
            return Boolean.class;
        }
        return String.class;
    }
    
}
