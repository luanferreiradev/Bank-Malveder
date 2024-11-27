package application;

import controler.Controler;
import view.Login;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            Controler controler = new Controler();
            controler.verificarECriarFuncionarioRoot();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new Login();
    }
}

//@autor: Luan Henrique de Souza FerreiraLICENSE