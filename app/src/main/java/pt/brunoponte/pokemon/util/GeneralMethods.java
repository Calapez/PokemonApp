package pt.brunoponte.pokemon.util;

public class GeneralMethods {

    public static String capitalizeFirstLetter(String str) {
        if (str.isEmpty())
            return "";

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
