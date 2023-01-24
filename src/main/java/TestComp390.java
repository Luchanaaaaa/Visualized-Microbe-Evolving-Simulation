
import org.graphstream.graph.Graph;
import org.graphstream.ui.swing.SwingGraphRenderer;
import org.graphstream.ui.swing_viewer.DefaultView;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class TestComp390 {


    public static void main(String[] args) {

        MyGraph myGraph = new MyGraph();
        MyGraphUtil myGraphUtil = new MyGraphUtil();

        MyThread myThread = new MyThread();
        Thread thread = new Thread(myThread);

        JPanel nodeNamePanel = new JPanel();
        JLabel label = new JLabel("Which selected as Individual Variation");
        JTextField nodeName = new JTextField(9);
        nodeNamePanel.add(label);
        nodeNamePanel.add(nodeName);
        nodeNamePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));

        JPanel createNodePanel = new JPanel();
        JLabel createNodeLabel = new JLabel("The number of nodes in the population");
        JTextField nodeNumber = new JTextField(9);
        nodeNumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!MyUtil.isNumber(nodeNumber.getText() + e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        createNodePanel.add(createNodeLabel);
        createNodePanel.add(nodeNumber);
        createNodePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));

        JPanel normalAdaptPanel = new JPanel();
        JLabel normalAdaptLabel = new JLabel("Fitness for Normal Variation(Current Value:" + myGraph.getNormalAdapt() + ")");
        JTextField normalAdapt = new JTextField(9);
        normalAdapt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //判断是否为数字或小数点 如果不符合要求则退回输入
                if (!MyUtil.isNumeric(normalAdapt.getText(), e.getKeyChar())) {
                    e.consume();
                }
            }
        });
        normalAdaptPanel.add(normalAdaptLabel);
        normalAdaptPanel.add(normalAdapt);
        normalAdaptPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));

        JPanel mutationAdaptPanel = new JPanel();
        JLabel mutationAdaptLabel = new JLabel("Fitness for Individual Variation: (Current Value:" + myGraph.getMutationAdapt() + ")");
        JTextField mutationAdapt = new JTextField(9);
        mutationAdapt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //判断是否为数字或小数点 如果不符合要求则退回输入
                if (!MyUtil.isNumeric(mutationAdapt.getText(), e.getKeyChar())) {
                    e.consume();
                }
            }
        });

        mutationAdaptPanel.add(mutationAdaptLabel);
        mutationAdaptPanel.add(mutationAdapt);
        mutationAdaptPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();
        Graph graph = myGraph.getGraph();
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (myGraph.getIsPause() == true) {
                    myThread.resumeThread(myGraph);
                } else {
                    thread.start();
                }

            }
        });

        //存储按钮
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(4, 4, 5, 5));
        JButton jButton1 = new JButton("The square lattice");
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myGraph.setMoranProcessNumber(5);
                myGraphUtil.moRanProcess();
            }
        });
        JButton jButton2 = new JButton("Brust");
        jButton2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                myGraph.setNodeNumber(80);
                myGraphUtil.radiation();

            }
        });
        JButton jButton3 = new JButton(" The star structure");
        jButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myGraph.setNodeNumber(80);
                myGraphUtil.magnificationStructure();
            }
        });
        JButton jButton4 = new JButton("The super-star");
        JButton stop = new JButton("Pause");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myGraph.setIsPause(true);

            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null != mutationAdapt.getText().trim() && !"".equals(mutationAdapt.getText().trim())) {
                    myGraph.setMutationAdapt(Double.valueOf(mutationAdapt.getText()));
                    mutationAdaptLabel.setText("输入变异适应度(当前为" + myGraph.getMutationAdapt() + ")");
                    mutationAdapt.setText("");
                }

                if (null != normalAdapt.getText().trim() && !"".equals(normalAdapt.getText().trim())) {
                    myGraph.setNormalAdapt(Double.valueOf(normalAdapt.getText()));
                    normalAdaptLabel.setText("输入正常体适应度(当前为" + myGraph.getNormalAdapt() + ")");
                    normalAdapt.setText("");
                }

                if (!"".equals(nodeName.getText().trim())) {
                    if (null == myGraph.getNode(nodeName.getText().trim())) {
                        JOptionPane.showMessageDialog(null, "请输入正确的节点名称！", "Error ", 0);
                    } else {
                        myGraphUtil.addMutationMap(nodeName.getText());
                        myGraphUtil.setNodeColor(nodeName.getText());
                        System.out.println(myGraph.getNormalMap().size());
                        System.out.println(myGraph.getMutationMap().size());
                    }
                    nodeName.setText("");
                }

                if (!"".equals(nodeNumber.getText().trim())) {
                    switch (myGraph.getType()) {
                        case 1:
                            myGraph.setMoranProcessNumber(Integer.valueOf(nodeNumber.getText().trim()));
                            myGraphUtil.moRanProcess();
                            break;
                        case 2:
                            myGraph.setNodeNumber(Integer.valueOf(nodeNumber.getText().trim()));
                            myGraphUtil.radiation();
                            break;
                        case 3:
                            break;
                        default:
                            break;
                    }
                }


            }
        });


        jPanel2.add(jButton1);
        jPanel2.add(jButton2);
        jPanel2.add(jButton3);
        jPanel2.add(jButton4);

        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel viewPanel = new DefaultView(viewer, "123", new SwingGraphRenderer());
        //ViewPanel viewPanel = viewer.addDefaultView(false);
        // viewPanel.add(jButton);
        viewer.addView(viewPanel);
        viewPanel.setPreferredSize(new Dimension(1500, 1000));
        // JPanel jPanel1 = new JPanel();
        JPanel jPanel3 = new JPanel();
        // jPanel3.add(label);
        jPanel3.add(nodeNamePanel);
        jPanel3.add(createNodePanel);
        // jPanel3.add(nodeNumber);
        jPanel3.add(start, new FlowLayout(FlowLayout.LEFT, 40, 1));
        jPanel3.add(stop, new FlowLayout(FlowLayout.LEFT, 40, 1));


        //   jPanel3.add(normalAdaptLabel);
        jPanel3.add(normalAdaptPanel);
        //jPanel3.add(mutationAdaptLabel);
        jPanel3.add(mutationAdaptPanel);

        jPanel3.add(submitButton);
        // jPanel3.setLayout(new FlowLayout(FlowLayout.LEFT,40,1));
        viewer.enableAutoLayout();
        // jPanel.add(jButton,BorderLayout.NORTH);
        jPanel.add(viewPanel);
        //jFrame.add(start, BorderLayout.NORTH);
        jFrame.add(jPanel, BorderLayout.CENTER);
        // jFrame.add(jPanel1,BorderLayout.AFTER_LAST_LINE);
        //jFrame.add(jPanel1,BorderLayout.CENTER);
        jFrame.add(jPanel2, BorderLayout.WEST);
        jFrame.add(jPanel3, BorderLayout.NORTH);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myGraphUtil.moRanProcess();
        // GridGenerator()


    }


}