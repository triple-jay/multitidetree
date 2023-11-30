package multitypetree.evolution.tree;

import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.evolution.tree.Tree;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Class to initialise a MultiTypeTree from a " +
        "BEAST tree in which single-child nodes represent " +
        "type changes.")
public class MultiTypeTreeFromFlatTree extends MultiTypeTree {

    public Input<Tree> flatTreeInput = new Input<>(
            "flatTree",
            "Flat representation of multi-type tree.",
            Input.Validate.REQUIRED);

    @Override
    public void initAndValidate() {
        super.initAndValidate();

        initFromFlatTree(flatTreeInput.get(), true);
    }
}
