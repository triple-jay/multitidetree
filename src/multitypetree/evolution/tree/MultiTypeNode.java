/*
 * Copyright (C) 2013 Tim Vaughan <tgvaughan@gmail.com>
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
package multitypetree.evolution.tree;

import beast.base.core.Description;
import beast.base.evolution.tree.Node;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim Vaughan <tgvaughan@gmail.com>
 */
@Description("A node in a multi-type phylogenetic tree.")
public class MultiTypeNode extends Node {

    // Type metadata:
    int nTypeChanges = 0;
    List<Integer> changeTypes = new ArrayList<Integer>();
    List<Double> changeTimes = new ArrayList<Double>();
    int nodeType = 0;

    /**
     * Retrieve the total number of changes on the branch above this node.
     *
     * @return type change count
     */
    public int getChangeCount() {
        return nTypeChanges;
    }

    /**
     * Obtain destination type (reverse time) of the change specified by idx on
     * the branch between this node and its parent.
     *
     * @param idx
     * @return change type
     */
    public int getChangeType(int idx) {
        return changeTypes.get(idx);
    }

    /**
     * Obtain time of the change specified by idx on the branch between this
     * node and its parent.
     *
     * @param idx
     * @return time of change
     */
    public double getChangeTime(int idx) {
        return changeTimes.get(idx);
    }

    /**
     * Retrieve final type on branch above this node.
     *
     * @return final type
     */
    public int getFinalType() {
        if (nTypeChanges>0)
            return changeTypes.get(nTypeChanges-1);
        else
            return nodeType;
    }

    /**
     * Retrieve time of final type change on branch above this node, or the
     * height of the node if no changes exist.
     *
     * @return final type change time
     */
    public double getFinalChangeTime() {
        if (nTypeChanges>0)
            return changeTimes.get(nTypeChanges-1);
        else
            return getHeight();
    }

    /**
     * Obtain type value at this node. (Used for caching or for specifying type
     * changes at nodes.)
     *
     * @return type value at this node
     */
    public int getNodeType() {
        return nodeType;
    }

    /**
     * Sets type of node.
     *
     * @param nodeType New node type.
     */
    public void setNodeType(int nodeType) {
        startEditing();
        this.nodeType = nodeType;
    }

    /**
     * Add a new type change to branch above node.
     *
     * @param newType Destination (reverse time) type of change
     * @param time Time at which change occurs.
     */
    public void addChange(int newType, double time) {
        startEditing();
        changeTypes.add(newType);
        changeTimes.add(time);
        nTypeChanges += 1;
    }

    /**
     * Remove all type changes from branch above node.
     */
    public void clearChanges() {
        startEditing();
        changeTypes.clear();
        changeTimes.clear();
        nTypeChanges = 0;
    }

    /**
     * Change time at which type change idx occurs.
     *
     * @param idx Index of type-change to modify.
     * @param newTime New time of type-change.
     */
    public void setChangeTime(int idx, double newTime) {
        startEditing();
        changeTimes.set(idx, newTime);
    }

    /**
     * Change destination type of type change idx.
     *
     * @param idx Index of type-change to modify.
     * @param newType New destination type of type-change.
     */
    public void setChangeType(int idx, int newType) {
        startEditing();
        changeTypes.set(idx, newType);
    }

    /**
     * Truncate type change lists so that a maximum of newNChanges remain. Does
     * nothing if newNChanges>= the current value.
     *
     * @param newNChanges new change count.
     */
    public void truncateChanges(int newNChanges) {
        startEditing();

        while (nTypeChanges>newNChanges) {
            changeTypes.remove(nTypeChanges-1);
            changeTimes.remove(nTypeChanges-1);
            nTypeChanges -= 1;
        }
    }

    /**
     * Insert a new colour change at index idx to colour newColour at the
     * specified time.
     *
     * @param idx
     * @param newType
     * @param newTime
     */
    public void insertChange(int idx, int newType, double newTime) {
        startEditing();

        if (idx>nTypeChanges)
            throw new IllegalArgumentException("Index to insertChange() out of range.");

        changeTimes.add(idx, newTime);
        changeTypes.add(idx, newType);
        nTypeChanges += 1;
    }

    /**
     * Remove the colour change at index idx from the branch above node.
     *
     * @param idx
     */
    public void removeChange(int idx) {
        startEditing();

        if (idx>=nTypeChanges)
            throw new IllegalArgumentException("Index to removeChange() out of range.");

        changeTimes.remove(idx);
        changeTypes.remove(idx);
        nTypeChanges -= 1;

    }
    
    /**
     * @return shallow copy of node
     */
    public MultiTypeNode shallowCopy() {
        MultiTypeNode node = new MultiTypeNode();
        node.height = height;
        node.parent = parent;        
        node.children.addAll(children);

        node.nTypeChanges = nTypeChanges;
        node.changeTimes.addAll(changeTimes);
        node.changeTypes.addAll(changeTypes);
        node.nodeType = nodeType;
                
        node.labelNr = labelNr;
        node.metaDataString = metaDataString;
        node.ID = ID;

        return node;
    }
    
    /**
     * **************************
     * Methods ported from Node *
     ***************************
     */


    /**
     * @return (deep) copy of node
     */
    @Override
    public MultiTypeNode copy() {
        MultiTypeNode node = new MultiTypeNode();
        node.height = height;
        node.labelNr = labelNr;
        node.metaDataString = metaDataString;
        node.parent = null;
        node.ID = ID;
        node.nTypeChanges = nTypeChanges;
        node.changeTimes.addAll(changeTimes);
        node.changeTypes.addAll(changeTypes);
        node.nodeType = nodeType;
        if (getLeft()!=null) {
            node.setLeft(getLeft().copy());
            node.getLeft().setParent(node);
            if (getRight()!=null) {
                node.setRight(getRight().copy());
                node.getRight().setParent(node);
            }
        }
        return node;
    }

    /**
     * assign values from a tree in array representation *
     * @param nodes
     * @param node
     */
    @Override
    public void assignFrom(Node[] nodes, Node node) {
        height = node.getHeight();
        labelNr = node.getNr();
        metaDataString = node.metaDataString;
        parent = null;
        ID = node.getID();
        
        MultiTypeNode mtNode = (MultiTypeNode)node;
        nTypeChanges = mtNode.nTypeChanges;
        changeTimes.clear();
        changeTimes.addAll(mtNode.changeTimes);
        changeTypes.clear();
        changeTypes.addAll(mtNode.changeTypes);
        nodeType = mtNode.nodeType;
        
        if (node.getLeft()!=null) {
            setLeft(nodes[node.getLeft().getNr()]);
            getLeft().assignFrom(nodes, node.getLeft());
            getLeft().setParent(this);
            if (node.getRight()!=null) {
                setRight(nodes[node.getRight().getNr()]);
                getRight().assignFrom(nodes, node.getRight());
                getRight().setParent(this);
            }
        }
    }
}
