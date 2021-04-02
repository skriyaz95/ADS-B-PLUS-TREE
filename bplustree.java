import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        this.internalNodeMinimumDegree = degree;
        this.internalNodeMinimumDegree = (int) Math.ceil(degree / 2);
        this.maximumDataInLeafNode = degree - 1;
        this.minimumDataInLeafNode = (int) Math.ceil((degree / 2) - 1);
        this.midPointIndex = (int)Math.ceil((degree + 1) / 2) - 1;
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
        private List listOfData = new ArrayList<Data>();

        public LeafNode() {
            this.numberOfPairs = 0;
            leftSibling = null;
            leftSibling = null;
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

        public List getListOfData() {
            return listOfData;
        }

        public void setListOfData(List listOfData) {
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
    }

    class InternalNode {
        private int maximumDegree;
        private int minimumDegree;
        private int degree;
        private InternalNode leftSibling;
        private InternalNode rightSibling;
        private InternalNode parentNode;
        private ArrayList<Integer> listOfKeys = new ArrayList();
        private ArrayList listOfChildren = new ArrayList();

        public int getMaximumDegree() {
            return maximumDegree;
        }

        public void setMaximumDegree(int maximumDegree) {
            this.maximumDegree = maximumDegree;
        }

        public int getMinimumDegree() {
            return minimumDegree;
        }

        public void setMinimumDegree(int minimumDegree) {
            this.minimumDegree = minimumDegree;
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


            }
        } else {
            LeafNode leafNode = new LeafNode();
            leafNode.insertData(maximumDataInLeafNode, data);
            firstLeafNode = leafNode;
        }
    }

    public LeafNode getLeafNode(InternalNode node, int key) {
        ArrayList<Integer> keys = root.getListOfKeys();
        int index = 0;

        while (index < node.getDegree() - 1) {
            if (keys.get(index) < key) {
                break;
            }
            index++;
        }

        Object child = root.getListOfChildren().get(index);
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
