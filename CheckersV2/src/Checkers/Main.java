package Checkers;

import Checkers.Controller.Controller;
import Checkers.Model.Model;
import Checkers.View.GUI;
import Checkers.View.ViewFacade;

public class Main {

    public static void main(String[] args) {
        Controller cont = new Controller();
        ViewFacade vw = new GUI(cont);
        Model model = new Model();
        cont.setComponents(vw,model);
	    cont.run();
    }
}
