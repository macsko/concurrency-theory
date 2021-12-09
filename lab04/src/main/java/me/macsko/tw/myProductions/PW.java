package me.macsko.tw.myProductions;

import me.macsko.tw.mesh.Vertex;
import me.macsko.tw.production.AbstractProduction;
import me.macsko.tw.production.PDrawer;

public class PW extends AbstractProduction<Vertex> {

    public PW(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex M1) {
        System.out.println("PW");
        Vertex M2 = new Vertex(null, M1, null, null, "M");
        M1.setLeft(M2);
        return M2;
    }
}
