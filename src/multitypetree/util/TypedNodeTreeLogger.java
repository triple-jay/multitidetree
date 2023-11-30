package multitypetree.util;

import beast.base.core.BEASTObject;
import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.core.Loggable;
import beast.base.evolution.tree.Node;
import multitypetree.evolution.tree.MultiTypeNode;
import multitypetree.evolution.tree.MultiTypeTree;

import java.io.PrintStream;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
public class TypedNodeTreeLogger extends BEASTObject implements Loggable {

    public Input<MultiTypeTree> multiTypeTreeInput = new Input<>(
            "multiTypeTree",
            "Multi-type tree to log.",
            Input.Validate.REQUIRED);

    MultiTypeTree mtTree;

    @Override
    public void initAndValidate() {
        mtTree = multiTypeTreeInput.get();
    }

    @Override
    public void init(PrintStream out) {
        mtTree.init(out);
    }

    @Override
    public void log(long nSample, PrintStream out) {

        // Set up metadata string
        for (Node node : mtTree.getNodesAsArray()) {
            MultiTypeNode mtNode = (MultiTypeNode)node;
            mtNode.metaDataString = mtTree.getTypeLabel()
                    + "=\""
                    + mtTree.getTypeSet().getTypeName(mtNode.getNodeType())
                    + "\"";
        }

        out.print("tree STATE_" + nSample + " = ");
        out.print(mtTree.getRoot().toSortedNewick(new int[1], true));
        out.print(";");
    }

    @Override
    public void close(PrintStream out) {
        mtTree.close(out);
    }
}
