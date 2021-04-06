import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.sort;

/**
 * B+ tree Implementation. The primary value of a B+ tree is in storing data for
 * efficient retrieval in a block-oriented storage context —in particular,
 * filesystems
 * <p>
 * Note that this implementation is not synchronized.
 *
 * @author Riyaz Shaik
 */
@SuppressWarnings("unchecked")
public class bplustree {
    public static Logger writer = Logger.getLogger(bplustree.class.getName());

    //Operations Supported in B+ tree
    public static final String INITIALIZE = "Initialize";
    public static final String INSERT = "Insert";
    public static final String DELETE = "Delete";
    public static final String SEARCH = "Search";

    public static final String NULL_VALUE = "Null";

    //Degree of the B+ tree
    private int degree;
    //The Minimum Number of Child Pointers to be present in Internal Node
    private int internalNodeMinimumDegree;
    //The Maximum Number of Child Pointers to be present in Internal Node
    private int internalNodeMaximumDegree;
    //The Minimum Number of Key Value Pairs to be present in Leaf Node
    private int minimumDataInLeafNode;
    //The Maximum Number of Key Value Pairs to be present in Leaf Node
    private int maximumDataInLeafNode;
    //The Index at which Keys of Internal Node and Key Value pairs in Leaf Node are to be split
    private int midPointIndex;
    //The root of the B+ Tree
    private InternalNode root;
    //The Initial Leaf Node Used as root when there are no Internal Nodes
    private LeafNode firstLeafNode;

    /**
     * Constructs an empty B+Tree with degree provided and initializes all the properties needed to perform operations.
     *
     * @param degree The degree of B+ Tree. Normally an integer greater than 2.
     */
    public bplustree(int degree) {
        this.degree = degree;
        this.root = null;
        this.internalNodeMaximumDegree = degree;
        this.internalNodeMinimumDegree = (int) Math.ceil((double) degree / (double) 2);
        this.maximumDataInLeafNode = degree - 1;
        this.minimumDataInLeafNode = (int) Math.ceil((double) degree / (double) 2) - 1;
        this.midPointIndex = (int) Math.ceil((double) (degree + 1) / (double) 2) - 1;
    }

    /**
     * Getter Method to get degree of the B+ Tree.
     *
     * @return degree of B+ tree
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Setter Method to set degree of the B+ Tree.
     *
     * @param degree of B+ tree
     */
    public void setDegree(int degree) {
        this.degree = degree;
    }

    /**
     * Getter Method to get root of the B+ Tree.
     *
     * @return root of B+ tree
     */
    public InternalNode getRoot() {
        return root;
    }

    /**
     * Setter Method to set root of the B+ Tree.
     *
     * @param root of B+ tree
     */
    public void setRoot(InternalNode root) {
        this.root = root;
    }

    /**
     * Getter Method to get firstLeafNode of the B+ Tree.
     *
     * @return firstLeafNode of B+ tree
     */
    public LeafNode getFirstLeafNode() {
        return firstLeafNode;
    }

    /**
     * Setter Method to set firstLeafNode of the B+ Tree.
     *
     * @param firstLeafNode of B+ tree
     */
    public void setFirstLeafNode(LeafNode firstLeafNode) {
        this.firstLeafNode = firstLeafNode;
    }

    /**
     * Getter Method to get the value of minimum number of child pointers of Internal Node of the B+ Tree.
     *
     * @return minimum number of child pointers of Internal Node of B+ tree
     */
    public int getInternalNodeMinimumDegree() {
        return internalNodeMinimumDegree;
    }

    /**
     * Setter Method to set the value of minimum number of child pointers of Internal Node of the B+ Tree.
     *
     * @param internalNodeMinimumDegree - minimum number of child pointers of Internal Node of B+ tree
     */
    public void setInternalNodeMinimumDegree(int internalNodeMinimumDegree) {
        this.internalNodeMinimumDegree = internalNodeMinimumDegree;
    }

    /**
     * Getter Method to get the value of maximum number of child pointers of Internal Node of the B+ Tree.
     *
     * @return maximum number of child pointers of Internal Node of B+ tree
     */
    public int getInternalNodeMaximumDegree() {
        return internalNodeMaximumDegree;
    }

    /**
     * Setter Method to set the value of maximum number of child pointers of Internal Node of the B+ Tree.
     *
     * @param internalNodeMaximumDegree -  maximum number of child pointers of Internal Node of B+ tree
     */
    public void setInternalNodeMaximumDegree(int internalNodeMaximumDegree) {
        this.internalNodeMaximumDegree = internalNodeMaximumDegree;
    }

    /**
     * Getter Method to get the value of minimum number of key value pairs in Leaf Node of the B+ Tree.
     *
     * @return minimum number of key value pairs in Leaf Node of B+ tree
     */
    public int getMinimumDataInLeafNode() {
        return minimumDataInLeafNode;
    }

    /**
     * Setter Method to set the value of minimum number of key value pairs in Leaf Node of the B+ Tree.
     *
     * @param minimumDataInLeafNode -  minimum number of key value pairs in Leaf Node of B+ tree
     */
    public void setMinimumDataInLeafNode(int minimumDataInLeafNode) {
        this.minimumDataInLeafNode = minimumDataInLeafNode;
    }

    /**
     * Getter Method to get the value of maximum number of key value pairs in Leaf Node of the B+ Tree.
     *
     * @return maximum number of key value pairs in Leaf Node of B+ tree
     */
    public int getMaximumDataInLeafNode() {
        return maximumDataInLeafNode;
    }

