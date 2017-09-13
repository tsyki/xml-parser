package jp.gr.java_conf.tsyki.visitor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Node;

/**
 * Object Browser ERにより作成されたedmファイル内で、同一MODELVIEWタグ内でIDが重複して存在しているENTITYタグを探す
 * @author TOSHIYUKI.IMAIZUMI
 * @since 2017/09/13
 */
public class EdmDuplicateEntityVisitor implements NodeVisitor {

    private static final String TAG_MODELVIEW = "MODELVIEW";

    private static final String ATTR_LOGICAL_NAME = "L-NAME";

    private static final String ATTR_PHYSICAL_NAME = "P-NAME";

    private static final String TAG_ENTITY = "ENTITY";

    private static final String ATTR_ID = "ID";

    public static class DuplicateInfo {
        public DuplicateInfo( String modelViewName, long entityId) {
            this.modelViewName = modelViewName;
            this.entityId = entityId;
        }

        /** モデル名 */
        private String modelViewName;

        /** 重複しているエンティティID */
        private Long entityId;

        public String getModelViewName() {
            return modelViewName;
        }

        public long getEntityId() {
            return entityId;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((entityId == null) ? 0 : entityId.hashCode());
            result = prime * result + ((modelViewName == null) ? 0 : modelViewName.hashCode());
            return result;
        }

        @Override
        public boolean equals( Object obj) {
            if ( this == obj)
                return true;
            if ( obj == null)
                return false;
            if ( getClass() != obj.getClass())
                return false;
            DuplicateInfo other = ( DuplicateInfo) obj;
            if ( entityId == null) {
                if ( other.entityId != null)
                    return false;
            }
            else if ( !entityId.equals( other.entityId))
                return false;
            if ( modelViewName == null) {
                if ( other.modelViewName != null)
                    return false;
            }
            else if ( !modelViewName.equals( other.modelViewName))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "DuplicateInfo [modelViewName=" + modelViewName + ", entityId=" + entityId + "]";
        }

    }

    /** 発見した重複するEntityタグの情報 */
    private Set<DuplicateInfo> duplicateInfos = new LinkedHashSet<>();

    private Map<Long, String> entityLogicalNameMap = new HashMap<>();

    private Map<Long, String> entityPhysicalNameMap = new HashMap<>();

    /** MODELVIEWタグ内を探索しているかどうか */
    private boolean inModelView = false;

    /** 探索中のMODELVIEWの論理名 */
    private String modelViewName = null;

    /** MODELVIEW内を探索中に発見したENTITYタグのID */
    private Set<String> entityIds = new HashSet<>();

    @Override
    public void visit( Node xmlNode) {
        // コメントは無視
        if ( xmlNode.getNodeType() == Node.COMMENT_NODE) {
            return;
        }
        String nodeName = xmlNode.getNodeName();
        if ( TAG_MODELVIEW.equals( nodeName)) {
            inModelView = true;
            modelViewName = xmlNode.getAttributes().getNamedItem( ATTR_LOGICAL_NAME).getNodeValue();
        }
        else if ( TAG_ENTITY.equals( nodeName)) {
            if ( inModelView) {
                String id = getAttributeValue( xmlNode, ATTR_ID);
                // 重複している
                if ( entityIds.contains( id)) {
                    DuplicateInfo info = new DuplicateInfo( modelViewName, Long.valueOf( id));
                    duplicateInfos.add( info);
                }
                entityIds.add( id);
            }
            else {
                String idStr = getAttributeValue( xmlNode, ATTR_ID);
                if ( idStr == null) {
                    return;
                }
                Long id = Long.valueOf( idStr);
                String logicalName = getAttributeValue( xmlNode, ATTR_LOGICAL_NAME);
                // NOTE VIEWタグの下のENTITYタグの場合は論理名などが入っていない
                if ( logicalName != null) {
                    entityLogicalNameMap.put( id, logicalName);
                }
                String physicalName = getAttributeValue( xmlNode, ATTR_PHYSICAL_NAME);
                if ( physicalName != null) {
                    entityPhysicalNameMap.put( id, physicalName);
                }
            }
        }
    }

    private String getAttributeValue( Node node, String attr) {
        Node attrNode = node.getAttributes().getNamedItem( attr);
        return attrNode != null ? attrNode.getNodeValue() : null;
    }

    @Override
    public void afterChildVisit( Node xmlNode) {
        // コメントは無視
        if ( xmlNode.getNodeType() == Node.COMMENT_NODE) {
            return;
        }
        String nodeName = xmlNode.getNodeName();
        if ( TAG_MODELVIEW.equals( nodeName)) {
            // MODELVIEWはネストしないという前提で抜けたことにする
            inModelView = false;
            modelViewName = null;
            entityIds.clear();
        }
    }

    public Set<DuplicateInfo> getDuplicateInfos() {
        return duplicateInfos;
    }

    public Map<Long, String> getEntityLogicalNameMap() {
        return entityLogicalNameMap;
    }

    public Map<Long, String> getEntityPhysicalNameMap() {
        return entityPhysicalNameMap;
    }
}
