package io.ibj.JLib.utils;

import java.util.Collection;

/**
 * Created by joe on 12/7/14.
 */
public class StringUtils {
    public static String joinList(Object... list)
    {
        return joinList(", ", list);
    }

    public static String joinList(String seperator, Object... list)
    {
        StringBuilder buf = new StringBuilder();
        for (Object each : list)
        {
            if (buf.length() > 0)
            {
                buf.append(seperator);
            }

            if (each instanceof Collection)
            {
                buf.append(joinList(seperator, ((Collection)each).toArray()));
            }
            else
            {
                try
                {
                    buf.append(each.toString());
                }
                catch (Exception e)
                {
                    buf.append(each.toString());
                }
            }
        }
        return buf.toString();
    }

    public static String joinListSkip(String seperator, String skip, Object... list)
    {
        StringBuilder buf = new StringBuilder();
        for (Object each : list)
        {
            if (each.toString().equalsIgnoreCase(skip)) {
                continue;
            }

            if (buf.length() > 0)
            {
                buf.append(seperator);
            }

            if (each instanceof Collection)
            {
                buf.append(joinListSkip(seperator, skip, ((Collection)each).toArray()));
            }
            else
            {
                try
                {
                    buf.append(each.toString());
                }
                catch (Exception e)
                {
                    buf.append(each.toString());
                }
            }
        }
        return buf.toString();
    }
}
