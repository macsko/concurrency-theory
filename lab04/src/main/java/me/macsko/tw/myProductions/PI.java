package me.macsko.tw.myProductions;

import me.macsko.tw.mesh.Vertex;
import me.macsko.tw.production.AbstractProduction;
import me.macsko.tw.production.PDrawer;

public class PI extends AbstractProduction<Vertex> {

    public PI(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex s) {
        System.out.println("PI");
        Vertex M = new Vertex(null, null, null, null, "M");
        return M;
    }
}
