package com.sohu.mtc.push.thread;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.ClientConnectionManager;

public class IdleHttpConnMonitorThread extends Thread {

	private final ClientConnectionManager connMgr;
    private volatile boolean shutdown;
    
    public IdleHttpConnMonitorThread(ClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(1000 * 60);
                    // Close expired connections
                    connMgr.closeExpiredConnections();
                    // Optionally, close connections
                    // that have been idle longer than 30 sec
                    // 相当于设置了keep-alive
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }
    
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
    
}
