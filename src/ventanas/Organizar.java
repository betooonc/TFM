package ventanas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Organizar {
    private double arrayDDC[][], arrayDDM [][];
    private double vectorDDC[], vectorDDM [];
    private int numIterColumna, numIterMatriz;
    private boolean convergeColumna, convergeMatriz;


    public void resetData(){
        /**
         * Reinicia los valores utilizados en las validaciones para no afectar a las nuevas ejecuciones
         */
        numIterColumna = 0;
        numIterMatriz = 0;
        convergeColumna = false;
        convergeMatriz = false;
    }

    public int getNumIterColumna() {
        return numIterColumna;
    }

    public int getNumIterMatriz() {
        return numIterMatriz;
    }

    public boolean getConvergeColumna() {
        return convergeColumna;
    }

    public boolean getConvergeMatriz() {
        return convergeMatriz;
    }

    public void diagonalDominantePorColumna(double array[][], double vector[]){
        /**
         * Organiza la matriz aumentada por columna
         * Recibe una matriz[NxN] con los coeficientes del sistema y un vector[N] con los tErminos independientes
         */
        double burbuja = 0, numMayor = 0;
        int fila = 0;
        arrayDDC = new double[array.length][array.length];
        vectorDDC = new double[vector.length];

        //Se transfieren los valores de la matriz y el vector a variables locales para que sean manipuladas sin afectar los parAmetros.
        for (int i = 0; i < array.length; i++) {
            arrayDDC[i] = Arrays.copyOf(array[i], array[i].length);
            vectorDDC[i] = vector [i];
        }

        //Busca el valor mAs alto de cada columna y mueve toda la fila para que dicho valor coincida con la diagonal principal
        for (int k = 0; k < arrayDDC.length; k++){
            fila = k;
            numMayor = 0;
            for (int i = k; i < arrayDDC.length; i++){
                if (Math.abs(arrayDDC[i][k]) > Math.abs(numMayor)){
                    numMayor = arrayDDC[i][k];
                    fila = i;
                }
            }
            if (fila != k){
                for (int j = 0; j < arrayDDC.length; j++){
                    burbuja = arrayDDC[k][j];
                    arrayDDC[k][j] = arrayDDC[fila][j];
                    arrayDDC[fila][j] = burbuja;
                }
                burbuja = vectorDDC[k];
                vectorDDC[k] = vectorDDC[fila];
                vectorDDC[fila] = burbuja;
            }
        }

        //Si la Ultima posiciOn de la diagonal principal es 0 cambia toda la fila por la anterior.
        if (arrayDDC[arrayDDC.length-1][arrayDDC.length-1] == 0){
            for (int j = 0; j < arrayDDC.length; j++){
                burbuja = arrayDDC[arrayDDC.length-1][j];
                arrayDDC[arrayDDC.length-1][j] = arrayDDC[arrayDDC.length-2][j];
                arrayDDC[arrayDDC.length-2][j] = burbuja;
            }
            burbuja = vectorDDC[vectorDDC.length-1];
            vectorDDC[vectorDDC.length-1] = vectorDDC[vectorDDC.length-2];
            vectorDDC[vectorDDC.length-2] = burbuja;
        }
    }

    public void diagonalDominantePorMatriz(double array[][], double vector[]){
        /**
         * Organiza la matriz aumentada por matriz
         * Recibe una matriz[NxN] con los coeficientes del sistema y un vector[N] con los tErminos independientes
         */
        double numAuxiliar = 0, burbuja = 0;
        int posI = 0, posJ = 0;
        List<Integer> listaOmision = new ArrayList<>();
        arrayDDM = new double[array.length][array.length];
        vectorDDM = new double[vector.length];
        listaOmision.add(0,-1);

        //Se transfieren los valores de la matriz y el vector a variables locales para que sean manipuladas sin afectar los parAmetros.
        for (int i = 0; i < array.length; i++) {
            arrayDDM[i] = Arrays.copyOf(array[i], array[i].length);
            vectorDDM[i] = vector [i];
        }

        //Busca el valor mAs alto de toda la matriz y mueve toda la fila para que dicho valor coincida con la diagonal principal
        for (int k = 0; k < arrayDDM.length; k++) {
            numAuxiliar = 0;
            for (int i = 0; i < arrayDDM.length; i++) {
                if (!listaOmision.contains(i)){
                    for (int j = 0; j < arrayDDM.length; j++) {
                        if (!listaOmision.contains(j)){
                            if (Math.abs(arrayDDM[i][j]) > Math.abs(numAuxiliar)) {
                                numAuxiliar = arrayDDM[i][j];
                                posI = i;
                                posJ = j;
                            }
                        }
                    }
                }
            }
            for (int j = 0; j < arrayDDM.length; j++){
                burbuja = arrayDDM[posJ][j];
                arrayDDM[posJ][j] = arrayDDM[posI][j];
                arrayDDM[posI][j] = burbuja;
            }
            burbuja = vectorDDM[posJ];
            vectorDDM[posJ] = vectorDDM[posI];
            vectorDDM[posI] = burbuja;
            listaOmision.add(k+1,posJ);
        }

        //Si la Ultima posiciOn de la diagonal principal es 0 cambia toda la fila por la fila con el menor valor en la diagonal principal.
        if (arrayDDM[arrayDDM.length-1][arrayDDM.length-1] == 0){
            numAuxiliar = 0;
            posI = 0;
            for (int i = 0; i < arrayDDM.length; i++) {
                if (Math.abs(arrayDDM[i][i]) < Math.abs(numAuxiliar) && arrayDDM[i][arrayDDM.length-1] != 0) {
                    numAuxiliar = arrayDDM[i][i];
                    posI = i;
                }
            }
            for (int j = 0; j < arrayDDM.length; j++){
                burbuja = arrayDDM[arrayDDM.length-1][j];
                arrayDDM[arrayDDM.length-1][j] = arrayDDM[posI][j];
                arrayDDM[posI][j] = burbuja;
            }
            burbuja = vectorDDM[vectorDDM.length-1];
            vectorDDM[vectorDDM.length-1] = vectorDDM[posI];
            vectorDDM[posI] = burbuja;
        }
    }

    public double[][] getDDC() {
        /**
         * Retorna la matriz organizada por columna
         * No recibe parAmetros.
         */
        double [][] concatenado = new double[arrayDDC.length][arrayDDC.length+2];
        double [] vectorSolucion= new double[arrayDDC.length];

        System.out.println("--------------------Por Columna-------------------------");
        for (int i = 0; i < arrayDDC.length; i++) {
            for (int j = 0; j < arrayDDC.length; j++) {
                System.out.print(arrayDDC[i][j] + "\t");
            }
            System.out.print(vectorDDC[i]);
            System.out.println();
        }
        vectorSolucion = gaussSeidel(arrayDDC,vectorDDC,Math.pow(10,-5),500,1);
        /**Si se cumple la condiciOn de diagonal dominante concatena la matriz con los tErminos independeitnes y la soluciOn obtenida, caso contrario imprime un mensaje informativo en el log
        if (esEstrictamenteDiagonalDominante(arrayDDC)){
        }else{
            System.out.println("No es diagonal dominante");
        }*/
        for (int i = 0; i < arrayDDC.length; i++) {
            for (int j = 0; j < arrayDDC.length; j++) {
                concatenado[i][j] = arrayDDC[i][j];
            }
            concatenado[i][arrayDDC.length] = vectorDDC[i];
            concatenado[i][arrayDDC.length+1] = vectorSolucion[i];
        }
        return concatenado;
    }

    public double[][] getDDM() {
        /**
         * Retorna la matriz organizada por matriz
         * No recibe parAmetros.
         */
        double [][] concatenado = new double[arrayDDM.length][arrayDDM.length+2];
        double [] vectorSolucion= new double[arrayDDM.length];

        System.out.println("--------------------Por Matriz-------------------------");
        for (int i = 0; i < arrayDDM.length; i++) {
            for (int j = 0; j < arrayDDM.length; j++) {
                System.out.print(arrayDDM[i][j] + "\t");
            }
            System.out.print(vectorDDM[i]);
            System.out.println();
        }
        vectorSolucion = gaussSeidel(arrayDDM,vectorDDM,Math.pow(10,-5),500,2);
        /**Si se cumple la condiciOn de diagonal dominante concatena la matriz con los tErminos independeitnes y la soluciOn obtenida, caso contrario imprime un mensaje informativo en el log
        if (esEstrictamenteDiagonalDominante(arrayDDM)){
        }else{
            System.out.println("No es diagonal dominante");
        }*/

        for (int i = 0; i < arrayDDM.length; i++) {
            for (int j = 0; j < arrayDDM.length; j++) {
                concatenado[i][j] = arrayDDM[i][j];
            }
            concatenado[i][arrayDDM.length] = vectorDDM[i];
            concatenado[i][arrayDDM.length+1] = vectorSolucion[i];
        }

        return concatenado;
    }

    public double getDiagonalPrincipal(double array[][]){
        /**
         * Retorna la suatoria de la diagonal principal de una matriz cuadrada
         * Recibe como parAmetro la matriz a evaluar
         */
        double diagonalPrincial = 0;
        for (int i = 0; i < array.length; i++) {
            diagonalPrincial = diagonalPrincial + Math.abs(array[i][i]);
        }
        return Math.round(diagonalPrincial*100)/100;
    }

    public boolean checkDiagonalPrincipal (double array[][]){
        /**
         * Valida que la diagonal principal de una matriz cuadrada no contenga ningUn 0.
         * Gauss-Seidel no puede ejecutar si hay un 0 en la diagonal, genera una indeterminaciOn.
         * Recibe como parAmetro la matriz a evaluar
         */
        boolean checkDiagonalPrincial = true;
        for (int i = 0; i < array.length; i++) {
            if (array[i][i] == 0){
                checkDiagonalPrincial = false;
            }
        }
        return checkDiagonalPrincial;
    }

    public boolean esEstrictamenteDiagonalDominante(double array[][]){
        /**
         * Retorna verdadero o falso si una matriz cumple con la condiciOn de diagonal estrIctamente dominante
         * Recibe como parAmetro la matriz a evaluar.
         */
        boolean esEDD = true;
        double diagonalPrincipal = 0, diagonalInferior = 0, diagonalSuperior = 0;
        for (int i = 0; i < array.length; i++) {
            diagonalPrincipal = diagonalPrincipal + Math.abs(array[i][i]);
        }
        for (int i = 1; i < array.length; i++) {
            for (int j = 0; j < array.length - i; j++) {
                diagonalSuperior = diagonalSuperior + Math.abs(array[j][j+i]);
                diagonalInferior = diagonalInferior + Math.abs(array[j+i][j]);
            }
            if (diagonalPrincipal < diagonalSuperior || diagonalPrincipal < diagonalInferior){
                esEDD = false;
                return esEDD;
            }
            diagonalSuperior = 0;
            diagonalInferior = 0;
        }
        return esEDD;
    }

    public boolean esDiagonalDominante(double array[][]){
        /**
         * Retorna verdadero o falso si una matriz cumple con la condiciOn de diagonal dominante
         * Recibe como parAmetro la matriz a evaluar.
         */
        boolean esDD = true;
        double sumatoria = 0;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (i != j){
                    sumatoria = sumatoria + Math.abs(array[i][j]);
                }
            }
            if (Math.abs(array[i][i]) < sumatoria){
                esDD = false;
                return esDD;
            }
            sumatoria = 0;
        }
        return esDD;
    }

    public double[] gaussSeidel (double array[][], double vectorTerminosIndependientes[], double tol, int maxIter, int organizador){
        /**
         * Ejecuta el mEtodo de Gauss-Seidel para buscar la soluciones del sistema
         * Recibe como parAmetros la matriz, el vector de tErminos independientes,
         * la tolerancia del error, el mAximo de iteraciones que realizarA el mEtodo
         * y un cOdigo para diferenciar la organizaciOn que se estA evaluando. 1 = PorColumna, 2 = PorMatriz.
         */
        double vectorSolucion [] = new double[array.length];
        double suma = 0;
        boolean bandera, converge=true;
        for (int i = 0; i < vectorSolucion.length; i++) {
            System.out.println(vectorSolucion[i]);
        }

        //Despeja el valor de la diagonal principal en cada ecuaciOn para calcular los posibles valores de cada incognita
        System.out.println("---------------GAUSSSSS---------------");
        for (int iter = 1; iter <= maxIter; iter++) {
            bandera = true;
            for (int i = 0; i < array.length; i++) {
                suma = 0;
                for (int j = 0; j < array.length; j++) {
                    if (j != i){
                        suma += array[i][j] * vectorSolucion[j];
                    }
                }

                //Valida si la diferencia entre el valor a calcular y el calculado en la iteraciOn anterior es menor al error,
                // de ser asI se baja una bandera lOgica, si una de las soluciones no cumple la condiciOn se continua con la siguiente iteraciOn
                if (Math.abs(((vectorTerminosIndependientes[i]-suma)/array[i][i])-vectorSolucion[i]) > tol){
                    bandera = false;
                }

                //Al "despejar" la incOgnita pasa a dividir el valor de la diagonal principal
                if (Double.isInfinite((vectorTerminosIndependientes[i]-suma)/array[i][i]) || Double.isNaN(suma)){
                    converge = false;
                    if (organizador == 1){
                        convergeColumna = false;
                        numIterColumna = 0;
                        break;
                    }
                    if (organizador == 2){
                        convergeMatriz = false;
                        numIterMatriz = 0;
                        break;
                    }
                }
                vectorSolucion[i] = (vectorTerminosIndependientes[i]-suma)/array[i][i];
                System.out.println(vectorSolucion[i]);
            }
            if (converge == false){
                break;
            }
            //En caso de que todos los valores cumplan la tolerancia indicada en el parAmetro se terminan las iteraciones y se imprime el resultado.
            if (bandera == true){
                System.out.println("========================");
                System.out.println(iter);
                System.out.println("========================");

                if (organizador == 1){
                    convergeColumna = true;
                    numIterColumna = iter;
                    break;
                }
                if (organizador == 2){
                    convergeMatriz = true;
                    numIterMatriz = iter;
                    break;
                }
            }
            System.out.println("----------");
        }
        return vectorSolucion;
    }
}
