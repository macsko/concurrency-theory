package me.macsko.tw.mesh;

public class Vertex {

    //label
    String mLabel;
    //links to adjacent elements
    Vertex mLeft;
    Vertex mRight;
    Vertex mUp;
    Vertex mDown;

    //methods for adding links
    public Vertex(Vertex _left, Vertex _right, Vertex _up, Vertex _down, String _lab) {
        this.mLeft = _left;
        this.mRight = _right;
        this.mUp = _up;
        this.mDown = _down;
        this.mLabel = _lab;
    }
    //empty constructor

    public Vertex() {
    }

    public void setLeft(Vertex _left) {
        this.mLeft = _left;
    }

    public void setRight(Vertex _right) {
        this.mRight = _right;
    }

    public void setUp(Vertex _up) {
        this.mUp = _up;
    }

    public void setDown(Vertex _down) {
        this.mDown = _down;
    }

    public void setLabel(String _lab) {
        this.mLabel = _lab;
    }

    public Vertex getLeft() {
        return this.mLeft;
    }

    public Vertex getRight() {
        return this.mRight;
    }

    public Vertex getUp() {
        return this.mUp;
    }

    public Vertex getDown() {
        return this.mDown;
    }

    public String getLabel() {
        return this.mLabel;
    }
}
