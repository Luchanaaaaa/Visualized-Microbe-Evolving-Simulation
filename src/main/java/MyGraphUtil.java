import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MyGraphUtil {


    //添加正常节点到变异节点集合中
    public static void addMutationMap(String name, MyGraph myGraph) {
        //存储正常体
        Map<String, Node> normalMap = myGraph.getNormalMap();

        //存储变异体
        Map<String, Node> mutationMap = myGraph.getMutationMap();

        if (null == mutationMap.get(name)) {
            normalMap.remove(name);
            mutationMap.put(name, myGraph.getGraph().getNode(name));
            if (mutationMap.size() == myGraph.getGraph().getNodeCount()) {
                myGraph.setMutationEvolutionNumber((myGraph.getMutationEvolutionNumber() + 1));
                MyGraphView.updateStructureMessageLabel(myGraph);
            }
        }

    }


    //将节点设置成变异节点的颜色
    public static void setMutationNodeColor(String name, Graph graph) {
        graph.getNode(name).setAttribute("ui.style", "fill-color: red;");
    }


    //计算突变体和正常体被选择的概率 并返回被选择的节点
    public static Node variationChoice(MyGraph myGraph) {
        //存储正常体
        Map<String, Node> normalMap = myGraph.getNormalMap();

        //存储变异体
        Map<String, Node> mutationMap = myGraph.getMutationMap();
        //正常体适应度(默认为1)
        double normalAdapt = myGraph.getNormalAdapt();

        //突变体适应度(默认为0.5)
        double mutationAdapt = myGraph.getMutationAdapt();

        List<WeightRandom.WeightObj<Node>> weightList = new ArrayList<WeightRandom.WeightObj<Node>>();


        for (Node node : mutationMap.values()) {
            WeightRandom.WeightObj<Node> b = new WeightRandom.WeightObj<Node>(node, mutationAdapt);
            weightList.add(b);
        }


        for (Node node : normalMap.values()) {
            WeightRandom.WeightObj<Node> a = new WeightRandom.WeightObj<Node>(node, normalAdapt);
            weightList.add(a);
        }

        WeightRandom wr = RandomUtil.weightRandom(weightList);
        return (Node) wr.next();


    }

    //返回根据权重随机之后的边缘
    public static Edge calculate(List<Edge> edges) {
        List<WeightRandom.WeightObj<Edge>> weightList = new ArrayList<WeightRandom.WeightObj<Edge>>();
        edges.forEach(o -> {
            WeightRandom.WeightObj<Edge> b = new WeightRandom.WeightObj<Edge>(o, (int) (o.getAttribute("ui.label")));
            weightList.add(b);
        });
        //根据权重随机选择
        WeightRandom wr = RandomUtil.weightRandom(weightList);
        return (Edge) wr.next();
    }


    //生成方形结构图
    public static void squareLattice(MyGraph myGraph) {
        myGraph.setNormalEvolutionNumber(0);
        myGraph.setMutationEvolutionNumber(0);
        Graph graph = myGraph.getGraph();
        graph.clear();
        GridGenerator gen = new GridGenerator();
        gen.setDirectedEdges(true, false);
        gen.addSink(graph);
        gen.begin(graph);

        for (int i = 1; i < myGraph.getNodeNumber(); i++) {
            gen.nextEvents(graph);
        }
        settingsWeight(graph);
        gen.end();
        start(myGraph);
        setAttribute(graph);

    }


    //生成放射结构图
    public static void burst(MyGraph myGraph) {
        myGraph.setNormalEvolutionNumber(0);
        myGraph.setMutationEvolutionNumber(0);
        Graph graph = myGraph.getGraph();
        graph.clear();
        graph.addNode("v1");
        graph.getNode("v1").setAttribute("ui.label", "v1");
        for (int i = 2; i <= myGraph.getNodeNumber(); i++) {

            String name = "v1 " + "_" + "v" + i + "";
            graph.addNode(("v" + i));
            graph.getNode(("v" + i)).setAttribute("ui.label", "v" + i);

            graph.addEdge(name, ("v1"), ("v" + i), true);

        }

        Random r = new Random();
        graph.nodes().forEach(o -> {

            Stream<Edge> edgeSet = o.leavingEdges();
            edgeSet.forEach(s -> {
                s.setAttribute("ui.label", r.nextInt(9) + 1);

            });
        });
        start(myGraph);
        setAttribute(graph);
    }

    //生成放大结构
    public static void starStructure(MyGraph myGraph) {
        myGraph.setNormalEvolutionNumber(0);
        myGraph.setMutationEvolutionNumber(0);
        Graph graph = myGraph.getGraph();
        graph.clear();
        graph.addNode("v1");
        graph.getNode("v1").setAttribute("ui.label", "v1");
        for (int i = 2; i <= myGraph.getNodeNumber(); i++) {

            String name = "v1 " + "_" + "v" + i + "";
            graph.addNode(("v" + i));
            graph.getNode(("v" + i)).setAttribute("ui.label", "v" + i);
            graph.addEdge(name, "v1", ("v" + i), true);
            graph.addEdge("v" + i + "_" + "v1", ("v" + i), "v1", true);

        }

        settingsWeight(graph);
        start(myGraph);
        setAttribute(graph);
    }


    //生成超新星结构
    public static void superstar(MyGraph myGraph) {
        myGraph.setNormalEvolutionNumber(0);
        myGraph.setMutationEvolutionNumber(0);
        Graph graph = myGraph.getGraph();
        graph.clear();
        graph.addNode("v1").setAttribute("ui.label", "v1");
        Random r = new Random();
        int nodeCount = myGraph.getNodeNumber();
        int n = (nodeCount - 1) / 5;
        int remainder = (nodeCount - 1) % 5;
        int nameNumber = 2;
        Node node = null;
        for (int i = 1; i <= 5; i++) {

            for (int j = 1; j <= n; j++) {
                graph.addNode("v" + nameNumber).setAttribute("ui.label", "v" + nameNumber);
                if (j == 1) {
                    node = graph.getNode("v" + nameNumber);
                    graph.addEdge("v" + nameNumber + "_v1", "v" + nameNumber, "v1", true).setAttribute("ui.label", r.nextInt(9) + 1);
                    nameNumber++;
                    continue;
                }
                graph.addEdge("v1" + "_v" + nameNumber, "v1", "v" + nameNumber, true).setAttribute("ui.label", r.nextInt(9) + 1);
                graph.addEdge("v" + nameNumber + "_" + node.getId(), "v" + nameNumber, node.getId(), true).setAttribute("ui.label", r.nextInt(9) + 1);
                nameNumber++;

            }
            if (remainder != 0) {
                graph.addNode("v" + nameNumber).setAttribute("ui.label", "v" + nameNumber);
                graph.addEdge("v1" + "_v" + nameNumber, "v1", "v" + nameNumber, true).setAttribute("ui.label", r.nextInt(9) + 1);
                graph.addEdge("v" + nameNumber + "_" + node.getId(), "v" + nameNumber, node.getId(), true).setAttribute("ui.label", r.nextInt(9) + 1);
                remainder--;
                nameNumber++;

            }
        }
        setSuperstarAttribute(graph);
        start(myGraph);
    }


    //设置权重
    public static void settingsWeight(Graph graph) {
        Random r = new Random();
        graph.nodes().forEach(o -> {
            Stream<Edge> edgeSet = o.leavingEdges();
            Stream<Edge> edgeStream = o.enteringEdges();
            edgeSet.forEach(s -> {
                s.setAttribute("ui.label", r.nextInt(9) + 1);
                s.setAttribute("ui.style", "text-alignment: under; ");
                s.setAttribute("ui.style", "text-offset:" + "" + 10 + "" + "," + "" + -23 + "" + ";");
            });

            edgeStream.forEach(w -> {
                w.setAttribute("ui.style", "text-alignment: above; ");
                w.setAttribute("ui.style", "text-offset:" + "" + -13 + "" + "," + "" + 15 + "" + ";");
            });
        });

    }

//    //莫兰过程设置权重 并设置一定的样式
//    public static void settingsWeight2(Graph graph) {
//        graph.nodes().forEach(o -> {
//
//            Stream<Edge> edgeSet = o.leavingEdges();
//            Stream<Edge> edgeStream = o.enteringEdges();
//            edgeSet.forEach(s -> {
//                s.setAttribute("ui.label", 1);
//                s.setAttribute("ui.style", "text-alignment: under; ");
//                s.setAttribute("ui.style", "text-offset:" + "" + 10 + "" + "," + "" + -23 + "" + ";");
//            });
//
//            edgeStream.forEach(w -> {
//                w.setAttribute("ui.style", "text-alignment: above; ");
//                w.setAttribute("ui.style", "text-offset:" + "" + -13 + "" + "," + "" + 15 + "" + ";");
//            });
//        });
//
//    }

    //计算各个结构的固定概率
    public static double calculateFixedProbability(MyGraph myGraph) {

        Double fixedProbability = 0.0;
        double mutationAdapt = myGraph.getMutationAdapt();
        double normalAdapt = myGraph.getNormalAdapt();
        if (mutationAdapt == normalAdapt) {
            fixedProbability = mutationAdapt / myGraph.getGraph().getNodeCount();
            return fixedProbability;
        }


        switch (myGraph.getOneSelfGraph()) {

            //方形结构
            case 1:
                double pow = Math.pow(mutationAdapt, myGraph.getGraph().getNodeCount());
                BigDecimal b1 = new BigDecimal(1.0 - (1.0 / mutationAdapt));
                BigDecimal b2 = new BigDecimal((1.0 - (1.0 / pow)));
                fixedProbability = b1.divide(b2, 4, BigDecimal.ROUND_CEILING).doubleValue();
                break;
            //抑制结构
            case 2:
                fixedProbability = 1.0 / (double) myGraph.getGraph().getNodeCount();
                break;
            //放大结构
            case 3:
                double pow1 = Math.pow(mutationAdapt, 2);
                double pow2 = Math.pow(mutationAdapt, myGraph.getGraph().getNodeCount() * 2);
                BigDecimal bigDecimal = new BigDecimal(1.0 - (1.0 / pow1));
                BigDecimal bigDecimal1 = new BigDecimal((1.0 - (1.0 / pow2)));
                fixedProbability = bigDecimal.divide(bigDecimal1, 4, BigDecimal.ROUND_CEILING).doubleValue();
                break;
            //超新星结构
            case 4:
                int k = 3;
                double superstarPow1 = Math.pow(mutationAdapt, k);
                double superstarPow2 = Math.pow(mutationAdapt, k * myGraph.getGraph().getNodeCount());
                BigDecimal superstarBigDecimal = new BigDecimal(1.0 - (1.0 / superstarPow1));
                BigDecimal superstarBigDecimal1 = new BigDecimal((1.0 - (1.0 / superstarPow2)));
                fixedProbability = superstarBigDecimal.divide(superstarBigDecimal1, 4, BigDecimal.ROUND_CEILING).doubleValue();
                break;
            default:
                break;

        }

        return fixedProbability;
    }


    //随机生成变异体
    public static void start(MyGraph myGraph) {
        Graph graph = myGraph.getGraph();
        //存储正常体
        Map<String, Node> normalMap = myGraph.getNormalMap();

        //存储变异体
        Map<String, Node> mutationMap = myGraph.getMutationMap();
        List<Node> collect = graph.nodes().collect(Collectors.toList());
        Random random = new Random();
        mutationMap.clear();
        normalMap.clear();
        //随机生成初始变异体
        int mutation = random.nextInt(collect.size());
        for (int i = 0; i < collect.size(); i++) {
            if (i == mutation) {
                mutationMap.put(collect.get(i).getId(), collect.get(i));
                collect.get(i).setAttribute("ui.style", "fill-color: red;");
                continue;
            }
            collect.get(i).setAttribute("ui.style", "fill-color: #444;");
            normalMap.put(collect.get(i).getId(), collect.get(i));
        }


    }

    //设置graph样式
    public static void setAttribute(Graph graph) {
        String styleSheet =
                "edge {" +
                        "   arrow-size: 8px;" +
                        "	text-size: 18px;" +
                        "	size: 2px;" +
                        "   text-alignment: center;" +
                        "   text-color: blue;" +
                        "}" +
                        "node {" +
                        "	size: 15px;" +
                        "	text-size: 20px;" +
                        "   text-color: #ff9900;" +
                        "	fill-color: #444;" +
                        "text-style: bold-italic;" +
                        "}";
        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
    }


    //设置超新星的样式
    public static void setSuperstarAttribute(Graph graph) {
        String styleSheet =
                "edge {" +
                        "   arrow-size: 8px;" +
                        "	text-size: 18px;" +
                        "	size: 2px;" +
                        "   text-alignment: center;" +
                        "   text-color: blue;" +
                        "   shape: cubic-curve;" +
                        "}" +
                        "node {" +
                        "	size: 20px;" +
                        "	text-size: 20px;" +
                        "   text-color: #ff9900;" +
                        "	fill-color: #444;" +
                        "text-style: bold-italic;" +
                        "}";
        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
    }

}
