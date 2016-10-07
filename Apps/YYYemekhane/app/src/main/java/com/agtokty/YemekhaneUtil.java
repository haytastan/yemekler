package com.agtokty;

/**
 * Created by agit on 07/10/2016.
 */

public class YemekhaneUtil {

    public static int hangiay(String ay) {

        if (ay.equals("Ocak"))
            return 1;
        else if (ay.equals("Subat"))
            return 2;
        else if (ay.equals("Mart"))
            return 3;
        else if (ay.equals("Nisan"))
            return 4;
        else if (ay.equals("Mayis"))
            return 5;
        else if (ay.equals("Haziran"))
            return 6;
        else if (ay.equals("Temmuz"))
            return 7;
        else if (ay.equals("Agustos"))
            return 8;
        else if (ay.equals("Eylül"))
            return 9;
        else if (ay.equals("Ekim"))
            return 10;
        else if (ay.equals("Kasim"))
            return 11;
        else if (ay.equals("Aralik"))
            return 12;
        else
            return 13;

    }
}
