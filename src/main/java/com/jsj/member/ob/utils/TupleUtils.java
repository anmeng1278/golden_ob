package com.jsj.member.ob.utils;

import com.jsj.member.ob.tuple.ThreeTuple;
import com.jsj.member.ob.tuple.TwoTuple;

public class TupleUtils {

    public static <A, B> TwoTuple<A, B> tuple(A a, B b) {
        return new TwoTuple<A, B>(a, b);
    }

    public static <A, B, C> ThreeTuple<A, B, C> tuple(A a, B b, C c) {
        return new ThreeTuple<A, B, C>(a, b, c);
    }
}