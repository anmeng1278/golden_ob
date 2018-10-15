package com.jsj.member.ob.dto;

import com.sargeraswang.util.ExcelUtil.ExcelCell;


/**
 * The <code>Model</code>
 *
 * @author SargerasWang Created at 2014年8月7日 下午5:09:29
 */
public class ExcelDto {

    @ExcelCell(index = 0)
    private String a;
    @ExcelCell(index = 1)
    private String b;
    @ExcelCell(index = 2)
    private String c;
    @ExcelCell(index = 3)
    private String d;
    @ExcelCell(index = 4)
    private String e;

    @ExcelCell(index = 5)
    private String f;
    @ExcelCell(index = 6)
    private String g;
    @ExcelCell(index = 7)
    private String h;
    @ExcelCell(index = 8)
    private String i;
    @ExcelCell(index = 9)
    private String j;

    @ExcelCell(index = 10)
    private String k;

    @ExcelCell(index = 11)
    private String l;
    @ExcelCell(index = 12)
    private String m;
    @ExcelCell(index = 13)
    private String n;
    @ExcelCell(index = 14)
    private String o;

    @ExcelCell(index = 15)
    private String p;
    @ExcelCell(index = 16)
    private String q;


    public ExcelDto() {
    }

    public ExcelDto(String a, String b, String c, String d, String e) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
    }

    public ExcelDto(String a, String b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public ExcelDto(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n, String o, String p, String q) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;

        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;

        this.k = k;
        this.l = l;
        this.m = m;
        this.n = n;
        this.o = o;

        this.p = p;
        this.q = q;
    }

    public ExcelDto(String a, String b, String c, String d, String e, String f, String g, String h, String i) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }


    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }


}