import org.graphstream.graph.Graph;
import org.graphstream.ui.swing.SwingGraphRenderer;
import org.graphstream.ui.swing_viewer.DefaultView;
import org.graphstream.ui.swing_viewer.SwingViewer;
import org.graphstream.ui.swing_viewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.HashMap;

public class MyGraphView {

    //判断当前窗口显示的结构(默认显示方形结构)
    public static int showStructure = MyGraphEnum.SQUARE_LATTICE.getTypeId();

    //存储各个结构信息
    private HashMap<Integer, MyGraph> graphMessageMap = new HashMap<>();

    private static JLabel structureMessageLabel = new JLabel();

    //显示当前变异体数量
    private JLabel mutationNumberLabel = new JLabel();

    //显示当前正常体数量
    private JLabel normalNumberLabel = new JLabel();

    //存储结构全部演化成变异体的次数
    private JLabel mutationEvolutionNumberLabel = new JLabel();

    //存储结构全部演化成正常体的次数
    private JLabel normalEvolutionNumberLabel = new JLabel();

    //当前运行次数
    private JLabel nowEvolutionNumberLabel = new JLabel();

    private JLabel createNodeLabel = new JLabel("Number of rows and columns generated: ");


    //目标次数
    private JLabel targetNumberLabel = new JLabel();
    private MyGraphView myGraphView = this;

