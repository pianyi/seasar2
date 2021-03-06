/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
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
package org.seasar.framework.container.util;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.AspectDef;
import org.seasar.framework.container.S2Container;

/**
 * {@link AspectDef}の補助クラスです。
 * 
 * @author higa
 * 
 */
public class AspectDefSupport {

    private List aspectDefs = new ArrayList();

    private S2Container container;

    /**
     * {@link AspectDefSupport}を作成します。
     */
    public AspectDefSupport() {
    }

    /**
     * {@link AspectDef}を追加します。
     * 
     * @param aspectDef
     */
    public void addAspectDef(AspectDef aspectDef) {
        if (container != null) {
            aspectDef.setContainer(container);
        }
        aspectDefs.add(aspectDef);
    }

    /**
     * {@link AspectDef}を追加します。
     * 
     * @param index
     * @param aspectDef
     */
    public void addAspectDef(int index, AspectDef aspectDef) {
        if (container != null) {
            aspectDef.setContainer(container);
        }
        aspectDefs.add(index, aspectDef);
    }

    /**
     * {@link AspectDef}の数を返します。
     * 
     * @return {@link AspectDef}の数
     */
    public int getAspectDefSize() {
        return aspectDefs.size();
    }

    /**
     * {@link AspectDef}を返します。
     * 
     * @param index
     * @return {@link AspectDef}
     */
    public AspectDef getAspectDef(int index) {
        return (AspectDef) aspectDefs.get(index);
    }

    /**
     * {@link S2Container}を設定します。
     * 
     * @param container
     */
    public void setContainer(S2Container container) {
        this.container = container;
        for (int i = 0; i < getAspectDefSize(); ++i) {
            getAspectDef(i).setContainer(container);
        }
    }
}