package controller;

import java.text.DecimalFormat;
import java.util.concurrent.Semaphore;

public class ThreadServidor extends Thread {

    Semaphore semaforo;
    private int idThread;

    public ThreadServidor(int idThread, Semaphore semaforo) {
        this.idThread = idThread;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        try {
            execCalc();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }

    private void execCalc() throws InterruptedException {
        if (idThread % 3 == 1) {
            execTransacao(0.2, 1.0, 1.0);
            execTransacao(0.2, 1.0, 1.0);
        } else if (idThread % 3 == 2) {
            execTransacao(0.5, 1.5, 1.5);
            execTransacao(0.5, 1.5, 1.5);
            execTransacao(0.5, 1.5, 1.5);
        } else {
            execTransacao(1.0, 2.0, 1.5);
            execTransacao(1.0, 2.0, 1.5);
            execTransacao(1.0, 2.0, 1.5);
        }
    }

    private void execTransacao(double tempoMin, double tempoMax, double tempo) throws InterruptedException {
        calc(tempoMin, tempoMax);
        calcBD(tempo);
    }

    private void calc(double tempoMin, double tempoMax) throws InterruptedException {
        double tempo = (Math.random() * (tempoMax - tempoMin + 1)) + tempoMin;
        DecimalFormat df = new DecimalFormat("#0.00");
        String tempoFormatado = df.format(tempo);
        System.out.println("Thread #" + idThread + " realizou os cálculos em: " + tempoFormatado + "s.");
        sleep((long) (tempo * 1000));
    }

    private void calcBD(double tempo) throws InterruptedException {
        try {
            semaforo.acquire();
            System.out.println("Thread #" + idThread + " realizando transação de BD.");
            sleep((int) tempo * 1000);
            System.out.println("Thread #" + idThread + " realizou a transação de BD.");
        } finally {
            semaforo.release();
        }
    }
}