    /**
     * Setter Method to set the value of maximum number of key value pairs in Leaf Node of the B+ Tree.
     *
     * @param maximumDataInLeafNode - maximum number of key value pairs in Leaf Node of B+ tree
     */
    public void setMaximumDataInLeafNode(int maximumDataInLeafNode) {
        this.maximumDataInLeafNode = maximumDataInLeafNode;
    }

    /**
     * Getter Method to get the value of Index at which Keys of Internal Node and Key Value pairs in Leaf Node are to be split in the B+ Tree.
     *
     * @return index at which Keys of Internal Node and Key Value pairs in Leaf Node are to be split in the B+ Tree
     */
    public int getMidPointIndex() {
        return midPointIndex;
    }

    /**
     * Setter Method to set the value of Index at which Keys of Internal Node and Key Value pairs in Leaf Node are to be split in the B+ Tree.
     *
     * @param midPointIndex - index at which Keys of Internal Node and Key Value pairs in Leaf Node are to be split in the B+ Tree
     */
    public void setMidPointIndex(int midPointIndex) {
        this.midPointIndex = midPointIndex;
    }

    /**
     * Nested class to hold B+ tree node key value pair.
     *
     * @author Riyaz Shaik
     */
    public class Data {
        // The key of the key value pair
        private int key;
        // The value of the key value pair
        private double value;

        /**
         * Constructs a Data Instance with key value pairs provided in the params.
         *
         * @param key   - The key of the key value pair.
         * @param value - The value of the key value pair.
         */
        public Data(int key, double value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Getter Method to get key of the Key Value pair.
         *
         * @return key of key value pair
         */
        public int getKey() {
            return key;
        }

        /**
         * Setter Method to set key of the Key Value pair.
         *
         * @param key - key of key value pair
         */
        public void setKey(int key) {
            this.key = key;
        }

        /**
         * Getter Method to get value of the Key Value pair.
         *
         * @return value of key value pair
         */
        public double getValue() {
            return value;
        }

        /**
         * Setter Method to set value of the Key Value pair.
         *
         * @param value - value of key value pair
         */
        public void setValue(double value) {
            this.value = value;
        }
    }

    /**
     * Nested class to sort key value pairs in the increasing order of keys.
     *
     * @author Riyaz Shaik
     */
    public class DataComparator implements Comparator<Data> {

        /**
         * Overriding Compare method of Comparator Interface.
         * Compares its two arguments for order.  Returns a negative integer,
         * zero, or a positive integer as the first key value pair is less than, equal
         * to, or greater than the second.
         *
         * @param d1 the first key value pair to be compared.
         * @param d2 the second key value pair to be compared.
         * @return a negative integer, zero, or a positive integer as the
         * first argument is less than, equal to, or greater than the
         * second.
         */
        @Override
        public int compare(Data d1, Data d2) {
            return Integer.compare(d1.getKey(), d2.getKey());
        }
    }

    /**
     * Nested class to represant Leaf Node of a B+ Tree.
     * Contains Internal Node as parent and left, right sibling leaf nodes forming a doubly liked list of leaf nodes
     * and list of key value pairs stored by the leaf node
     *
     * @author Riyaz Shaik
     */
    public class LeafNode {
        //Number of Key Value pairs in the Leaf Node
        private int numberOfPairs;
        //The parent(Internal Node) of the Leaf Node
        private InternalNode parent;
        //The left sibling in the DLL of the Leaf Node
        private LeafNode leftSibling;
        //The right sibling in the DLL of the Leaf Node
        private LeafNode rightSibling;
        //The list of key value pairs stored by the Leaf Node
        private ArrayList<Data> listOfData = new ArrayList();

        /**
         * Constructs a Leaf Node Instance having no key value pairs
         * and no left and right sibling
         */
        public LeafNode() {
            this.numberOfPairs = 0;
            leftSibling = null;
            leftSibling = null;
        }

        /**
         * Constructs a Leaf Node Instance having key value pairs provided in the method arguments
         * and with parent value provided in the method arguments
         *
         * @param dataList - The list of key value pairs
         * @param parent   - The parent of the Leaf Node
         */
        public LeafNode(ArrayList<Data> dataList, InternalNode parent) {
            this.listOfData = dataList;
            this.numberOfPairs = dataList.size();
            this.parent = parent;
        }

        /**
         * Getter Method to get number of Key Value pairs in Leaf Node.
         *
         * @return number of Key Value pairs in Leaf Node
         */
        public int getNumberOfPairs() {
            return numberOfPairs;
        }

        /**
         * Setter Method to set number of Key Value pairs in Leaf Node.
         *
         * @param numberOfPairs - number of Key Value pairs in Leaf Node
         */
        public void setNumberOfPairs(int numberOfPairs) {
            this.numberOfPairs = numberOfPairs;
        }

        /**
         * Getter Method to get left sibling in DLL of Leaf Node.
         *
         * @return left sibling in DLL of Leaf Node
         */
        public LeafNode getLeftSibling() {
            return leftSibling;
        }

        /**
         * Setter Method to set left sibling in DLL of Leaf Node.
         *
         * @param leftSibling - left sibling in DLL of Leaf Node
         */
        public void setLeftSibling(LeafNode leftSibling) {
            this.leftSibling = leftSibling;
        }

        /**
         * Getter Method to get right sibling in DLL of Leaf Node.
         *
         * @return right sibling in DLL of Leaf Node
         */
        public LeafNode getRightSibling() {
            return rightSibling;
        }

        /**
         * Setter Method to set right sibling in DLL of Leaf Node.
         *
         * @param rightSibling - right sibling in DLL of Leaf Node
         */
        public void setRightSibling(LeafNode rightSibling) {
            this.rightSibling = rightSibling;
        }

