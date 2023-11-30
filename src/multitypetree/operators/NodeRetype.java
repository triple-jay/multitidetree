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
import beast.base.evolution.tree.Node;
import beast.base.util.Randomizer;
import multitypetree.evolution.tree.MultiTypeNode;

/**
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("Retypes a randomly chosen node and its attached branches. "
        + "This variant uses the uniformization branch retyping procedure.")
public class NodeRetype extends UniformizationRetypeOperator {
    
    @Override
    public double proposal() {
        double logHR = 0.0;
        
        // Select node:
        Node node = mtTree.getNode(mtTree.getLeafNodeCount()
                + Randomizer.nextInt(mtTree.getInternalNodeCount()));
        
        // Record probability of current types along attached branches:
        if (!node.isRoot())
            logHR += getBranchTypeProb(node);

        logHR += getBranchTypeProb(node.getLeft())
                + getBranchTypeProb(node.getRight());
        
        // Select new node type:
        ((MultiTypeNode)node).setNodeType(
            Randomizer.nextInt(migModel.getNTypes()));
        
        // Retype attached branches:
        try {
            if (!node.isRoot())
                logHR -= retypeBranch(node);

            logHR -= retypeBranch(node.getLeft())
                    + retypeBranch(node.getRight());
        } catch (NoValidPathException e) {
            return Double.NEGATIVE_INFINITY;
        }
        
        
        return logHR;
    }
    
}