    public void showGraphView() {

        //设置窗口显示风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }

        //创建初始显示图形信息(方形结构)
        MyGraph myGraph = new MyGraph();
        //设置The Square Lattice生成的成行数和列数
        myGraph.setNodeNumber(4);
        myGraph.setOneSelfGraph(MyGraphEnum.SQUARE_LATTICE.getTypeId());
        //创建重写线程的run方法
        MyThread myThread = new MyThread(this, myGraph);
        //创建线程
        Thread thread = new Thread(myThread);
        myGraph.setThread(thread);
        myGraph.setMyThread(myThread);
        graphMessageMap.put(MyGraphEnum.SQUARE_LATTICE.getTypeId(), myGraph);

        JPanel messageJPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        //点击重置按钮 重置结构图 重置结构信息
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyGraph graph = graphMessageMap.get(showStructure);
                graph.setIsPause(true);
                graph.setMutationAdapt(1.5);
                graph.setNormalAdapt(1.0);
                graph.setTargetNumber(1);
                graph.setMutationEvolutionNumber(0);
                graph.setNormalEvolutionNumber(0);
                switch (showStructure) {
                    case 1:
                        graph.setNodeNumber(4);
                        MyGraphUtil.squareLattice(graph);
                        break;
                    case 2:
                        graph.setNodeNumber(40);
                        MyGraphUtil.burst(graph);
                        break;
                    case 3:
                        graph.setNodeNumber(40);
                        MyGraphUtil.starStructure(graph);
                        break;
                    case 4:
                        graph.setNodeNumber(41);
                        MyGraphUtil.superstar(graph);
                        break;
                }

                updateMessage(graph);
                updateStructureMessageLabel(graph);
            }
        });
        messageJPanel.add(targetNumberLabel);

        messageJPanel.add(nowEvolutionNumberLabel);

        messageJPanel.add(normalEvolutionNumberLabel);

        messageJPanel.add(mutationEvolutionNumberLabel);

        messageJPanel.add(normalNumberLabel);

        messageJPanel.add(mutationNumberLabel);
        messageJPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 1));
        JButton start = new JButton("Start");
        start.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MyGraph nowShowGraph = graphMessageMap.get(showStructure);

                if (nowShowGraph.getIsPause() == true) {
                    int nodeCount = nowShowGraph.getGraph().getNodeCount();
                    //判断当前是否运行结束的状态 是则刷新
                    if (nowShowGraph.getMutationMap().size() == nodeCount || nowShowGraph.getNormalMap().size() == nodeCount) {
                        MyGraphUtil.start(nowShowGraph);
                        nowShowGraph.getMyThread().reset();
                    }
                    nowShowGraph.getMyThread().resumeThread(nowShowGraph);
                }

            }

        });

        JButton stop = new JButton("Pause");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {

                    graphMessageMap.get(showStructure).setIsPause(true);

            }
        });
        messageJPanel.add(start);
        messageJPanel.add(stop);
        messageJPanel.add(resetButton);

        JPanel nodeNamePanel = new JPanel();
        JLabel label = new JLabel("Select for the mutations");
        JTextField nodeName = new JTextField(9);
        nodeNamePanel.add(label);
        nodeNamePanel.add(nodeName);
        nodeNamePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));

        JPanel createNodePanel = new JPanel();
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
//        JLabel normalAdaptLabel = new JLabel("Fitness of Resident: ");
//        JTextField normalAdapt = new JTextField(9);
//        normalAdapt.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                //判断是否为数字或小数点 如果不符合要求则退回输入
//                if (!MyUtil.isNumeric(normalAdapt.getText(), e.getKeyChar())) {
//                    e.consume();
//                }
//            }
//        });
//        normalAdaptPanel.add(normalAdaptLabel);
//        normalAdaptPanel.add(normalAdapt);
        normalAdaptPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));

        JPanel mutationAdaptPanel = new JPanel();
        JLabel mutationAdaptLabel = new JLabel("Fitness of mutant: ");
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

        JPanel targetNumberPanel = new JPanel();
        JLabel targetNumberLabel = new JLabel("Run Number：");
        JTextField targetNumberText = new JTextField(9);
        targetNumberPanel.add(targetNumberLabel);
        targetNumberPanel.add(targetNumberText);
        targetNumberPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 2));
        //效验输入值是否为数字
        targetNumberText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!MyUtil.isNumber(targetNumberText.getText() + e.getKeyChar())) {
                    e.consume();
                }
            }
        });


        JPanel jPanel3 = new JPanel();
        jPanel3.add(nodeNamePanel);
        jPanel3.add(createNodePanel);
        jPanel3.add(normalAdaptPanel);
        jPanel3.add(mutationAdaptPanel);
        jPanel3.add(targetNumberPanel);


        JFrame jFrame = new JFrame();
        JPanel jPanel = new JPanel();



        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(4, 4, 5, 5));
        JButton squareLatticeButton = new JButton("The Square Lattice");
        squareLatticeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyGraph squareLatticeGraph;
                //修改生成节点功能的提示信息
                createNodeLabel.setText("Number of rows and columns generated: ");
                //判断容器中是否已经存储方形结构 空则生成并存入容器中 非空则取出
                if (null == graphMessageMap.get(MyGraphEnum.SQUARE_LATTICE.getTypeId())) {
                    squareLatticeGraph = new MyGraph();
                    //存储结构类型标识
                    squareLatticeGraph.setOneSelfGraph(MyGraphEnum.SQUARE_LATTICE.getTypeId());
                    squareLatticeGraph.setMyThread(new MyThread(myGraphView, squareLatticeGraph));
                    //创建线程
                    squareLatticeGraph.setThread(new Thread(squareLatticeGraph.getMyThread()));
                    //将结构信息存储到容器中
                    graphMessageMap.put(showStructure, squareLatticeGraph);
                    squareLatticeGraph.setNodeNumber(5);
                    //生成结构
                    MyGraphUtil.squareLattice(squareLatticeGraph);
                    //线程启动并默认暂停
                    squareLatticeGraph.getThread().start();

                } else {
                    squareLatticeGraph = graphMessageMap.get(MyGraphEnum.SQUARE_LATTICE.getTypeId());
                }

                //重新绘制画板
                repaintGraph(jPanel, squareLatticeGraph);
                //修改结构信息
                updateMessage(squareLatticeGraph);
                //修改当前显示结构标识
                showStructure = MyGraphEnum.SQUARE_LATTICE.getTypeId();

            }
        });
        JButton radiation = new JButton("The ‘Burst’");
        radiation.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                MyGraph burstGraph;
                createNodeLabel.setText("The number of Generated nodes: ");
                //判断容器中是否已经存储抑制结构 空则生成并存入容器中 非空则取出
                if (null == graphMessageMap.get(MyGraphEnum.BURST_STRUCTURE.getTypeId())) {
                    burstGraph = new MyGraph();
                    //存储结构类型标识
                    burstGraph.setOneSelfGraph(MyGraphEnum.BURST_STRUCTURE.getTypeId());
                    burstGraph.setMyThread(new MyThread(myGraphView, burstGraph));
                    //创建线程
                    burstGraph.setThread(new Thread(burstGraph.getMyThread()));
                    //将结构信息存储到容器中
                    graphMessageMap.put(MyGraphEnum.BURST_STRUCTURE.getTypeId(), burstGraph);
                    burstGraph.setNodeNumber(40);
                    //生成结构
                    MyGraphUtil.burst(burstGraph);
                    //线程启动并默认暂停
                    burstGraph.getThread().start();
                } else {
                    burstGraph = graphMessageMap.get(MyGraphEnum.BURST_STRUCTURE.getTypeId());
                }
                //重新绘制画板
                repaintGraph(jPanel, burstGraph);
                //修改结构信息
                updateMessage(burstGraph);
                //修改当前显示结构标识
                showStructure = MyGraphEnum.BURST_STRUCTURE.getTypeId();
            }
        });
        JButton amplificationButton = new JButton("The Star ");
        amplificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MyGraph starStructureGraph;
                createNodeLabel.setText("The number of Generated nodes: ");
                //判断容器中是否已经存储星形结构 空则生成并存入容器中 非空则取出
                if (null == graphMessageMap.get(MyGraphEnum.STAR_STRUCTURE.getTypeId())) {
                    starStructureGraph = new MyGraph();
                    //存储结构类型标识
                    starStructureGraph.setOneSelfGraph(MyGraphEnum.STAR_STRUCTURE.getTypeId());
                    starStructureGraph.setMyThread(new MyThread(myGraphView, starStructureGraph));
                    //创建线程
                    starStructureGraph.setThread(new Thread(starStructureGraph.getMyThread()));
                    //将结构信息存储到容器中
                    graphMessageMap.put(MyGraphEnum.STAR_STRUCTURE.getTypeId(), starStructureGraph);
                    starStructureGraph.setNodeNumber(40);
                    //生成结构
                    MyGraphUtil.starStructure(starStructureGraph);
                    //线程启动并默认暂停
                    starStructureGraph.getThread().start();
                } else {
                    starStructureGraph = graphMessageMap.get(MyGraphEnum.STAR_STRUCTURE.getTypeId());
                }
                //重新绘制画板
                repaintGraph(jPanel, starStructureGraph);
                //修改结构信息
                updateMessage(starStructureGraph);
                //修改当前显示结构标识
                showStructure = MyGraphEnum.STAR_STRUCTURE.getTypeId();
            }
        });
        JButton superstarButton = new JButton("Super Star");
        superstarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyGraph superstarGraph;
                createNodeLabel.setText("The number of Generated nodes: ");
                //判断容器中是否已经存储星形结构 空则生成并存入容器中 非空则取出并显示
                if (null == graphMessageMap.get(MyGraphEnum.SUPERSTAR.getTypeId())) {
                    superstarGraph = new MyGraph();
                    //存储结构类型标识
                    superstarGraph.setOneSelfGraph(MyGraphEnum.SUPERSTAR.getTypeId());
                    superstarGraph.setMyThread(new MyThread(myGraphView, superstarGraph));
                    //创建线程
                    superstarGraph.setThread(new Thread(superstarGraph.getMyThread()));
                    //将结构信息存储到容器中
                    graphMessageMap.put(MyGraphEnum.SUPERSTAR.getTypeId(), superstarGraph);
                    superstarGraph.setNodeNumber(41);
                    //生成结构
                    MyGraphUtil.superstar(superstarGraph);
                    //线程启动并默认暂停
                    superstarGraph.getThread().start();
                } else {
                    superstarGraph = graphMessageMap.get(MyGraphEnum.SUPERSTAR.getTypeId());
                }
                //重新绘制画板
                repaintGraph(jPanel, superstarGraph);
                //修改结构信息
                updateMessage(superstarGraph);
                //修改当前显示结构标识
                showStructure = MyGraphEnum.SUPERSTAR.getTypeId();
            }

        });


        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                MyGraph nowShowGraph = graphMessageMap.get(showStructure);
                //效验值是否为空
                if (null != targetNumberText.getText().trim() && !"".equals(targetNumberText.getText().trim())) {
                    if (Integer.valueOf(targetNumberText.getText().trim()) < 1) {
                        JOptionPane.showMessageDialog(null, "The number of runs is at least 1!", "Error ", 0);
                    } else {
                        nowShowGraph.setTargetNumber(Integer.valueOf(targetNumberText.getText().trim()));
                        //刷新结构信息
                        updateMessage(nowShowGraph);
                    }
                }

                //效验值是否为空
                if (null != mutationAdapt.getText().trim() && !"".equals(mutationAdapt.getText().trim())) {
                    if (nowShowGraph.getMutationAdapt() == Double.valueOf(mutationAdapt.getText())) {
                        return;
                    }
                    if (Double.valueOf(mutationAdapt.getText()) <= 0) {
                        JOptionPane.showMessageDialog(null, "Please enter a number greater than 0!", "Error ", 0);
                    }
                    //修改适应度 将程序暂停
                    nowShowGraph.setIsPause(true);
                    //重新生成结构，因修改适应度 之前的演变清除
                    MyGraphUtil.start(nowShowGraph);
                    nowShowGraph.setMutationAdapt(Double.valueOf(mutationAdapt.getText()));
                    mutationAdaptLabel.setText("The Fitness of Mutants ");
                    //提交之后清空输入框
                    mutationAdapt.setText("");

                    //因修改适应度 之前的演变次数重置
                    nowShowGraph.setMutationEvolutionNumber(0);
                    nowShowGraph.setNormalEvolutionNumber(0);
                    updateMessage(nowShowGraph);

                    //刷新适应度, 重新计算固定概率
                    updateStructureMessageLabel(nowShowGraph);

                }