        /**
         * Getter Method to get list of key value pairs of Leaf Node.
         *
         * @return list of key value pairs of Leaf Node
         */
        public ArrayList<Data> getListOfData() {
            return listOfData;
        }

        /**
         * Setter Method to set list of key value pairs of Leaf Node.
         *
         * @param listOfData - list of key value pairs of Leaf Node
         */
        public void setListOfData(ArrayList<Data> listOfData) {
            this.listOfData = listOfData;
        }

        /**
         * Getter Method to get parent of Leaf Node.
         *
         * @return parent (Internal Node) of Leaf Node
         */
        public InternalNode getParent() {
            return parent;
        }

        /**
         * Setter Method to set parent of Leaf Node.
         *
         * @param parent - parent (Internal Node) of Leaf Node
         */
        public void setParent(InternalNode parent) {
            this.parent = parent;
        }

        /**
         * This method adds new key value pair (Data) to Leaf Node.
         * It checks whether the current list of Key Value pairs size is not greater than maximum
         * number of key value pairs that a leaf node can hold
         * 1. It inserts if the size of list < maximum number of key value pairs of leaf node can have and returns true
         * 2. If not it doesnt insert and returns false
         *
         * @param leafNodeMaximumPairs - The maximum number of key value pairs a leaf node can have.
         * @param data                 - The Key value pair(represented in stored in Data class).
         * @return true - insertion successful, false - insertion unsuccessful.
         */
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

        /**
         * This method is used to sort key value pairs in increasing order of keys.
         * It uses DataComparator class for this purpose.
         */
        public void sortData() {
            sort(listOfData, new DataComparator());
        }

        /**
         * This method is used to split the list of key value pairs on a
         * midpoint index into 2 seperate lists of key value pairs.
         * <p>
         * It returns back the list containing the second half of key value pairs
         * and removes those elements from the original list.
         *
         * @param midPointIndex - The index on which the list of key value pairs are to be split
         * @return The list containing the second half of key value pairs
         */
        public ArrayList<Data> splitDataList(int midPointIndex) {
            ArrayList<Data> secondHalfDataList = new ArrayList();

            for (int i = midPointIndex; i < listOfData.size(); i++) {
                secondHalfDataList.add(listOfData.get(i));
            }

            for (int i = 0; i < secondHalfDataList.size(); i++) {
                if (secondHalfDataList.get(i).getKey() == listOfData.get(midPointIndex).getKey()) {
                    listOfData.remove(midPointIndex);
                }
            }
            numberOfPairs = listOfData.size();
            return secondHalfDataList;
        }

        /**
         * This method is used to check if the current Leaf Node can borrow a Key Value pair from its sibling Node.
         *
         * @param minimumDataInLeafNode - The minimum number of key value pairs that leaf node can hold
         * @param sibling               - The sibling of the current leaf node
         * @return true - If Leaf Node can borrow a key value pair from its sibling,
         * false - If Leaf Node cannot borrow a key value pair from its sibling
         */
        public boolean checkCanBorrow(int minimumDataInLeafNode, LeafNode sibling) {
            return sibling != null && sibling.getParent() == parent
                    && sibling.getNumberOfPairs() > minimumDataInLeafNode;
        }

        /**
         * This method is used to check if the current Leaf Node can merge with its sibling Node.
         *
         * @param minimumDataInLeafNode - The minimum number of key value pairs that leaf node can hold
         * @param sibling               - The sibling of the current leaf node
         * @return true - If Leaf Node can merge with its sibling,
         * false - If Leaf Node cannot merge with its sibling
         */
        public boolean checkCanMerge(int minimumDataInLeafNode, LeafNode sibling) {
            return sibling != null && sibling.getParent() == parent
                    && sibling.getNumberOfPairs() == minimumDataInLeafNode;
        }

        /**
         * This method is used to find the index of a key in the list of key value pairs of current Leaf Node.
         *
         * @param key - The key whose index needs to be found
         * @return index - index of the key if key value pair is present,
         * -1 - if key is not present
         */
        public int findIndexOfKeyInData(int key) {
            int index = -1;
            for (int i = 0; i < listOfData.size(); i++) {
                if (listOfData.get(i).getKey() == key) {
                    index = i;
                }
            }

            return index;
        }
    }

    /**
     * Nested class to represent Internal Node of a B+ Tree.
     * Contains Internal Node as parent and left, right sibling Internal nodes forming a doubly liked list of Internal nodes,
     * list of keys whose values are present in the leaf nodes of its children,
     * list of child pointers
     *
     * @author Riyaz Shaik
     */
    public class InternalNode {
        // The current number of child pointers of the internal node
        private int degree;
        // The left sibling of the Internal Node
        private InternalNode leftSibling;
        // The right sibling of the Internal Node
        private InternalNode rightSibling;
        // The parent (Internal Node) of the Internal Node
        private InternalNode parentNode;
        // The list of keys of the Internal Node
        private ArrayList<Integer> listOfKeys = new ArrayList();
        // The list of child pointers of the Internal Node
        private ArrayList listOfChildren = new ArrayList();

        /**
         * Constructs a 0 degree Internal Node Instance having keys provided in the method arguments
         *
         * @param keys - The list of keys
         */
        public InternalNode(ArrayList<Integer> keys) {
            this.degree = 0;
            this.listOfKeys = keys;
        }

        /**
         * Constructs an Internal Node with degree and list of child pointers provided in the method arguments
         *
         * @param keys     - The list of keys
         * @param children - The list of child pointers
         */
        public InternalNode(ArrayList<Integer> keys, ArrayList children) {
            this.degree = children.size();
            this.listOfKeys = keys;
            this.listOfChildren = children;
        }

        /**
         * Getter Method to get degree of Internal Node.
         *
         * @return degree of Internal Node
         */
        public int getDegree() {
            return degree;
        }

