import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyThread implements Runnable {

    private MyGraphView myGraphView;

    private MyGraph myGraph;

    //突变体演变次数
    private int mutationEvolutionNumber = 0;

    //正常体演变次数
    private int normalEvolutionNumber = 0;

    public MyThread(MyGraphView myGraphView, MyGraph myGraph) {
        this.myGraphView = myGraphView;
        this.myGraph = myGraph;
    }


    public MyThread() {

    }

    //判断是否重置和暂停
    public void isReset() {
        if (myGraph.getMutationEvolutionNumber() == 0 && myGraph.getNormalEvolutionNumber() == 0
                || (mutationEvolutionNumber + normalEvolutionNumber) > myGraph.getTargetNumber()) {
            reset();
        }
        if (isOver()) {
            myGraph.setIsPause(true);
        }
    }

    //重置次数
    public void reset() {

        mutationEvolutionNumber = 0;
        normalEvolutionNumber = 0;

    }

    public void updateMutationEvolutionNumber() {
        mutationEvolutionNumber += 1;
    }

    //判断是否处于结束状态
    public boolean isOver() {
        if ((mutationEvolutionNumber + normalEvolutionNumber) == myGraph.getTargetNumber()) {
            return true;
        }
        return false;
    }

    private final Object lock = new Object();

    //线程阻塞
    public void onPause() {
        synchronized (lock) {
            try {
                lock.wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    //唤醒线程
    public void resumeThread(MyGraph myGraph) {
        myGraph.setIsPause(false);
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void run() {
        final long timeInterval = 1;
        Map<String, Node> mutationMap = myGraph.getMutationMap();
        Map<String, Node> normalMap = myGraph.getNormalMap();
        boolean normalOrMutation = false;

        Edge edge = null;
        Node opposite = null;
        while (true) {


            isReset();

            while (myGraph.getIsPause()) {
                onPause();
            }

            int nodeCount = myGraph.getGraph().getNodeCount();
            //判断是否全部演化成突变体
            if (mutationMap.size() == nodeCount) {
                mutationEvolutionNumber++;
                myGraph.setMutationEvolutionNumber(myGraph.getMutationEvolutionNumber() + 1);
                if (!isOver()) {
                    MyGraphUtil.start(myGraph);

                }
                mutationMap = myGraph.getMutationMap();
                normalMap = myGraph.getNormalMap();
                if (MyGraphView.showStructure == myGraph.getOneSelfGraph()) {
                    myGraphView.updateMessage(myGraph);
                    MyGraphView.updateStructureMessageLabel(myGraph);

                }


            }
            //判断是否全部演化成正常体
            if (normalMap.size() == nodeCount) {
                normalEvolutionNumber++;
                myGraph.setNormalEvolutionNumber(myGraph.getNormalEvolutionNumber() + 1);
                if (!isOver()) {
                    MyGraphUtil.start(myGraph);
                }

                mutationMap = myGraph.getMutationMap();
                normalMap = myGraph.getNormalMap();
                if (MyGraphView.showStructure == myGraph.getOneSelfGraph()) {
                    myGraphView.updateMessage(myGraph);
                    MyGraphView.updateStructureMessageLabel(myGraph);

                }

            }
            Node node2 = MyGraphUtil.variationChoice(myGraph);
            List<Edge> edgeList = node2.leavingEdges().collect(Collectors.toList());
            if (edgeList.size() <= 0) {
                continue;
            }
            //获取根据权重随机选择的边
            edge = MyGraphUtil.calculate(edgeList);
            //获取边连接的节点
            opposite = edge.getOpposite(node2);
            edge.setAttribute("ui.style", "fill-color: green;");
            //判断节点是否为变异节点
            if (null == normalMap.get(node2.getId())) {
                //判断指向的节点是否为正常体
                if (null != normalMap.get(opposite.getId())) {
                    //将节点在正常体的map里删除
                    normalMap.remove(opposite.getId());
                    //将节点保存到变异体的map中
                    mutationMap.put(opposite.getId(), opposite);

                }
                normalOrMutation = false;

                opposite.setAttribute("ui.style", "fill-color: blue;");
            } else {
                if (null != mutationMap.get(opposite.getId())) {
                    //将节点在变异体的map里删除
                    mutationMap.remove(opposite.getId());
                    //将节点保存到正常体的map中
                    normalMap.put(opposite.getId(), opposite);
                }
                normalOrMutation = true;
                opposite.setAttribute("ui.style", "fill-color: blue;");
            }

            // ------- code for task to run
            // ------- 要运行的任务代码
            // ------- ends here
            try {
                // sleep()：同步延迟数据，并且会阻塞线程
                Thread.sleep(timeInterval);
                if (null != opposite && null != edge) {
                    edge.setAttribute("ui.style", "fill-color: #444;");
                    if (normalOrMutation) {
                        opposite.setAttribute("ui.style", "fill-color: #444;");
                    } else {
                        opposite.setAttribute("ui.style", "fill-color: red;");
                    }

                    if (MyGraphView.showStructure == myGraph.getOneSelfGraph()) {
                        myGraphView.updateMessage(myGraph);

                    }
                }


                opposite = null;
                edge = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
