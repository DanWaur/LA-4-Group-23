package controller;

// import model...;
import model.Observer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class YahtController implements ActionListener {
    // private Model model;

    public YahtController() {
        // this.model = new Model()
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        /*
         * TODO:
         * add commands for:
         * rolling,
         * holding,
         * adding to score,
         * updating which score categories are met
         */
    }

    public void addObserver(Observer observer) {
        // model.registerObserver(observer);
    }
}
