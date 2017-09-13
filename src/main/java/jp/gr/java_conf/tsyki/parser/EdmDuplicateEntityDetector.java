package jp.gr.java_conf.tsyki.parser;

import java.io.IOException;
import java.util.Collection;

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
        EdmDuplicateEntityVisitor visitor = detector.detect( args[0]);
        detector.showDuplicateInfo( visitor);
    }

    public EdmDuplicateEntityVisitor detect( String path) throws IOException {
        XmlParser parser = new XmlParser();
        EdmDuplicateEntityVisitor visitor = new EdmDuplicateEntityVisitor();
        parser.addVisitor( visitor);
        parser.parse( path);
        return visitor;
    }

    public void showDuplicateInfo( EdmDuplicateEntityVisitor visitor) {
        Collection<DuplicateInfo> dupInfos = visitor.getDuplicateInfos();
        if ( dupInfos.isEmpty()) {
            System.out.println( "重複するENTITYタグは存在しませんでした。");
            return;
        }
        for ( DuplicateInfo dupInfo : dupInfos) {
            Long entityId = dupInfo.getEntityId();
            StringBuilder sb = new StringBuilder();
            sb.append( "モデル名=");
            sb.append( dupInfo.getModelViewName());
            sb.append( " 重複しているENTITY");
            sb.append( " ID=");
            sb.append( entityId);
            sb.append( " 論理名=");
            sb.append( visitor.getEntityLogicalNameMap().get( entityId));
            sb.append( " 物理名=");
            sb.append( visitor.getEntityPhysicalNameMap().get( entityId));
            System.out.println( sb.toString());
        }
    }
}
