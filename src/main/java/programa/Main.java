package programa;
import view.*;

import javax.swing.*;
import sistema.Hospital;

public class Main {
    public static void main(String[] args) {

        JFrame telaLogin = new TelaLogin(new Hospital());
        telaLogin.setVisible(true);
    }
}
