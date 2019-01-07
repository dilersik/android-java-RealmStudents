package com.dilerdesenvolv.realmstudents.bo;

/**
 * Created by T-Gamer on 09/07/2016.
 */
public class DisciplineBO {

    public static String isName(String name) {
        if (name.length() < 1 || name.length() > 50) {
            return "Deve conter entre 1 e 50 caracteres";
        }

        return "1";
    }

}
