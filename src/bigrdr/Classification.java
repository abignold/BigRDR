/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bigrdr;

/**
 *
 * @author Adam
 */
public class Classification {
    private Node classificationNode;
    private Node insertionNode;
    private Boolean insertionClassification;

    public Classification(Node classificationNode, Node insertionNode, Boolean insertionClassification) {
        this.classificationNode = classificationNode;
        this.insertionNode = insertionNode;
        this.insertionClassification = insertionClassification;
    }

    public Node getClassificationNode() {
        return classificationNode;
    }

    public Node getInsertionNode() {
        return insertionNode;
    }

    public Boolean getInsertionClassification() {
        return insertionClassification;
    }

    @Override
    public String toString() {
        return this.classificationNode.getClassificationString();
    }
}