//                if (null != normalAdapt.getText().trim() && !"".equals(normalAdapt.getText().trim())) {
//                    if (Double.valueOf(normalAdapt.getText()) <= 0) {
//                        JOptionPane.showMessageDialog(null, "请输入大于0的数！", "Error ", 0);
//                    }
//                    nowShowGraph.setNormalAdapt(Double.valueOf(normalAdapt.getText()));
//                    normalAdaptLabel.setText("输入正常体适应度: ");
//                    normalAdapt.setText("");
//                    updateStructureMessageLabel(nowShowGraph);
//
//                }

                if (!"".equals(nodeName.getText().trim())) {
                    //非空效验
                    if (null == nowShowGraph.getNode(nodeName.getText().trim())) {
                        JOptionPane.showMessageDialog(null, "Please enter the correct node name！", "Error ", 0);
                    } else {
                        MyGraphUtil.addMutationMap(nodeName.getText(), nowShowGraph);
                        MyGraphUtil.setMutationNodeColor(nodeName.getText(), nowShowGraph.getGraph());
                        updateMessage(nowShowGraph);
                        if (nowShowGraph.getMutationMap().size() == nowShowGraph.getGraph().getNodeCount()) {
                            nowShowGraph.getMyThread().updateMutationEvolutionNumber();
                        }
                    }
                    nodeName.setText("");
                }

                if (!"".equals(nodeNumber.getText().trim())) {
                    if (Integer.valueOf(nodeNumber.getText().trim()) <= 1) {
                        JOptionPane.showMessageDialog(null, "Rows and column should greater than 1", "Error ", 0);
                    } else {
                        if (showStructure == 1 && Integer.valueOf(nodeNumber.getText().trim()) > 15) {
                            JOptionPane.showMessageDialog(null, "Rows and column should below 15", "Error ", 0);
                            return;
                        }

                        if (Integer.valueOf(nodeNumber.getText().trim()) > 200) {
                            JOptionPane.showMessageDialog(null, "The number of Nodes should below 200", "Error ", 0);
                            return;
                        }

                        MyGraph graph = graphMessageMap.get(showStructure);
                        graph.setIsPause(true);
                        graph.setNodeNumber(Integer.valueOf(nodeNumber.getText().trim()));
                        //判断当前显示结构 重新生成对应的结构
                        switch (showStructure) {
                            case 1:
                                MyGraphUtil.squareLattice(graph);
                                break;
                            case 2:
                                MyGraphUtil.burst(graph);
                                break;
                            case 3:
                                MyGraphUtil.starStructure(graph);
                                break;
                            case 4:
                                MyGraphUtil.superstar(graph);
                                break;
                            default:
                                break;
                        }
                        updateMessage(graph);
                        updateStructureMessageLabel(graph);
                    }
                    nodeNumber.setText("");

                }

