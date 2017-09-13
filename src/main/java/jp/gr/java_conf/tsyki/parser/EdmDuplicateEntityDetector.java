package jp.gr.java_conf.tsyki.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;

import jp.gr.java_conf.tsyki.visitor.EdmDuplicateEntityVisitor;
import jp.gr.java_conf.tsyki.visitor.EdmDuplicateEntityVisitor.DuplicateInfo;

/**
 * Object Browser ERにより作成されたedmファイル内で、同一MODELVIEWタグ内でIDが重複して存在しているENTITYタグを探す
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2017/09/13
 */
public class EdmDuplicateEntityDetector {
    public static void main( String[] args) throws IOException {
        if ( args.length == 0) {
            System.err.println( "検証対象のedmファイルのパスを指定してください。");
            return;
        }
        EdmDuplicateEntityDetector detector = new EdmDuplicateEntityDetector();
        Set<DuplicateInfo> dupInfos = detector.detect( args[0]);
        detector.showDuplicateInfo( dupInfos);
    }

    public Set<DuplicateInfo> detect( String path) throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( path);
        Set<DuplicateInfo> dupInfos = visitor.getDuplicateInfos();
        return dupInfos;
    }

    public void showDuplicateInfo( Collection<DuplicateInfo> dupInfos) {
        if ( dupInfos.isEmpty()) {
            System.out.println( "重複するENTITYタグは存在しませんでした。");
            return;
        }
        for ( DuplicateInfo dupInfo : dupInfos) {
            System.out.println( "モデル名=" + dupInfo.getModelViewName() + " 重複しているENTITY ID=" + dupInfo.getEntityId());
        }
    }
}
