package net.java.html.junit;

import net.java.html.js.JavaScriptBody;
import org.junit.runner.notification.RunListener;

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

abstract class AbstractTestRunner {

    @JavaScriptBody(args = {"html"}, body = ""
        + "var element = document.getElementById('junit-browser-runner');\n"
        + "if (!element) {\n" + "  element = document.createElement('div');\n"
        + "  element.id = 'junit-browser-runner';\n"
        + "  document.body.appendChild(element);\n"
        + "}\n"
        + "element.innerHTML = html;\n"
    )
    static native void exposeHTML(String html);
    
    abstract String name();
    abstract RunListener listener();
    abstract void invokeLater(Runnable run);
    abstract void invokeNow(Runnable runnable);
}
