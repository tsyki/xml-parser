package jp.gr.java_conf.tsyki.visitor;

import org.w3c.dom.Node;

/**
 * XML探索用Visitor
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2016/08/02
 */
public interface NodeVisitor {

    void visit( Node xmlNode);
}