        /**
         * Setter Method to set degree of Internal Node.
         *
         * @param degree - degree of Internal Node
         */
        public void setDegree(int degree) {
            this.degree = degree;
        }

        /**
         * Getter Method to get left sibling of Internal Node.
         *
         * @return left sibling of Internal Node
         */
        public InternalNode getLeftSibling() {
            return leftSibling;
        }

        /**
         * Setter Method to set left sibling of Internal Node.
         *
         * @param leftSibling - left sibling of Internal Node
         */
        public void setLeftSibling(InternalNode leftSibling) {
            this.leftSibling = leftSibling;
        }

        /**
         * Getter Method to get right sibling of Internal Node.
         *
         * @return right sibling of Internal Node
         */
        public InternalNode getRightSibling() {
            return rightSibling;
        }

        /**
         * Setter Method to set right sibling of Internal Node.
         *
         * @param rightSibling - right sibling of Internal Node
         */
        public void setRightSibling(InternalNode rightSibling) {
            this.rightSibling = rightSibling;
        }

        /**
         * Getter Method to get parent of Internal Node.
         *
         * @return parent of Internal Node
         */
        public InternalNode getParentNode() {
            return parentNode;
        }

        /**
         * Setter Method to set parent of Internal Node.
         *
         * @param parentNode - parent of Internal Node
         */
        public void setParentNode(InternalNode parentNode) {
            this.parentNode = parentNode;
        }

        /**
         * Getter Method to get list of keys of Internal Node.
         *
         * @return list of keys of Internal Node
         */
        public ArrayList<Integer> getListOfKeys() {
            return listOfKeys;
        }

        /**
         * Setter Method to set list of keys of Internal Node.
         *
         * @param listOfKeys - list of keys of Internal Node
         */
        public void setListOfKeys(ArrayList<Integer> listOfKeys) {
            this.listOfKeys = listOfKeys;
        }

        /**
         * Getter Method to get list of child pointers of Internal Node.
         *
         * @return list of child pointers of Internal Node
         */
        public ArrayList getListOfChildren() {
            return listOfChildren;
        }

        /**
         * Setter Method to set list of child pointers of Internal Node.
         *
         * @param listOfChildren - list of child pointers of Internal Node
         */
        public void setListOfChildren(ArrayList listOfChildren) {
            this.listOfChildren = listOfChildren;
        }

        /**
         * This method is used to sort keys of the internal node in the increasing order of keys.
         */
        public void sortKeys() {
            sort(listOfKeys);
        }

        /**
         * This method is used to add a new child pointer to the exsiting list of child pointers.
         *
         * @param node - The child to be added to the existing list of children
         */
        public void addChildPointer(Object node) {
            listOfChildren.add(node);
            degree++;
        }

        /**
         * This method is used to add a new child pointer at a specific index
         * to the existing list of child pointers.
         * The new child is inserted at position index and children
         * previously at position index and above are pushed right.
         *
         * @param node  - The child to be added to the existing list of children
         * @param index - The index at which the child needs to be added
         */
        public void addChildPointer(Object node, int index) {
            listOfChildren.add(index, node);
            degree++;
        }

        /**
         * This method is used to search for a child in list of children.
         * It returns the index of child if the child is found else it returns -1.
         *
         * @param node - The child to be found.
         * @return index - index of the key if key value pair is present,
         * -1 - if key is not present
         */
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

        /**
         * This method is used to split the list of keys on a
         * midpoint index into 2 separate lists of keys.
         * <p>
         * It returns back the list containing the second half of keys
         * and removes those elements from the original list.
         *
         * @param midPointIndex - The index on which the list of keys are to be split
         * @return The list containing the second half of keys
         */
        public ArrayList<Integer> splitKeys(int midPointIndex) {
            ArrayList<Integer> secondHalfKeysList = new ArrayList();

            listOfKeys.remove(midPointIndex);
            for (int i = midPointIndex; i < listOfKeys.size(); i++) {
                secondHalfKeysList.add(listOfKeys.get(i));
            }

            for (int i = 0; i < secondHalfKeysList.size(); i++) {
                if (secondHalfKeysList.get(i) == listOfKeys.get(midPointIndex)) {
                    listOfKeys.remove(midPointIndex);
                }
            }

            return secondHalfKeysList;
        }

        /**
         * This method is used to split the list of child pointers on a
         * midpoint index into 2 separate lists of child pointers.
         * <p>
         * It returns back the list containing the second half of child pointers
         * and removes those elements from the original list.
         *
         * @param midPointIndex - The index on which the list of keys are to be split
         * @return The list containing the second half of child pointers
         */
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

        /**
         * This method is used to check if the current Internal Node can borrow a Key from its sibling Node.
         *
         * @param internalNodeMinimumDegree - The minimum number of children that internal node can have
         * @param sibling                   - The sibling of the current internal node
         * @return true - If Internal Node can borrow a key from its sibling,
         * false - If Internal Node cannot borrow a key from its sibling
         */
        public boolean checkCanBorrow(int internalNodeMinimumDegree, InternalNode sibling) {
            return sibling != null && sibling.getDegree() > internalNodeMinimumDegree;
        }

        /**
         * This method is used to check if the current Internal Node can merge with its sibling Node.
         *
         * @param internalNodeMinimumDegree - The minimum number of key value pairs that leaf node can hold
         * @param sibling                   - The sibling of the current internal node
         * @return true - If Internal Node can merge with its sibling,
         * false - If Internal Node cannot merge with its sibling
         */
        public boolean checkCanMerge(int internalNodeMinimumDegree, InternalNode sibling) {
            return sibling != null && sibling.getParentNode() == parentNode && sibling.getDegree() == internalNodeMinimumDegree;
        }
    }

