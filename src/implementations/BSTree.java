package implementations;

import java.io.Serializable;
import java.util.ArrayList;
import utilities.Iterator;
import java.util.NoSuchElementException;
import utilities.BSTreeADT;

/**
 * BSTree is a linked-node-based implementation of a Binary Search Tree (BST).
 * Elements are ordered according to their natural ordering (Comparable).
 * Duplicate elements are not stored; attempting to add a duplicate is a no-op
 *
 * This class supports in-order, pre-order, and post-order traversal
 * via the Iterator interface, as well as serialization for tree persistence.
 *
 * @param <E> The type of elements stored in this tree; must implement Comparable.
 * @author Keith Sharman
 * @version 1.0, April 20, 2026
 */
public class BSTree<E extends Comparable<? super E>> implements BSTreeADT<E>, Serializable
{
    // Serial version UID for serialization compatibility
    private static final long serialVersionUID = 1L;

    // The root node of the tree; null when the tree is empty
    private BSTreeNode<E> root;

    // The number of elements currently stored in the tree
    private int size;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs an empty BSTree with no root node and a size of zero.
     */
    public BSTree()
    {
        this.root = null;
        this.size = 0;
    }

    /**
     * Constructs a BSTree with a single root node containing the given element.
     *
     * @param element the element to store at the root
     * @throws NullPointerException if the element is null
     */
    public BSTree( E element ) throws NullPointerException
    {
        if ( element == null )
        {
            throw new NullPointerException( "Element cannot be null." );
        }
        this.root = new BSTreeNode<>( element );
        this.size = 1;
    }

    // -------------------------------------------------------------------------
    // BSTreeADT interface methods
    // -------------------------------------------------------------------------

    /**
     * Returns the root node of this tree.
     *
     * @return the root node
     * @throws NullPointerException if the tree is empty
     */
    @Override
    public BSTreeNode<E> getRoot() throws NullPointerException
    {
        if ( root == null )
        {
            throw new NullPointerException( "The tree is empty; there is no root node." );
        }
        return root;
    }

    /**
     * Returns the height of the tree — the length of the longest path from root
     * to a leaf node. An empty tree has height 0; a single-node tree has height 1.
     *
     * @return the height of the tree
     */
    @Override
    public int getHeight()
    {
        return calculateHeight( root );
    }

    /**
     * Recursively calculates the height of the subtree rooted at the given node.
     *
     * @param node the subtree root
     * @return the height of the subtree
     */
    private int calculateHeight( BSTreeNode<E> node )
    {
        // Base case: empty subtree contributes 0 to height
        if ( node == null )
        {
            return 0;
        }
        // Height is 1 (for this node) plus the taller of the two subtrees
        int leftHeight  = calculateHeight( node.getLeft() );
        int rightHeight = calculateHeight( node.getRight() );
        return 1 + Math.max( leftHeight, rightHeight );
    }

    /**
     * Returns the number of elements currently stored in the tree.
     *
     * @return the number of elements in the tree
     */
    @Override
    public int size()
    {
        return size;
    }

    /**
     * Checks whether the tree contains no elements.
     *
     * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Removes all elements from the tree, making it empty.
     */
    @Override
    public void clear()
    {
        root = null;
        size = 0;
    }

    /**
     * Checks whether the tree contains the given element.
     *
     * @param entry the element to search for
     * @return true if the element is found, false otherwise
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean contains( E entry ) throws NullPointerException
    {
        if ( entry == null )
        {
            throw new NullPointerException( "Search element cannot be null." );
        }
        return search( entry ) != null;
    }

    /**
     * Searches the tree for the given element and returns the node containing it,
     * or null if the element is not present.
     *
     * @param entry the element to search for
     * @return the node containing the element, or null if not found
     * @throws NullPointerException if the element is null
     */
    @Override
    public BSTreeNode<E> search( E entry ) throws NullPointerException
    {
        if ( entry == null )
        {
            throw new NullPointerException( "Search element cannot be null." );
        }
        return searchRecursive( root, entry );
    }

