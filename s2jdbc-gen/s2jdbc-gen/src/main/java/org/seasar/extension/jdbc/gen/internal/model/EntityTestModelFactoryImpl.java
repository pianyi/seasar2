/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
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
package org.seasar.extension.jdbc.gen.internal.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import org.junit.runner.RunWith;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.gen.model.EntityTestModel;
import org.seasar.extension.jdbc.gen.model.EntityTestModelFactory;
import org.seasar.extension.unit.S2TestCase;
import org.seasar.framework.unit.Seasar2;
import org.seasar.framework.unit.TestContext;
import org.seasar.framework.util.ClassUtil;

/**
 * {@link EntityTestModelFactory}の実装クラスです。
 * 
 * @author taedium
 */
public class EntityTestModelFactoryImpl implements EntityTestModelFactory {

    /** 設定ファイルのパス */
    protected String configPath;

    /** {@link JdbcManager}のコンポーネント名 */
    protected String jdbcManagerName;

    /** テストクラス名のサフィックス */
    protected String testClassNameSuffix;

    /** S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false} */
    protected boolean useS2junit4;

    /** クラスモデルのサポート */
    protected ClassModelSupport classModelSupport = new ClassModelSupport();

    /**
     * インスタンスを構築します。
     * 
     * @param configPath
     *            設定ファイルのパス
     * @param jdbcManagerName
     *            {@link JdbcManager}のコンポーネント名
     * @param testClassNameSuffix
     *            テストクラス名のサフィックス
     * @param useS2junit4
     *            S2JUnit4を使用する場合{@code true}、S2Unitを使用する場合{@code false}
     */
    public EntityTestModelFactoryImpl(String configPath,
            String jdbcManagerName, String testClassNameSuffix,
            boolean useS2junit4) {
        if (configPath == null) {
            throw new NullPointerException("configPath");
        }
        if (jdbcManagerName == null) {
            throw new NullPointerException("jdbcManagerName");
        }
        if (testClassNameSuffix == null) {
            throw new NullPointerException("testClassNameSuffix");
        }
        this.configPath = configPath;
        this.jdbcManagerName = jdbcManagerName;
        this.testClassNameSuffix = testClassNameSuffix;
        this.useS2junit4 = useS2junit4;
    }

    public EntityTestModel getEntityTestModel(EntityMeta entityMeta) {
        EntityTestModel entityTestModel = new EntityTestModel();
        entityTestModel.setConfigPath(configPath);
        entityTestModel.setJdbcManagerName(jdbcManagerName);
        String packageName = ClassUtil.splitPackageAndShortClassName(entityMeta
                .getEntityClass().getName())[0];
        entityTestModel.setPackageName(packageName);
        entityTestModel.setShortClassName(entityMeta.getName()
                + testClassNameSuffix);
        entityTestModel.setShortEntityClassName(entityMeta.getName());
        entityTestModel.setUseS2junit4(useS2junit4);
        doIdValue(entityTestModel, entityMeta);
        doAssociationName(entityTestModel, entityMeta);
        doImportName(entityTestModel, entityMeta);
        return entityTestModel;
    }

    /**
     * 識別子の式を処理します。
     * 
     * @param entityTestModel
     *            テストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doIdValue(EntityTestModel entityTestModel,
            EntityMeta entityMeta) {
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            Class<?> propertyClass = propertyMeta.getPropertyClass();
            entityTestModel.addIdExpression(getExpression(propertyClass));
        }
    }

    /**
     * プロパティのクラスの値を表す式を取得します。
     * 
     * @param propertyClass
     *            プロパティのクラス
     * @return 識別子の式
     */
    protected String getExpression(Class<?> propertyClass) {
        Class<?> clazz = ClassUtil.getPrimitiveClassIfWrapper(propertyClass);
        if (clazz.isPrimitive()) {
            if (clazz == boolean.class) {
                return "true";
            }
            if (clazz == char.class) {
                return "'a'";
            }
            if (clazz == byte.class) {
                return "(byte) 1";
            }
            if (clazz == short.class) {
                return "(short) 1";
            }
            if (clazz == int.class) {
                return "1";
            }
            if (clazz == long.class) {
                return "1L";
            }
            if (clazz == float.class) {
                return "1f";
            }
            if (clazz == double.class) {
                return "1d";
            }
        }
        if (clazz == String.class) {
            return "\"aaa\"";
        }
        if (clazz == BigDecimal.class) {
            return "BigDecimal.ONE";
        }
        if (clazz == BigInteger.class) {
            return "BigInteger.ONE";
        }
        if (clazz == Date.class) {
            return "new Date()";
        }
        if (clazz == Calendar.class) {
            return "Calendar.getInstance()";
        }
        if (clazz == java.sql.Date.class) {
            return "Date.valueOf(\"2008-01-01\")";
        }
        if (clazz == Timestamp.class) {
            return "Timestamp.valueOf(\"2008-01-01 12:00:00\")";
        }
        if (clazz == Time.class) {
            return "Time.valueOf(\"12:00:00\")";
        }
        throw new IllegalArgumentException("propertyClass");
    }

    /**
     * 関連名を処理します。
     * 
     * @param entityTestModel
     *            テストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doAssociationName(EntityTestModel entityTestModel,
            EntityMeta entityMeta) {
        for (PropertyMeta propertyMeta : entityMeta.getAllPropertyMeta()) {
            if (propertyMeta.isRelationship()) {
                entityTestModel.addAssociationName(propertyMeta.getName());
            }
        }
    }

    /**
     * インポート名を処理します。
     * 
     * @param entityTestModel
     *            テストモデル
     * @param entityMeta
     *            エンティティメタデータ
     */
    protected void doImportName(EntityTestModel entityTestModel,
            EntityMeta entityMeta) {
        classModelSupport.addImportName(entityTestModel, JdbcManager.class);
        if (useS2junit4) {
            classModelSupport.addImportName(entityTestModel, RunWith.class);
            classModelSupport.addImportName(entityTestModel, Seasar2.class);
            classModelSupport.addImportName(entityTestModel, TestContext.class);
        } else {
            classModelSupport.addImportName(entityTestModel, S2TestCase.class);
        }
        for (PropertyMeta propertyMeta : entityMeta.getIdPropertyMetaList()) {
            classModelSupport.addImportName(entityTestModel, propertyMeta
                    .getPropertyClass());
        }
    }
}