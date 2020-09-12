package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.model.BaseModel;

import java.util.ArrayList;

public class Navigation {

    public enum TRANSITION_ANIMATION {
        NONE,
        FADE,
        UPDOWN,
        DOWNUP,
        LEFTRIGHT,
        RIGHTLEFT
    }

    public static class Node <O extends BaseModel> extends BaseModel {

        static {
            BaseModel.Register(Node.class);
        }

        /** Class of object at this node */
        private Class nodeClass;

        /** The object at this node, has class nodeClass */
        private O nodeObject;

        /** Children are initialized if node is root */
        private ArrayList<Node<O>> children;

        /** If root is null, this field is ignored and the view is considered root */
        private boolean isRoot;

        public void setRoot(boolean root) {
            isRoot = root;
            if (!root) {
                children = null;
            }
            else if (children == null) {
                children = new ArrayList<>();
            }
        }

        public Class getNodeClass() {
            return nodeClass;
        }

        public void setNodeClass(Class nodeClass) {
            this.nodeClass = nodeClass;
        }

        public boolean isRoot() {
            return isRoot;
        }

        public ArrayList<Node<O>> getChildren() {
            return children;
        }

        public void setNodeObject(O nodeObject) {
            this.nodeObject = nodeObject;
            this.nodeClass = nodeObject.getClass();
        }

        public O getNodeObject() {
            return nodeObject;
        }
    }

    public static class Tree <O extends BaseModel> extends BaseModel {

        static {
            BaseModel.Register(Tree.class);
        }

        private ArrayList<Node<O>> nodes = new ArrayList<>();

        /** If true it will keep the object at every node in memory */
        private boolean keepNodesAlive = false;

        /** If keepNodesAlive = true the tree will nullify the earliest
         * as new nodes are added */
        //TODO: implement
        private int maxLiveNodes;

        /**
         * This is the current bottom most node
         */
        public Node<O> bottomNode() {
            if (nodes.size() == 0) return null;
            return nodes.get(nodes.size() - 1);
        }

        /**
         * If root is null, the view is assumed to be root
         */
        public Node<O> rootOf(Node node) {
            int index = nodes.indexOf(node);

            for (int i = nodes.size() - 1; 0 <= i && i <= index; i--) {
                Node root = nodes.get(i);
                if (root != node && root.isRoot) return root;
            }
            return null;
        }

        public ArrayList<Node<O>> getNodes() {
            return nodes;
        }

        public void popTo (Integer index) {

            ArrayList<Node<O>> arr = new ArrayList(nodes);
            for (int i = arr.size() - 1; 0 <= i && i <= index; i--) {
                nodes.remove(i);
            }
        }

        public void pop() {
            if (0 < nodes.size()) {
                nodes.remove(nodes.size() - 1);
            }
        }

        public boolean hasASingleNode() {
            return nodes.size() == 1;
        }

        public boolean hasNoNode() {
            return nodes.size() == 0;
        }
    }
}