    /**
     * Nested class to format the output file records.
     *
     * @author Riyaz Shaik
     */
    public static class OutputRecordsFormatter extends Formatter {
        /**
         * This Method is used to format the output file records.
         * It accepts a log record and returns the formatted output record
         *
         * @param record - output file record
         * @return formatted output file record
         */
        @Override
        public String format(LogRecord record) {
            return formattedMessage(record.getMessage());
        }

        /**
         * This Method is used to format the message.
         * It accepts a message and formats it
         *
         * @param message - message to be formatted
         * @return formatted message
         */
        public String formattedMessage(String message) {
            StringBuilder sb = new StringBuilder();
            sb.append(message);
            sb.append("\n");
            return sb.toString();
        }
    }

    /**
     * This Method is used to remove in between whitespaces from a string.
     * It accepts a string and removes any whitespaces present in it.
     * For example `Delete (10)` becomes `Delete(10)`
     *
     * @param s - string whose in between whitespaces are to be removed
     * @return string with in between whitespaces removed
     */
    public static String removeInBetweenWhiteSpaces(String s) {
        // Creating a pattern for whitespaces
        Pattern patt = Pattern.compile("[\\s]");

        // Searching patt in s.
        Matcher mat = patt.matcher(s);

        // Replacing
        return mat.replaceAll("");
    }

