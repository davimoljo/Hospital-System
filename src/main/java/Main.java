import view.*;
import javax.swing.*;
import sistema.Hospital;

/*
    João Pedro Lemos Guadalupe
    Matrícula: 202435004

    Davi Moljo Domingues
    Matrícula: 202435031

    Carlos Roberto da Silva
    Matrícula: 202435016
*/

public class Main {
    public static void main(String[] args) {

        JFrame telaLogin = new TelaLogin(new Hospital());
        telaLogin.setVisible(true);
    }
}
