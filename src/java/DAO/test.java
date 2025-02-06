/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Model.Sex;
import java.util.List;

/**
 *
 * @author admin1
 */
public class test {

    public static void main(String[] args) {
        try {
            SexDAO sexDAO = new SexDAO();
            List<Sex> sexes = sexDAO.getAllSexes();
            for (Sex sex : sexes) {
                System.out.println(sex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
