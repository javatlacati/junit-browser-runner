package net.java.html.junit;

import static org.junit.Assert.fail;
import org.junit.Test;

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

public class FailingInBrowserTest {
    @Test
    public void runAndFail() throws ClassNotFoundException, InterruptedException {
        try {
            BrowserRunner.execute(FailInBrowser.class.getName());
        } catch (AssertionError ex) {
            if (ex.getMessage().contains("Simulate a failure")) {
                return;
            }
        }
        fail("Expecting failure in a test run");
    }
}
