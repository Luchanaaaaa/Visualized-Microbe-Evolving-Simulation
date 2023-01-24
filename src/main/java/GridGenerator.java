import org.graphstream.algorithm.generator.BaseGenerator;
import org.graphstream.graph.Graph;

public class GridGenerator extends BaseGenerator {
    protected boolean cross;
    protected boolean tore;
    protected boolean generateXY;
    protected int currentSize;
    protected int edgeNames;

    public GridGenerator() {
        this(false, false);
    }

    public GridGenerator(boolean cross, boolean tore) {
        this(cross, tore, false);
    }

    public GridGenerator(boolean cross, boolean tore, boolean generateXY) {
        this(cross, tore, false, false);
    }

    public GridGenerator(boolean cross, boolean tore, boolean generateXY, boolean directed) {
        this.cross = false;
        this.tore = false;
        this.generateXY = true;
        this.currentSize = 0;
        this.edgeNames = 0;
        this.cross = cross;
        this.tore = tore;
        this.generateXY = generateXY;
        this.directed = directed;
    }

    public void begin(Graph graph) {
        String id = this.nodeName(0, 0);
        this.addNode(id, 0.0D, 0.0D);
        graph.getNode(id).setAttribute("ui.label", id);
    }

    @Override
    public void begin() {

    }

    @Override
    public boolean nextEvents() {
        return false;
    }

    public boolean nextEvents(Graph graph) {
        ++this.currentSize;
        int x;
        String id;
        for (x = 0; x < this.currentSize; ++x) {
            id = this.nodeName(this.currentSize, x);

            this.addNode(id, (double) this.currentSize, (double) x);
            graph.getNode(id).setAttribute("ui.label", id);
            this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize - 1, x), id);
            this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(this.currentSize - 1, x));
            if (x > 0) {
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, x - 1), id);
                this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(this.currentSize, x - 1));

                if (this.cross) {
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize - 1, x - 1), id);
                    this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(this.currentSize - 1, x - 1));

                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize - 1, x), this.nodeName(this.currentSize, x - 1));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, x - 1), this.nodeName(this.currentSize - 1, x));
                }
            }
        }

        for (x = 0; x <= this.currentSize; ++x) {
            id = this.nodeName(x, this.currentSize);
            this.addNode(id, (double) x, (double) this.currentSize);
            graph.getNode(id).setAttribute("ui.label", id);
            this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, this.currentSize - 1), id);
            this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(x, this.currentSize - 1));
            if (x > 0) {
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x - 1, this.currentSize), id);
                this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(x - 1, this.currentSize));
                if (this.cross) {

                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x - 1, this.currentSize - 1), id);
                    this.addEdge(Integer.toString(this.edgeNames++), id, this.nodeName(x - 1, this.currentSize - 1));

                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x - 1, this.currentSize), this.nodeName(x, this.currentSize - 1));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, this.currentSize - 1), this.nodeName(x - 1, this.currentSize));

                }
            }
        }

        return true;
    }

    public void end() {
        if (this.tore && this.currentSize > 0) {
            int x;
            for (x = 0; x <= this.currentSize; ++x) {
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, x), this.nodeName(0, x));
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(0, x), this.nodeName(this.currentSize, x));
                if (this.cross && x > 0) {
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, x), this.nodeName(0, x - 1));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(0, x - 1), this.nodeName(this.currentSize, x));

                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, x - 1), this.nodeName(0, x));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(0, x), this.nodeName(this.currentSize, x - 1));
                }
            }

            for (x = 0; x <= this.currentSize; ++x) {
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, this.currentSize), this.nodeName(x, 0));
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, 0), this.nodeName(x, this.currentSize));

                if (this.cross && x > 0) {
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, this.currentSize), this.nodeName(x - 1, 0));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x - 1, 0), this.nodeName(x, this.currentSize));

                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x - 1, this.currentSize), this.nodeName(x, 0));
                    this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(x, 0), this.nodeName(x - 1, this.currentSize));

                }
            }

            if (this.cross) {
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, 0), this.nodeName(0, this.currentSize));
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(0, this.currentSize), this.nodeName(this.currentSize, 0));

                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(0, 0), this.nodeName(this.currentSize, this.currentSize));
                this.addEdge(Integer.toString(this.edgeNames++), this.nodeName(this.currentSize, this.currentSize), this.nodeName(0, 0));
            }
        }

        super.end();
    }

    protected String nodeName(int x, int y) {
        return "v" + x +"_"+ y;
    }


}