    /**
     * This Method is used to initialize output file writer.
     */
    public static void initializeWriter() {
        try {
            FileHandler fileHandler = new FileHandler("output_file.txt");
            fileHandler.setFormatter(new OutputRecordsFormatter());
            writer.addHandler(fileHandler);
            writer.setUseParentHandlers(false);
        } catch (IOException e) {
            System.out.println("Error occured while creating output text file - " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * This Method is used to insert a key value pair in the B+ Tree.
     * It accepts a key value pair and inserts it into a leaf node
     * and balances internal nodes from bottom to top.
     *
     * @param key   - Key to inserted
     * @param value - value to inserted
     */
    public void insert(int key, double value) {
        Data data = new Data(key, value);

        //Not empty tree
        if (firstLeafNode != null) {
            LeafNode lastNode = null;
            // Find Leaf Node to be inserted into
            lastNode = root != null ? getLeafNode(root, key) : firstLeafNode;
            boolean isInserted = lastNode.insertData(maximumDataInLeafNode, data);
            //Insertion Unsuccessful
            if (!isInserted) {
                lastNode.getListOfData().add(data);
                lastNode.setNumberOfPairs(lastNode.getNumberOfPairs() + 1);
                lastNode.sortData();

                //split the key value pairs list
                ArrayList<Data> secondHalfDataList = lastNode.splitDataList(midPointIndex);

                // Node has parent. Add the second half list first element to parent
                if (lastNode.getParent() != null) {
                    int newParentKey = secondHalfDataList.get(0).getKey();
                    lastNode.getParent().getListOfKeys().add(lastNode.getParent().getDegree() - 1, newParentKey);
                    lastNode.getParent().sortKeys();
                }
                // Node has no parent. Create a parent and add the second half list first element to parent
                else {
                    ArrayList<Integer> parentKeys = new ArrayList();
                    parentKeys.add(secondHalfDataList.get(0).getKey());
                    InternalNode parent = new InternalNode(parentKeys);
                    parent.addChildPointer(lastNode);
                    lastNode.setParent(parent);
                }

                // create a new Leaf Node and add it to parent
                LeafNode leafNode = new LeafNode(secondHalfDataList, lastNode.getParent());
                int childPointerIndex = lastNode.getParent().findChildIndex(lastNode) + 1;
                lastNode.getParent().addChildPointer(leafNode, childPointerIndex);

                // adjust the left and right siblings
                leafNode.setRightSibling(lastNode.getRightSibling());
                if (leafNode.getRightSibling() != null) {
                    leafNode.getRightSibling().setLeftSibling(leafNode);
                }
                lastNode.setRightSibling(leafNode);
                leafNode.setLeftSibling(lastNode);

                // Split and Adjust internal nodes to balance the tree
                if (root != null) {
                    InternalNode internalNode = lastNode.getParent();
                    while (internalNode != null) {
                        //If the Node is overfull. Split the Node
                        if (internalNode.getDegree() == internalNodeMaximumDegree + 1) {
                            splitInternalNode(midPointIndex, internalNode);
                        } else {
                            break;
                        }
                        internalNode = internalNode.getParentNode();
                    }
                } else {
                    root = lastNode.getParent();
                }
            }
        }
        // Empty Tree. Create a new Leaf node and add key value pair to it
        else {
            LeafNode leafNode = new LeafNode();
            leafNode.insertData(maximumDataInLeafNode, data);
            firstLeafNode = leafNode;
        }
    }

    /**
     * This method is used to split the overfull node and balance the B+ tree.
     * This method splits keys and children lists based on midpoint index and creates a new sibling node
     * containing half the keys and child pointers and adds the sibling node to parent
     *
     * @param midPointIndex - index at which keys and child pointers are to be split
     * @param internalNode  - node to be split
     */
    public void splitInternalNode(int midPointIndex, InternalNode internalNode) {
        //split the keys and child pointers
        InternalNode parent = internalNode.getParentNode();
        int newParentKey = internalNode.getListOfKeys().get(midPointIndex);
        ArrayList<Integer> secondHalfKeysList = internalNode.splitKeys(midPointIndex);
        ArrayList secondHalfPointersList = internalNode.splitChildPointers(midPointIndex);
        internalNode.setDegree(internalNode.getListOfChildren().size());

        //create a new sibling and adds other half of keys and child pointers to it
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

        // adjust left and right siblings
        sibling.setRightSibling(internalNode.getRightSibling());
        if (sibling.getRightSibling() != null) {
            sibling.getRightSibling().setLeftSibling(sibling);
        }
        internalNode.setRightSibling(sibling);
        sibling.setLeftSibling(internalNode);

        // Node has a parent. Add a new key from splitting of node keys to parent. Add sibling to parent
        if (parent != null) {
            parent.getListOfKeys().add(newParentKey);
            parent.sortKeys();

            int childPointerIndex = parent.findChildIndex(internalNode) + 1;
            parent.addChildPointer(sibling, childPointerIndex);
            sibling.setParentNode(parent);
        }
        // Node has a no parent (root node). Create a new parent and add key from splitting of node keys to parent.
        // Add node and sibling to parent. Set parent as new root node
        else {
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

    /**
     * This method is used to get the leaf node which has the key from an internal node.
     * It accepts Internal Node and Key and returns the Leaf Node which contains the key
     *
     * @param node - the Internal Node from where leaf node needs to be found
     * @param key - key to be found
     *
     * @return Leaf Node which contains the key
     */
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

    /**
     * This method is used to adjust internal nodes when it becomes deficient.
     * It accepts an Internal node and checks -
     * 1. If it can borrow a key from left or right sibling. If yes it borrows a key through parent
     * 2. If it can merge with left or right sibling. If yes it merges with sibling and deletes in between parent key
     *
     * It adjust from the current node till root
     *
     * @param node - node to be adjusted
     */
    public void adjustInternalNodes(InternalNode node) {
        InternalNode sibling;
        InternalNode parent = node.getParentNode();
        // If root is current node and is deficient. Check if it has Internal Node as child. If not set it to null
        if (root.equals(node)) {
            for (Object child : node.getListOfChildren()) {
                if (child != null) {
                    // Internal Node is a child. Set it to root
                    if (child instanceof InternalNode) {
                        root = (InternalNode) child;
                        root.setParentNode(null);
                    } else {
                        root = null;
                    }
                }
            }
        }
        // Current Node is deficient but not root. It can borrow from Left sibling
        else if (node.checkCanBorrow(internalNodeMinimumDegree, node.getLeftSibling())) {
            sibling = node.getLeftSibling();
            // Get Last key and child from sibling
            int borrowedKey = sibling.getListOfKeys().get(sibling.getDegree() - 1);
            Object child = sibling.getListOfChildren().get(sibling.getDegree());

            // Add parents node - 1 key to node and add siblings last child to node. Add borrowed key to parent
            int nodeIndexFromParent = parent.findChildIndex(node) - 1;
            node.getListOfKeys().add(0, parent.getListOfKeys().get(nodeIndexFromParent));
            node.getListOfChildren().add(0, child);
            parent.getListOfKeys().add(0, borrowedKey);

            //Remove node - 1 key of parent, remove sibling last key and last child
            parent.getListOfKeys().remove(nodeIndexFromParent);
            sibling.getListOfChildren().remove(child);
            sibling.getListOfKeys().remove(sibling.getDegree() - 1);
            sibling.setDegree(sibling.getDegree() - 1);
            sibling.sortKeys();
        }
        // Current Node is deficient but not root. It can borrow from Right sibling
        else if (node.checkCanBorrow(internalNodeMinimumDegree, node.getRightSibling())) {
            sibling = node.getRightSibling();

            // Get first key and child from sibling
            int borrowedKey = sibling.getListOfKeys().get(0);
            Object child = sibling.getListOfChildren().get(0);

            // Add parents node position key to node and add siblings last child to node. Add borrowed key to parent
            int nodeIndexFromParent = parent.findChildIndex(node);
            node.getListOfKeys().add(node.getDegree() - 1, parent.getListOfKeys().get(nodeIndexFromParent));
            node.getListOfChildren().add(node.getDegree(), child);
            parent.getListOfKeys().add(parent.getDegree() - 1, borrowedKey);

            //Remove node key of parent, remove sibling last key and last child
            parent.getListOfKeys().remove(nodeIndexFromParent);
            sibling.getListOfChildren().remove(child);
            sibling.getListOfKeys().remove(0);
            sibling.setDegree(sibling.getDegree() - 1);
            sibling.sortKeys();
        }
        // Current Node is deficient but not root. It can merge with Left sibling
        else if (node.checkCanMerge(internalNodeMinimumDegree, node.getLeftSibling())) {
            sibling = node.getLeftSibling();

            // Add node - 1 key from parent to sibling
            int childPointerIndex = parent.findChildIndex(node);
            sibling.getListOfKeys().add(sibling.getDegree() - 1, parent.getListOfKeys().get(childPointerIndex - 1));
            sibling.sortKeys();

            //Add node's children to sibling
            int nodeChildListSize = node.getListOfChildren().size();
            for (int i = 0; i < nodeChildListSize; i++) {
                Object child = node.getListOfChildren().get(i);
                if (child != null) {
                    //sibling.shiftRightChildren(child);
                    sibling.getListOfChildren().add(sibling.getDegree(), child);
                    sibling.setDegree(sibling.getDegree() + 1);
                    if (child instanceof InternalNode) {
                        InternalNode childInternal = (InternalNode) child;
                        childInternal.setParentNode(sibling);
                    } else {
                        LeafNode leafNode = (LeafNode) child;
                        leafNode.setParent(sibling);
                    }
                    node.getListOfChildren().remove(child);
                    node.setDegree(node.getDegree() - 1);
                }
            }

            //Remove node - 1 key from parent and remove node from parent
            parent.getListOfKeys().remove(childPointerIndex - 1);
            parent.getListOfChildren().remove(node);
            parent.setDegree(parent.getDegree() - 1);

            // Adjust left and right siblings
            if (node.getRightSibling() != null) {
                node.getRightSibling().setLeftSibling(sibling);
            }
            sibling.setRightSibling(node.getRightSibling());
        } else if (node.checkCanMerge(internalNodeMinimumDegree, node.getRightSibling())) {
            sibling = node.getRightSibling();

            // Add node key from parent to sibling
            int childPointerIndex = parent.findChildIndex(node);
            sibling.getListOfKeys().add(0, parent.getListOfKeys().get(childPointerIndex));
            sibling.sortKeys();

            //Add node's children to sibling
            int nodeChildListSize = node.getListOfChildren().size();
            for (int i = 0; i < nodeChildListSize; i++) {
                Object child = node.getListOfChildren().get(i);
                if (child != null) {
                    //sibling.shiftRightChildren(child);
                    sibling.getListOfChildren().add(0, child);
                    sibling.setDegree(sibling.getDegree() + 1);
                    if (child instanceof InternalNode) {
                        InternalNode childInternal = (InternalNode) child;
                        childInternal.setParentNode(sibling);
                    } else {
                        LeafNode leafNode = (LeafNode) child;
                        leafNode.setParent(sibling);
                    }
                    node.getListOfChildren().remove(child);
                    node.setDegree(node.getDegree() - 1);
                }
            }

            //Remove node key from parent and remove node from parent
            parent.getListOfKeys().remove(childPointerIndex);
            parent.getListOfChildren().remove(node);
            parent.setDegree(parent.getDegree() - 1);

            // Adjust left and right siblings
            if (node.getLeftSibling() != null) {
                node.getLeftSibling().setRightSibling(sibling);
            }
            sibling.setLeftSibling(node.getLeftSibling());
        }

        // Recursively adjust parent Internal Node if it is deficient
        if (parent != null && parent.getDegree() < internalNodeMinimumDegree) {
            adjustInternalNodes(parent);
        }
    }

    /**
     * This method is used to delete key value pair from B+ Tree whose key is provided in the arguments.
     * It accepts a key and delete corresponding key value from leaf node.
     * After removing a key value pair if the leaf node becomes deficient. It checks if -
     * 1. If it can borrow a key value pair from left or right sibling. If yes it borrows the pair through parent
     * 2. If it can merge with left or right sibling. If yes it merges with sibling and deletes in between parent key
     *
     * After adjusting leaf node if internal node becomes deficient it adjust internal node all the way upto root
     *
     * @param key - key of the key value pair to be deleted
     */
    public void delete(int key) {
        // Tree is not empty
        if (firstLeafNode != null) {
            // Find the leaf node where the key value pair is and find the index of the key in the leaf node
            LeafNode lastNode = root != null ? getLeafNode(root, key) : firstLeafNode;
            int deleteKeyIndex = lastNode.findIndexOfKeyInData(key);
            if (deleteKeyIndex > -1) {
                //delete the key
                lastNode.getListOfData().remove(deleteKeyIndex);
                lastNode.setNumberOfPairs(lastNode.getNumberOfPairs() - 1);

                // Empty Tree
                if (root == null && firstLeafNode.getNumberOfPairs() == 0) {
                    firstLeafNode = null;
                }
                // Node is not deficient
                else if (lastNode.getNumberOfPairs() >= minimumDataInLeafNode) {
                    lastNode.sortData();
                }
                // Node is deficient
                else {
                    LeafNode sibling = null;
                    InternalNode parent = lastNode.getParent();
                    // It can borrow from Left sibling
                    if (lastNode.checkCanBorrow(minimumDataInLeafNode, lastNode.getLeftSibling())) {
                        sibling = lastNode.getLeftSibling();

                        // Insert sibling last element into lastNode
                        Data borrowedElement = sibling.getListOfData().get(sibling.getNumberOfPairs() - 1);
                        lastNode.insertData(maximumDataInLeafNode, borrowedElement);
                        lastNode.sortData();

                        // Remove sibling last element
                        sibling.getListOfData().remove(sibling.getNumberOfPairs() - 1);
                        sibling.setNumberOfPairs(sibling.getNumberOfPairs() - 1);

                        // Update parent pointer if needed
                        int childPointerIndex = parent.findChildIndex(lastNode);
                        if (borrowedElement.getKey() < parent.getListOfKeys().get(childPointerIndex - 1)) {
                            parent.getListOfKeys().set(childPointerIndex - 1, lastNode.getListOfData().get(0).getKey());
                        }
                    }
                    // It can borrow from Right sibling
                    else if (lastNode.checkCanBorrow(minimumDataInLeafNode, lastNode.getRightSibling())) {
                        sibling = lastNode.getRightSibling();

                        // Insert sibling first element into lastNode
                        Data borrowedElement = sibling.getListOfData().get(0);
                        lastNode.insertData(maximumDataInLeafNode, borrowedElement);
                        lastNode.sortData();

                        // Remove sibling first element
                        sibling.getListOfData().remove(0);
                        sibling.setNumberOfPairs(sibling.getNumberOfPairs() - 1);

                        // Update parent pointer if needed
                        int childPointerIndex = parent.findChildIndex(lastNode);
                        if (borrowedElement.getKey() >= parent.getListOfKeys().get(childPointerIndex)) {
                            parent.getListOfKeys().set(childPointerIndex, sibling.getListOfData().get(0).getKey());
                        }
                    }
                    // It can merge with Left sibling
                    else if (lastNode.checkCanMerge(minimumDataInLeafNode, lastNode.getLeftSibling())) {
                        sibling = lastNode.getLeftSibling();

                        // Remove lastNode child pointer and lastNode - 1 key from parent
                        int childPointerIndex = parent.findChildIndex(lastNode);
                        parent.getListOfKeys().remove(childPointerIndex - 1);
                        parent.getListOfChildren().remove(childPointerIndex);
                        parent.setDegree(parent.getDegree() - 1);

                        // Adjust left and right siblings
                        if (lastNode.getRightSibling() != null) {
                            lastNode.getRightSibling().setLeftSibling(sibling);
                        }
                        sibling.setRightSibling(lastNode.getRightSibling());
                    }
                    // It can merge with Right sibling
                    else if (lastNode.checkCanMerge(minimumDataInLeafNode, lastNode.getRightSibling())) {
                        sibling = lastNode.getRightSibling();

                        // Remove lastNode child pointer and lastNode key from parent
                        int childPointerIndex = parent.findChildIndex(lastNode);
                        parent.getListOfKeys().remove(childPointerIndex);
                        parent.getListOfChildren().remove(childPointerIndex);
                        parent.setDegree(parent.getDegree() - 1);

                        // Adjust left and right siblings
                        sibling.setLeftSibling(lastNode.getLeftSibling());
                        if (sibling.getLeftSibling() == null) {
                            firstLeafNode = sibling;
                        } else {
                            sibling.getLeftSibling().setRightSibling(sibling);
                        }
                    }

                    // Adjust parent Internal Node if it is deficient
                    if (parent.getDegree() < internalNodeMinimumDegree) {
                        adjustInternalNodes(parent);
                    }
                }
            } else {
                System.out.println("Element to be deleted does not exist");
            }
        }else{
            System.out.println("Tree is empty. Cannot delete");
        }
    }

    /**
     * This method searches for a given key in the B+ Tree and prints the value
     * of that particular key value pair to the output file.
     * It accepts a key and writes value of that key if key is found else writes 'Null' to output file
     *
     * @param key - key to be searched
     */
    public void search(int key) {
        String result = null;

        if (firstLeafNode != null) {
            // Search for leaf node that contains the key
            LeafNode node = root != null ? getLeafNode(root, key) : firstLeafNode;
            for (Data data : node.getListOfData()) {
                if (data.getKey() == key) {
                    result = Double.toString(data.getValue());
                    break;
                }
            }
        }

        // Value not found write 'Null'
        if (result == null) {
            writer.info(NULL_VALUE);
        }
        // Value found write Value
        else {
            writer.info(result);
        }
    }

    /**
     * This method searches for keys in the B+ Tree which are in between
     * lowerBound and upperBound included and writes their values to output file.
     *
     * It accepts lowerBound and upperBound and writes value of those keys which fall in range of [lowerBound, upperBound]
     * else writes 'Null' if no  values are found to output file
     *
     * @param lowerBound - lowerBound of the range of keys to be searched
     * @param upperBound - upperBound of the range of keys to be searched
     */
    public void search(int lowerBound, int upperBound) {
        ArrayList<Double> resultsList = new ArrayList();

        if (firstLeafNode != null) {
            // Search for leaf node that contains the key
            LeafNode node = root != null ? getLeafNode(root, lowerBound) : firstLeafNode;
            while (node != null) {
                ArrayList<Data> dataList = node.getListOfData();
                for (Data data : dataList) {
                    if (lowerBound <= data.getKey() && data.getKey() <= upperBound) {
                        resultsList.add(data.getValue());
                    }
                }
                node = node.getRightSibling();
            }
        }

        // Values not found write 'Null'
        if (resultsList.isEmpty()) {
            writer.info(NULL_VALUE);
        }
        // Values found write all values comma seperated eg., 1.0,2.0,3.0
        else {
            StringBuffer result = new StringBuffer();
            for (int i = 0; i < resultsList.size(); i++) {
                result.append(resultsList.get(i));
                if (i != resultsList.size() - 1) {
                    result.append(",");
                }
            }
            writer.info(result.toString());
        }
    }

    /**
     * Main Method of the bplustree class. It creates B+ plus tree.
     * It reads input file to insert values to it and delete values from it.
     * It writes the search results to output file
     *
     * @param args - Pass the name of the input file containing tree operations
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            File file = new File(args[0]);
            BufferedReader bufferedReader = null;
            String line = null;
            bplustree bplustree = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                initializeWriter();
                // Perform an operation for each line in the input file
                while ((line = bufferedReader.readLine()) != null) {
                    String[] tokens = removeInBetweenWhiteSpaces(line).trim().split("\\(|,|\\)");
                    String option = tokens[0];
                    // Initializes an m-order B+ tree
                    if (option.equalsIgnoreCase(INITIALIZE)) {
                        bplustree = new bplustree(Integer.parseInt(tokens[1].trim()));
                    }
                    // Insert a key value pair into the B+ tree
                    else if (option.equalsIgnoreCase(INSERT)) {
                        bplustree.insert(Integer.parseInt(tokens[1].trim()), Double.parseDouble(tokens[2].trim()));
                    }
                    // Perform a search or range search operation on the B+ tree
                    else if (option.equalsIgnoreCase(SEARCH)) {
                        if (tokens.length == 2) {
                            bplustree.search(Integer.parseInt(tokens[1].trim()));
                        } else if (tokens.length == 3) {
                            bplustree.search(Integer.parseInt(tokens[1].trim()), Integer.parseInt(tokens[2].trim()));
                        } else {
                            System.out.println("Invalid Search Option");
                        }
                    }
                    // Delete a key value pair from the B+ tree
                    else if (option.equalsIgnoreCase(DELETE)) {
                        bplustree.delete(Integer.parseInt(tokens[1].trim()));
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
