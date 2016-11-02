package com.agtokty;

/**
 * Created by agit on 07/10/2016.
 */

public class YemekhaneUtil {

    public static int hangiay(String ay) {

        if (ay.equals("Ocak"))
            return 1;
        else if (ay.equals("Subat") || ay.equals("Şubat"))
            return 2;
        else if (ay.equals("Mart"))
            return 3;
        else if (ay.equals("Nisan") || ay.equals("Nısan"))
            return 4;
        else if (ay.equals("Mayis") || ay.equals("Mayıs"))
            return 5;
        else if (ay.equals("Haziran") || ay.equals("Hazıran"))
            return 6;
        else if (ay.equals("Temmuz"))
            return 7;
        else if (ay.equals("Agustos") || ay.equals("Ağustos"))
            return 8;
        else if (ay.equals("Eylül") || ay.equals("Eylul"))
            return 9;
        else if (ay.equals("Ekim") || ay.equals("Ekım"))
            return 10;
        else if (ay.equals("Kasim") || ay.equals("Kasım"))
            return 11;
        else if (ay.equals("Aralik") || ay.equals("Aralık"))
            return 12;
        else
            return 13;

    }
}
