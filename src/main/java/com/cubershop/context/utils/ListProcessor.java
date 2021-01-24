package com.cubershop.context.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public final class ListProcessor {

    public static <T> List<List<T>> groupBy(List<T> Ts, int n) {
        int len = Ts.size();

        if(len == 0) return Collections.emptyList();

        List<List<T>> output = new Vector<>();

        Iterator<T> iter = Ts.iterator();

        for(int i = 0; i < len / n; i++) {
            List<T> aux = new Vector<>();

            for(int j = 0; j < n; j++)
                if(iter.hasNext())  aux.add(iter.next());

            output.add(aux);

        }

        if(len % n > 0) {
            List<T> aux = new Vector<>();
            for(int i = 0; i < len % n; i++)
                if(iter.hasNext())  aux.add(iter.next());
            output.add(aux);
        }

        return output;
    }
}
