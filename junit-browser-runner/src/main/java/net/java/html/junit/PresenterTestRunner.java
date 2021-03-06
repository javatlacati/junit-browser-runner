package net.java.html.junit;

import java.util.List;
import java.util.ServiceLoader;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import net.java.html.BrwsrCtx;
import net.java.html.boot.BrowserBuilder;
import org.junit.runner.notification.RunListener;
import org.junit.runners.model.InitializationError;
import org.netbeans.html.boot.spi.Fn;

/*
 * #%L
 * DukeScript JUnit Runner - a library from the DukeScript project.
 * Visit http://dukescript.com for support and commercial license.
 * %%
 * Copyright (C) 2015 - 2016 Dukehoff GmbH
 * %%
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v. 1.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * #L%
 */

final class PresenterTestRunner extends AbstractTestRunner {
    private static final Timer TIMER = new Timer("Invoke Later");
    private final String name;
    private final BrwsrCtx ctx;
    private final RunListener listener;

    PresenterTestRunner(String name, String url, Fn.Presenter p, Class<?> klass) throws InitializationError {
        this.name = name;
        this.ctx = initPresenter(url, p, klass);
        this.listener = new InvokeNowListener(this);
    }

    static void registerPresenters(List<? super AbstractTestRunner> ctxs, String url, Class<?> klass) throws InitializationError {
        for (Fn.Presenter p : ServiceLoader.load(Fn.Presenter.class)) {
            ctxs.add(new PresenterTestRunner(p.getClass().getSimpleName(), url, p, klass));
        }
    }

    private static BrwsrCtx initPresenter(String url, Fn.Presenter p, final Class<?> klass) throws InitializationError {
        final BrwsrCtx[] ret = {null};
        final CountDownLatch cdl = new CountDownLatch(1);
        final BrowserBuilder bb = BrowserBuilder.newBrowser(p).loadFinished(new Runnable() {
            @Override
            public void run() {
                ret[0] = BrwsrCtx.findDefault(klass);
                cdl.countDown();
            }
        }).loadPage(url);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                bb.showAndWait();
            }
        });
        try {
            cdl.await();
        } catch (InterruptedException ex) {
            throw new InitializationError(ex);
        }
        return ret[0];
    }

    @Override
    RunListener listener() {
        return listener;
    }

    @Override
    void invokeNow(final Runnable run) {
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.execute(run);
            }
        }, 1);
    }

    @Override
    void invokeLater(final Runnable run) {
        TIMER.schedule(new TimerTask() {
            @Override
            public void run() {
                ctx.execute(run);
            }
        }, 100);
    }

    @Override
    String name() {
        return name;
    }
}
