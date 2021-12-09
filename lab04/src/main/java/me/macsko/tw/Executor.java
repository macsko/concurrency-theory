package me.macsko.tw;

import me.macsko.tw.myProductions.PH;
import me.macsko.tw.myProductions.PW;
import me.macsko.tw.myProductions.PI;
import me.macsko.tw.myProductions.PS;
import me.macsko.tw.mesh.Vertex;
import me.macsko.tw.mesh.GraphDrawer;
import me.macsko.tw.parallelism.BlockRunner;
import me.macsko.tw.production.AbstractProduction;
import me.macsko.tw.production.PDrawer;

public class Executor extends Thread {
    public int N = 10;
    private final BlockRunner runner;
    
    public Executor(BlockRunner _runner){
        this.runner = _runner;
    }

    @Override
    public void run() {

        PDrawer drawer = new GraphDrawer();
        Vertex s = new Vertex(null, null, null, null, "S");

        // PI
        PI PI = new PI(s, drawer);
        this.runner.addThread(PI);

        // start threads
        this.runner.startAll();

        AbstractProduction<Vertex>[] topProds = new AbstractProduction[N];
        topProds[0] = PI;

        // Rozszerzanie siatki w lewo i dół produkcjami PW i PS, dodatkowo łącząc możliwe poziome "dziury" produkcjami PH
        for(int i = 0; i < this.N - 1; i++) {
            for(int j = 0; j < i; j++) {
                Vertex VTop = topProds[j].getObj();
                if(VTop.getUp() != null && VTop.getUp().getLeft() == null) {
                    PH PH = new PH(VTop.getUp(), drawer);
                    this.runner.addThread(PH);
                }
            }

            PW PW = new PW(topProds[i].getObj(), drawer);
            this.runner.addThread(PW);

            for(int j = 0; j < i + 1; j++) {
                PS PS = new PS(topProds[j].getObj(), drawer);
                this.runner.addThread(PS);
                topProds[j] = PS;
            }

            topProds[i + 1] = PW;

            this.runner.startAll();
        }

        // Dokańczanie połowy siatki w dół produkcjami PS oraz łączenie poziomych "dziur" produkcjami PH
        for(int i = 0; i < this.N; i++) {
            for(int j = i; j < N; j++) {
                Vertex VTop = topProds[j].getObj();
                if(VTop.getUp() != null && VTop.getUp().getRight() != null) {
                    PH PH = new PH(VTop.getUp().getRight().getDown(), drawer);
                    this.runner.addThread(PH);
                }
            }

            for(int j = i + 1; j < N; j++) {
                PS PS = new PS(topProds[j].getObj(), drawer);
                this.runner.addThread(PS);
                topProds[j] = PS;
            }

            this.runner.startAll();
        }

        //done
        System.out.println("done");
        drawer.draw(PI.getObj());

    }
}
