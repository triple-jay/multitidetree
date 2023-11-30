/*
 * Copyright (C) 2012 Tim Vaughan <tgvaughan@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package multitypetree.operators;

import beast.base.core.Description;
import beast.base.core.Input;
import beast.base.evolution.tree.Node;
import beast.base.util.Randomizer;
import multitypetree.evolution.tree.MultiTypeNode;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Subtree/branch exchange operator for coloured trees.  This"
        + " is the `random recolouring' variant where new branch colourings"
        + " are selected using an unconditioned random walk.  This will likely"
        + " be very inefficient as this operator requires recolouring of"
        + " two branches, meaning that the acceptance probability goes as"
        + " the _square_ of the inverse of the number of colours.")
public class TypedSubtreeExchangeRandom extends RandomRetypeOperator {
    
    public Input<Boolean> isNarrowInput = new Input<Boolean>("isNarrow",
            "Whether or not to use narrow exchange. (Default true.)", true);
    
    @Override
    public double proposal() {
        double logHR = 0.0;

        // Select source and destination nodes:
        
        Node srcNode, srcNodeParent, destNode, destNodeParent;
        if (isNarrowInput.get()) {
            
            // Narrow exchange selection:
            do {
                srcNode = mtTree.getNode(Randomizer.nextInt(mtTree.getNodeCount()));
            } while (srcNode.isRoot() || srcNode.getParent().isRoot());
            srcNodeParent = srcNode.getParent();            
            destNode = getOtherChild(srcNodeParent.getParent(), srcNodeParent);
            destNodeParent = destNode.getParent();
            
        } else {
            
            // Wide exchange selection:
            do {
                srcNode = mtTree.getNode(Randomizer.nextInt(mtTree.getNodeCount()));
            } while (srcNode.isRoot());
            srcNodeParent = srcNode.getParent();
            
            do {
                destNode = mtTree.getNode(Randomizer.nextInt(mtTree.getNodeCount()));
            } while(destNode == srcNode
                    || destNode.isRoot()
                    || destNode.getParent() == srcNode.getParent());
            destNodeParent = destNode.getParent();
        }
        
        // Reject outright if substitution would result in negative branch
        // lengths:
        if (destNode.getHeight()>srcNodeParent.getHeight()
                || srcNode.getHeight()>destNodeParent.getHeight())
            return Double.NEGATIVE_INFINITY;
        
        // Record probability of old colours:
        logHR += getBranchTypeProb(srcNode) + getBranchTypeProb(destNode);
        
        // Make changes to tree topology:
        replace(srcNodeParent, srcNode, destNode);
        replace(destNodeParent, destNode, srcNode);
        
        // Recolour branches involved:
        logHR -= retypeBranch(srcNode) + retypeBranch(destNode);
        
        // Force rejection if colouring inconsistent:
        if ((((MultiTypeNode)srcNode).getFinalType() != ((MultiTypeNode)destNodeParent).getNodeType())
                || (((MultiTypeNode)destNode).getFinalType() != ((MultiTypeNode)srcNodeParent).getNodeType()))
            return Double.NEGATIVE_INFINITY;
        
        return logHR;
    }    
    
}
