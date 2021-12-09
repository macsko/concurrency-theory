package me.macsko.tw.myProductions;

import me.macsko.tw.mesh.Vertex;
import me.macsko.tw.production.AbstractProduction;
import me.macsko.tw.production.PDrawer;

public class PS extends AbstractProduction<Vertex> {

    public PS(Vertex _obj, PDrawer<Vertex> _drawer) {
        super(_obj, _drawer);
    }

    @Override
    public Vertex apply(Vertex M1) {
        System.out.println("PS");
        Vertex M2 = new Vertex(null, null, M1, null, "M");
        M1.setDown(M2);
        return M2;
    }
}
