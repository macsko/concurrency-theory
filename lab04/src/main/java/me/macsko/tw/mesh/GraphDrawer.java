package me.macsko.tw.mesh;

import me.macsko.tw.production.PDrawer;

public class GraphDrawer implements PDrawer<Vertex> {

    @Override
    public synchronized void draw(Vertex v) {
        //go up
        while (v.mUp != null) {
            v = v.mUp;
        }

        //go right
        while (v.mRight != null) {
            v = v.mRight;
        }
        int N = 1;
        //go left and count N
        while (v.mLeft != null) {
            v = v.mLeft;
            N++;
        }

        Vertex[] widthSegments = new Vertex[N];
        int i = 0;
        widthSegments[i++] = v;
        //go right and fill widthSegments
        while (v.mRight != null) {
            v = v.mRight;
            widthSegments[i++] = v;
        }
        //go left
        while (v.mLeft != null) {
            v = v.mLeft;
        }

        // plot entire NxN graph
        do {
            for (int j = 0; j < N; j++) {
                if (widthSegments[j] != null) {
                    System.out.print(v.mLabel);
                    if (widthSegments[j].mRight != null) {
                        System.out.print("--");
                    } else {
                        System.out.print("  ");
                    }
                    widthSegments[j] = widthSegments[j].mDown;
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("");

            for (int j = 0; j < N; j++) {
                if (widthSegments[j] != null) {
                    System.out.print("|");
                } else {
                    System.out.print(" ");
                }
                System.out.print("  ");
            }
            System.out.println("");
        }while (widthSegments[N - 1] != null);
    }
}
