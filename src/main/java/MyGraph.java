import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

public class MyGraph {

    //创建图表
    private Graph graph;

    private Thread thread;

    private MyThread myThread;

    //判断自己显示的结构为哪一种
    private int oneSelfGraph;

    //存储正常体
    private Map<String, Node> normalMap = new HashMap<>();

    //存储变异体
    private Map<String, Node> mutationMap = new HashMap<>();

    //目标运行次数(默认为1)
    private int targetNumber = 1;

    //存储结构全部演化成正常体的次数
    private int normalEvolutionNumber = 0;

    //存储结构全部演化成变异体的次数
    private int mutationEvolutionNumber = 0;

    //正常体适应度(默认为1)
    private double normalAdapt = 1;

    //生成节点个数
    private int nodeNumber;

    //突变体适应度(默认为1.5)
    private double mutationAdapt = 1.5;

    //判断是否暂停
    private boolean isPause = true;


    public Graph getGraph() {
        if (null == graph) {
            graph = new SingleGraph("graph");
        }
        return graph;
    }

    public int getOneSelfGraph() {
        return oneSelfGraph;
    }

    public void setOneSelfGraph(int oneSelfGraph) {
        this.oneSelfGraph = oneSelfGraph;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public MyThread getMyThread() {
        return myThread;
    }

    public void setMyThread(MyThread myThread) {
        this.myThread = myThread;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
    }

    public int getNormalEvolutionNumber() {
        return normalEvolutionNumber;
    }

    public int getMutationEvolutionNumber() {
        return mutationEvolutionNumber;
    }

    public void setNormalEvolutionNumber(int normalEvolutionNumber) {
        this.normalEvolutionNumber = normalEvolutionNumber;
    }

    public void setMutationEvolutionNumber(int mutationEvolutionNumber) {
        this.mutationEvolutionNumber = mutationEvolutionNumber;
    }


    public void setNodeNumber(int nodeNumber) {
        this.nodeNumber = nodeNumber;
    }

    public int getNodeNumber() {
        return nodeNumber;
    }

    public void setNormalAdapt(double normalAdapt) {
        this.normalAdapt = normalAdapt;
    }

    public void setMutationAdapt(double mutationAdapt) {
        this.mutationAdapt = mutationAdapt;
    }

    public double getMutationAdapt() {
        return mutationAdapt;
    }

    public boolean getIsPause() {
        return isPause;
    }

    public void setIsPause(boolean isPause) {
        this.isPause = isPause;
    }

    public Map<String, Node> getNormalMap() {
        return normalMap;
    }

    public Map<String, Node> getMutationMap() {
        return mutationMap;
    }

    public double getNormalAdapt() {
        return normalAdapt;
    }

    public Node getNode(String name) {
        return graph.getNode(name);
    }



}
