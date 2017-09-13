package jp.gr.java_conf.tsyki.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import jp.gr.java_conf.tsyki.parser.XmlParser;
import jp.gr.java_conf.tsyki.visitor.EdmDuplicateEntityVisitor.DuplicateInfo;

/**
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2017/09/13
 */
public class EdmDuplicateEntityVisitorTest {
    @Test
    public void testNotDuplicate() throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( getPath( "EdmDuplicateEntityVisitorTest_notDup.edm"));

        assertThat( visitor.getDuplicateInfos(), is( empty()));
    }

    @Test
    public void testDuplicate() throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( getPath( "EdmDuplicateEntityVisitorTest_dup.edm"));

        Set<DuplicateInfo> actual = visitor.getDuplicateInfos();
        Set<DuplicateInfo> expected = new LinkedHashSet<>();
        expected.add( new DuplicateInfo( "メインモデル", 1));
        expected.add( new DuplicateInfo( "サブモデル", 3));
        expected.add( new DuplicateInfo( "サブモデル", 4));
        assertThat( actual, is( equalTo( expected)));
    }

    @Test
    public void testLogicalName() throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( getPath( "EdmDuplicateEntityVisitorTest_dup.edm"));

        Map<Long, String> actual = visitor.getEntityLogicalNameMap();
        Map<Long, String> expected = new HashMap<>();
        expected.put( 1L, "エンティティ1");
        expected.put( 2L, "エンティティ2");
        expected.put( 3L, "エンティティ3");
        expected.put( 4L, "エンティティ4");
        expected.put( 5L, "エンティティ5");
        assertThat( actual, is( equalTo( expected)));
    }

    @Test
    public void testPhysicalName() throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( getPath( "EdmDuplicateEntityVisitorTest_dup.edm"));

        Map<Long, String> actual = visitor.getEntityPhysicalNameMap();
        Map<Long, String> expected = new HashMap<>();
        expected.put( 1L, "entity_1");
        expected.put( 2L, "entity_2");
        expected.put( 3L, "entity_3");
        expected.put( 4L, "entity_4");
        expected.put( 5L, "entity_5");
        assertThat( actual, is( equalTo( expected)));
    }

    private String getPath( String fileName) {
        URL url = this.getClass().getResource( fileName);
        try {
            String path = URLDecoder.decode( url.getFile(), "UTF-8");
            return path;
        }
        catch ( UnsupportedEncodingException e) {
            // 起こらないだろう
            throw new RuntimeException( e);
        }
    }
}
