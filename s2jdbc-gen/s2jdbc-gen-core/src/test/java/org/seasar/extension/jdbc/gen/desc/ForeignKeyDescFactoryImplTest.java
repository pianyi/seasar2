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
package org.seasar.extension.jdbc.gen.desc;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.junit.Before;
import org.junit.Test;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.gen.ForeignKeyDesc;
import org.seasar.extension.jdbc.gen.desc.ForeignKeyDescFactoryImpl;
import org.seasar.extension.jdbc.meta.ColumnMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.extension.jdbc.meta.TableMetaFactoryImpl;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.framework.convention.impl.PersistenceConventionImpl;

import static org.junit.Assert.*;

/**
 * @author taedium
 * 
 */
public class ForeignKeyDescFactoryImplTest {

    private PropertyMetaFactoryImpl propertyMetaFactory;

    private EntityMetaFactoryImpl entityMetaFactory;

    private ForeignKeyDescFactoryImpl foreignKeyDescFactory;

    @Before
    public void setUp() throws Exception {
        PersistenceConvention pc = new PersistenceConventionImpl();
        ColumnMetaFactoryImpl cmf = new ColumnMetaFactoryImpl();
        cmf.setPersistenceConvention(pc);
        propertyMetaFactory = new PropertyMetaFactoryImpl();
        propertyMetaFactory.setPersistenceConvention(pc);
        propertyMetaFactory.setColumnMetaFactory(cmf);
        TableMetaFactoryImpl tmf = new TableMetaFactoryImpl();
        tmf.setPersistenceConvention(pc);
        entityMetaFactory = new EntityMetaFactoryImpl();
        entityMetaFactory.setPersistenceConvention(pc);
        entityMetaFactory.setPropertyMetaFactory(propertyMetaFactory);
        entityMetaFactory.setTableMetaFactory(tmf);
        foreignKeyDescFactory = new ForeignKeyDescFactoryImpl(entityMetaFactory);
    }

    @Test
    public void testSingleForeignKey() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ForeignKeyDesc foreignKeyDesc = foreignKeyDescFactory
                .getForeignKeyDesc(entityMeta.getPropertyMeta("bbb"));
        assertNotNull(foreignKeyDesc);
        assertEquals(1, foreignKeyDesc.getColumnNameList().size());
        assertEquals("BBB_ID", foreignKeyDesc.getColumnNameList().get(0));
        assertEquals("hoge", foreignKeyDesc.getReferencedCatalogName());
        assertEquals("foo", foreignKeyDesc.getReferencedSchemaName());
        assertEquals("BBB", foreignKeyDesc.getReferencedTableName());
        assertEquals(1, foreignKeyDesc.getReferencedColumnNameList().size());
        assertEquals("ID", foreignKeyDesc.getReferencedColumnNameList().get(0));
    }

    @Test
    public void testCompositeForeignKey() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ForeignKeyDesc foreignKeyDesc = foreignKeyDescFactory
                .getForeignKeyDesc(entityMeta.getPropertyMeta("ccc"));
        assertNotNull(foreignKeyDesc);
        assertEquals(2, foreignKeyDesc.getColumnNameList().size());
        assertEquals("CCC_ID1", foreignKeyDesc.getColumnNameList().get(0));
        assertEquals("CCC_ID2", foreignKeyDesc.getColumnNameList().get(1));
        assertEquals("hoge", foreignKeyDesc.getReferencedCatalogName());
        assertEquals("foo", foreignKeyDesc.getReferencedSchemaName());
        assertEquals("CCC", foreignKeyDesc.getReferencedTableName());
        assertEquals(2, foreignKeyDesc.getReferencedColumnNameList().size());
        assertEquals("ID1", foreignKeyDesc.getReferencedColumnNameList().get(0));
        assertEquals("ID2", foreignKeyDesc.getReferencedColumnNameList().get(1));
    }

    @Test
    public void testNoForeignKey() throws Exception {
        EntityMeta entityMeta = entityMetaFactory.getEntityMeta(Aaa.class);
        ForeignKeyDesc foreignKeyDesc = foreignKeyDescFactory
                .getForeignKeyDesc(entityMeta.getPropertyMeta("id"));
        assertNull(foreignKeyDesc);
    }

    @Entity
    @Table(catalog = "hoge", schema = "foo", name = "AAA")
    public static class Aaa {

        @Id
        public Integer id;

        public Integer bbbId;

        public Integer cccId1;

        public Integer cccId2;

        @ManyToOne
        public Bbb bbb;

        @OneToOne
        @JoinColumns( {
                @JoinColumn(name = "CCC_ID1", referencedColumnName = "ID1"),
                @JoinColumn(name = "CCC_ID2", referencedColumnName = "ID2") })
        public Ccc ccc;
    }

    @Entity
    @Table(catalog = "hoge", schema = "foo", name = "BBB")
    public static class Bbb {

        @Id
        public Integer id;

    }

    @Entity
    @Table(catalog = "hoge", schema = "foo", name = "CCC")
    public static class Ccc {

        @Id
        public Integer id1;

        @Id
        public Integer id2;

    }
}