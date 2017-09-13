package jp.gr.java_conf.tsyki.visitor;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import jp.gr.java_conf.tsyki.parser.XmlParser;

/**
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2017/09/13
 */
public class TagNameCountVisitorTest {
    @Test
    public void test() throws IOException {
        XmlParser parser = new XmlParser();
        // 部品の出現数カウント
        TagNameCountVisitor visitor = new TagNameCountVisitor();
        parser.addVisitor( visitor);
        parser.parse( getPath( "TagNameCountVisitorTest.xml"));
        Map<String, Long> actualMap = visitor.getCountMap();
        Map<String, Long> expectedMap = new HashMap<>();
        expectedMap.put( "UiBinder", 1L);
        expectedMap.put( "VerticalPanel", 1L);
        expectedMap.put( "HTML", 4L);
        expectedMap.put( "Label", 1L);
        expectedMap.put( "Cell", 1L);
        expectedMap.put( "Button", 1L);

        assertThat( actualMap, is( equalTo( expectedMap)));
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
