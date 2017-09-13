package jp.gr.java_conf.tsyki.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jp.gr.java_conf.tsyki.visitor.TagNameCountVisitor;

/**
 * 指定のディレクトリ以下のui.xmlを探索し、各部品の出現数を出力します
 * @author TOSHIYUKI.IMAIZUMI
 */
public class WidgetCounter {
    public static void main( String[] args) throws IOException {
        if ( args.length == 0) {
            System.err.println( "パース対象のディレクトリを指定してください。");
            return;
        }
        WidgetCounter counter = new WidgetCounter();
        Map<String, Long> countMap = counter.count( args);
        counter.writeResult( countMap);
    }

    /**
     * 指定したディレクトリは以下のui.xmlファイルの要素の出現数のMapを返す
     * @param rootDirPathes
     * @throws IOException
     */
    public Map<String, Long> count( String... rootDirPathes) throws IOException {
        XmlParser parser = new XmlParser();
        // 部品の出現数カウント
        TagNameCountVisitor visitor = new TagNameCountVisitor();
        parser.addVisitor( visitor);

        // 色々やりたい場合はここでvisitorを追加すればOK

        // NOTE classes以下にもui.xmlがコピーされているので二重に加算されてしまう
        List<String> excludeDirNames = new ArrayList<>();
        excludeDirNames.add( "classes");

        for ( String rootDirPath : rootDirPathes) {
            System.out.println( "parse " + rootDirPath);

            // ui.xmlのみパースさせる
            parser.parseDirectory( rootDirPath, ".ui.xml", excludeDirNames);
        }
        return visitor.getCountMap();
    }

    /**
     * 出現数の多い順にタグ名と出現数を出力する
     * @param countMap
     */
    public void writeResult( Map<String, Long> countMap) {
        // 出現数の降順でソート
        List<Map.Entry<String, Long>> entries = new ArrayList<Map.Entry<String, Long>>( countMap.entrySet());
        Collections.sort( entries, new Comparator<Map.Entry<String, Long>>() {

            @Override
            public int compare( Entry<String, Long> entry1, Entry<String, Long> entry2) {
                return (( Long) entry2.getValue()).compareTo( ( Long) entry1.getValue());
            }
        });
        for ( Entry<String, Long> entry : entries) {
            String nodeName = entry.getKey();
            Long count = entry.getValue();
            System.out.println( nodeName + " " + count);
        }
    }
}
