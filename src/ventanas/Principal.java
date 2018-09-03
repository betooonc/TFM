package ventanas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Principal {
    private JPanel mainWindow;
    private JLabel title;
    private JButton helpBtn;
    private JButton searchBtn;
    private JLabel imageLogo;
    private JRadioButton rb3x3 = new JRadioButton();
    private JRadioButton rb4x4 = new JRadioButton();
    private JRadioButton rb5x5 = new JRadioButton();
    private JRadioButton rb6x6 = new JRadioButton();
    private JButton inputMatrixBtn;
    private JTable matrixTbl;
    private JPanel inputPnl;
    private JButton crearMatrizBtn;
    private JButton cargarMatrizButton;
    private JButton ayudaBtn;
    private JButton runInputMatrix;
    private JPanel resultsPnl;
    private JLabel lblIterDPC;
    private JLabel lblIterDPM;
    private JLabel lblDDM;
    private JLabel lblEDDM;
    private JLabel lblEDDC;
    private JLabel lblDDC;
    private JPanel loadPnl;
    private JLabel lblDPC;
    private JLabel lblDPM;
    private JLabel lblTxtIterDPC;
    private JLabel lblTxtIterDPM;
    double[][] matrizDatos;
    double[] termIndep;
    ButtonGroup matrixSizeGroup = new ButtonGroup();
    DefaultTableModel tableModel;
    int nEcuaciones = 0;
    Organizar organizar = new Organizar();
    ManipularArchivo manipularArchivo = new ManipularArchivo();

    private void createUIComponents() {
        // logo de la universidad para la aplicaciOn
        imageLogo = new JLabel(new ImageIcon("src/img/unirLogoSmall.png"));
        //matriz para ingresar los sistemas de ecuaciones desde la aplicaciOn
        matrixTbl = new JTable();
        //grupo de radioButtons para seleccionar la dimensiOn del sistema al momento de crearlo desde el aplicaciOn
        matrixSizeGroup.add(rb5x5);
        matrixSizeGroup.add(rb3x3);
        matrixSizeGroup.add(rb4x4);
        matrixSizeGroup.add(rb6x6);
    }

    public Principal() {
        $$$setupUI$$$();
        searchBtn.addActionListener(new ActionListener() {
            /**
             * carga un archivo (*.xls) con la matriz del sistema de ecuaciones, ejecuta los mEtodos de organizaciOn
             * y resuelve las matrices resultantes con el mEtodo de Gauss-Seidel.
             * Imprime los resultados en el GUI y crea un archivo (*.xls) con los resultados.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //se resetean los datos para que no afecten a las nuevas ejecuciones
                lblDDC.setVisible(false);
                lblEDDC.setVisible(false);
                lblDDM.setVisible(false);
                lblEDDM.setVisible(false);
                lblIterDPC.setVisible(false);
                lblIterDPM.setVisible(false);
                lblTxtIterDPC.setText("");
                lblTxtIterDPM.setText("");
                lblIterDPC.setText("");
                lblIterDPM.setText("");
                lblDPC.setText("");
                lblDPM.setText("");
                organizar.resetData();

                try {
                    manipularArchivo.cargar();
                    matrizDatos = manipularArchivo.getMatrizDatos();
                    termIndep = manipularArchivo.getTermIndep();
                    //Ejecuta ambas organizaciones con la matriz obtenida del archivo
                    organizar.diagonalDominantePorColumna(matrizDatos, termIndep);
                    organizar.diagonalDominantePorMatriz(matrizDatos, termIndep);

                    //habilita el panel de resultados
                    resultsPnl.setVisible(true);

                    //valida las condiciones que cumple cada matriz resultante de ambas organizaciones y
                    // muestra los mensajes respectivos en la GUI
                    if (organizar.esDiagonalDominante(organizar.getDDC())) {
                        lblDDC.setVisible(true);
                    }
                    if (organizar.esEstrictamenteDiagonalDominante(organizar.getDDC())) {
                        lblEDDC.setVisible(true);
                    }
                    if (organizar.esDiagonalDominante(organizar.getDDM())) {
                        lblDDM.setVisible(true);
                    }
                    if (organizar.esEstrictamenteDiagonalDominante(organizar.getDDM())) {
                        lblEDDM.setVisible(true);
                    }

                    /**
                     * valida que la diagonal dominante no contenga ceros, de ser asI muestra un mensaje informativo al usuario
                     * caso contrario valida la convergencia y presenta el nUmero de iteraciones que fueron ejecutadas o un mensaje
                     * informando que el sistema no converge con la organizaciOn respectiva
                     */
                    if (!organizar.checkDiagonalPrincipal(organizar.getDDC())) {
                        lblTxtIterDPC.setText("Esta organización ubicó un 0 en la diagonal principal, Gauss-Seidel no se ejecutó.");
                    } else {
                        if (organizar.getNumIterColumna() == 0) {
                            lblTxtIterDPC.setText("El sistema no converge con esta organización");
                            lblIterDPC.setVisible(false);
                        } else {
                            lblTxtIterDPC.setText("Iteraciones para resolver el sistema:");
                            lblIterDPC.setVisible(true);
                            lblIterDPC.setText(String.valueOf(organizar.getNumIterColumna()));
                        }
                    }
                    if (!organizar.checkDiagonalPrincipal(organizar.getDDM())) {
                        lblTxtIterDPM.setText("Esta organización ubicó un 0 en la diagonal principal, Gauss-Seidel no se ejecutó.");
                    } else {
                        if (organizar.getNumIterMatriz() == 0) {
                            lblTxtIterDPM.setText("El sistema no converge con esta organización");
                            lblIterDPM.setVisible(false);
                        } else {
                            lblTxtIterDPM.setText("Iteraciones para resolver el sistema:");
                            lblIterDPM.setVisible(true);
                            lblIterDPM.setText(String.valueOf(organizar.getNumIterMatriz()));
                        }
                    }

                    //imprime la sumatoria de la diagonal principal obtenida de cada organizaciOn
                    lblDPC.setText(String.valueOf(organizar.getDiagonalPrincipal(organizar.getDDC())));
                    lblDPM.setText(String.valueOf(organizar.getDiagonalPrincipal(organizar.getDDM())));

                    //Crea el archivo con los resultados obtenidos de la ejecuciOn en la misma carpeta del archivo original
                    manipularArchivo.crear(organizar.getDDC(), organizar.getDDM(), organizar.getConvergeColumna(), organizar.getConvergeMatriz(), organizar.getNumIterColumna(), organizar.getNumIterMatriz(), organizar.getDiagonalPrincipal(organizar.getDDC()), organizar.getDiagonalPrincipal(organizar.getDDM()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        runInputMatrix.addActionListener(new ActionListener() {
            /**
             * lee la matriz ingrsada por el usuario con el sistema de ecuaciones, ejecuta los mEtodos de organizaciOn
             * y resuelve las matrices resultantes con el mEtodo de Gauss-Seidel.
             * Imprime los resultados en el GUI y crea un archivo (*.xls) con los resultados.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                //se resetean los datos para que no afecten a las nuevas ejecuciones
                lblDDC.setVisible(false);
                lblEDDC.setVisible(false);
                lblDDM.setVisible(false);
                lblEDDM.setVisible(false);
                lblIterDPC.setVisible(false);
                lblIterDPM.setVisible(false);
                lblTxtIterDPC.setText("");
                lblTxtIterDPM.setText("");
                lblIterDPC.setText("");
                lblIterDPM.setText("");
                lblDPC.setText("");
                lblDPM.setText("");
                organizar.resetData();

                matrizDatos = new double[nEcuaciones][nEcuaciones];
                termIndep = new double[nEcuaciones];
                for (int i = 0; i < nEcuaciones; i++) {
                    for (int j = 0; j < nEcuaciones; j++) {
                        matrizDatos[i][j] = Double.valueOf(String.valueOf(matrixTbl.getValueAt(i, j)));
                    }
                    termIndep[i] = Double.valueOf(String.valueOf(matrixTbl.getValueAt(i, nEcuaciones + 1)));
                }

                try {
                    //Ejecuta ambas organizaciones con la matriz obtenida del archivo
                    organizar.diagonalDominantePorColumna(matrizDatos, termIndep);
                    organizar.diagonalDominantePorMatriz(matrizDatos, termIndep);

                    resultsPnl.setVisible(true);
                    if (organizar.esDiagonalDominante(organizar.getDDC())) {
                        lblDDC.setVisible(true);
                    }
                    if (organizar.esEstrictamenteDiagonalDominante(organizar.getDDC())) {
                        lblEDDC.setVisible(true);
                    }
                    if (organizar.esDiagonalDominante(organizar.getDDM())) {
                        lblDDM.setVisible(true);
                    }
                    if (organizar.esEstrictamenteDiagonalDominante(organizar.getDDM())) {
                        lblEDDM.setVisible(true);
                    }

                    /**
                     * valida que la diagonal dominante no contenga ceros, de ser asI muestra un mensaje informativo al usuario
                     * caso contrario valida la convergencia y presenta el nUmero de iteraciones que fueron ejecutadas o un mensaje
                     * informando que el sistema no converge con la organizaciOn respectiva
                     */
                    if (!organizar.checkDiagonalPrincipal(organizar.getDDC())) {
                        lblTxtIterDPC.setText("Esta organización ubicó un 0 en la diagonal principal, Gauss-Seidel no se ejecutó.");
                    } else {
                        if (organizar.getNumIterColumna() == 0) {
                            lblTxtIterDPC.setText("El sistema no converge con esta organización");
                            lblIterDPC.setVisible(false);
                        } else {
                            lblTxtIterDPC.setText("Iteraciones para resolver el sistema:");
                            lblIterDPC.setVisible(true);
                            lblIterDPC.setText(String.valueOf(organizar.getNumIterColumna()));
                        }
                    }
                    if (!organizar.checkDiagonalPrincipal(organizar.getDDM())) {
                        lblTxtIterDPM.setText("Esta organización ubicó un 0 en la diagonal principal, Gauss-Seidel no se ejecutó.");
                    } else {
                        if (organizar.getNumIterMatriz() == 0) {
                            lblTxtIterDPM.setText("El sistema no converge con esta organización");
                            lblIterDPM.setVisible(false);
                        } else {
                            lblTxtIterDPM.setText("Iteraciones para resolver el sistema:");
                            lblIterDPM.setVisible(true);
                            lblIterDPM.setText(String.valueOf(organizar.getNumIterMatriz()));
                        }
                    }

                    //imprime la sumatoria de la diagonal principal obtenida de cada organizaciOn
                    lblDPC.setText(String.valueOf(organizar.getDiagonalPrincipal(organizar.getDDC())));
                    lblDPM.setText(String.valueOf(organizar.getDiagonalPrincipal(organizar.getDDM())));

                    //Crea el archivo con los resultados obtenidos de la ejecuciOn en la misma carpeta del archivo original
                    manipularArchivo.crear(organizar.getDDC(), organizar.getDDM(), organizar.getConvergeColumna(), organizar.getConvergeMatriz(), organizar.getNumIterColumna(), organizar.getNumIterMatriz(), organizar.getDiagonalPrincipal(organizar.getDDC()), organizar.getDiagonalPrincipal(organizar.getDDM()));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        helpBtn.addActionListener(new ActionListener() {
            /**
             * Muestra un mensaje de ayuda para el usuario explicando cOmo se debe crear el archivo de excel con la matriz
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "El archivo (*.xls) debe contener una matriz cuadrada y un vector de términos independientes separados por una columna en blanco");
            }
        });

        ayudaBtn.addActionListener(new ActionListener() {
            /**
             * Muestra un mensaje de ayuda para el usuario explicando las funcionalidades generales de la aplicaciOn
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Presione 'Crear Nueva Matriz'     para ingresar los valores directamente en el programa. Esta opción permite trabajar con sistemas de las dimensiones predeterminadas." +
                        "\nPresione 'Cargar Matriz Existente'     para cargar un archivo (*.xls) con la información del sistema. Esta opción permite trabajar con sistemas de N ecuaciones.");
            }
        });

        crearMatrizBtn.addActionListener(new ActionListener() {
            /**
             * Habilita el bloque de creaciOn de sistemas y oculta los otros paneles
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                loadPnl.setVisible(false);
                inputPnl.setVisible(true);
                resultsPnl.setVisible(false);
            }
        });

        cargarMatrizButton.addActionListener(new ActionListener() {
            /**
             * Habilita el bloque de carga de archivos y oculta los otros paneles
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                inputPnl.setVisible(false);
                loadPnl.setVisible(true);
                resultsPnl.setVisible(false);
            }
        });

        inputMatrixBtn.addActionListener(new ActionListener() {
            /**
             * Habilita las celdas necesarias en la tabla para ingresar un sistema de la dimensiOn seleccionada por el usuario
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rb3x3.isSelected()) {
                    nEcuaciones = 3;
                } else {
                    if (rb4x4.isSelected()) {
                        nEcuaciones = 4;
                    } else {
                        if (rb5x5.isSelected()) {
                            nEcuaciones = 5;
                        } else {
                            if (rb6x6.isSelected()) {
                                nEcuaciones = 6;
                            } else {
                                JOptionPane.showMessageDialog(null, "Debe seleccionar una dimensión para el sistema o cargar uno desde la otra opción.");
                            }
                        }
                    }
                }
                tableModel = new DefaultTableModel(nEcuaciones, nEcuaciones + 2);
                matrixTbl.setModel(tableModel);
            }
        });
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus no disponible, utilize el skin por default");
        }

        JFrame frame = new JFrame("TFM");
        frame.setContentPane(new Principal().mainWindow);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainWindow = new JPanel();
        mainWindow.setLayout(new BorderLayout(0, 0));
        mainWindow.setAutoscrolls(true);
        mainWindow.setMaximumSize(new Dimension(1000, 500));
        mainWindow.setMinimumSize(new Dimension(1000, 500));
        mainWindow.setPreferredSize(new Dimension(1000, 500));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(10, 0, 0, 10), -1, -1));
        mainWindow.add(panel1, BorderLayout.NORTH);
        title = new JLabel();
        title.setEnabled(true);
        title.setText("TFM - Transformación a sistemas con diagonal dominante");
        panel1.add(title, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        imageLogo.setBackground(new Color(-1));
        imageLogo.setEnabled(true);
        imageLogo.setHorizontalAlignment(0);
        imageLogo.setHorizontalTextPosition(0);
        imageLogo.setText("");
        panel1.add(imageLogo, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, 1, 1, new Dimension(100, 100), new Dimension(100, 100), new Dimension(100, 100), 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 3, new Insets(0, 10, 0, 10), -1, -1));
        panel1.add(panel2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        crearMatrizBtn = new JButton();
        crearMatrizBtn.setLabel("Crear Nueva Matriz");
        crearMatrizBtn.setText("Crear Nueva Matriz");
        panel2.add(crearMatrizBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 40), new Dimension(-1, 40), new Dimension(-1, 40), 0, false));
        cargarMatrizButton = new JButton();
        cargarMatrizButton.setLabel("Cargar Matriz Existente");
        cargarMatrizButton.setText("Cargar Matriz Existente");
        panel2.add(cargarMatrizButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 40), new Dimension(-1, 40), new Dimension(-1, 40), 0, false));
        ayudaBtn = new JButton();
        ayudaBtn.setText("Ayuda");
        panel2.add(ayudaBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 40), new Dimension(-1, 40), new Dimension(-1, 40), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer2 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer2, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer3 = new com.intellij.uiDesigner.core.Spacer();
        panel2.add(spacer3, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer4 = new com.intellij.uiDesigner.core.Spacer();
        panel1.add(spacer4, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 20), null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        panel3.setVisible(true);
        mainWindow.add(panel3, BorderLayout.CENTER);
        inputPnl = new JPanel();
        inputPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 5, new Insets(0, 0, 0, 0), -1, -1));
        inputPnl.setVisible(false);
        panel3.add(inputPnl, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        rb3x3.setText("3 x 3");
        inputPnl.add(rb3x3, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        inputPnl.add(panel4, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel4.add(matrixTbl, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer5 = new com.intellij.uiDesigner.core.Spacer();
        panel4.add(spacer5, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(10, 10), null, null, 0, false));
        runInputMatrix = new JButton();
        runInputMatrix.setText("Ejecutar");
        runInputMatrix.setVisible(true);
        panel4.add(runInputMatrix, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 35), new Dimension(-1, 35), new Dimension(-1, 35), 0, false));
        inputMatrixBtn = new JButton();
        inputMatrixBtn.setText("Ingresar datos");
        inputMatrixBtn.setVisible(true);
        inputPnl.add(inputMatrixBtn, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 35), new Dimension(-1, 35), new Dimension(-1, 35), 0, false));
        rb5x5.setSelected(false);
        rb5x5.setText("5 x 5");
        inputPnl.add(rb5x5, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rb6x6.setText("6 x 6");
        inputPnl.add(rb6x6, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rb4x4.setText("4 x 4");
        inputPnl.add(rb4x4, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Dimensión del sistema de ecuaciones:");
        inputPnl.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resultsPnl = new JPanel();
        resultsPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(12, 5, new Insets(0, 0, 0, 0), -1, -1));
        resultsPnl.setVisible(false);
        panel3.add(resultsPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("ORGANIZACIÓN POR COLUMNA");
        resultsPnl.add(label2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("ORGANIZACIÓN POR MATRIZ");
        resultsPnl.add(label3, new com.intellij.uiDesigner.core.GridConstraints(7, 0, 1, 5, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTxtIterDPC = new JLabel();
        lblTxtIterDPC.setText("Iteraciones para resolver el sistema:  ");
        resultsPnl.add(lblTxtIterDPC, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblTxtIterDPM = new JLabel();
        lblTxtIterDPM.setText("Iteraciones para resolver el sistema: ");
        resultsPnl.add(lblTxtIterDPM, new com.intellij.uiDesigner.core.GridConstraints(10, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblIterDPC = new JLabel();
        lblIterDPC.setText("");
        resultsPnl.add(lblIterDPC, new com.intellij.uiDesigner.core.GridConstraints(4, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblIterDPM = new JLabel();
        lblIterDPM.setText("");
        resultsPnl.add(lblIterDPM, new com.intellij.uiDesigner.core.GridConstraints(10, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Sumatoria de la Diagonal Principal: ");
        resultsPnl.add(label4, new com.intellij.uiDesigner.core.GridConstraints(5, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDPC = new JLabel();
        lblDPC.setText("");
        resultsPnl.add(lblDPC, new com.intellij.uiDesigner.core.GridConstraints(5, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Sumatoria de la Diagonal Principal: ");
        resultsPnl.add(label5, new com.intellij.uiDesigner.core.GridConstraints(11, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDPM = new JLabel();
        lblDPM.setText("");
        lblDPM.setVisible(true);
        resultsPnl.add(lblDPM, new com.intellij.uiDesigner.core.GridConstraints(11, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblEDDC = new JLabel();
        lblEDDC.setText("Es Estríctamente Diagonal Dominante");
        lblEDDC.setVisible(false);
        resultsPnl.add(lblEDDC, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblEDDM = new JLabel();
        lblEDDM.setText("Es Estríctamente Diagonal Dominante");
        lblEDDM.setVisible(false);
        resultsPnl.add(lblEDDM, new com.intellij.uiDesigner.core.GridConstraints(9, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDDC = new JLabel();
        lblDDC.setText("Es Diagonal Domiante");
        lblDDC.setVisible(false);
        resultsPnl.add(lblDDC, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lblDDM = new JLabel();
        lblDDM.setText("Es Diagonal Domiante");
        lblDDM.setVisible(false);
        resultsPnl.add(lblDDM, new com.intellij.uiDesigner.core.GridConstraints(8, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer6 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer6, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer7 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer7, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer8 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer8, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer9 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer9, new com.intellij.uiDesigner.core.GridConstraints(5, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer10 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer10, new com.intellij.uiDesigner.core.GridConstraints(8, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer11 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer11, new com.intellij.uiDesigner.core.GridConstraints(9, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer12 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer12, new com.intellij.uiDesigner.core.GridConstraints(10, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer13 = new com.intellij.uiDesigner.core.Spacer();
        resultsPnl.add(spacer13, new com.intellij.uiDesigner.core.GridConstraints(11, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, new Dimension(20, -1), null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setRequestFocusEnabled(false);
        label6.setText("RESULTADOS");
        resultsPnl.add(label6, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText(" ");
        resultsPnl.add(label7, new com.intellij.uiDesigner.core.GridConstraints(6, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 1), new Dimension(-1, 1), new Dimension(-1, 1), 0, false));
        loadPnl = new JPanel();
        loadPnl.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        loadPnl.setVisible(false);
        panel3.add(loadPnl, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        searchBtn = new JButton();
        searchBtn.setHorizontalAlignment(0);
        searchBtn.setHorizontalTextPosition(2);
        searchBtn.setIcon(new ImageIcon(getClass().getResource("/img/loadIconSmall.png")));
        searchBtn.setIconTextGap(4);
        searchBtn.setSelected(false);
        searchBtn.setText("Cargar   ");
        loadPnl.add(searchBtn, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, 60), new Dimension(150, 60), new Dimension(150, 60), 0, false));
        helpBtn = new JButton();
        helpBtn.setHideActionText(false);
        helpBtn.setHorizontalAlignment(0);
        helpBtn.setHorizontalTextPosition(2);
        helpBtn.setIcon(new ImageIcon(getClass().getResource("/img/helpIconSmall.png")));
        helpBtn.setIconTextGap(4);
        helpBtn.setLabel("Ayuda      ");
        helpBtn.setText("Ayuda      ");
        loadPnl.add(helpBtn, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, new Dimension(150, 60), new Dimension(150, 60), new Dimension(150, 60), 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer14 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer14, new com.intellij.uiDesigner.core.GridConstraints(3, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer15 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer15, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer16 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer16, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer17 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer17, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer18 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer18, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer19 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer19, new com.intellij.uiDesigner.core.GridConstraints(4, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer20 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer20, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer21 = new com.intellij.uiDesigner.core.Spacer();
        panel3.add(spacer21, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainWindow;
    }
}
