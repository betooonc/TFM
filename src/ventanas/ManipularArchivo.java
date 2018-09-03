package ventanas;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import javax.swing.*;
import java.io.File;

public class ManipularArchivo {

    JFileChooser fileChooser = new JFileChooser();
    File inputWorkbook;
    Workbook w;
    Sheet sheet;
    double[][] matrizDatos;
    double[] termIndep;
    String filePath;


    public double[][] getMatrizDatos() {
        return matrizDatos;
    }

    public double[] getTermIndep() {
        return termIndep;
    }

    public void cargar() throws Exception {
        /**
         * Carga un archivo de excel al sistema
         * No recibe parAmetros
         */

        //Se dispara cuando se abre el explorador de archivos
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            java.io.File excel = fileChooser.getSelectedFile();
            inputWorkbook = new File(excel.getPath());
            filePath = excel.getPath();
            w = Workbook.getWorkbook(inputWorkbook);
            sheet = w.getSheet(0);
            matrizDatos = new double[sheet.getRows()][sheet.getColumns()-2];
            termIndep = new double[sheet.getRows()];
            Cell cell = null;

            //Carga la informaciOn del docuemto excel en una variable local para su manipulaciOn
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns()-2; j++) {
                    cell = sheet.getCell(j, i);
                    matrizDatos[i][j] = Double.parseDouble(cell.getContents());
                }
                cell = sheet.getCell(sheet.getColumns()-1,i);
                termIndep[i] = Double.parseDouble(cell.getContents());
            }

            for (int i = 0; i < matrizDatos.length; i++) {
                for (int j = 0; j < matrizDatos.length; j++) {
                    System.out.print(matrizDatos[i][j]+" \t");
                }
                System.out.print(termIndep[i]);
                System.out.println();
            }
        }else {
            //Si existe algUn problema en lacarga de archivo imprime un mensaje en el log y muestra un error al usuario
            System.out.println("Error al cargar archivo!!!");
            JOptionPane.showMessageDialog(null, "Ocurrió un error al cargar el archivo, favor verifique que la extensión sea (*.xls) y que el formato esté acorde a las indicaciones del archivo manual.txt");
        }
    }

    public void crear(double[][] porColumna, double[][] porMatriz, boolean convergeColumna, boolean convergeMatriz, int iterColumna, int iterMatriz, double diagonalPrincipalColumna, double diagonalPrincipalMatriz) throws Exception{
        /**
         * Crea un archivo de excel en un directorio del ordenador
         * Recibe como parAmetros la matriz organizada por columna y por matriz,
         * un booleano que indica si converge o no con cada una de las organizaciones
         * y el nUmero de iteraciones que le tomO a cada mEtodo llegar al resultado
         */
        if(filePath != null){
            inputWorkbook = new File(filePath.substring(0,filePath.lastIndexOf('.'))+"Solucion.xls");
        }else{
            inputWorkbook = new File("C:\\Users\\SolucionMatrizIngresada.xls");
        }
        WritableWorkbook excelSolucion = Workbook.createWorkbook(inputWorkbook);
        WritableSheet hojaPorColumna = excelSolucion.createSheet("Por Columna",0);
        WritableSheet hojaPorMatriz = excelSolucion.createSheet("Por Matriz",1);
        Label labelPorColumna;
        Label labelPorMatriz;

        labelPorColumna = new Label(0,1,"Sistema de Ecuaciones Organizado");
        hojaPorColumna.addCell(labelPorColumna);
        labelPorMatriz = new Label(0,1,"Sistema de Ecuaciones Organizado");
        hojaPorMatriz.addCell(labelPorMatriz);

        labelPorColumna = new Label(porColumna.length+5,1,"Soluciones del Sistema");
        hojaPorColumna.addCell(labelPorColumna);
        labelPorMatriz = new Label(porColumna.length+5,1,"Soluciones del Sistema");
        hojaPorMatriz.addCell(labelPorMatriz);

        labelPorColumna = new Label(1,porColumna.length+6,"Sumatoria de la diagonal principal = "+diagonalPrincipalColumna);
        hojaPorColumna.addCell(labelPorColumna);
        labelPorMatriz = new Label(1,porColumna.length+6,"Sumatoria de la diagonal principal = "+diagonalPrincipalMatriz);
        hojaPorMatriz.addCell(labelPorMatriz);

        //tItulos
        for (int i = 0; i < porColumna.length; i++) {
            labelPorColumna = new Label(i + 1, 3, "X" + (i + 1));
            hojaPorColumna.addCell(labelPorColumna);
            labelPorMatriz = new Label(i + 1, 3, "X" + (i + 1));
            hojaPorMatriz.addCell(labelPorMatriz);
        }
        labelPorColumna = new Label(porColumna.length + 2, 3, "TI");
        hojaPorColumna.addCell(labelPorColumna);
        labelPorMatriz = new Label(porColumna.length + 2, 3, "TI");
        hojaPorMatriz.addCell(labelPorMatriz);


        for (int i = 0; i < porColumna.length; i++) {
            for (int j = 0; j < porColumna.length; j++) {
                labelPorColumna = new Label(j+1,i+4,Double.toString(porColumna[i][j]));
                hojaPorColumna.addCell(labelPorColumna);
                labelPorMatriz = new Label(j+1,i+4,Double.toString(porMatriz[i][j]));
                hojaPorMatriz.addCell(labelPorMatriz);
            }
            //ecuaciones
            labelPorColumna = new Label(0,i+4,"Ecu. "+(i+1));
            hojaPorColumna.addCell(labelPorColumna);
            labelPorMatriz = new Label(0,i+4,"Ecu. "+(i+1));
            hojaPorMatriz.addCell(labelPorMatriz);
            //separador
            labelPorColumna = new Label(porColumna.length+1,i+4,"       ¦");
            hojaPorColumna.addCell(labelPorColumna);
            labelPorMatriz = new Label(porMatriz.length+1,i+4,"       ¦");
            hojaPorMatriz.addCell(labelPorMatriz);
            //vector tErminos independientes
            labelPorColumna = new Label(porColumna.length+2,i+4,Double.toString(porColumna[i][porColumna.length]));
            hojaPorColumna.addCell(labelPorColumna);
            labelPorMatriz = new Label(porMatriz.length+2,i+4,Double.toString(porMatriz[i][porMatriz.length]));
            hojaPorMatriz.addCell(labelPorMatriz);
            //soluciones
            if (convergeColumna == true){
                //tItulos
                labelPorColumna = new Label(porColumna.length+5, i+4, "X" + (i + 1));
                hojaPorColumna.addCell(labelPorColumna);
                //resultados
                labelPorColumna = new Label(porColumna.length+6,i+4,Double.toString(porColumna[i][porColumna.length+1]));
                hojaPorColumna.addCell(labelPorColumna);
            }
            if (convergeMatriz == true){
                //tItulos
                labelPorMatriz = new Label(porColumna.length+5, i+4, "X" + (i + 1));
                hojaPorMatriz.addCell(labelPorMatriz);
                //resultados
                labelPorMatriz = new Label(porColumna.length+6,i+4,Double.toString(porMatriz[i][porMatriz.length+1]));
                hojaPorMatriz.addCell(labelPorMatriz);
            }
        }

        //valida si el sistema converge e imprime el nUmero de iteraciones que fueron necesarias, caso contrario indica que no converge
        if (convergeColumna == true){
            labelPorColumna = new Label(porColumna.length+5,porColumna.length+6,iterColumna+" iteraciones realizadas");
            hojaPorColumna.addCell(labelPorColumna);
        }else {
            labelPorColumna = new Label(porColumna.length+5,4,"El sistema no converge con esta organización");
            hojaPorColumna.addCell(labelPorColumna);
        }
        if (convergeMatriz == true){
            labelPorMatriz = new Label(porColumna.length+5,porMatriz.length+6,iterMatriz+" iteraciones realizadas");
            hojaPorMatriz.addCell(labelPorMatriz);
        }else {
            labelPorMatriz = new Label(porColumna.length+5,4,"El sistema no converge con esta organización");
            hojaPorMatriz.addCell(labelPorMatriz);
        }
        excelSolucion.write();
        excelSolucion.close();
    }
}
