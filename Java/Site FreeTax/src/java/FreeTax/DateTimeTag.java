/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FreeTax;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author rapha
 */
public class DateTimeTag {
    public static String tagTime()
    {
        return new SimpleDateFormat("dd/MM/YYY hh:mm:ss")
                   .format(Calendar.getInstance().getTime());
    }
    
    public static long currentMillis()
    {
        return Calendar.getInstance().getTimeInMillis();
    }
    
    public static int usingTime(long start)
    {
        return (int) ((currentMillis() - start) / 1000);
    }
}