//                if ((nowShowGraph.getNormalEvolutionNumber() + nowShowGraph.getMutationEvolutionNumber()) == nowShowGraph.getTargetNumber()) {
//                    nowShowGraph.setMutationEvolutionNumber(0);
//                    nowShowGraph.setNormalEvolutionNumber(0);
//                    updateMessage(nowShowGraph);
//                }
            }


        });

        jPanel3.add(submitButton);
        jPanel2.add(squareLatticeButton);
        jPanel2.add(radiation);
        jPanel2.add(amplificationButton);
        jPanel2.add(superstarButton);
        MyGraphUtil.squareLattice(myGraph);
        //添加graph
        jPanel.add(getViews(myGraph));

        jFrame.add(jPanel, BorderLayout.CENTER);
        // jFrame.add(jPanel1,BorderLayout.AFTER_LAST_LINE);
        //jFrame.add(jPanel1,BorderLayout.CENTER);
        jFrame.add(jPanel2, BorderLayout.WEST);
        jFrame.add(jPanel3, BorderLayout.SOUTH);
        jFrame.add(messageJPanel, BorderLayout.NORTH);
        jFrame.pack();
        jFrame.setLocationRelativeTo(null);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //通过调用GraphicsEnvironment的getDefaultScreenDevice方法获得当前的屏幕设备了
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // 全屏设置
        gd.setFullScreenWindow(jFrame);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateMessage(myGraph);
        thread.start();
        // GridGenerator()


    }

    //修改显示信息
    public void updateMessage(MyGraph myGraph) {
        targetNumberLabel.setText("Aim Numbers of run:  " + myGraph.getTargetNumber());
        nowEvolutionNumberLabel.setText("Cumulative run numbers:    " + (myGraph.getNormalEvolutionNumber() + myGraph.getMutationEvolutionNumber()));
        normalEvolutionNumberLabel.setText("Numbers of Resident has occupied : " + myGraph.getNormalEvolutionNumber());
        mutationEvolutionNumberLabel.setText("Numbers of Mutants has occupied : " + myGraph.getMutationEvolutionNumber());
        normalNumberLabel.setText("Resident Individual in the Population:   " + myGraph.getNormalMap().size());
        mutationNumberLabel.setText("Mutants Individual in the Population:  " + myGraph.getMutationMap().size());
    }

    //重新绘制组件
    public void repaintGraph(JPanel jPanel, MyGraph myGraph) {
        jPanel.removeAll();
        jPanel.repaint();
        jPanel.add(getViews(myGraph));
        jPanel.revalidate();
    }

    //更改显示的结构
    public ViewPanel getViews(MyGraph myGraph) {
        Graph graph = myGraph.getGraph();
        Viewer viewer = new SwingViewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel viewPanel = new DefaultView(viewer, "123", new SwingGraphRenderer());
        viewer.addView(viewPanel);
        viewer.enableAutoLayout();

        viewPanel.setPreferredSize(new Dimension(1700, 975));
        updateStructureMessageLabel(myGraph);
        viewPanel.add(structureMessageLabel);
        viewPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 2));
        return viewPanel;
    }


    //显示个体适应度和固定概率
    public static void updateStructureMessageLabel(MyGraph myGraph) {
        double probability = 0.0;
        //判断正常体次数或者变异体次数是否为0 防止抛出无穷大
        if (myGraph.getNormalEvolutionNumber() == 0 && myGraph.getMutationEvolutionNumber() == myGraph.getTargetNumber()) {
            probability = 1;
        } else if (myGraph.getMutationEvolutionNumber() != 0) {
            probability = (double) myGraph.getMutationEvolutionNumber() / ((double) myGraph.getMutationEvolutionNumber() + (double) myGraph.getNormalEvolutionNumber());
            BigDecimal bigDecimal = new BigDecimal(probability);
            probability = bigDecimal.setScale(4, BigDecimal.ROUND_CEILING).doubleValue();
        }

        structureMessageLabel.setText("<html> The Fitness of Resident： " + myGraph.getNormalAdapt() + "<br> The Fitness of Mutants： " + myGraph.getMutationAdapt() +
                "<br> Fixation probability of the new mutant ： " + MyGraphUtil.calculateFixedProbability(myGraph) +
                "<br> Actual probability of the new mutant : " + probability + " </html>");
    }


}
