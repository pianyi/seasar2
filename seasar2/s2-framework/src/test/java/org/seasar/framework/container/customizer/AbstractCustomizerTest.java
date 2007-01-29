/*
 * Copyright 2004-2007 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.container.customizer;

import junit.framework.TestCase;

import org.seasar.framework.container.ComponentDef;
import org.seasar.framework.container.impl.ComponentDefImpl;

/**
 * @author koichik
 * 
 */
public class AbstractCustomizerTest extends TestCase {

    private boolean matched;

    public void testNoPattern() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertTrue(matched);
    }

    public void testApplyOnly() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addClassPattern("org.seasar.framework.container.customizer",
                "AbstractCustomizerTest\\$B.*");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertTrue(matched);
    }

    public void testIgnoreOnly() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addIgnoreClassPattern(
                "org.seasar.framework.container.customizer",
                "AbstractCustomizerTest\\$B.*");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertFalse(matched);
    }

    public void testApplyIgnore() throws Exception {
        TestCustomizer customizer = new TestCustomizer();
        customizer.addClassPattern("org.seasar.framework.container.customizer",
                "AbstractCustomizerTest\\$B.*");
        customizer.addIgnoreClassPattern(
                "org.seasar.framework.container.customizer", ".*z");
        matched = false;
        customizer.customize(new ComponentDefImpl(Foo.class));
        assertFalse(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Bar.class));
        assertTrue(matched);
        matched = false;
        customizer.customize(new ComponentDefImpl(Baz.class));
        assertFalse(matched);
    }

    public class TestCustomizer extends AbstractCustomizer {
        protected void doCustomize(ComponentDef componentDef) {
            System.out.println(componentDef.getComponentClass().getName());
            matched = true;
        }
    }

    public static class Foo {
    }

    public static class Bar {
    }

    public static class Baz {
    }
}