    /**
     * Recursively searches the subtree rooted at the given node for the entry.
     *
     * @param node  the current subtree root
     * @param entry the element to find
     * @return the matching node, or null if not found
     */
    private BSTreeNode<E> searchRecursive( BSTreeNode<E> node, E entry )
    {
        // Base case: node not found
        if ( node == null )
        {
            return null;
        }

        int cmp = entry.compareTo( node.getElement() );

        if ( cmp < 0 )
        {
            // Entry is smaller — search left subtree
            return searchRecursive( node.getLeft(), entry );
        }
        else if ( cmp > 0 )
        {
            // Entry is larger — search right subtree
            return searchRecursive( node.getRight(), entry );
        }
        else
        {
            // Found the node
            return node;
        }
    }

    /**
     * Adds a new element to the tree in its correct BST position.
     * Duplicate elements are not added; returns false if the element already exists.
     *
     * @param newEntry the element to add
     * @return true if the element was added successfully, false if it already exists
     * @throws NullPointerException if the element is null
     */
    @Override
    public boolean add( E newEntry ) throws NullPointerException
    {
        if ( newEntry == null )
        {
            throw new NullPointerException( "Cannot add a null element to the tree." );
        }

        if ( root == null )
        {
            // Tree is empty — the new node becomes the root
            root = new BSTreeNode<>( newEntry );
            size++;
            return true;
        }

        return addRecursive( root, newEntry );
    }

    /**
     * Recursively finds the correct position for the new entry and inserts it.
     *
     * @param node     the current subtree root
     * @param newEntry the element to insert
     * @return true if inserted successfully, false if already present
     */
    private boolean addRecursive( BSTreeNode<E> node, E newEntry )
    {
        int cmp = newEntry.compareTo( node.getElement() );

        if ( cmp < 0 )
        {
            // New entry belongs in the left subtree
            if ( node.getLeft() == null )
            {
                node.setLeft( new BSTreeNode<>( newEntry ) );
                size++;
                return true;
            }
            return addRecursive( node.getLeft(), newEntry );
        }
        else if ( cmp > 0 )
        {
            // New entry belongs in the right subtree
            if ( node.getRight() == null )
            {
                node.setRight( new BSTreeNode<>( newEntry ) );
                size++;
                return true;
            }
            return addRecursive( node.getRight(), newEntry );
        }
        else
        {
            // Duplicate — do not insert
            return false;
        }
    }

    /**
     * Removes and returns the node containing the smallest (minimum) element in
     * the tree, i.e. the leftmost node. Returns null if the tree is empty.
     *
     * @return the removed minimum node, or null if the tree is empty
     */
    @Override
    public BSTreeNode<E> removeMin()
    {
        if ( root == null )
        {
            return null;
        }

        // Walk left until we find the leftmost node; track its parent
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;

        while ( current.hasLeft() )
        {
            parent = current;
            current = current.getLeft();
        }

        // current is now the minimum node
        // Replace it with its right child (which may be null)
        if ( parent == null )
        {
            // Minimum is the root itself
            root = root.getRight();
        }
        else
        {
            parent.setLeft( current.getRight() );
        }

        size--;
        // Detach children before returning so the caller gets a clean node
        current.setLeft( null );
        current.setRight( null );
        return current;
    }

    /**
     * Removes and returns the node containing the largest (maximum) element in
     * the tree, i.e. the rightmost node. Returns null if the tree is empty.
     *
     * @return the removed maximum node, or null if the tree is empty
     */
    @Override
    public BSTreeNode<E> removeMax()
    {
        if ( root == null )
        {
            return null;
        }

        // Walk right until we find the rightmost node; track its parent
        BSTreeNode<E> parent = null;
        BSTreeNode<E> current = root;

        while ( current.hasRight() )
        {
            parent = current;
            current = current.getRight();
        }

        // current is now the maximum node
        // Replace it with its left child (which may be null)
        if ( parent == null )
        {
            // Maximum is the root itself
            root = root.getLeft();
        }
        else
        {
            parent.setRight( current.getLeft() );
        }

        size--;
        // Detach children before returning so the caller gets a clean node
        current.setLeft( null );
        current.setRight( null );
        return current;
    }

