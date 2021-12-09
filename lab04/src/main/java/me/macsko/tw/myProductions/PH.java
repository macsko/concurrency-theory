package me.macsko.tw.myProductions;

import me.macsko.tw.mesh.Vertex;
import me.macsko.tw.production.AbstractProduction;
import me.macsko.tw.production.PDrawer;

public class PH extends AbstractProduction<Vertex> {

    public PH(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex M1) {
        System.out.println("PH");
        Vertex M2 = M1.getUp().getLeft().getDown();
        M1.setLeft(M2);
        M2.setRight(M1);
        return M1;
    }
}
