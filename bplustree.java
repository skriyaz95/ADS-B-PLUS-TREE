import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import static java.util.Collections.sort;

public class bplustree {

    public static final String INITIALIZE = "Initialize";
    public static final String INSERT = "Insert";
    public static final String DELETE = "Delete";
    public static final String Search = "Search";

    private int degree;
    private int internalNodeMinimumDegree;
    private int internalNodeMaximumDegree;
    private int minimumDataInLeafNode;
    private int maximumDataInLeafNode;
    private int midPointIndex;
    private InternalNode root;
    private LeafNode firstLeafNode;

    public bplustree(int degree) {
        this.degree = degree;
        this.root = null;
        this.internalNodeMaximumDegree = degree;
        this.internalNodeMinimumDegree = (int) Math.ceil(degree / 2);
        this.maximumDataInLeafNode = degree - 1;
        this.minimumDataInLeafNode = (int) Math.ceil((degree / 2) - 1);
        this.midPointIndex = (int) Math.ceil((degree + 1) / 2) - 1;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public InternalNode getRoot() {
        return root;
    }

    public void setRoot(InternalNode root) {
        this.root = root;
    }

    public LeafNode getFirstLeafNode() {
        return firstLeafNode;
    }

    public void setFirstLeafNode(LeafNode firstLeafNode) {
        this.firstLeafNode = firstLeafNode;
    }

    public int getInternalNodeMinimumDegree() {
        return internalNodeMinimumDegree;
    }

    public void setInternalNodeMinimumDegree(int internalNodeMinimumDegree) {
        this.internalNodeMinimumDegree = internalNodeMinimumDegree;
    }

    public int getInternalNodeMaximumDegree() {
        return internalNodeMaximumDegree;
    }

    public void setInternalNodeMaximumDegree(int internalNodeMaximumDegree) {
        this.internalNodeMaximumDegree = internalNodeMaximumDegree;
    }

    public int getMinimumDataInLeafNode() {
        return minimumDataInLeafNode;
    }

    public void setMinimumDataInLeafNode(int minimumDataInLeafNode) {
        this.minimumDataInLeafNode = minimumDataInLeafNode;
    }

    public int getMaximumDataInLeafNode() {
        return maximumDataInLeafNode;
    }

    public void setMaximumDataInLeafNode(int maximumDataInLeafNode) {
        this.maximumDataInLeafNode = maximumDataInLeafNode;
    }

    public int getMidPointIndex() {
        return midPointIndex;
    }

    public void setMidPointIndex(int midPointIndex) {
        this.midPointIndex = midPointIndex;
    }

    class Data {
        private int key;
        private double value;

        public Data(int key, double value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }

    public class DataComparator implements Comparator<Data> {
        @Override
        public int compare(Data d1, Data d2) {
            return Integer.compare(d1.getKey(), d2.getKey());
        }
    }

    class LeafNode {
        private int numberOfPairs;
        private InternalNode parent;
        private LeafNode leftSibling;
        private LeafNode rightSibling;
        private ArrayList<Data> listOfData = new ArrayList();

        public LeafNode() {
            this.numberOfPairs = 0;
            leftSibling = null;
            leftSibling = null;
        }

        public LeafNode(ArrayList<Data> dataList, InternalNode parent) {
            this.listOfData = dataList;
            this.numberOfPairs = dataList.size();
            this.parent = parent;
        }

        public int getNumberOfPairs() {
            return numberOfPairs;
        }

        public void setNumberOfPairs(int numberOfPairs) {
            this.numberOfPairs = numberOfPairs;
        }

        public LeafNode getLeftSibling() {
            return leftSibling;
        }

        public void setLeftSibling(LeafNode leftSibling) {
            this.leftSibling = leftSibling;
        }

        public LeafNode getRightSibling() {
            return rightSibling;
        }

        public void setRightSibling(LeafNode rightSibling) {
            this.rightSibling = rightSibling;
        }

        public ArrayList<Data> getListOfData() {
            return listOfData;
        }

        public void setListOfData(ArrayList<Data> listOfData) {
            this.listOfData = listOfData;
        }

        public InternalNode getParent() {
            return parent;
        }

        public void setParent(InternalNode parent) {
            this.parent = parent;
        }

        public boolean insertData(int leafNodeMaximumPairs, Data data) {
            boolean isInserted = false;

            if (leafNodeMaximumPairs > numberOfPairs) {
                listOfData.add(data);
                numberOfPairs++;
                sortData();
                isInserted = true;
            }

            return isInserted;
        }

        public void sortData() {
            sort(listOfData, new DataComparator());
        }

        public ArrayList<Data> splitDataList(int midPointIndex) {
            ArrayList<Data> secondHalfDataList = new ArrayList();

            for (int i = midPointIndex; i < listOfData.size(); i++) {
                secondHalfDataList.add(listOfData.get(i));
                //listOfData.remove(i);
            }

            for (int i = 0; i < secondHalfDataList.size(); i++) {
                if (secondHalfDataList.get(i).getKey() == listOfData.get(midPointIndex).getKey()) {
                    listOfData.remove(midPointIndex);
                }
            }
            numberOfPairs = listOfData.size();
            return secondHalfDataList;
        }
    }

    class InternalNode {
        private int degree;
        private InternalNode leftSibling;
        private InternalNode rightSibling;
        private InternalNode parentNode;
        private ArrayList<Integer> listOfKeys = new ArrayList();
        private ArrayList listOfChildren = new ArrayList();

        public InternalNode(ArrayList<Integer> keys) {
            this.degree = 0;
            this.listOfKeys = keys;
        }

        public InternalNode(ArrayList<Integer> secondHalfKeysList, ArrayList secondHalfPointersList) {
            this.degree = secondHalfPointersList.size();
            this.listOfKeys = secondHalfKeysList;
            this.listOfChildren = secondHalfPointersList;
        }

        public int getDegree() {
            return degree;
        }

        public void setDegree(int degree) {
            this.degree = degree;
        }

        public InternalNode getLeftSibling() {
            return leftSibling;
        }

        public void setLeftSibling(InternalNode leftSibling) {
            this.leftSibling = leftSibling;
        }

        public InternalNode getRightSibling() {
            return rightSibling;
        }

        public void setRightSibling(InternalNode rightSibling) {
            this.rightSibling = rightSibling;
        }

        public InternalNode getParentNode() {
            return parentNode;
        }

        public void setParentNode(InternalNode parentNode) {
            this.parentNode = parentNode;
        }

        public ArrayList<Integer> getListOfKeys() {
            return listOfKeys;
        }

        public void setListOfKeys(ArrayList<Integer> listOfKeys) {
            this.listOfKeys = listOfKeys;
        }

        public ArrayList getListOfChildren() {
            return listOfChildren;
        }

        public void setListOfChildren(ArrayList listOfChildren) {
            this.listOfChildren = listOfChildren;
        }

        public void sortKeys() {
            sort(listOfKeys);
        }

        public void addChildPointer(Object node) {
            listOfChildren.add(node);
            degree++;
        }

        public void addChildPointer(Object node, int index) {
           /* int childrenListInitialSize = listOfChildren.size();
            for (int i = degree - 1; i >= index; i--) {
                listOfChildren.add(i + 1, listOfChildren.get(i));
            }
            if (index >= listOfChildren.size()) {
                int initialSize = listOfChildren.size();
                for (int i = initialSize; i <= index; i++) {
                    if (i == index) {
                        listOfChildren.add(node);
                    } else {
                        listOfChildren.add(null);
                    }
                }
            } else {
                listOfChildren.set(index, node);
            }*/
            listOfChildren.add(index, node);
            degree++;
        }

        public int findChildIndex(Object node) {
            int index = 0;
            while (index < listOfChildren.size()) {
                if (listOfChildren.get(index).equals(node)) {
                    return index;
                }
                index++;
            }

            return -1;
        }

        public ArrayList<Integer> splitKeys(int midPointIndex) {
            ArrayList<Integer> secondHalfKeysList = new ArrayList();

            listOfKeys.remove(midPointIndex);
            for (int i = midPointIndex; i < listOfKeys.size(); i++) {
                secondHalfKeysList.add(listOfKeys.get(i));
                // listOfKeys.remove(i);
            }

            for (int i = 0; i < secondHalfKeysList.size(); i++) {
                if (secondHalfKeysList.get(i) == listOfKeys.get(midPointIndex)) {
                    listOfKeys.remove(midPointIndex);
                }
            }

            // numberOfPairs = listOfData.size();
            return secondHalfKeysList;
        }

        public ArrayList splitChildPointers(int midPointIndex) {
            ArrayList secondHalfChildren = new ArrayList();

            for (int i = midPointIndex + 1; i < listOfChildren.size(); i++) {
                secondHalfChildren.add(listOfChildren.get(i));
            }

            for (int i = 0; i < secondHalfChildren.size(); i++) {
                listOfChildren.remove(midPointIndex + 1);
                degree--;
            }

            return secondHalfChildren;
        }
    }

    public void insert(int key, double value) {
        Data data = new Data(key, value);
        if (firstLeafNode != null) {
            LeafNode lastNode = null;
            lastNode = root != null ? getLeafNode(root, key) : firstLeafNode;
            boolean isInserted = lastNode.insertData(maximumDataInLeafNode, data);
            if (!isInserted) {
                lastNode.getListOfData().add(data);
                lastNode.setNumberOfPairs(lastNode.getNumberOfPairs() + 1);
                lastNode.sortData();

                ArrayList<Data> secondHalfDataList = lastNode.splitDataList(midPointIndex);

                if (lastNode.getParent() != null) {
                    int newParentKey = secondHalfDataList.get(0).getKey();
                    //int parentKeysSize = lastNode.getParent().getListOfKeys().size();
                    lastNode.getParent().getListOfKeys().add(lastNode.getParent().getDegree() - 1, newParentKey);
                    /*for (int i = parentKeysSize; i <= lastNode.getParent().getDegree() - 1; i++) {
                        if (i == lastNode.getParent().getDegree() - 1) {
                            lastNode.getParent().getListOfKeys().add(newParentKey);
                        } else {
                            lastNode.getParent().getListOfKeys().add(null);
                        }
                    }*/
                    //lastNode.getParent().getListOfKeys().set(lastNode.getParent().getDegree() - 1, newParentKey);
                    lastNode.getParent().sortKeys();
                } else {
                    ArrayList<Integer> parentKeys = new ArrayList();
                    parentKeys.add(secondHalfDataList.get(0).getKey());
                    InternalNode parent = new InternalNode(parentKeys);
                    parent.addChildPointer(lastNode);
                    lastNode.setParent(parent);
                }

                LeafNode leafNode = new LeafNode(secondHalfDataList, lastNode.getParent());
                int childPointerIndex = lastNode.getParent().findChildIndex(lastNode) + 1;
                lastNode.getParent().addChildPointer(leafNode, childPointerIndex);

                leafNode.setRightSibling(lastNode.getRightSibling());
                if (leafNode.getRightSibling() != null) {
                    leafNode.getRightSibling().setLeftSibling(leafNode);
                }
                lastNode.setRightSibling(leafNode);
                leafNode.setLeftSibling(lastNode);
                if (root != null) {
                    InternalNode internalNode = lastNode.getParent();
                    while (internalNode != null) {
                        if (internalNode.getDegree() == internalNodeMaximumDegree + 1) {
                            splitInternalNode(midPointIndex, internalNode);
                        } else {
                            break;
                        }
                    }
                } else {
                    root = lastNode.getParent();
                }
            }
        } else {
            LeafNode leafNode = new LeafNode();
            leafNode.insertData(maximumDataInLeafNode, data);
            firstLeafNode = leafNode;
        }
    }

    public void splitInternalNode(int midPointIndex, InternalNode internalNode) {
        InternalNode parent = internalNode.getParentNode();
        int newParentKey = internalNode.getListOfKeys().get(midPointIndex);
        ArrayList<Integer> secondHalfKeysList = internalNode.splitKeys(midPointIndex);
        ArrayList secondHalfPointersList = internalNode.splitChildPointers(midPointIndex);
        internalNode.setDegree(internalNode.getListOfChildren().size());

        InternalNode sibling = new InternalNode(secondHalfKeysList, secondHalfPointersList);
        for (Object childPointer : secondHalfPointersList) {
            if (childPointer != null) {
                if (childPointer instanceof InternalNode) {
                    InternalNode node = (InternalNode) childPointer;
                    node.setParentNode(sibling);
                } else {
                    LeafNode leafNode = (LeafNode) childPointer;
                    leafNode.setParent(sibling);
                }
            }
        }

        sibling.setRightSibling(internalNode.getRightSibling());
        if (sibling.getRightSibling() != null) {
            sibling.getRightSibling().setLeftSibling(sibling);
        }
        internalNode.setRightSibling(sibling);
        sibling.setLeftSibling(internalNode);

        if (parent != null) {
            parent.getListOfKeys().add(newParentKey);
            parent.sortKeys();

            int childPointerIndex = parent.findChildIndex(internalNode) + 1;
            parent.addChildPointer(sibling, childPointerIndex);
            sibling.setParentNode(parent);
        } else {
            ArrayList<Integer> keysList = new ArrayList();
            keysList.add(newParentKey);
            InternalNode newRoot = new InternalNode(keysList);
            newRoot.addChildPointer(internalNode);
            newRoot.addChildPointer(sibling);
            root = newRoot;

            internalNode.setParentNode(root);
            sibling.setParentNode(root);
        }
    }

    public LeafNode getLeafNode(InternalNode node, int key) {
        ArrayList<Integer> keys = node.getListOfKeys();
        int index = 0;

        while (index < node.getDegree() - 1) {
            if (keys.get(index) > key) {
                break;
            }
            index++;
        }

        Object child = node.getListOfChildren().get(index);
        return child instanceof InternalNode ? getLeafNode((InternalNode) child, key) : (LeafNode) child;
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            BufferedReader bufferedReader = null;
            String line = null;
            bplustree bplustree = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                while ((line = bufferedReader.readLine()) != null) {
                    String[] tokens = line.trim().split("\\(|,|\\)");
                    String option = tokens[0];
                    if (option.equalsIgnoreCase(INITIALIZE)) {
                        bplustree = new bplustree(Integer.parseInt(tokens[1]));
                    } else if (option.equalsIgnoreCase(INSERT)) {
                        bplustree.insert(Integer.parseInt(tokens[1]), Double.parseDouble(tokens[2]));
                    } else if (option.equalsIgnoreCase(Search)) {

                    } else if (option.equalsIgnoreCase(DELETE)) {

                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("Error occured while reading input file or writing output file - " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Error occured while performing B-Plus Tree operations - " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("usage: java bplustree <input file name>");
        }
    }
}
