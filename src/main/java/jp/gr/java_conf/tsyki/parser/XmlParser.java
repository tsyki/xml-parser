package jp.gr.java_conf.tsyki.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import jp.gr.java_conf.tsyki.util.FileUtil;
import jp.gr.java_conf.tsyki.visitor.NodeVisitor;

/**
 * 指定のディレクトリ以下のxmlを探索し、NodeVisitorに処理させます
 * @author TOSHIYUKI.IMAIZUMI
 */
public class XmlParser {

    private List<NodeVisitor> visitors = new ArrayList<>();

    public void addVisitor( NodeVisitor visitor) {
        visitors.add( visitor);
    }

    /**
     * 指定ディレクトリ以下の指定拡張子のxmlファイルをパースしてvisitorsのvisitを呼び出します
     */
    public void parseDirectory( String rootDirPath, String parseFileSuffix) throws IOException {
        parseDirectory( rootDirPath, parseFileSuffix, new ArrayList<>());
    }

    /**
     * 指定ディレクトリ以下の指定拡張子のxmlファイルをパースしてvisitorsのvisitを呼び出します。
     * @param excludeDirNames
     *            探索を行わないディレクトリ名。このディレクトリ以下は全て探索しません
     */
    public void parseDirectory( String rootDirPath, String parseFileSuffix, List<String> excludeDirNames) throws IOException {
        List<Path> subFiles = FileUtil.listSubordinateFile( rootDirPath, parseFileSuffix, excludeDirNames);
        for ( Path path : subFiles) {
            System.out.println( "parse " + path.toString());
            parse( path.toString());
        }
    }

    /**
     * 指定のxmlをパースしてvisitorsのvisitを呼び出します
     */
    public void parse( String xmlPath) {
        Element rootXmlNode = parseXml( xmlPath);
        recursiveSearch( rootXmlNode);
    }

    private void visit( Node xmlNode) {
        for ( NodeVisitor visitor : visitors) {
            visitor.visit( xmlNode);
        }
    }

    private void afterChildVisit( Node xmlNode) {
        for ( NodeVisitor visitor : visitors) {
            visitor.afterChildVisit( xmlNode);
        }
    }

    private void recursiveSearch( Node parentXMLNode) {
        // 空白や改行のゴミは見ない
        if ( parentXMLNode.getNodeType() == Node.TEXT_NODE)
            return;
        else {
            // 何か処理させる
            visit( parentXMLNode);
            NodeList nodeList = parentXMLNode.getChildNodes();
            if ( nodeList != null) {
                for ( int i = 0; i < nodeList.getLength(); i++) {
                    Node childXMLNode = nodeList.item( i);
                    if ( childXMLNode.getNodeType() == Node.TEXT_NODE)
                        continue;
                    recursiveSearch( childXMLNode);
                }
            }
            afterChildVisit( parentXMLNode);
        }
    }

    /**
     * 指定されたxmlをパースしてrootノードを返します
     * @param xmlPath
     * @return
     */
    public Element parseXml( String xmlPath) {
        // Nodeの木構造を作成する
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = dbfactory.newDocumentBuilder();
            doc = builder.parse( new File( xmlPath));
        }
        catch ( Exception e) {
            // 不正なファイルパスだったら適当にエラーにしておく
            throw new RuntimeException( e);
        }
        // rootのタグ取得
        Element rootXMLNode = doc.getDocumentElement();
        return rootXMLNode;
    }

}
