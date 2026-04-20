package implementations;

import java.io.Serializable;

/**
 * BSTreeNode represents a single node in a Binary Search Tree.
 * Each node stores an element and references to its left and right children.
 *
 * @param <E> The type of element this node holds.
 * @author Keith Sharman
 * @version 1.0, April 20, 2026
 */
public class BSTreeNode<E> implements Serializable
{
    // Serial version UID for serialization compatibility
    private static final long serialVersionUID = 1L;

    // The data element stored in this node
    private E element;

    // Reference to the left child node (elements less than this node)
    private BSTreeNode<E> left;

    // Reference to the right child node (elements greater than this node)
    private BSTreeNode<E> right;

    /**
     * Constructs a BSTreeNode with the given element and no children.
     *
     * @param element the data to store in this node
     */
    public BSTreeNode( E element )
    {
        this.element = element;
        this.left = null;
        this.right = null;
    }

    /**
     * Constructs a BSTreeNode with the given element and specified children.
     *
     * @param element the data to store in this node
     * @param left    the left child node
     * @param right   the right child node
     */
    public BSTreeNode( E element, BSTreeNode<E> left, BSTreeNode<E> right )
    {
        this.element = element;
        this.left = left;
        this.right = right;
    }

    /**
     * Returns the element stored in this node.
     *
     * @return the element stored in this node
     */
    public E getElement()
    {
        return element;
    }

    /**
     * Sets the element stored in this node.
     *
     * @param element the new element to store
     */
    public void setElement( E element )
    {
        this.element = element;
    }

    /**
     * Returns the left child of this node.
     *
     * @return the left child node, or null if none
     */
    public BSTreeNode<E> getLeft()
    {
        return left;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the new left child node
     */
    public void setLeft( BSTreeNode<E> left )
    {
        this.left = left;
    }

    /**
     * Returns the right child of this node.
     *
     * @return the right child node, or null if none
     */
    public BSTreeNode<E> getRight()
    {
        return right;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the new right child node
     */
    public void setRight( BSTreeNode<E> right )
    {
        this.right = right;
    }

    /**
     * Checks whether this node has a left child.
     *
     * @return true if this node has a left child, false otherwise
     */
    public boolean hasLeft()
    {
        return left != null;
    }

    /**
     * Checks whether this node has a right child.
     *
     * @return true if this node has a right child, false otherwise
     */
    public boolean hasRight()
    {
        return right != null;
    }

    /**
     * Checks whether this node is a leaf node (has no children).
     *
     * @return true if this node has no children, false otherwise
     */
    public boolean isLeaf()
    {
        return left == null && right == null;
    }
}
