package com.example.multiwind.rxtest;

public class Factorial {

    private int var;
    private int res;
    private String calcProc;

    public Factorial(int var) {
        this.var = var;
    }

    public int getVar() {
        return var;
    }

    public void setVar(int var) {
        this.var = var;
    }

    public String getCalcProc() {

        return calcProc;
    }

    public void setCalcProc(String calcProc) {
        this.calcProc = calcProc;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
