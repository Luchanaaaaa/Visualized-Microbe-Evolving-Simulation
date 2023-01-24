import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;
import java.util.stream.Stream;

public class Test {

    public static void main(String[] args) {

    System.setProperty("org.graphstream.ui","swing");
        Graph graph =new SingleGraph("");
        graph.clear();
        graph.addNode("v1");
        for (int i = 2; i <= 40; i++) {

            String name = "v1 " + "_" + "v" + i + "";
            graph.addNode(("v" + i));

            graph.addEdge(name, ("v1"), ("v" + i), true);


        }
        graph.getNode("v5").setAttribute("ui.style", "fill-color: red;");

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


            graph.display();
        }

}
