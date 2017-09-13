package jp.gr.java_conf.tsyki.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.tsyki.visitor.WidgetCountVisitor;

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

        XmlParser parser = new XmlParser();
        // 部品の出現数カウント
        WidgetCountVisitor visitor = new WidgetCountVisitor();
        parser.addVisitor( visitor);

        // 色々やりたい場合はここでvisitorを追加すればOK

        // NOTE classes以下にもui.xmlがコピーされているので二重に加算されてしまう
        List<String> excludeDirNames = new ArrayList<>();
        excludeDirNames.add( "classes");

        for ( String rootDirPath : args) {
            System.out.println( "parse " + rootDirPath);

            // ui.xmlのみパースさせる
            parser.parseDirectory( rootDirPath, ".ui.xml", excludeDirNames);
        }

        System.out.println();
        visitor.writeResult();
    }
}