    // -------------------------------------------------------------------------
    // Iterator methods
    // -------------------------------------------------------------------------

    /**
     * Returns an iterator that traverses the tree elements in ascending (in-order)
     * sequence: left subtree → root → right subtree.
     *
     * @return an in-order iterator over the tree elements
     */
    @Override
    public Iterator<E> inorderIterator()
    {
        ArrayList<E> elements = new ArrayList<>();
        inorderTraversal( root, elements );
        return new BSTreeIterator<>( elements );
    }

    /**
     * Recursively collects elements in in-order (left, root, right).
     *
     * @param node     the current subtree root
     * @param elements the list to collect elements into
     */
    private void inorderTraversal( BSTreeNode<E> node, ArrayList<E> elements )
    {
        if ( node == null ) return;
        inorderTraversal( node.getLeft(), elements );
        elements.add( node.getElement() );
        inorderTraversal( node.getRight(), elements );
    }

    /**
     * Returns an iterator that traverses the tree elements in pre-order sequence:
     * root → left subtree → right subtree.
     *
     * @return a pre-order iterator over the tree elements
     */
    @Override
    public Iterator<E> preorderIterator()
    {
        ArrayList<E> elements = new ArrayList<>();
        preorderTraversal( root, elements );
        return new BSTreeIterator<>( elements );
    }

    /**
     * Recursively collects elements in pre-order (root, left, right).
     *
     * @param node     the current subtree root
     * @param elements the list to collect elements into
     */
    private void preorderTraversal( BSTreeNode<E> node, ArrayList<E> elements )
    {
        if ( node == null ) return;
        elements.add( node.getElement() );
        preorderTraversal( node.getLeft(), elements );
        preorderTraversal( node.getRight(), elements );
    }

    /**
     * Returns an iterator that traverses the tree elements in post-order sequence:
     * left subtree → right subtree → root.
     *
     * @return a post-order iterator over the tree elements
     */
    @Override
    public Iterator<E> postorderIterator()
    {
        ArrayList<E> elements = new ArrayList<>();
        postorderTraversal( root, elements );
        return new BSTreeIterator<>( elements );
    }

    /**
     * Recursively collects elements in post-order (left, right, root).
     *
     * @param node     the current subtree root
     * @param elements the list to collect elements into
     */
    private void postorderTraversal( BSTreeNode<E> node, ArrayList<E> elements )
    {
        if ( node == null ) return;
        postorderTraversal( node.getLeft(), elements );
        postorderTraversal( node.getRight(), elements );
        elements.add( node.getElement() );
    }

    // -------------------------------------------------------------------------
    // Inner iterator class
    // -------------------------------------------------------------------------

    /**
     * BSTreeIterator is a simple iterator over a pre-collected list of tree
     * elements. It supports hasNext() and next() as defined in the Iterator
     * interface.
     *
     * @param <T> The type of elements this iterator returns.
     */
    private static class BSTreeIterator<T> implements Iterator<T>
    {
        // The snapshot of elements collected during traversal
        private final ArrayList<T> elements;

        // Current position in the snapshot
        private int index;

        /**
         * Constructs an iterator over the given list of elements.
         *
         * @param elements the pre-collected traversal snapshot
         */
        public BSTreeIterator( ArrayList<T> elements )
        {
            this.elements = elements;
            this.index = 0;
        }

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if next() would return an element
         */
        @Override
        public boolean hasNext()
        {
            return index < elements.size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         * @throws NoSuchElementException if no more elements remain
         */
        @Override
        public T next() throws NoSuchElementException
        {
            if ( !hasNext() )
            {
                throw new NoSuchElementException( "No more elements in the iterator." );
            }
            return elements.get( index++ );
        }
    }
}
